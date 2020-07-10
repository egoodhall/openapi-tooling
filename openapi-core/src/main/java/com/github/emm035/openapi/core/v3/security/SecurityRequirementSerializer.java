package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class SecurityRequirementSerializer extends JsonSerializer<SecurityRequirement> {
  @Override
  public void serialize(SecurityRequirement value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeStartObject();
    gen.writeObjectField(value.getName(), value.getScopes());
    gen.writeEndObject();
  }
}
