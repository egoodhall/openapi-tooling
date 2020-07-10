package com.github.emm035.openapi.core.v3.links;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
@JsonDeserialize
public abstract class AbstractIdLink implements Link, Extensible, Describable {

  public abstract String getOperationId();

  public abstract Map<String, Object> getParameters();

  public abstract Optional<Object> getRequestBody();

  @Check
  AbstractIdLink normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return IdLink
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
