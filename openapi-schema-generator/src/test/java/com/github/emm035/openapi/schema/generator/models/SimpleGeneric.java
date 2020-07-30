package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.emm035.openapi.schema.generator.annotations.SchemaProperty;

public class SimpleGeneric<T> {

  @JsonProperty("type")
  T getType() {
    return null;
  }

  @SchemaProperty("overridden-property")
  @JsonProperty("int")
  int getInt() {
    return 0;
  }
}
