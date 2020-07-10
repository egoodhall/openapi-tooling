package com.github.emm035.openapi.core.v3.examples;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Map;

class ExampleDeserializer extends JsonDeserializer<Example> {
  private static final Map<String, Class<? extends Example>> MAPPINGS = ImmutableMap.of(
    "value",
    ValueExample.class,
    "externalValue",
    ExternalExample.class
  );

  @Override
  public Example deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    if (node.has("value")) {
      return mapper.treeToValue(node, ValueExample.class);
    }
    if (node.has("externalValue")) {
      return mapper.treeToValue(node, ExternalExample.class);
    }
    throw ctxt.instantiationException(
      Example.class,
      "Unable to instantiate any example type from " + node.toString()
    );
  }
}
