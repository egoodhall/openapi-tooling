package com.github.emm035.openapi.core.v3;

public class PathsTest extends BasicJsonTest<Paths> {
  @Override
  public Class<Paths> getModelClass() {
    return Paths.class;
  }

  @Override
  public Paths getInstance() {
    return Paths.builder()
      .putAsMap("/some-path", new PathItemTest().getInstance())
      .putExtensions("x-paths", "test")
      .build();
  }
}
