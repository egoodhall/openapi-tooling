package com.github.emm035.openapi.core.v3.links;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

class LinkDeserializer extends JsonDeserializer<Link> {
  @Override
  public Link deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    if (node.has("operationId")) {
      return mapper.treeToValue(node, IdLink.class);
    }
    if (node.has("operationRef")) {
      return mapper.treeToValue(node, RefLink.class);
    }
    throw ctxt.instantiationException(Link.class, "Unable to instantiate Link. Either 'operationId' or 'operationRef' is required");
  }
}
