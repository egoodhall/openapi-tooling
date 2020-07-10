package com.github.emm035.openapi.core.v3.callbacks;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.PathItem;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonSerialize(using = CallbackSerializer.class)
@JsonDeserialize(using = CallbackDeserializer.class)
public abstract class CallbackIF implements Extensible, Referenceable<Callback> {
  public abstract String getExpression();
  public abstract PathItem getPathItem();

  @Check
  CallbackIF normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Callback.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
