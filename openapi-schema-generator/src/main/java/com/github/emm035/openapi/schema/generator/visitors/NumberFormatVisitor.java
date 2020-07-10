package com.github.emm035.openapi.schema.generator.visitors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.IntegerSchema;
import com.github.emm035.openapi.core.v3.schemas.NumberSchema;
import com.github.emm035.openapi.core.v3.schemas.NumericSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.assisted.Extension;
import com.github.emm035.openapi.schema.generator.base.Generator;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.util.Optional;

public class NumberFormatVisitor
  extends JsonNumberFormatVisitor.Base
  implements JsonIntegerFormatVisitor, Generator {
  private final JavaType javaType;
  private final SchemaExtension schemaExtension;
  private NumericSchema<?> schema;

  @Inject
  public NumberFormatVisitor(
    @Assisted JavaType javaType,
    @Extension SchemaExtension schemaExtension
  ) {
    this.javaType = javaType;
    this.schemaExtension = schemaExtension;
  }

  @Override
  public void numberType(JsonParser.NumberType type) {
    switch (type) {
      case INT:
      case LONG:
      case BIG_INTEGER:
        schema = buildIntegerSchema(type);
        break;
      case FLOAT:
      case DOUBLE:
      case BIG_DECIMAL:
        schema = buildNumberSchema(type);
    }
  }

  private IntegerSchema buildIntegerSchema(JsonParser.NumberType type) {
    IntegerSchema.Format format;
    switch (type) {
      case INT:
        format = IntegerSchema.Format.INT32;
        break;
      case LONG:
      case BIG_INTEGER:
        format = IntegerSchema.Format.INT64;
      default:
        throw new IllegalArgumentException(
          "Invalid format " + type + " for integer schema"
        );
    }
    return IntegerSchema.builder().setFormat(format).build();
  }

  private NumberSchema buildNumberSchema(JsonParser.NumberType type) {
    NumberSchema.Format format;
    switch (type) {
      case FLOAT:
        format = NumberSchema.Format.FLOAT;
        break;
      case DOUBLE:
      case BIG_DECIMAL:
        format = NumberSchema.Format.DOUBLE;
      default:
        throw new IllegalArgumentException(
          "Invalid format " + type + " for integer schema"
        );
    }
    return NumberSchema.builder().setFormat(format).build();
  }

  private Optional<String> getFormat(JsonParser.NumberType type) {
    switch (type) {
      case INT:
        return Optional.of("int32");
      case LONG:
        return Optional.of("int64");
      case FLOAT:
        return Optional.of("float");
      case DOUBLE:
        return Optional.of("double");
      case BIG_INTEGER:
      case BIG_DECIMAL:
      default:
        return Optional.empty();
    }
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) {
    return schemaExtension.modify(schema, javaType);
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    NumberFormatVisitor create(JavaType javaType);
  }
}
