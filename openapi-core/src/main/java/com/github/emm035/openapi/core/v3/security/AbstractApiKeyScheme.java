package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractApiKeyScheme implements SecurityScheme {

  public abstract String getName();

  public abstract Location getIn();

  @Value.Derived
  public Type getType() {
    return Type.API_KEY;
  }

  static enum Location {
    QUERY,
    HEADER,
    COOKIE;

    @JsonCreator
    static Location fromString(@JsonProperty String rawValue) {
      return valueOf(rawValue.toUpperCase());
    }

    @Override
    @JsonValue
    public String toString() {
      return name().toLowerCase();
    }
  }

  @Check
  AbstractApiKeyScheme normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return ApiKeyScheme
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
