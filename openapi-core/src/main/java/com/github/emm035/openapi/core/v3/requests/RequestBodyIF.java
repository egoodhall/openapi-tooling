package com.github.emm035.openapi.core.v3.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.content.Content;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.shared.WithMultipleExamples;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonDeserialize
public interface RequestBodyIF
  extends Describable, Extensible, WithMultipleExamples, Referenceable<RequestBody> {
  Optional<Boolean> getRequired();
  Content getContent();
}
