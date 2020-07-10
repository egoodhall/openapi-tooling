package com.github.emm035.openapi.core.v3.references;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.emm035.openapi.core.v3.jackson.Json;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import org.junit.Test;

import java.io.IOException;

public class RefTest {
  @Test
  public void deserialize_ref_deserailzesCorrectly() throws IOException {
    var ref = Json.MAPPER.readValue("{ \"$ref\": \"#/definitions/schemas/Test\"}", new TypeReference<Referenceable<Schema>>() {});
    var schema = Json.MAPPER.readValue("{ \"type\": \"string\" }", new TypeReference<Referenceable<Schema>>() {});
    System.out.println(ref + " | " + schema);
  }
}
