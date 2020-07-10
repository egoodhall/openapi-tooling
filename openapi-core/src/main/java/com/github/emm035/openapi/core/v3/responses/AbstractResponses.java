package com.github.emm035.openapi.core.v3.responses;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
@JsonDeserialize(using = ResponsesDeserializer.class)
@JsonSerialize(using = ResponsesSerializer.class)
public abstract class AbstractResponses implements Extensible {

  public abstract Optional<Referenceable<Response>> getDefault();

  @JsonUnwrapped
  public abstract Map<Integer, Referenceable<Response>> getResponses();

  @Check
  protected AbstractResponses normalizeExtensions() {
    Preconditions.checkState(
      getDefault().isPresent() || !getResponses().isEmpty(),
      "At least one response must be specified"
    );

    if (Checks.allValid(this)) {
      return this;
    }
    return Responses
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
