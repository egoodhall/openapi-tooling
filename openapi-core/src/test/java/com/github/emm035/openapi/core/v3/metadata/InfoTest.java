package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.BasicJsonTest;

public class InfoTest extends BasicJsonTest<Info> {
  @Override
  public Class<Info> getModelClass() {
    return Info.class;
  }

  @Override
  public Info getInstance() {
    return Info.builder()
      .setTitle("Example API")
      .setVersion("v1")
      .setDescription("This is the example API")
      .setTermsOfService("TOS")
      .setContact(new ContactTest().getInstance())
      .setLicense(new LicenseTest().getInstance())
      .putExtensions("x-info", "test")
      .build();
  }
}
