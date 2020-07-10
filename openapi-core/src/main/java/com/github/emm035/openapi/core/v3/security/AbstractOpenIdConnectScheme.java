package com.github.emm035.openapi.core.v3.security;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractOpenIdConnectScheme implements SecurityScheme {

  @Override
  @Value.Derived
  public SecurityScheme.Type getType() {
    return SecurityScheme.Type.OPEN_ID_CONNECT;
  }

  @Value.Check
  AbstractOpenIdConnectScheme normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return OpenIdConnectScheme.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
