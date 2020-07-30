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
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.RefFactory;
import com.github.emm035.openapi.schema.generator.internal.SchemasCache;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.annotations.Internal;
import com.github.emm035.openapi.schema.generator.internal.visitors.SchemaGeneratorVisitorWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

class SchemaGeneratorImpl implements SchemaGenerator {
  private final ObjectMapper objectMapper;
  private final TypeFactory typeFactory;
  private final RefFactory refFactory;
  private final SchemasCache schemasCache;
  private final SchemaGeneratorVisitorWrapper schemaGeneratorVisitorWrapper;

  @Inject
  private SchemaGeneratorImpl(
    @Internal ObjectMapper objectMapper,
    @Internal TypeFactory typeFactory,
    RefFactory refFactory,
    SchemasCache schemasCache,
    SchemaGeneratorVisitorWrapper schemaGeneratorVisitorWrapper
  ) {
    this.objectMapper = objectMapper;
    this.typeFactory = typeFactory;
    this.refFactory = refFactory;
    this.schemasCache = schemasCache;
    this.schemaGeneratorVisitorWrapper = schemaGeneratorVisitorWrapper;
  }

  public Referenceable<Schema> generateSchema(TypeReference<?> typeReference)
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
    if (schemasCache.contains(typeName)) {
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
      schemasCache.resolve(refOrSchema),
      schemasCache.getReferenced(refOrSchema)
    );
  }

  public Schema resolve(Referenceable<Schema> refOrSchema)
    throws SchemaGenerationException {
    return schemasCache.resolve(refOrSchema);
  }

  public void clearCachedSchemas() {
    schemasCache.clearCachedSchemas();
  }

  public Map<String, Schema> getCachedSchemas() {
    return schemasCache.getAll();
  }

  /**
   * Builder implementation
   */
  static class BuilderImpl implements Builder {
    private final ImmutableSet.Builder<Module> modules = ImmutableSet.builder();
    private final ImmutableSet.Builder<SchemaExtension> schemaExtensions = ImmutableSet.builder();
    private final ImmutableSet.Builder<Class<? extends SchemaExtension>> schemaExtensionClasses = ImmutableSet.builder();
    private final ImmutableSet.Builder<PropertyExtension> propertyExtensions = ImmutableSet.builder();
    private final ImmutableSet.Builder<Class<? extends PropertyExtension>> propertyExtensionClasses = ImmutableSet.builder();
    private Optional<ObjectMapper> objectMapper = Optional.empty();
    private ImmutableMap.Builder<TypeReference<?>, Schema> defaultSchemas = ImmutableMap.builder();

    BuilderImpl() {}

    public Builder setObjectMapper(ObjectMapper objectMapper) {
      this.objectMapper = Optional.of(objectMapper);
      return this;
    }

    public Builder overrideSchemas(Map<TypeReference<?>, Schema> defaultSchemas) {
      this.defaultSchemas.putAll(defaultSchemas);
      return this;
    }

    public Builder overrideSchema(TypeReference<?> type, Schema schema) {
      this.defaultSchemas.put(type, schema);
      return this;
    }

    public Builder addModules(Module... modules) {
      this.modules.addAll(Arrays.asList(modules));
      return this;
    }

    @Override
    public <T extends SchemaExtension> Builder bindSchemaExtension(T schemaExtension) {
      schemaExtensions.add(schemaExtension);
      return this;
    }

    @Override
    public Builder bindSchemaExtension(
      Class<? extends SchemaExtension> schemaExtension
    ) {
      schemaExtensionClasses.add(schemaExtension);
      return this;
    }

    @Override
    public <T extends PropertyExtension> Builder bindPropertyExtension(T propertyExtension) {
      propertyExtensions.add(propertyExtension);
      return this;
    }

    @Override
    public Builder bindPropertyExtension(
      Class<? extends PropertyExtension> propertyExtension
    ) {
      propertyExtensionClasses.add(propertyExtension);
      return this;
    }

    private SchemaGeneratorModule buildModule() {
      ObjectMapper objectMapperOrDefault = objectMapper.orElse(
        Json.MapperFactory.getInstance()
      );
      return new SchemaGeneratorModule(
        objectMapperOrDefault,
        modules.build(),
        defaultSchemas.build(),
        schemaExtensions.build(),
        schemaExtensionClasses.build(),
        propertyExtensions.build(),
        propertyExtensionClasses.build()
      );
    }

    public SchemaGenerator build() {
      return Guice.createInjector(buildModule()).getInstance(SchemaGeneratorImpl.class);
    }
  }
}
