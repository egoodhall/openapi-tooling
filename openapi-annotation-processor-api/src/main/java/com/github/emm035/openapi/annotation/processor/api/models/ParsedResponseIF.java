package com.github.emm035.openapi.annotation.processor.api.models;

import com.github.emm035.openapi.annotation.processor.api.SourceWriter;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiProcessorStyle
public interface ParsedResponseIF extends BaseResponse<TypeName>, SourceWriter {
  @Override
  default CodeBlock toCode() {
    return CodeBlock
      .builder()
      .add("$T.builder()", Response.class)
      .indent()
      .add(".setStatusCode($L)", getStatusCode())
      .add(".setType(new $T() {})", SourceWriter.typeReference(getType()))
      .add(".build()")
      .unindent()
      .build();
  }
}
