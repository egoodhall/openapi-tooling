package com.github.emm035.openapi.core.v3.examples;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.shared.Summarizable;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonDeserialize
public abstract class AbstractExternalExample implements Example, Summarizable {

  public abstract Optional<String> getExternalValue();

  @Check
  AbstractExternalExample normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return ExternalExample
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
