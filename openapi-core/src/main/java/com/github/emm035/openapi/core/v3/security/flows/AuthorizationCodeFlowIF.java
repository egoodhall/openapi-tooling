package com.github.emm035.openapi.core.v3.security.flows;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public interface AuthorizationCodeFlowIF extends Flow {
  String getAuthorizationUrl();
  String getTokenUrl();
  Optional<String> getRefreshUrl();
  Map<String, String> getScopes();
}
