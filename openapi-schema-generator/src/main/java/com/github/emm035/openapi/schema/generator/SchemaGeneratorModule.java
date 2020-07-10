package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.annotations.RefPrefix;
import com.github.emm035.openapi.schema.generator.internal.Internal;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
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
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class SchemaGeneratorModule extends AbstractModule {
  private final Map<String, Schema> schemas = Maps.newConcurrentMap();
  private final ObjectMapper objectMapper;
  private final Collection<Module> modules;

  SchemaGeneratorModule(
    ObjectMapper objectMapper,
    Collection<Module> modules,
    Map<String, Schema> defaultSchemas
  ) {
    this.objectMapper = objectMapper;
    this.modules = modules;
    this.schemas.putAll(defaultSchemas);
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

    // Make sure we have a map binder available
    MapBinder.newMapBinder(
      binder(),
      new TypeLiteral<TypeReference<?>>() {},
      new TypeLiteral<Schema>() {},
      Extension.class
    );

    OptionalBinder
      .newOptionalBinder(binder(), Key.get(SchemaExtension.class, Extension.class))
      .setDefault()
      .toInstance(SchemaExtension.unmodified());

    OptionalBinder
      .newOptionalBinder(binder(), Key.get(PropertyExtension.class, Extension.class))
      .setDefault()
      .toInstance(PropertyExtension.unmodified());

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
  @Internal
  public Map<String, Schema> providesSchemasMap(
    @Extension Map<TypeReference<?>, Schema> providedSchemas
  ) {
    LinkedHashMap<String, Schema> baseSchemas = new LinkedHashMap<>(schemas);

    for (Map.Entry<TypeReference<?>, Schema> schemaMapping : providedSchemas.entrySet()) {
      JavaType javaType = objectMapper
        .getTypeFactory()
        .constructType(schemaMapping.getKey());
      baseSchemas.put(TypeUtils.toTypeName(javaType), schemaMapping.getValue());
    }

    return baseSchemas;
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

  public static MapBinder<TypeReference<?>, Schema> typeOverrideBinder(Binder binder) {
    return MapBinder.newMapBinder(
      binder,
      new TypeLiteral<TypeReference<?>>() {},
      new TypeLiteral<Schema>() {},
      Extension.class
    );
  }
}
