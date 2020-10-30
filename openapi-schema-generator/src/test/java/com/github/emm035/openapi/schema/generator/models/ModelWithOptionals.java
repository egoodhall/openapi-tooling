package com.github.emm035.openapi.schema.generator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Optional;

public class ModelWithOptionals {
  @JsonProperty("instant")
  public Instant instant;

  @JsonProperty("optionalInstant")
  public Optional<Instant> optionalInstant;
}
