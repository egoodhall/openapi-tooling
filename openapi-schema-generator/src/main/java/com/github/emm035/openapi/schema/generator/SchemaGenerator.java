package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emm035.openapi.core.v3.jackson.Json;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.google.inject.Module;
import java.lang.reflect.Type;
import java.util.Map;

public interface SchemaGenerator {
  Referenceable<Schema> generateSchema(Type type) throws SchemaGenerationException;
  Referenceable<Schema> generateSchema(JavaType type) throws SchemaGenerationException;
  <T> Referenceable<Schema> generateSchema(TypeReference<T> type)
    throws SchemaGenerationException;
  SchemaResult resolveWithDependencies(Referenceable<Schema> schema)
    throws SchemaGenerationException;
  Schema resolve(Referenceable<Schema> schema) throws SchemaGenerationException;
  Map<String, Schema> getCachedSchemas();
  void clearCachedSchemas();

  static SchemaGenerator newInstance() {
    return builder().build();
  }

  static Builder builder() {
    return new SchemaGeneratorImpl.BuilderImpl();
  }

  interface Builder {
    Builder setObjectMapper(ObjectMapper objectMapper);
    Builder overrideSchemas(Map<String, Schema> defaultSchemas);
    Builder overrideSchema(String typeName, Schema schema);
    Builder addModules(Module... modules);
    SchemaGenerator build();
  }
}
