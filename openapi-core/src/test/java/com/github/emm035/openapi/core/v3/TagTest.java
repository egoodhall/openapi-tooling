package com.github.emm035.openapi.core.v3;

import com.github.emm035.openapi.core.v3.metadata.ExternalDocumentationTest;

public class TagTest extends BasicJsonTest<Tag> {

  @Override
  public Class<Tag> getModelClass() {
    return Tag.class;
  }

  @Override
  public Tag getInstance() {
    return Tag
      .builder()
      .setDescription("This is a tag")
      .setName("My Tag")
      .setExternalDocs(new ExternalDocumentationTest().getInstance())
      .putExtensions("x-tag", "test")
      .build();
  }
}
