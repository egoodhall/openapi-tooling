package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.emm035.openapi.core.v3.jackson.Json;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.github.emm035.openapi.schema.generator.internal.Internal;
import com.github.emm035.openapi.schema.generator.internal.RefFactory;
import com.github.emm035.openapi.schema.generator.internal.Schemas;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.visitors.SchemaGeneratorVisitorWrapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class SchemaGeneratorImpl implements SchemaGenerator {
  private final ObjectMapper objectMapper;
  private final TypeFactory typeFactory;
  private final Map<String, Schema> schemaMap;
  private final RefFactory refFactory;
  private final Schemas schemas;
  private final SchemaGeneratorVisitorWrapper schemaGeneratorVisitorWrapper;

  @Inject
  private SchemaGeneratorImpl(
    @Internal ObjectMapper objectMapper,
    @Internal TypeFactory typeFactory,
    @Internal Map<String, Schema> schemaMap,
    RefFactory refFactory,
    Schemas schemas,
    SchemaGeneratorVisitorWrapper schemaGeneratorVisitorWrapper
  ) {
    this.objectMapper = objectMapper;
    this.typeFactory = typeFactory;
    this.schemaMap = schemaMap;
    this.refFactory = refFactory;
    this.schemas = schemas;
    this.schemaGeneratorVisitorWrapper = schemaGeneratorVisitorWrapper;
  }

  public <T> Referenceable<Schema> generateSchema(TypeReference<T> typeReference)
    throws SchemaGenerationException {
    return generateSchema(typeFactory.constructType(typeReference));
  }

  public Referenceable<Schema> generateSchema(Type type)
    throws SchemaGenerationException {
    return generateSchema(typeFactory.constructType(type));
  }

  public Referenceable<Schema> generateSchema(JavaType javaType)
    throws SchemaGenerationException {
    String typeName = TypeUtils.toTypeName(javaType);
    if (schemaMap.containsKey(typeName)) {
      return refFactory.create(typeName);
    }

    try {
      objectMapper.acceptJsonFormatVisitor(javaType, schemaGeneratorVisitorWrapper);
      return schemaGeneratorVisitorWrapper.emit();
    } catch (JsonMappingException e) {
      throw new SchemaGenerationException("Failed to generate schema for: " + typeName);
    }
  }

  public SchemaResult resolveWithDependencies(Referenceable<Schema> refOrSchema)
    throws SchemaGenerationException {
    return SchemaResult.of(
      schemas.resolve(refOrSchema),
      schemas.getReferenced(refOrSchema)
    );
  }

  public Schema resolve(Referenceable<Schema> refOrSchema)
    throws SchemaGenerationException {
    return schemas.resolve(refOrSchema);
  }

  public void clearCachedSchemas() {
    schemaMap.clear();
  }

  public Map<String, Schema> getCachedSchemas() {
    return schemaMap;
  }

  /**
   * Builder implementation
   */
  static class BuilderImpl implements Builder {
    private final Set<Module> modules = Sets.newLinkedHashSet();
    private Optional<ObjectMapper> objectMapper = Optional.empty();
    private ImmutableMap.Builder<String, Schema> defaultSchemas = ImmutableMap.builder();

    BuilderImpl() {}

    public Builder setObjectMapper(ObjectMapper objectMapper) {
      this.objectMapper = Optional.of(objectMapper);
      return this;
    }

    public Builder overrideSchemas(Map<String, Schema> defaultSchemas) {
      this.defaultSchemas.putAll(defaultSchemas);
      return this;
    }

    public Builder overrideSchema(String typeName, Schema schema) {
      this.defaultSchemas.put(typeName, schema);
      return this;
    }

    public Builder addModules(Module... modules) {
      this.modules.addAll(Arrays.asList(modules));
      return this;
    }

    private SchemaGeneratorModule buildModule() {
      ObjectMapper objectMapperOrDefault = objectMapper.orElse(
        Json.MapperFactory.getInstance()
      );
      ImmutableSet<Module> frozenModules = ImmutableSet.copyOf(this.modules);
      return new SchemaGeneratorModule(
        objectMapperOrDefault,
        frozenModules,
        defaultSchemas.build()
      );
    }

    public SchemaGenerator build() {
      return Guice.createInjector(buildModule()).getInstance(SchemaGeneratorImpl.class);
    }
  }
}
