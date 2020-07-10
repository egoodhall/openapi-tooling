package com.github.emm035.openapi.core.v3.schemas;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.List;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
// Necessary to override deserializer for io.github.emm035.open.api.core.v3.components.schemas.Schema
@JsonDeserialize
public abstract class AbstractAllOfSchema implements Schema {

  public abstract List<Referenceable<Schema>> getAllOf();

  public static AllOfSchema of(Referenceable<Schema>... schemas) {
    return AllOfSchema.builder().addAllOf(schemas).build();
  }

  @Check
  AbstractAllOfSchema normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return AllOfSchema
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
