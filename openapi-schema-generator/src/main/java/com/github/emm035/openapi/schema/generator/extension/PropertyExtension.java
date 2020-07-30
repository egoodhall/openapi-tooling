package com.github.emm035.openapi.schema.generator.extension;

import com.fasterxml.jackson.databind.BeanProperty;
import com.github.emm035.openapi.core.v3.schemas.Schema;

@FunctionalInterface
public interface PropertyExtension extends Extension<BeanProperty> {
  Schema modify(Schema schema, BeanProperty beanProperty);

  default PropertyExtension andThen(PropertyExtension next) {
    return (schema, data) -> next.modify(modify(schema, data), data);
  }

  static <T> PropertyExtension all(Iterable<? extends PropertyExtension> extensions) {
    PropertyExtension composed = (schema, data) -> schema;
    for (PropertyExtension extension : extensions) {
      composed = composed.andThen(extension);
    }
    return composed;
  }
}
