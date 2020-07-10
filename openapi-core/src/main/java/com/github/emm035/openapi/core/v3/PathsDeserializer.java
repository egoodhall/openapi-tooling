package com.github.emm035.openapi.core.v3;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Iterator;

class PathsDeserializer extends StdDeserializer<Paths> {

  PathsDeserializer() {
    super(Paths.class);
  }

  @Override
  public Paths deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    Paths.Builder builder = Paths.builder();

    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);
    for (Iterator<String> iter = node.fieldNames(); iter.hasNext();) {
      String fieldName = iter.next();
      if (fieldName.startsWith("x-")) {
        builder.putExtensions(
          fieldName,
          mapper.treeToValue(node.get(fieldName), Object.class)
        );
      } else {
        builder.putAsMap(
          fieldName,
          mapper.treeToValue(node.get(fieldName), PathItem.class)
        );
      }
    }

    return builder.build();
  }
}
