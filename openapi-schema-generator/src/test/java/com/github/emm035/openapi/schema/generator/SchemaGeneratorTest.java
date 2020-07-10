package com.github.emm035.openapi.schema.generator;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.github.emm035.openapi.schema.generator.models.Base;
import org.junit.Before;
import org.junit.Test;

public class SchemaGeneratorTest {
  private static final SchemaGenerator SCHEMA_GRAPH = SchemaGenerator
    .builder()
    .withCachedThreadPool()
    .withDefaultObjectMapper()
    .addModules(new TestModule())
    .build();

  @Before
  public void setUp() throws Exception {
    SCHEMA_GRAPH.clearCachedSchemas();
  }

  @Test
  public void getSchema_picksUpJsonSubTypes() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GRAPH.generateSchema(
      new TypeReference<Base>() {}
    );
    assertThat(SCHEMA_GRAPH.getAllSchemas())
      .hasSize(3)
      .containsKeys("Base", "Impl1", "Impl2");
  }
}
