package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Impl2 implements Base {

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }

  @JsonGetter("string")
  public int getString() {
    return 1234;
  }
}
