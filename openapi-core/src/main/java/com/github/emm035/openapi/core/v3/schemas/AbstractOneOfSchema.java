package com.github.emm035.openapi.core.v3.schemas;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

import java.util.List;
import java.util.Optional;


@OpenApiStyle
@Immutable
// Necessary to override deserializer for io.github.emm035.open.api.core.v3.components.schemas.Schema
@JsonDeserialize
public abstract class AbstractOneOfSchema implements Schema {
  public abstract List<Referenceable<Schema>> getOneOf();

  public abstract Optional<Discriminator> getDiscriminator();

  public static OneOfSchema of(Referenceable<Schema>... schemas) {
    return OneOfSchema.builder()
      .addOneOf(schemas)
      .build();
  }

  @Value.Check
  AbstractOneOfSchema normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return OneOfSchema.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
