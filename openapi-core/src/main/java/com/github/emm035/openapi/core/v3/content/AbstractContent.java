package com.github.emm035.openapi.core.v3.content;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Map;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
@JsonSerialize(using = ContentSerializer.class)
@JsonDeserialize(using = ContentDeserializer.class)
public abstract class AbstractContent implements Extensible {

  @JsonUnwrapped
  public abstract Map<String, MediaType> getMediaTypes();
}
