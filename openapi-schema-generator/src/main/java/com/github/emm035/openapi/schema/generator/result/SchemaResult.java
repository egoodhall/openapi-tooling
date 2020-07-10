package com.github.emm035.openapi.schema.generator.result;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import java.util.Map;

public class SchemaResult {
  private final Schema schema;
  private final Map<String, Schema> referencedSchemas;

  private SchemaResult(Schema schema, Map<String, Schema> referencedSchemas) {
    this.schema = schema;
    this.referencedSchemas = referencedSchemas;
  }

  public static SchemaResult of(Schema schema, Map<String, Schema> referencedSchemas) {
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
