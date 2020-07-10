package com.github.emm035.openapi.core.v3.security;

import com.github.emm035.openapi.core.v3.security.flows.Flows;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractOAuth2Scheme implements SecurityScheme {

  @Override
  @Value.Derived
  public Type getType() {
    return Type.OAUTH2;
  }

  public abstract Flows getFlows();
}
