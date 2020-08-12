package com.github.emm035.openapi.annotation.processor;

import com.github.emm035.openapi.annotation.processor.api.MethodMatcher;
import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.SetMultimap;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.lang.model.element.Element;

public class OpenApiProcessingStep implements BasicAnnotationProcessor.ProcessingStep {
  private final Set<MethodMatcher> endpointFinders;
  private final EndpointProcessor endpointProcessor;

  @Inject
  public OpenApiProcessingStep(
    Set<MethodMatcher> endpointFinders,
    EndpointProcessor endpointProcessor
  ) {
    this.endpointFinders = endpointFinders;
    this.endpointProcessor = endpointProcessor;
  }

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return endpointFinders
      .stream()
      .map(MethodMatcher::getAnnotations)
      .flatMap(Set::stream)
      .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Set<? extends Element> process(
    SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation
  ) {
    elementsByAnnotation.forEach(endpointProcessor::parseEndpoint);
    return Collections.emptySet();
  }
}
