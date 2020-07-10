package com.github.emm035.openapi.core.v3.schemas;

import static org.immutables.value.Value.Check;
import static org.immutables.value.Value.Default;

import com.github.emm035.openapi.core.v3.shared.Enumerated;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@OpenApiStyle
@Immutable
public abstract class AbstractStringSchema implements TypedSchema, Enumerated<String> {

  @Override
  @Default
  public Type getType() {
    return Type.STRING;
  }

  public abstract Optional<String> getFormat();

  public abstract Optional<String> getPattern();

  public static final class Format {
    public static final String DATE = "date";
    public static final String DATE_TIME = "date-time";
    public static final String PASSWORD = "password";
    public static final String BYTE = "byte";
    public static final String BINARY = "binary";
  }

  @Check
  AbstractStringSchema normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return StringSchema
      .builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
