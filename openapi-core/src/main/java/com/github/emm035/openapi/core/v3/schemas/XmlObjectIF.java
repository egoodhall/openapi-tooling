package com.github.emm035.openapi.core.v3.schemas;

import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Immutable;

import java.util.Optional;


@Immutable
@OpenApiStyle
public interface XmlObjectIF {
  String getName();
  Optional<String> getNamespace();
  Optional<String> getPrefix();
  Optional<Boolean> isAttribute();
  Optional<Boolean> isWrapped();
}
