package com.github.emm035.openapi.core.v3.schemas;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Map;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public interface DiscriminatorIF {
  String getPropertyName();
  Map<String, String> getMappings();
}
