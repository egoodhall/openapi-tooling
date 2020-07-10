package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import java.util.Map;

public class SchemaResult {
  private final Schema schema;
  private final Map<String, Schema> referencedSchemas;

  private SchemaResult(Schema schema, Map<String, Schema> referencedSchemas) {
    this.schema = schema;
    this.referencedSchemas = referencedSchemas;
  }

  @JsonCreator
  public static SchemaResult of(
    @JsonProperty("schema") Schema schema,
    @JsonProperty("referencedSchemas") Map<String, Schema> referencedSchemas
  ) {
    return new SchemaResult(schema, referencedSchemas);
  }

  @JsonGetter("schema")
  public Schema getSchema() {
    return schema;
  }

  @JsonGetter("referencedSchemas")
  public Map<String, Schema> getReferencedSchemas() {
    return referencedSchemas;
  }
}
