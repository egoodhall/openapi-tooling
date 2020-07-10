package com.github.emm035.openapi.core.v3.callbacks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emm035.openapi.core.v3.PathItem;

import java.io.IOException;

class CallbackDeserializer extends JsonDeserializer<Callback> {
  @Override
  public Callback deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    String expression = node.fieldNames().next();
    PathItem pathItem = mapper.treeToValue(node.get(expression), PathItem.class);

    return Callback.builder()
      .setExpression(expression)
      .setPathItem(pathItem)
      .build();
  }
}
