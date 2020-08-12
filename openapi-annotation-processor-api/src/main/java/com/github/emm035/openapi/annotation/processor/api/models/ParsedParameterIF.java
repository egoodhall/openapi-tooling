package com.github.emm035.openapi.annotation.processor.api.models;

import com.github.emm035.openapi.annotation.processor.api.SourceWriter;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value.Immutable;

@OpenApiProcessorStyle
@Immutable
public interface ParsedParameterIF extends BaseParameter<TypeName>, SourceWriter {
  @Override
  default CodeBlock toCode() {
    return CodeBlock
      .builder()
      .add("$T.builder()", Parameter.class)
      .indent()
      .add(".setName($S)", getName())
      .add(".setIn($S)", getIn())
      .add(".setType(new $T() {})", SourceWriter.typeReference(getType()))
      .add(".build()")
      .unindent()
      .build();
  }
}
