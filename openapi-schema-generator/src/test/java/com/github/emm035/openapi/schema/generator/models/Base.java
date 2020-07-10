package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes(
  {
    @Type(value = Impl1.class, name = "Impl1"), @Type(value = Impl2.class, name = "Impl2")
  }
)
public interface Base {
  @JsonGetter("type")
  String getType();
}
