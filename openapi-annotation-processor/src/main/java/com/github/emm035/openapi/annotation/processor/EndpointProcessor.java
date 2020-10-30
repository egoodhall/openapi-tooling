package com.github.emm035.openapi.annotation.processor;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedEndpoint;
import com.github.emm035.openapi.annotation.processor.api.parsers.ConsumesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.MethodParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.Parser;
import com.github.emm035.openapi.annotation.processor.api.parsers.PathParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ProducesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ResponseParser;
import com.google.auto.common.MoreElements;
import com.google.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;

public class EndpointProcessor {
  private final Messager messager;
  private final Set<ParsedEndpoint> parsedEndpoints;
  private final Set<ConsumesContentTypeParser> consumesContentTypeParsers;
  private final Set<ProducesContentTypeParser> producesContentTypeParsers;
  private final Set<ParamParser> paramParsers;
  private final Set<ResponseParser> responseParsers;
  private final Set<PathParser> pathParsers;
  private final Set<MethodParser> methodParsers;

  @Inject
  public EndpointProcessor(
    Messager messager,
    Set<ParsedEndpoint> parsedEndpoints,
    Set<ConsumesContentTypeParser> consumesContentTypeParsers,
    Set<ProducesContentTypeParser> producesContentTypeParsers,
    Set<ParamParser> paramParsers,
    Set<ResponseParser> responseParsers,
    Set<PathParser> pathParsers,
    Set<MethodParser> methodParsers
  ) {
    this.messager = messager;
    this.parsedEndpoints = parsedEndpoints;
    this.consumesContentTypeParsers = consumesContentTypeParsers;
    this.producesContentTypeParsers = producesContentTypeParsers;
    this.paramParsers = paramParsers;
    this.responseParsers = responseParsers;
    this.pathParsers = pathParsers;
    this.methodParsers = methodParsers;
  }

  public void parseEndpoint(
    Class<? extends Annotation> annotationClass,
    Element element
  ) {
    if (element.getKind() != ElementKind.METHOD) {
      messager.printMessage(
        Diagnostic.Kind.WARNING,
        String.format(
          "Unable to process elements of type %s as endpoints (annotated with: %s)",
          element.getKind().name(),
          annotationClass.getSimpleName()
        )
      );
      return;
    }
    ExecutableElement method = MoreElements.asExecutable(element);

    ParsedEndpoint endpoint = ParsedEndpoint
      .builder()
      .setConsumes(
        getMatchingParser("consumed media types", consumesContentTypeParsers, method)
          .parse(method)
      )
      .setProduces(
        getMatchingParser("produced media types", producesContentTypeParsers, method)
          .parse(method)
      )
      .setParameters(
        method
          .getParameters()
          .stream()
          .map(param -> getMatchingParser("parameter", paramParsers, param).parse(param))
          .collect(Collectors.toUnmodifiableSet())
      )
      .setResponses(getMatchingParser("response", responseParsers, method).parse(method))
      .setMethod(getMatchingParser("HTTP method", methodParsers, method).parse(method))
      .setPath(getMatchingParser("path", pathParsers, method).parse(method))
      .build();

    parsedEndpoints.add(endpoint);
    messager.printMessage(
      Diagnostic.Kind.NOTE,
      String.format("Parsed %s %s", endpoint.getMethod(), endpoint.getPath())
    );
  }

  private <E extends Element, T extends Parser<E, ?>> T getMatchingParser(
    String type,
    Set<T> parsers,
    E element
  ) {
    Set<T> matchingParsers = parsers
      .stream()
      .filter(parser -> parser.canParse(element))
      .collect(Collectors.toUnmodifiableSet());

    if (matchingParsers.isEmpty()) {
      String message = String.format(
        "No parsers found to match the %s on \"%s\". Unable to parse element.",
        type,
        element.getSimpleName()
      );
      messager.printMessage(Diagnostic.Kind.ERROR, message);
      throw new IllegalStateException(message);
    }

    T selectedParser = matchingParsers.iterator().next();

    if (matchingParsers.size() > 1) {
      messager.printMessage(
        Diagnostic.Kind.WARNING,
        String.format(
          "Found multiple parsers matching \"%s\" - this may cause non-deterministic behavior. Using %s",
          element.getSimpleName(),
          selectedParser.getClass().getSimpleName()
        )
      );
    }

    return selectedParser;
  }
}
