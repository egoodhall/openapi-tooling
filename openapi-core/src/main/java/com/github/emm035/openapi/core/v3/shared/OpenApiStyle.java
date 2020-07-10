package com.github.emm035.openapi.core.v3.shared;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;
import org.immutables.value.Value.Style;

@Target({ ElementType.PACKAGE, ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
@JsonSerialize
@Value.Style(
  get = { "is*", "get*", "are*" },
  init = "set*", // Builder initialization methods will have 'set' prefix
  typeAbstract = { "Abstract*", "*IF" }, // 'Abstract' prefix, and 'IF' suffix, will be detected and trimmed
  typeImmutable = "*", // No prefix or suffix for generated immutable type
  optionalAcceptNullable = true, // allow for an Optional<T> to have a setter that takes a null value of T
  visibility = Style.ImplementationVisibility.SAME // Generated class will have the same visibility as the abstract class/interface)
)
public @interface OpenApiStyle {
}
