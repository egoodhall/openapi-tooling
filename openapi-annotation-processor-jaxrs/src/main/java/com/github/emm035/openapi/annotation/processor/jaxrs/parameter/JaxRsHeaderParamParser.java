package com.github.emm035.openapi.annotation.processor.jaxrs.parameter;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedParameter;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import javax.ws.rs.HeaderParam;

import static com.github.emm035.openapi.core.v3.parameters.Parameter.Location;

public class JaxRsHeaderParamParser implements ParamParser {
  @Override
  public boolean canParse(VariableElement element) {
    return element.getAnnotation(HeaderParam.class) != null;
  }

  @Override
  public ParsedParameter parse(VariableElement param) {
    return ParsedParameter.builder()
      .setIn(Location.HEADER.toString())
      .setName(param.getAnnotation(HeaderParam.class).value())
      .setType(TypeName.get(param.asType()))
      .build();
  }
}
