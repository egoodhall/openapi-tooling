package com.github.emm035.openapi.core.v3.content;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

class ContentDeserializer extends StdDeserializer<Content> {

  ContentDeserializer() {
    super(Content.class);
  }

  @Override
  public Content deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    Content.Builder builder = Content.builder();

    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);
    for (Iterator<String> iter = node.fieldNames(); iter.hasNext();) {
      String fieldName = iter.next();
      if (fieldName.startsWith("x-")) {
        builder.putExtensions(fieldName, mapper.treeToValue(node.get(fieldName), Object.class));
      } else {
        builder.putMediaTypes(fieldName, mapper.treeToValue(node.get(fieldName), MediaType.class));
      }
    }

    return builder.build();
  }
}
