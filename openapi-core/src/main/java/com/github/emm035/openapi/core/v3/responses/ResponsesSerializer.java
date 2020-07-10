package com.github.emm035.openapi.core.v3.responses;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.emm035.openapi.core.v3.references.Referenceable;

import java.io.IOException;
import java.util.Map;

public class ResponsesSerializer extends JsonSerializer<Responses> {
  @Override
  public void serialize(Responses value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    if (value.getDefault().isPresent()) {
      gen.writeObjectField("default", value.getDefault());
    }
    for (Map.Entry<Integer, Referenceable<Response>> entry : value.getResponses().entrySet()) {
      gen.writeObjectField(String.valueOf(entry.getKey()), entry.getValue());
    }
    for (Map.Entry<String, Object> entry : value.getExtensions().entrySet()) {
      gen.writeObjectField(entry.getKey(), entry.getValue());
    }
    gen.writeEndObject();
  }
}
