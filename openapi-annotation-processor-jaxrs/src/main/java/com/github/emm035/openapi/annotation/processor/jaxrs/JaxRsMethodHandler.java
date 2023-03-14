package com.github.emm035.openapi.annotation.processor.jaxrs;

import com.github.emm035.openapi.annotation.processor.api.MethodMatcher;
import com.github.emm035.openapi.annotation.processor.api.parsers.MethodParser;
import com.google.auto.common.MoreTypes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;

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
