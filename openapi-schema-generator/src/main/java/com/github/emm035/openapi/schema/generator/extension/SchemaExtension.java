package com.github.emm035.openapi.schema.generator.extension;

import com.fasterxml.jackson.databind.JavaType;
import com.github.emm035.openapi.core.v3.schemas.Schema;

@FunctionalInterface
public interface SchemaExtension extends Extension<JavaType> {
  Schema modify(Schema schema, JavaType javaType);

  default SchemaExtension andThen(SchemaExtension next) {
    return (schema, data) -> next.modify(modify(schema, data), data);
  }

  static <T> SchemaExtension all(Iterable<? extends SchemaExtension> extensions) {
    SchemaExtension composed = (schema, data) -> schema;
    for (SchemaExtension extension : extensions) {
      composed = composed.andThen(extension);
    }
    return composed;
  }
}
