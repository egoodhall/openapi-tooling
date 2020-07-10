package com.github.emm035.openapi.core.v3;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.metadata.ExternalDocumentation;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

import java.util.Optional;

@Immutable
@OpenApiStyle
public abstract class AbstractTag implements Extensible {
  public abstract String getName();
  public abstract Optional<String> getDescription();
  public abstract Optional<ExternalDocumentation> getExternalDocs();

  @Check
  protected AbstractTag normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Tag.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
