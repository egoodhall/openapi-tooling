package com.github.emm035.openapi.core.v3.content;

import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public interface HeaderIF extends Describable, Referenceable<Header> {
  Referenceable<Schema> getSchema();

  public static Header.Builder builder() {
    return Header.builder();
  }
}
