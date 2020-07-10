package com.github.emm035.openapi.core.v3;

public class PathItemTest extends BasicJsonTest<PathItem> {

  @Override
  public Class<PathItem> getModelClass() {
    return PathItem.class;
  }

  @Override
  public PathItem getInstance() {
    return PathItem
      .builder()
      .setDescription("This is a path")
      .putExtensions("x-path-item", "test")
      .build();
  }
}
