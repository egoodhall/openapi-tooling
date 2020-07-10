package com.github.emm035.openapi.schema.generator.internal.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.ArraySchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.internal.Internal;
import com.github.emm035.openapi.schema.generator.internal.Generator;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ArrayFormatVisitor extends JsonArrayFormatVisitor.Base implements Generator {
  private final ArraySchema.Builder arraySchema;
  private final JavaType javaType;
  private final ObjectMapper objectMapper;
  private final SchemaExtension schemaExtension;
  private final SchemaGeneratorVisitorWrapper visitorWrapper;

  @Inject
  public ArrayFormatVisitor(
    @Assisted JavaType javaType,
    @Internal ObjectMapper objectMapper,
    @Extension SchemaExtension schemaExtension,
    SchemaGeneratorVisitorWrapper visitorWrapper
  ) {
    this.javaType = javaType;
    this.objectMapper = objectMapper;
    this.schemaExtension = schemaExtension;
    this.visitorWrapper = visitorWrapper;
    this.arraySchema = ArraySchema.builder();
  }

  @Override
  public void itemsFormat(JsonFormatVisitable handler, JavaType elementType)
    throws JsonMappingException {
    objectMapper.acceptJsonFormatVisitor(elementType, visitorWrapper);
    arraySchema.setItems(visitorWrapper.emit());
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) {
    return schemaExtension.modify(arraySchema.build(), javaType);
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    ArrayFormatVisitor create(JavaType javaType);
  }
}
