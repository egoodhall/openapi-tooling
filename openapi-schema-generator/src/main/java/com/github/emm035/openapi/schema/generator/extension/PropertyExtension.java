package com.github.emm035.openapi.schema.generator.extension;

import com.fasterxml.jackson.databind.BeanProperty;
import com.github.emm035.openapi.core.v3.schemas.Schema;

@FunctionalInterface
public interface PropertyExtension extends Extension<BeanProperty> {
  Schema modify(Schema schema, BeanProperty beanProperty);
  static PropertyExtension unmodified() {
    return (schema, beanProperty) -> schema;
  }
}
