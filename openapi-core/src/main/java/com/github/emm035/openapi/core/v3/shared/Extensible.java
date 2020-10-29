package com.github.emm035.openapi.core.v3.shared;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.Function;

public interface Extensible {
  @JsonAnyGetter
  Map<String, Object> getExtensions();

  default <T> T getExtensions(Function<Map<String, Object>, T> converter) {
    return converter.apply(getExtensions());
  }

  interface Checks {
    static <T extends Extensible> boolean allValid(T extensible) {
      return extensible
        .getExtensions()
        .keySet()
        .stream()
        .allMatch(x -> x.startsWith("x-"));
    }

    static <T extends Extensible> Map<String, Object> validExtensions(T extensible) {
      return extensible
        .getExtensions()
        .entrySet()
        .stream()
        .filter(Checks::isValid)
        .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static boolean isValid(Map.Entry<String, ?> entry) {
      return entry.getKey().startsWith("x-");
    }
  }
}
