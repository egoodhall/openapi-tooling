package com.github.emm035.openapi.annotation.processor.jaxrs.content;

import com.github.emm035.openapi.annotation.processor.api.parsers.ProducesContentTypeParser;
import com.google.common.collect.ImmutableSet;
import jakarta.ws.rs.Produces;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;

public class JaxRsProducesContentTypeParser implements ProducesContentTypeParser {

  @Override
  public boolean canParse(ExecutableElement method) {
    return Optional
      .ofNullable(method.getAnnotation(Produces.class))
      .or(
        () ->
          Optional.ofNullable(method.getEnclosingElement().getAnnotation(Produces.class))
      )
      .isPresent();
  }

  @Override
  public Set<String> parse(ExecutableElement method) {
    return Optional
      .ofNullable(method.getAnnotation(Produces.class))
      .or(
        () ->
          Optional.ofNullable(method.getEnclosingElement().getAnnotation(Produces.class))
      )
      .map(Produces::value)
      .map(ImmutableSet::copyOf)
      .orElseGet(ImmutableSet::of);
  }
}
