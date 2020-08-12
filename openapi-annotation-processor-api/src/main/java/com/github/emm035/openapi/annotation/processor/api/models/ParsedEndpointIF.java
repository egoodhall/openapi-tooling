package com.github.emm035.openapi.annotation.processor.api.models;

import com.github.emm035.openapi.annotation.processor.api.SourceWriter;
import com.squareup.javapoet.CodeBlock;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiProcessorStyle
public interface ParsedEndpointIF
  extends BaseEndpoint<ParsedParameter, ParsedResponse>, SourceWriter {
  @Override
  default CodeBlock toCode() {
    return CodeBlock
      .builder()
      .add("$T.builder()", Endpoint.class)
      .indent()
      .add(".setMethod($S)", getMethod())
      .add(".setConsumes(")
      .add(SourceWriter.stringList(getConsumes()))
      .add(")")
      .add(".setProduces(")
      .add(SourceWriter.stringList(getConsumes()))
      .add(")")
      .add(".setParameters(")
      .add(SourceWriter.list(getParameters()))
      .add(")")
      .add(".setResponses(")
      .add(SourceWriter.list(getResponses()))
      .add(")")
      .add(".build()")
      .unindent()
      .build();
  }
}
