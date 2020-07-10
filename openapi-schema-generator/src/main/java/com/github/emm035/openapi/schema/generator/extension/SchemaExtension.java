package com.github.emm035.openapi.schema.generator.extension;

import com.fasterxml.jackson.databind.JavaType;
import com.github.emm035.openapi.core.v3.schemas.Schema;

@FunctionalInterface
public interface SchemaExtension extends Extension<JavaType> {
  Schema modify(Schema schema, JavaType javaType);
  static SchemaExtension unmodified() {
    return (schema, javaType) -> schema;
  }
}
