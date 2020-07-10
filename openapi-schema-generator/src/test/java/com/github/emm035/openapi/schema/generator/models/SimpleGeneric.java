package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGeneric<T> {

  @JsonProperty("type")
  T getType() {
    return null;
  }

  @JsonProperty("int")
  int getInt() {
    return 0;
  }
}
