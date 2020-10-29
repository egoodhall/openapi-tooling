package com.github.emm035.openapi.core.v3.references;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import org.immutables.value.Value;

@JsonDeserialize(using = ReferenceableDeserializer.class)
public interface Referenceable<T extends Referenceable<T>> {
  @JsonIgnore
  @Value.Derived
  default boolean isReferential() {
    return false;
  }

  default Optional<T> asType(Class<T> clazz) {
    if (isReferential() && clazz.isAssignableFrom(this.getClass())) {
      return Optional.of(clazz.cast(this));
    }
    return Optional.empty();
  }
}
