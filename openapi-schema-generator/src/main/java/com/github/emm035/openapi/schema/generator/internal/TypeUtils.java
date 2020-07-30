package com.github.emm035.openapi.schema.generator.internal;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TypeUtils {

  private TypeUtils() {}

  public static String toTypeName(JavaType javaType) {
    StringBuilder nameBuilder = new StringBuilder(javaType.getRawClass().getSimpleName());
    if (!javaType.hasGenericTypes()) {
      return nameBuilder.toString();
    }
    return nameBuilder
      .append("<")
      .append(
        javaType
          .getBindings()
          .getTypeParameters()
          .stream()
          .map(TypeUtils::toTypeName)
          .collect(Collectors.joining(","))
      )
      .append(">")
      .toString();
  }

  public static Optional<JsonTypeInfo> getTypeInfo(JavaType javaType) {
    return Optional.ofNullable(javaType.getRawClass().getAnnotation(JsonTypeInfo.class));
  }

  public static List<Class<?>> getTypeImplementations(JavaType javaType) {
    return Optional
      .ofNullable(javaType.getRawClass().getAnnotation(JsonSubTypes.class))
      .map(JsonSubTypes::value)
      .map(ImmutableList::copyOf)
      .orElseGet(ImmutableList::of)
      .stream()
      .map(JsonSubTypes.Type::value)
      .collect(ImmutableList.toImmutableList());
  }

  public static Map<String, Class<?>> getTypeMappings(JavaType javaType) {
    return Optional
      .ofNullable(javaType.getRawClass().getAnnotation(JsonSubTypes.class))
      .map(JsonSubTypes::value)
      .map(ImmutableList::copyOf)
      .orElseGet(ImmutableList::of)
      .stream()
      .filter(Predicates.not(type -> Strings.isNullOrEmpty(type.name())))
      .collect(
        ImmutableMap.toImmutableMap(JsonSubTypes.Type::name, JsonSubTypes.Type::value)
      );
  }

  public static JavaType unwrap(JavaType javaType) {
    if (javaType.getRawClass().equals(Optional.class)) {
      return javaType.getBindings().getBoundType(0);
    }
    return javaType;
  }
}
