package com.github.emm035.openapi.core.v3.shared;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public interface Extensible {
  @JsonAnyGetter
  Map<String, Object> getExtensions();

  class Checks {

    private Checks() {}

    public static <T extends Extensible> boolean allValid(T extensible) {
      return extensible
        .getExtensions()
        .keySet()
        .stream()
        .allMatch(x -> x.startsWith("x-"));
    }

    public static <T extends Extensible> Map<String, Object> validExtensions(
      T extensible
    ) {
      return extensible
        .getExtensions()
        .entrySet()
        .stream()
        .filter(Checks::isValid)
        .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static boolean isValid(Map.Entry<String, ?> entry) {
      if (!isValid(entry.getKey())) {
        throw new IllegalStateException(entry.getKey());
        //        System.out.println("Found invalid extension: " + entry.getKey());
        //        return false;
      }
      return true;
    }

    public static boolean isValid(String key) {
      return key.startsWith("x-");
    }
  }
}
