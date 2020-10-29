package com.github.emm035.openapi.core.v3;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import com.github.emm035.openapi.core.v3.jackson.Json;
import com.google.common.collect.Streams;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import org.junit.Test;

public class OpenApiTest {
  private static final ObjectMapper om = Json.MAPPER;

  @Test
  public void testWithHubspotCrmCardsSpec() throws IOException {
    testSwagger("hs-crm-cards.json");
  }

  @Test
  public void testWithHubspotPropertiesSpec() throws IOException {
    testSwagger("hs-properties.json");
  }

  @Test
  public void testWithHubspotTimelineSpec() throws IOException {
    testSwagger("hs-timeline.json");
  }

  private void testSwagger(String filename) {
    try {
      String jsonString = loadResourceFileAsString(filename);
      JsonNode readFromFile = om.readTree(jsonString);
      OpenApi parsed = om.readValue(jsonString, OpenApi.class);
      JsonNode serialized = om.valueToTree(parsed);

      onlyRemovedEmptyNodes(readFromFile, serialized);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void onlyRemovedEmptyNodes(JsonNode source, JsonNode target) {
    JsonNode diff = JsonDiff.asJson(source, target, EnumSet.noneOf(DiffFlags.class));

    boolean onlyRemovedEmptyContent = Streams
      .stream(diff.iterator())
      .map(
        node ->
          node.get("op").asText().equals("remove") &&
          node.get("value").isContainerNode() &&
          node.get("value").size() == 0
      )
      .reduce(true, (l, r) -> l && r);

    // Uncomment to log json
    if (!onlyRemovedEmptyContent) {
      System.err.println(source.toString());
      System.err.println(target.toString());
      System.err.println(diff);
    }
    assertThat(onlyRemovedEmptyContent).isTrue();
  }

  private String loadResourceFileAsString(String fileName) {
    InputStream resourceAsStream =
      OpenApiTest.class.getClassLoader().getResourceAsStream(fileName);
    try {
      return new String(ByteStreams.toByteArray(resourceAsStream));
    } catch (IOException e) {
      throw new RuntimeException("Unable to load resource '" + fileName + "'", e);
    }
  }
}
