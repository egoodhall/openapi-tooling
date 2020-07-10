package com.github.emm035.openapi.core.v3.parameters;

import static org.immutables.value.Value.Check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Optional;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public abstract class AbstractPathParameter implements Parameter {

  @Override
  @Derived
  public Location getIn() {
    return Location.PATH;
  }

  public abstract Optional<Boolean> getRequired();

  public abstract Optional<Style> getStyle();

  public abstract Optional<Boolean> getExplode();

  @Derived
  @JsonIgnore
  public boolean getRequiredOrDefault() {
    return getRequired().orElse(true);
  }

  @Derived
  @JsonIgnore
  public Style getStyleOrDefault() {
    return getStyle().orElse(Style.SIMPLE);
  }

  @Derived
  @JsonIgnore
  public boolean getExplodeOrDefault() {
    return getExplode().orElse(false);
  }

  public static enum Style {
    SIMPLE,
    LABEL,
    MATRIX;

    @JsonCreator
    private static Style fromString(@JsonProperty String rawValue) {
      return valueOf(rawValue.toUpperCase());
    }

    @Override
    @JsonValue
    public String toString() {
      return name().toLowerCase();
    }
  }

  @Check
  AbstractPathParameter normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }

    return PathParameter
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
