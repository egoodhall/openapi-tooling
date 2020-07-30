package com.github.emm035.openapi.schema.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.AbstractIntegerSchema;
import com.github.emm035.openapi.core.v3.schemas.AbstractNumberSchema;
import com.github.emm035.openapi.core.v3.schemas.IntegerSchema;
import com.github.emm035.openapi.core.v3.schemas.NumberSchema;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.schemas.StringSchema;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.github.emm035.openapi.schema.generator.models.SimpleGeneric;
import com.github.emm035.openapi.schema.generator.models.SimpleModel;
import com.google.common.collect.ImmutableList;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class SchemaGeneratorExtensionTest {

  @Test
  public void schemaExtension_calledOnAllPrimitiveSchemas()
    throws SchemaGenerationException {
    ImmutableList.Builder<Schema> builder = ImmutableList.builder();

    SchemaGenerator generator = SchemaGenerator
      .builder()
      .bindSchemaExtension(
        (schema, data) -> {
          builder.add(schema);
          return schema;
        }
      )
      .build();

    generator.generateSchema(int.class);
    generator.generateSchema(long.class);
    generator.generateSchema(double.class);
    generator.generateSchema(float.class);
    generator.generateSchema(String.class);
    Assertions
      .assertThat(builder.build())
      .containsExactlyInAnyOrder(
        IntegerSchema.builder().setFormat(IntegerSchema.Format.INT32).build(),
        IntegerSchema.builder().setFormat(IntegerSchema.Format.INT64).build(),
        NumberSchema.builder().setFormat(NumberSchema.Format.DOUBLE).build(),
        NumberSchema.builder().setFormat(NumberSchema.Format.FLOAT).build(),
        StringSchema.builder().build()
      );
  }

  @Test
  public void propertyExtension_calledOnAllProperties() throws SchemaGenerationException {
    ImmutableList.Builder<Schema> builder = ImmutableList.builder();

    SchemaGenerator generator = SchemaGenerator
      .builder()
      .bindPropertyExtension(
        (schema, data) -> {
          builder.add(schema);
          return schema;
        }
      )
      .build();

    generator.generateSchema(new TypeReference<SimpleGeneric<String>>() {});
    Assertions
      .assertThat(builder.build())
      .containsExactlyInAnyOrder(
        IntegerSchema.builder().setFormat(IntegerSchema.Format.INT32).build(),
        StringSchema.builder().build()
      );
  }

  @Test
  public void schemaProperty_overridesPropertyName() throws SchemaGenerationException {
    SchemaGenerator generator = SchemaGenerator.newInstance();

    Referenceable<Schema> referenceable = generator.generateSchema(
      new TypeReference<SimpleGeneric<String>>() {}
    );
    ObjectSchema schema = (ObjectSchema) generator.resolve(referenceable);
    Assertions
      .assertThat(schema.getProperties().keySet())
      .containsExactlyInAnyOrder("type", "overridden-property");
  }
}
