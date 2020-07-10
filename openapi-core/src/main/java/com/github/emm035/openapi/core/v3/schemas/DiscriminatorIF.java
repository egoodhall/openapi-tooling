package com.github.emm035.openapi.core.v3.schemas;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Immutable;

import java.util.Map;


@Immutable
@OpenApiStyle
public interface DiscriminatorIF {
  String getPropertyName();
  Map<String, String> getMappings();
}
