package com.github.emm035.openapi.core.v3.references;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

@Value.Immutable
@OpenApiStyle
public interface RefIF<T extends Referenceable<T>> extends Referenceable<T> {
  @JsonIgnore
  @Value.Derived
  default boolean isReferential() {
    return true;
  }

  @JsonProperty("$ref")
  @Parameter
  String getRef();
}
