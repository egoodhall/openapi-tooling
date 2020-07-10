package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Optional;

public class Impl1 implements Base {

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }

  @JsonGetter("int")
  public Optional<Integer> getInt() {
    return Optional.of(1234);
  }
}
