package com.github.emm035.openapi.core.v3.parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Derived;

import java.util.Optional;


@OpenApiStyle
@Value.Immutable
public abstract class AbstractQueryParameter implements Parameter {
  @Override
  @Derived
  public Location getIn() {
    return Location.QUERY;
  }
  public abstract Optional<Boolean> getRequired();
  public abstract Optional<Style> getStyle();
  public abstract Optional<Boolean> getExplode();

  @Derived
  @JsonIgnore
  public boolean getRequiredOrDefault() {
    return getRequired().orElse(false);
  }

  @Derived
  @JsonIgnore
  public Style getStyleOrDefault() {
    return getStyle().orElse(Style.FORM);
  }

  @Derived
  @JsonIgnore
  public boolean getExplodeOrDefault() {
    return getExplode().orElse(true);
  }
  public abstract Optional<Boolean> getAllowReserved();
  public abstract Optional<Boolean> getAllowEmptyValue();

  public static enum Style {
    FORM,
    SPACE_DELIMITED,
    PIPE_DELIMITED,
    DEEP_OBJECT;

    @JsonCreator
    private static Style fromString(@JsonProperty String rawValue) {
      return valueOf(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, rawValue));
    }

    @Override
    @JsonValue
    public String toString() {
      return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
  }

  @Check
  AbstractQueryParameter normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return QueryParameter.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
