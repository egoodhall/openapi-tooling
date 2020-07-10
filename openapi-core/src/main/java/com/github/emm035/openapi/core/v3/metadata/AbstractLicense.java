package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.Optional;

@Immutable
@OpenApiStyle
public abstract class AbstractLicense implements Extensible {
  @Parameter
  public abstract String getName();
  @Parameter
  public abstract Optional<String> getUrl();

  @Check
  AbstractLicense normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return License.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
