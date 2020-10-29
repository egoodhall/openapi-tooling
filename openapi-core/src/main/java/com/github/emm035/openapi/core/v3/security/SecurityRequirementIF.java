package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public interface SecurityRequirementIF {
  @JsonAnyGetter
  Map<String, List<String>> getRequirements();

  default Optional<List<String>> getScopes(String scheme) {
    return Optional.ofNullable(getRequirements().get(scheme));
  }

  default boolean hasRequirement(String scheme) {
    return getRequirements().containsKey(scheme);
  }
}
