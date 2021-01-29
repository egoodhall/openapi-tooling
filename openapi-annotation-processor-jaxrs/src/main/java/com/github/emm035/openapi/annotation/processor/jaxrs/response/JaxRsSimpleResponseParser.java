package com.github.emm035.openapi.annotation.processor.jaxrs.response;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedResponse;
import com.github.emm035.openapi.annotation.processor.api.parsers.ResponseParser;
import com.squareup.javapoet.TypeName;
import java.util.Collections;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

public class JaxRsSimpleResponseParser implements ResponseParser {

  @Override
  public boolean canParse(ExecutableElement element) {
    return true;
  }

  @Override
  public Set<ParsedResponse> parse(ExecutableElement element) {
    if (element.getReturnType().getKind() == TypeKind.VOID) {
      return Collections.singleton(
        ParsedResponse
          .<TypeName>builder()
          .setStatusCode(204)
          .setType(TypeName.VOID)
          .build()
      );
    }
    return Collections.singleton(
      ParsedResponse
        .<TypeName>builder()
        .setStatusCode(200)
        .setType(TypeName.get(element.getReturnType()))
        .build()
    );
  }
}
