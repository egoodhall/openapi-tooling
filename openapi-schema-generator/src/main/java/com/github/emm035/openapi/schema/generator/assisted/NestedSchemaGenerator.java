package com.github.emm035.openapi.schema.generator.assisted;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.SchemaGeneratorVisitorWrapper;
import com.github.emm035.openapi.schema.generator.annotations.Internal;
import com.google.inject.Inject;

public class NestedSchemaGenerator {
  private final ObjectMapper objectMapper;
  private final SchemaGeneratorVisitorWrapper visitorWrapper;

  @Inject
  public NestedSchemaGenerator(
    @Internal ObjectMapper objectMapper,
    SchemaGeneratorVisitorWrapper visitorWrapper
  ) {
    this.objectMapper = objectMapper;
    this.visitorWrapper = visitorWrapper;
  }

  public Referenceable<Schema> generateSchema(Class<?> clazz)
    throws JsonMappingException {
    return generateSchema(clazz, true);
  }

  public Referenceable<Schema> generateSchema(JavaType javaType)
    throws JsonMappingException {
    return generateSchema(javaType, true);
  }

  public Referenceable<Schema> generateSchema(Class<?> clazz, boolean asReference)
    throws JsonMappingException {
    objectMapper.acceptJsonFormatVisitor(clazz, visitorWrapper);
    return visitorWrapper.emit(asReference);
  }

  public Referenceable<Schema> generateSchema(JavaType javaType, boolean asReference)
    throws JsonMappingException {
    objectMapper.acceptJsonFormatVisitor(javaType, visitorWrapper);
    return visitorWrapper.emit(asReference);
  }
}
