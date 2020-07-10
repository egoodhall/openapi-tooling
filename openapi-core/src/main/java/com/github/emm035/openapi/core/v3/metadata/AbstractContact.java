package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractContact implements Extensible {

  public abstract Optional<String> getName();

  public abstract Optional<String> getUrl();

  public abstract Optional<String> getEmail();

  @Check
  AbstractContact normalizeExtensions(Contact extensible) {
    if (Checks.allValid(this)) {
      return this;
    }
    return Contact
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
