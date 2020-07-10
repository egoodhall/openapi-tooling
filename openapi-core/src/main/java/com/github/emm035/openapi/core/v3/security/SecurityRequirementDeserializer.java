package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import java.io.IOException;
import java.util.List;

class SecurityRequirementDeserializer extends JsonDeserializer<SecurityRequirement> {
  @Override
  public SecurityRequirement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    String name = node.fieldNames().next();
    List<String> scopes = Streams.stream(((ArrayNode) node.get(name)).iterator())
      .map(JsonNode::asText)
      .collect(ImmutableList.toImmutableList());

    return SecurityRequirement.builder()
      .setName(name)
      .setScopes(scopes)
      .build();
  }
}
