package com.github.emm035.openapi.annotation.processor.jaxrs.parameter;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedParameter;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.github.emm035.openapi.core.v3.parameters.Parameter;
import com.squareup.javapoet.TypeName;
import jakarta.ws.rs.CookieParam;
import javax.lang.model.element.VariableElement;

public class JaxRsCookieParamParser implements ParamParser {

  @Override
  public boolean canParse(VariableElement element) {
    return element.getAnnotation(CookieParam.class) != null;
  }

  @Override
  public ParsedParameter parse(VariableElement param) {
    return ParsedParameter
      .<TypeName>builder()
      .setIn(Parameter.Location.PATH.toString())
      .setName(param.getAnnotation(CookieParam.class).value())
      .setType(TypeName.get(param.asType()))
      .build();
  }
}
