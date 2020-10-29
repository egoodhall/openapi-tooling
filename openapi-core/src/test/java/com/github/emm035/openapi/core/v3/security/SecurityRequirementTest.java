package com.github.emm035.openapi.core.v3.security;

import com.github.emm035.openapi.core.v3.BasicJsonTest;
import java.util.List;
import java.util.Map;

public class SecurityRequirementTest extends BasicJsonTest<SecurityRequirement> {

  @Override
  public Class<SecurityRequirement> getModelClass() {
    return SecurityRequirement.class;
  }

  @Override
  public SecurityRequirement getInstance() {
    return SecurityRequirement
      .builder()
      .putAllRequirements(
        Map.ofEntries(
          Map.entry("k1", List.of("v1", "v2")),
          Map.entry("k2", List.of("v1", "v2"))
        )
      )
      .build();
  }
}
