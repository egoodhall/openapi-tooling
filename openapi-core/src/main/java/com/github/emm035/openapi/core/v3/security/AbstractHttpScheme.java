package com.github.emm035.openapi.core.v3.security;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

import java.util.Optional;

@Immutable
@OpenApiStyle
public abstract class AbstractHttpScheme implements SecurityScheme {

  public abstract String getScheme();
  public abstract Optional<String> getBearerFormat();

  @Override
  @Derived
  public Type getType() {
    return Type.HTTP;
  }

  @Check
  AbstractHttpScheme normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return HttpScheme.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
