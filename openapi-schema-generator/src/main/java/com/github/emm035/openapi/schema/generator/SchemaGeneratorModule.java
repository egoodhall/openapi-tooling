package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.annotations.RefPrefix;
import com.github.emm035.openapi.schema.generator.annotations.RequireNonOptionalScalarProperties;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.annotations.CachedSchemas;
import com.github.emm035.openapi.schema.generator.internal.annotations.DefaultSchemas;
import com.github.emm035.openapi.schema.generator.internal.annotations.Internal;
import com.github.emm035.openapi.schema.generator.internal.generators.SubTypeGenerator;
import com.github.emm035.openapi.schema.generator.internal.visitors.ArrayFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.BooleanFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.MapFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.NumberFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.ObjectFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.StringFormatVisitor;
import com.github.emm035.openapi.schema.generator.internal.visitors.SubTypedObjectFormatVisitor;
import com.google.common.collect.Maps;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

class SchemaGeneratorModule extends AbstractModule {
  private final Map<TypeReference<?>, Schema> defaultSchemas;
  private final Set<SchemaExtension> schemaExtensions;
  private final Set<Class<? extends SchemaExtension>> schemaExtensionClasses;
  private final Set<PropertyExtension> propertyExtensions;
  private final Set<Class<? extends PropertyExtension>> propertyExtensionClasses;
  private final boolean requireNonOptionalScalarProperties;
  private final ObjectMapper objectMapper;
  private final Collection<Module> modules;

  SchemaGeneratorModule(
    ObjectMapper objectMapper,
    Collection<Module> modules,
    Map<TypeReference<?>, Schema> defaultSchemas,
    Set<SchemaExtension> schemaExtensions,
    Set<Class<? extends SchemaExtension>> schemaExtensionClasses,
    Set<PropertyExtension> propertyExtensions,
    Set<Class<? extends PropertyExtension>> propertyExtensionClasses,
    boolean requireNonOptionalScalarProperties
  ) {
    this.objectMapper = objectMapper;
    this.modules = modules;
    this.defaultSchemas = defaultSchemas;
    this.schemaExtensions = schemaExtensions;
    this.schemaExtensionClasses = schemaExtensionClasses;
    this.propertyExtensions = propertyExtensions;
    this.propertyExtensionClasses = propertyExtensionClasses;
    this.requireNonOptionalScalarProperties = requireNonOptionalScalarProperties;
  }

  @Override
  protected void configure() {
    // Install all supplied modules
    modules.forEach(this::install);

    bindAssistedFactory(BooleanFormatVisitor.Factory.class);
    bindAssistedFactory(NumberFormatVisitor.Factory.class);
    bindAssistedFactory(ObjectFormatVisitor.Factory.class);
    bindAssistedFactory(StringFormatVisitor.Factory.class);
    bindAssistedFactory(ArrayFormatVisitor.Factory.class);
    bindAssistedFactory(MapFormatVisitor.Factory.class);
    bindAssistedFactory(SubTypedObjectFormatVisitor.Factory.class);
    bindAssistedFactory(SubTypeGenerator.Factory.class);

    Multibinder<SchemaExtension> schemaExtensionMultibinder = Multibinder.newSetBinder(
      binder(),
      Key.get(SchemaExtension.class, Internal.class)
    );
    for (SchemaExtension extension : schemaExtensions) {
      schemaExtensionMultibinder.addBinding().toInstance(extension);
    }
    for (Class<? extends SchemaExtension> extension : schemaExtensionClasses) {
      schemaExtensionMultibinder.addBinding().to(extension);
    }

    Multibinder<PropertyExtension> propertyExtensionMultibinder = Multibinder.newSetBinder(
      binder(),
      Key.get(PropertyExtension.class, Internal.class)
    );
    for (PropertyExtension extension : propertyExtensions) {
      propertyExtensionMultibinder.addBinding().toInstance(extension);
    }
    for (Class<? extends PropertyExtension> extension : propertyExtensionClasses) {
      propertyExtensionMultibinder.addBinding().to(extension);
    }

    OptionalBinder
      .newOptionalBinder(binder(), Key.get(String.class, RefPrefix.class))
      .setDefault()
      .toInstance("#/components/schemas/");
  }

  private void bindAssistedFactory(Class<?> clazz) {
    install(new FactoryModuleBuilder().build(clazz));
  }

  @Provides
  @Singleton
  @RequireNonOptionalScalarProperties
  public boolean providesNonOptionalScalarPropertiesFlag() {
    return requireNonOptionalScalarProperties;
  }

  @Provides
  @Singleton
  @CachedSchemas
  public Map<String, Schema> providesCachedSchemasMap(
    @DefaultSchemas Map<String, Schema> providedSchemas
  ) {
    return providedSchemas
      .entrySet()
      .stream()
      .collect(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue));
  }

  @Provides
  @Singleton
  @DefaultSchemas
  public Map<String, Schema> providesDefaultSchemasMap(
    @Internal TypeFactory typeFactory
  ) {
    return defaultSchemas
      .entrySet()
      .stream()
      .map(
        entry ->
          Maps.immutableEntry(
            TypeUtils.toTypeName(typeFactory.constructType(entry.getKey())),
            entry.getValue()
          )
      )
      .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
  }

  @Provides
  @Singleton
  @Extension
  public SchemaExtension providesSchemaExtension(
    @Internal Set<SchemaExtension> providedExtensions
  ) {
    return SchemaExtension.all(providedExtensions);
  }

  @Provides
  @Singleton
  @Extension
  public PropertyExtension providesPropertyExtension(
    @Internal Set<PropertyExtension> providedExtensions
  ) {
    return PropertyExtension.all(providedExtensions);
  }

  @Provides
  @Singleton
  @Internal
  public MutableGraph<String> providesReferenceGraph() {
    return GraphBuilder.directed().allowsSelfLoops(true).build();
  }

  @Provides
  @Singleton
  @Internal
  public ObjectMapper providesObjectMapper() {
    return objectMapper;
  }

  @Provides
  @Singleton
  @Internal
  public TypeFactory providesTypeFactory() {
    return objectMapper.getTypeFactory();
  }
}
