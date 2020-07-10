package com.github.emm035.openapi.core.v3.parameters;

import com.github.emm035.openapi.core.v3.BasicJsonTest;
import com.github.emm035.openapi.core.v3.schemas.StringSchema;

public class ParameterTest extends BasicJsonTest<Parameter> {
  @Override
  public Class<Parameter> getModelClass() {
    return Parameter.class;
  }

  @Override
  public Parameter getInstance() {
    return Parameter.cookieBuilder()
      .setName("test")
      .setSchema(StringSchema.builder().build())
      .build();
  }
}
