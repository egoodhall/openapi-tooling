package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

import java.util.Optional;

@Immutable
@OpenApiStyle
public abstract class AbstractExternalDocumentation implements Extensible {
  public abstract Optional<String> getDescription();
  public abstract String getUrl();

  @Check
  AbstractExternalDocumentation normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return ExternalDocumentation.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
