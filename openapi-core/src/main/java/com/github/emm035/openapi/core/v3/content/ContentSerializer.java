package com.github.emm035.openapi.core.v3.content;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Map;

class ContentSerializer extends JsonSerializer<Content> {

  @Override
  public void serialize(Content value, JsonGenerator gen, SerializerProvider serializers)
    throws IOException, JsonProcessingException {
    gen.writeStartObject();
    for (Map.Entry<String, MediaType> entry : value.getMediaTypes().entrySet()) {
      gen.writeObjectField(entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, Object> entry : value.getExtensions().entrySet()) {
      gen.writeObjectField(entry.getKey(), entry.getValue());
    }
    gen.writeEndObject();
  }
}
