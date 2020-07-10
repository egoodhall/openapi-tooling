package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonSerialize(using = SecurityRequirementSerializer.class)
@JsonDeserialize(using = SecurityRequirementDeserializer.class)
public interface SecurityRequirementIF {
  String getName();
  List<String> getScopes();
}
