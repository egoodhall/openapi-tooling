package com.github.emm035.openapi.core.v3.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;

public class Json {
  public static final ObjectMapper MAPPER = MapperFactory.getInstance();

  public static String toJson(Object o) throws JsonProcessingException {
    return MAPPER.writeValueAsString(o);
  }

  public static JsonNode toTree(Object o) throws JsonProcessingException {
    return MAPPER.valueToTree(o);
  }

  public static <T> T readValue(String value, TypeReference<T> type) throws IOException {
    return MAPPER.readValue(value, type);
  }

  public static <T> T readValue(String value, Class<T> type) throws IOException {
    return MAPPER.readValue(value, type);
  }

  public static class MapperFactory {

    private MapperFactory() {}

    public static ObjectMapper getInstance() {
      return new ObjectMapper()
        .registerModule(new Jdk8Module())
        .registerModule(new GuavaModule())
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
  }
}
