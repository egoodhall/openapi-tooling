package com.github.emm035.openapi.core.v3.servers;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
@OpenApiStyle
public abstract class AbstractServer implements Extensible {

  @Parameter
  public abstract String getUrl();

  public abstract Optional<String> getDescription();

  public abstract Map<String, ServerVariable> getVariables();

  @Check
  protected AbstractServer normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Server
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
