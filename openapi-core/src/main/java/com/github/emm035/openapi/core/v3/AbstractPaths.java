package com.github.emm035.openapi.core.v3;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import org.immutables.value.Value.Immutable;

import java.util.Map;

@Immutable
@OpenApiStyle
@JsonSerialize(using = PathsSerializer.class)
@JsonDeserialize(using = PathsDeserializer.class)
public abstract class AbstractPaths implements Extensible {
  @JsonUnwrapped
  public abstract Map<String, PathItem> getAsMap();

  public PathItem get(String path) {
    return getAsMap().get(path);
  }

  public static Paths empty() {
    return Paths.builder().build();
  }
}
