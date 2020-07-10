package com.github.emm035.openapi.schema.generator.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.base.Generator;
import com.github.emm035.openapi.schema.generator.base.Schemas;
import com.github.emm035.openapi.schema.generator.base.TypeUtils;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGeneratorInternalException;
import com.github.emm035.openapi.schema.generator.nested.SubTypeGenerator;
import com.github.emm035.openapi.schema.generator.visitors.ArrayFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.BooleanFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.MapFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.NumberFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.ObjectFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.StringFormatVisitor;
import com.github.emm035.openapi.schema.generator.visitors.SubTypedObjectFormatVisitor;
import com.google.inject.Inject;

public class SchemaGeneratorVisitorWrapper
  extends JsonFormatVisitorWrapper.Base
  implements Generator {
  private final StringFormatVisitor.Factory stringFormatVisitorFactory;
  private final NumberFormatVisitor.Factory numberFormatVisitorFactory;
  private final BooleanFormatVisitor.Factory booleanFormatVisitorFactory;
  private final ObjectFormatVisitor.Factory objectFormatVisitorFactory;
  private final ArrayFormatVisitor.Factory arrayFormatVisitorFactory;
  private final MapFormatVisitor.Factory mapFormatVisitorFactory;
  private final SubTypedObjectFormatVisitor.Factory compositeObjectFormatVisitorFactory;
  private final Schemas schemas;

  private Generator schemaGenerator = null;

  @Inject
  public SchemaGeneratorVisitorWrapper(
    StringFormatVisitor.Factory stringFormatVisitorFactory,
    NumberFormatVisitor.Factory numberFormatVisitorFactory,
    BooleanFormatVisitor.Factory booleanFormatVisitorFactory,
    ObjectFormatVisitor.Factory objectFormatVisitorFactory,
    ArrayFormatVisitor.Factory arrayFormatVisitorFactory,
    MapFormatVisitor.Factory mapFormatVisitorFactory,
    SubTypeGenerator.Factory subTypeGeneratorFactory,
    SubTypedObjectFormatVisitor.Factory compositeObjectFormatVisitorFactory,
    Schemas schemas
  ) {
    this.stringFormatVisitorFactory = stringFormatVisitorFactory;
    this.numberFormatVisitorFactory = numberFormatVisitorFactory;
    this.booleanFormatVisitorFactory = booleanFormatVisitorFactory;
    this.objectFormatVisitorFactory = objectFormatVisitorFactory;
    this.arrayFormatVisitorFactory = arrayFormatVisitorFactory;
    this.mapFormatVisitorFactory = mapFormatVisitorFactory;
    this.compositeObjectFormatVisitorFactory = compositeObjectFormatVisitorFactory;
    this.schemas = schemas;
  }

  @Override
  public JsonObjectFormatVisitor expectObjectFormat(JavaType type)
    throws JsonMappingException {
    if (!TypeUtils.getTypeImplementations(type).isEmpty()) {
      SubTypedObjectFormatVisitor subTypedObjectFormatVisitor = compositeObjectFormatVisitorFactory.create(
        type
      );
      schemaGenerator = subTypedObjectFormatVisitor;
      return subTypedObjectFormatVisitor;
    }
    ObjectFormatVisitor objectFormatVisitor = objectFormatVisitorFactory.create(type);
    schemaGenerator = objectFormatVisitor;
    return objectFormatVisitor;
  }

  @Override
  public JsonArrayFormatVisitor expectArrayFormat(JavaType type)
    throws JsonMappingException {
    ArrayFormatVisitor arrayFormatVisitor = arrayFormatVisitorFactory.create(type);
    schemaGenerator = arrayFormatVisitor;
    return arrayFormatVisitor;
  }

  @Override
  public JsonStringFormatVisitor expectStringFormat(JavaType type)
    throws JsonMappingException {
    StringFormatVisitor stringFormatVisitor = stringFormatVisitorFactory.create(type);
    schemaGenerator = stringFormatVisitor;
    return stringFormatVisitor;
  }

  @Override
  public JsonNumberFormatVisitor expectNumberFormat(JavaType type)
    throws JsonMappingException {
    NumberFormatVisitor numberFormatVisitor = numberFormatVisitorFactory.create(type);
    schemaGenerator = numberFormatVisitor;
    return numberFormatVisitor;
  }

  @Override
  public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type)
    throws JsonMappingException {
    NumberFormatVisitor numberFormatVisitor = numberFormatVisitorFactory.create(type);
    schemaGenerator = numberFormatVisitor;
    return numberFormatVisitor;
  }

  @Override
  public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type)
    throws JsonMappingException {
    BooleanFormatVisitor booleanFormatVisitor = booleanFormatVisitorFactory.create(type);
    schemaGenerator = booleanFormatVisitor;
    return booleanFormatVisitor;
  }

  @Override
  public JsonMapFormatVisitor expectMapFormat(JavaType type) throws JsonMappingException {
    MapFormatVisitor mapFormatVisitor = mapFormatVisitorFactory.create(type);
    schemaGenerator = mapFormatVisitor;
    return mapFormatVisitor;
  }

  @Override
  public Referenceable<Schema> emit(boolean asReference) throws JsonMappingException {
    if (schemaGenerator == null) {
      throw new SchemaGeneratorInternalException(
        "Schema not generated. Call ObjectMapper#acceptJsonFormatVisitor(JavaType, JsonFormatVisitorWrapper) to populate this field."
      );
    }
    return schemaGenerator.emit(asReference);
  }
}
