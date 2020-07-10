package com.github.emm035.openapi.core.v3.servers;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractServerVariable implements Extensible {

  public abstract List<String> getEnum();

  public abstract String getDefault();

  public abstract Optional<String> getDescription();

  @Value.Check
  AbstractServerVariable normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return ServerVariable
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
