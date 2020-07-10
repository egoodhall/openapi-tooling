package com.github.emm035.openapi.core.v3.schemas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.google.common.base.CaseFormat;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public abstract class AbstractNumberSchema implements NumericSchema<Double> {

  @Override
  @Derived
  public Type getType() {
    return Type.NUMBER;
  }

  public abstract Optional<Format> getFormat();

  public static enum Format {
    FLOAT,
    DOUBLE;

    @JsonCreator
    private static Format fromString(@JsonProperty String rawValue) {
      return valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, rawValue));
    }

    @Override
    @JsonValue
    public String toString() {
      return name().toLowerCase();
    }
  }

  @Check
  AbstractNumberSchema normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return NumberSchema
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
