package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.BasicJsonTest;

public class ContactTest extends BasicJsonTest<Contact> {

  @Override
  public Class<Contact> getModelClass() {
    return Contact.class;
  }

  @Override
  public Contact getInstance() {
    return Contact
      .builder()
      .setName("Robert Tables")
      .setEmail("little.bobby@tables.com")
      .setUrl("https://example.com/contact")
      .putExtensions("x-contact", "test")
      .build();
  }
}
