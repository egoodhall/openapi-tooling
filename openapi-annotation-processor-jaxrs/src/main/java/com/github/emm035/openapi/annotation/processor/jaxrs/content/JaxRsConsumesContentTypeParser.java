package com.github.emm035.openapi.annotation.processor.jaxrs.content;

import com.github.emm035.openapi.annotation.processor.api.parsers.ConsumesContentTypeParser;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.ws.rs.Consumes;

@AutoService(ConsumesContentTypeParser.class)
public class JaxRsConsumesContentTypeParser implements ConsumesContentTypeParser {

  @Override
  public boolean canParse(ExecutableElement method) {
    return Optional
      .ofNullable(method.getAnnotation(Consumes.class))
      .or(
        () ->
          Optional.ofNullable(method.getEnclosingElement().getAnnotation(Consumes.class))
      )
      .isPresent();
  }

  @Override
  public Set<String> parse(ExecutableElement method) {
    return Optional
      .ofNullable(method.getAnnotation(Consumes.class))
      .or(
        () ->
          Optional.ofNullable(method.getEnclosingElement().getAnnotation(Consumes.class))
      )
      .map(Consumes::value)
      .map(ImmutableSet::copyOf)
      .orElseGet(ImmutableSet::of);
  }
}
