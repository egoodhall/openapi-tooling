package com.github.emm035.openapi.core.v3.security.flows;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Immutable;

import java.util.Map;
import java.util.Optional;


@OpenApiStyle
@Immutable
public interface ImplicitFlowIF {
  String getAuthorizationUrl();
  Optional<String> getRefreshUrl();
  Map<String, String> getScopes();
}
