package com.github.emm035.openapi.schema.generator;

import com.github.emm035.openapi.schema.generator.assisted.Extension;
import com.github.emm035.openapi.schema.generator.base.TypeUtils;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.OptionalBinder;

public class TestModule implements Module {

  @Override
  public void configure(Binder binder) {
    OptionalBinder
      .newOptionalBinder(binder, Key.get(SchemaExtension.class, Extension.class))
      .setBinding()
      .toProvider(binder.getProvider(SchemaExtension.class));

    OptionalBinder
      .newOptionalBinder(binder, Key.get(PropertyExtension.class, Extension.class))
      .setBinding()
      .toProvider(binder.getProvider(PropertyExtension.class));
  }

  @Provides
  @Singleton
  SchemaExtension providesSchemaExtension() {
    return (schema, javaType) -> {
      System.out.println(TypeUtils.toTypeName(javaType));
      return schema;
    };
  }

  @Provides
  @Singleton
  PropertyExtension providesPropertyExtension() {
    return (schema, beanProperty) -> {
      System.out.println(
        beanProperty.getName() + " -> " + TypeUtils.toTypeName(beanProperty.getType())
      );
      return schema;
    };
  }
}
