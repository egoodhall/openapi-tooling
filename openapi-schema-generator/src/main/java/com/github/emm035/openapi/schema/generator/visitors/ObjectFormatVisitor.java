package com.github.emm035.openapi.schema.generator.visitors;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.assisted.RefFactory;
import com.github.emm035.openapi.schema.generator.base.Generator;
import com.github.emm035.openapi.schema.generator.base.Schemas;
import com.github.emm035.openapi.schema.generator.base.TypeUtils;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.nested.NestedSchemaGenerator;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ObjectFormatVisitor
  extends JsonObjectFormatVisitor.Base
  implements Generator {
  private final JavaType javaType;
  private final Schemas schemas;
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
    Schemas schemas,
    RefFactory refFactory,
    NestedSchemaGenerator nestedSchemaGenerator
  ) {
    this.javaType = javaType;
    this.schemas = schemas;
    this.schemaExtension = schemaExtension;
    this.propertyExtension = propertyExtension;
    this.refFactory = refFactory;
    this.nestedSchemaGenerator = nestedSchemaGenerator;
    this.schemaBuilder = ObjectSchema.builder();
  }

  @Override
  public void property(BeanProperty prop) throws JsonMappingException {
    this.schemaBuilder.addRequired(prop.getName());
    optionalProperty(prop);
  }

  @Override
  public void optionalProperty(BeanProperty prop) throws JsonMappingException {
    String typeName = TypeUtils.toTypeName(TypeUtils.unwrap(prop.getType()));

    Referenceable<Schema> schema;
    if (schemas.exists(typeName)) {
      schema = refFactory.create(typeName);
    } else {
      schema =
        nestedSchemaGenerator.generateSchema(TypeUtils.unwrap(prop.getType()), false);
    }
    Schema modifiedSchema = propertyExtension.modify(schemas.resolve(schema), prop);

    // Overwrite schema if needed
    if (schema.isReferential()) {
      this.schemaBuilder.putProperties(
          prop.getName(),
          schemas.putSchema(prop.getName(), modifiedSchema)
        );
    } else {
      this.schemaBuilder.putProperties(prop.getName(), modifiedSchema);
    }
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) throws JsonMappingException {
    Schema schema = schemaExtension.modify(schemaBuilder.build(), javaType);
    if (asReference) {
      return schemas.putSchema(TypeUtils.toTypeName(javaType), schema);
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
