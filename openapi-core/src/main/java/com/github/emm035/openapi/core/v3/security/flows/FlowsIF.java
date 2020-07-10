package com.github.emm035.openapi.core.v3.security.flows;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public interface FlowsIF {
  Optional<ImplicitFlow> getImplicit();
  Optional<PasswordFlow> getPassword();
  Optional<ClientCredentialsFlow> getClientCredentials();
  Optional<AuthorizationCodeFlow> getAuthorizationCode();
}
