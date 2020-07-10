package com.github.emm035.openapi.core.v3.schemas;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

class SchemaDeserializer extends JsonDeserializer<Schema> {

  @Override
  public Schema deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);
    JsonParser tokens = mapper.treeAsTokens(node);
    tokens.nextToken();
    return (Schema) resolveDeserializer(ctxt, node).deserialize(tokens, ctxt);
  }

  private JsonDeserializer<?> resolveDeserializer(
    DeserializationContext ctxt,
    JsonNode node
  )
    throws JsonMappingException {
    if (node.has("allOf")) {
      JavaType type = ctxt.getTypeFactory().constructType(AllOfSchema.class);
      return ctxt.findRootValueDeserializer(type);
    } else if (node.has("anyOf")) {
      JavaType type = ctxt.getTypeFactory().constructType(AnyOfSchema.class);
      return ctxt.findRootValueDeserializer(type);
    } else if (node.has("oneOf")) {
      JavaType type = ctxt.getTypeFactory().constructType(OneOfSchema.class);
      return ctxt.findRootValueDeserializer(type);
    }
    JavaType type = ctxt.getTypeFactory().constructType(TypedSchema.class);
    return ctxt.findRootValueDeserializer(type);
  }
}
