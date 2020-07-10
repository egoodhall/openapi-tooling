package com.github.emm035.openapi.core.v3.servers;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.emm035.openapi.core.v3.BasicJsonTest;

public class ServerVariableTest extends BasicJsonTest<ServerVariable> {

  @Override
  public Class<ServerVariable> getModelClass() {
    return ServerVariable.class;
  }

  @Override
  public ServerVariable getInstance() {
    return ServerVariable
      .builder()
      .setDefault("app")
      .addEnum("app", "build", "test")
      .setDescription("The environment to call")
      .putExtensions("x-server-variable", "test")
      .build();
  }
}
