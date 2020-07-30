package com.github.emm035.openapi.schema.generator.internal.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.Generator;
import com.github.emm035.openapi.schema.generator.internal.annotations.Internal;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class MapFormatVisitor extends JsonMapFormatVisitor.Base implements Generator {
  private final JavaType objectJavaType;
  private final JavaType javaType;
  private final SchemaExtension schemaExtension;
  private final SchemaGeneratorVisitorWrapper visitorWrapper;
  private final ObjectSchema.Builder schema;

  @Inject
  public MapFormatVisitor(
    @Assisted JavaType javaType,
    @Internal ObjectMapper objectMapper,
    @Extension SchemaExtension schemaExtension,
    SchemaGeneratorVisitorWrapper visitorWrapper
  ) {
    this.javaType = javaType;
    this.schemaExtension = schemaExtension;
    this.visitorWrapper = visitorWrapper;
    this.objectJavaType = objectMapper.getTypeFactory().constructType(Object.class);
    this.schema = ObjectSchema.builder();
  }

  @Override
  public void keyFormat(JsonFormatVisitable handler, JavaType keyType)
    throws JsonMappingException {}

  @Override
  public void valueFormat(JsonFormatVisitable handler, JavaType valueType)
    throws JsonMappingException {
    if (valueType.equals(objectJavaType)) {
      schema.setAdditionalProperties(ObjectSchema.builder().build());
      return;
    }

    handler.acceptJsonFormatVisitor(visitorWrapper, valueType);
    schema.setAdditionalProperties(visitorWrapper.emit());
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) {
    return schemaExtension.modify(schema.build(), javaType);
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    MapFormatVisitor create(JavaType javaType);
  }
}
