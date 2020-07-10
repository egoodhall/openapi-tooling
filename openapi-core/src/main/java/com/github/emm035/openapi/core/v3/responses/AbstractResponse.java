package com.github.emm035.openapi.core.v3.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.content.Content;
import com.github.emm035.openapi.core.v3.content.Header;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

@Immutable
@OpenApiStyle
@JsonDeserialize
public abstract class AbstractResponse implements Describable, Extensible, Referenceable<Response> {
  @JsonInclude(ALWAYS)
  public abstract Optional<Content> getContent();
  public abstract Optional<Referenceable<Schema>> getSchema();
  public abstract Map<String, Header> getHeaders();

  public static Response.Builder builder() {
    return Response.builder();
  }

  public static Response empty() {
    return Response.builder().build();
  }

  @Check
  AbstractResponse normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Response.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
