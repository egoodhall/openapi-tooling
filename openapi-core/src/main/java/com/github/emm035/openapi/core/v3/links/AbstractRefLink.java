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
public abstract class AbstractRefLink implements Link, Extensible, Describable {

  public abstract String getOperationRef();

  public abstract Map<String, Object> getParameters();

  public abstract Optional<Object> getRequestBody();

  @Check
  AbstractRefLink normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return RefLink
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
