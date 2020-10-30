package com.github.emm035.openapi.schema.generator;

import com.github.emm035.openapi.core.v3.references.Ref;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.AbstractIntegerSchema;
import com.github.emm035.openapi.core.v3.schemas.BooleanSchema;
import com.github.emm035.openapi.core.v3.schemas.IntegerSchema;
import com.github.emm035.openapi.core.v3.schemas.NumberSchema;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.OneOfSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.github.emm035.openapi.schema.generator.models.Base;
import com.github.emm035.openapi.schema.generator.models.Impl1;
import com.github.emm035.openapi.schema.generator.models.ModelWithOptionals;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaGeneratorTest {
  private static final SchemaGenerator SCHEMA_GENERATOR = SchemaGenerator.newInstance();

  @Before
  public void setUp() throws Exception {
    SCHEMA_GENERATOR.clearCachedSchemas();
  }

  @Test
  public void getSchema_withSubTypes_picksUpJsonSubTypes()
    throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(Base.class);
    assertThat(schema).isInstanceOf(OneOfSchema.class);
    assertThat(SCHEMA_GENERATOR.getCachedSchemas())
      .hasSize(3)
      .containsKeys("Base", "Impl1", "Impl2");
  }

  @Test
  public void getSchema_withoutSubTypes_returnsReference()
    throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(Impl1.class);
    assertThat(schema).isInstanceOf(Ref.class);
  }

  @Test
  public void resolve_returnsReferenceIfFound() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.resolve(
      SCHEMA_GENERATOR.generateSchema(Impl1.class)
    );
    assertThat(schema).isInstanceOf(ObjectSchema.class);
    assertThat(((ObjectSchema) schema).getRequired()).isEmpty();
  }

  @Test
  public void getSchema_boolean_generatesBooleanSchema()
    throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(boolean.class);
    assertThat(schema).isInstanceOf(BooleanSchema.class);
  }

  @Test
  public void getSchema_int_generatesIntegerSchema() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(int.class);
    assertThat(schema).isInstanceOf(IntegerSchema.class);
    assertThat(((IntegerSchema) schema).getFormat())
      .contains(AbstractIntegerSchema.Format.INT32);
  }

  @Test
  public void getSchema_long_generatesIntegerSchema() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(long.class);
    assertThat(schema).isInstanceOf(IntegerSchema.class);
    assertThat(((IntegerSchema) schema).getFormat())
      .contains(AbstractIntegerSchema.Format.INT64);
  }

  @Test
  public void getSchema_float_generatesNumberSchema() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(float.class);
    assertThat(schema).isInstanceOf(NumberSchema.class);
    assertThat(((NumberSchema) schema).getFormat()).contains(NumberSchema.Format.FLOAT);
  }

  @Test
  public void getSchema_double_generatesNumberSchema() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.generateSchema(double.class);
    assertThat(schema).isInstanceOf(NumberSchema.class);
    assertThat(((NumberSchema) schema).getFormat()).contains(NumberSchema.Format.DOUBLE);
  }

  @Test
  public void getSchema_requireNonOptionalScalarProperties_generatesRequiredProperties() throws SchemaGenerationException {
    Referenceable<Schema> schema = SCHEMA_GENERATOR.resolve(SCHEMA_GENERATOR.generateSchema(ModelWithOptionals.class));
    assertThat(schema).isInstanceOf(ObjectSchema.class);
    assertThat(((ObjectSchema) schema).getRequired()).containsExactly("instant");
  }
}
