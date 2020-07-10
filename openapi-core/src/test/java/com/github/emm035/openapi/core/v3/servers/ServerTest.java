package com.github.emm035.openapi.core.v3.servers;

import com.github.emm035.openapi.core.v3.BasicJsonTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest extends BasicJsonTest<Server> {

  @Override
  public Class<Server> getModelClass() {
    return Server.class;
  }

  @Override
  public Server getInstance() {
    return Server.builder()
      .setDescription("The example server")
      .setUrl("https://{env}.example.com/")
      .putVariables("env", new ServerVariableTest().getInstance())
      .putExtensions("x-server", "test")
      .build();
  }
}
