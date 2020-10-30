package com.github.emm035.openapi.annotation.processor.jaxrs;

import com.github.emm035.openapi.annotation.processor.api.MethodMatcher;
import com.github.emm035.openapi.annotation.processor.api.parsers.MethodParser;
import com.google.auto.common.MoreTypes;

import javax.lang.model.element.ExecutableElement;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

public class JaxRsMethodHandler implements MethodMatcher, MethodParser {

  @Override
  public Set<? extends Class<? extends Annotation>> getAnnotations() {
    return Set.of(
      GET.class,
      POST.class,
      PUT.class,
      DELETE.class,
      OPTIONS.class,
      HEAD.class
    );
  }

  @Override
  public boolean canParse(ExecutableElement element) {
    return element
      .getAnnotationMirrors()
      .stream()
      .map(
        mirror ->
          MoreTypes.asElement(mirror.getAnnotationType()).getAnnotation(HttpMethod.class)
      )
      .anyMatch(Objects::nonNull);
  }

  @Override
  public String parse(ExecutableElement element) {
    return element
      .getAnnotationMirrors()
      .stream()
      .map(
        mirror ->
          MoreTypes.asElement(mirror.getAnnotationType()).getAnnotation(HttpMethod.class)
      )
      .filter(Objects::nonNull)
      .map(HttpMethod::value)
      .findFirst()
      .get();
  }
}
