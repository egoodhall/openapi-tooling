package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Optional;

public class SimpleModel {
  @JsonProperty("instant")
  public Instant instant;

  @JsonProperty("optionalInstant")
  public Optional<Instant> optionalInstant;

  @JsonProperty("customName")
  public SimpleGeneric<String> internalName;

  @JsonProperty("withSubTypes")
  public Base base;
}
