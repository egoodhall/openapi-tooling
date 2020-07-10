package com.github.emm035.openapi.core.v3.schemas;

import com.github.emm035.openapi.core.v3.shared.Enumerated;
import java.util.Optional;

public interface NumericSchema<T extends Number> extends TypedSchema, Enumerated<T> {
  Optional<T> getMinimum();
  Optional<T> getMaximum();
  Optional<T> getMultipleOf();
  Optional<Boolean> getExclusiveMinimum();
  Optional<Boolean> getExclusiveMaximum();
}
