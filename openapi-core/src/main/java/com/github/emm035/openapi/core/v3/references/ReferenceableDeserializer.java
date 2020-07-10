package com.github.emm035.openapi.core.v3.references;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class ReferenceableDeserializer<T extends Referenceable<T>> extends JsonDeserializer<Referenceable<T>> implements ContextualDeserializer {

  private JavaType valueType;

  @Override
  public Referenceable<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode jsonNode = mapper.readTree(p);
    if (jsonNode.path("$ref").isTextual()) {
      return Ref.of(jsonNode.path("$ref").asText());
    }

    return mapper.readValue(mapper.treeAsTokens(jsonNode), valueType);
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    ReferenceableDeserializer<?> deser = new ReferenceableDeserializer<>();
    deser.valueType = ctxt.getContextualType().getBindings().getBoundType(0);
    return deser;
  }
}
