package com.github.emm035.openapi.annotation.processor.jaxrs.parameter;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedParameter;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.github.emm035.openapi.core.v3.parameters.Parameter.Location;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.VariableElement;
import javax.ws.rs.PathParam;

@AutoService(ParamParser.class)
public class JaxRsPathParamParser implements ParamParser {

  @Override
  public boolean canParse(VariableElement parameter) {
    return parameter.getAnnotation(PathParam.class) != null;
  }

  @Override
  public ParsedParameter parse(VariableElement param) {
    return ParsedParameter
      .<TypeName>builder()
      .setIn(Location.PATH.toString())
      .setName(param.getAnnotation(PathParam.class).value())
      .setType(TypeName.get(param.asType()))
      .build();
  }
}
