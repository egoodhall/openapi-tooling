package com.github.emm035.openapi.core.v3.examples;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonDeserialize
public abstract class AbstractValueExample implements Example {

  public abstract Object getValue();

  @Check
  AbstractValueExample normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return ValueExample
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
