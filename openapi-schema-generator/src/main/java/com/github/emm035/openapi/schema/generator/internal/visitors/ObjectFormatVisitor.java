package com.github.emm035.openapi.schema.generator.internal.visitors;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.annotations.RequireNonOptionalScalarProperties;
import com.github.emm035.openapi.schema.generator.annotations.SchemaProperty;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.Generator;
import com.github.emm035.openapi.schema.generator.internal.RefFactory;
import com.github.emm035.openapi.schema.generator.internal.SchemasCache;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.generators.NestedSchemaGenerator;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.util.Optional;

public class ObjectFormatVisitor
  extends JsonObjectFormatVisitor.Base
  implements Generator {
  private final JavaType javaType;
  private final boolean requireNonOptionalScalarProperties;
  private final SchemasCache schemasCache;
  private final SchemaExtension schemaExtension;
  private final PropertyExtension propertyExtension;
  private final RefFactory refFactory;
  private final NestedSchemaGenerator nestedSchemaGenerator;
  private final ObjectSchema.Builder schemaBuilder;

  @Inject
  public ObjectFormatVisitor(
    @Assisted JavaType javaType,
    @Extension SchemaExtension schemaExtension,
    @Extension PropertyExtension propertyExtension,
    @RequireNonOptionalScalarProperties boolean requireNonOptionalScalarProperties,
    SchemasCache schemasCache,
    RefFactory refFactory,
    NestedSchemaGenerator nestedSchemaGenerator
  ) {
    this.javaType = javaType;
    this.requireNonOptionalScalarProperties = requireNonOptionalScalarProperties;
    this.schemasCache = schemasCache;
    this.schemaExtension = schemaExtension;
    this.propertyExtension = propertyExtension;
    this.refFactory = refFactory;
    this.nestedSchemaGenerator = nestedSchemaGenerator;
    this.schemaBuilder = ObjectSchema.builder();
  }

  @Override
  public void property(BeanProperty prop) throws JsonMappingException {
    optionalProperty(prop);
  }

  @Override
  public void optionalProperty(BeanProperty prop) throws JsonMappingException {
    String typeName = TypeUtils.toTypeName(TypeUtils.unwrap(prop.getType()));
    if (
      prop.isRequired() ||
      (requireNonOptionalScalarProperties && !TypeUtils.isOptional(prop.getType()))
    ) {
      this.schemaBuilder.addRequired(prop.getName());
    }

    Referenceable<Schema> schema;
    if (schemasCache.contains(typeName)) {
      schema = refFactory.create(typeName);
    } else {
      schema =
        nestedSchemaGenerator.generateSchema(TypeUtils.unwrap(prop.getType()), false);
    }
    Schema modifiedSchema = propertyExtension.modify(schemasCache.resolve(schema), prop);

    // Overwrite schema if needed
    if (schema.isReferential()) {
      this.schemaBuilder.putProperties(
          getPropertyName(prop),
          schemasCache.putSchema(prop.getName(), modifiedSchema)
        );
    } else {
      this.schemaBuilder.putProperties(getPropertyName(prop), modifiedSchema);
    }
  }

  private String getPropertyName(BeanProperty prop) {
    return Optional
      .ofNullable(prop.getAnnotation(SchemaProperty.class))
      .map(SchemaProperty::value)
      .orElseGet(prop::getName);
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) throws JsonMappingException {
    Schema schema = schemaExtension.modify(schemaBuilder.build(), javaType);
    if (asReference) {
      return schemasCache.putSchema(TypeUtils.toTypeName(javaType), schema);
    }
    return schema;
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    ObjectFormatVisitor create(JavaType javaType);
  }
}
