package com.github.emm035.openapi.schema.generator.internal.generators;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.AllOfSchema;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.RefFactory;
import com.github.emm035.openapi.schema.generator.internal.SchemasCache;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.annotations.Internal;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SubTypeGenerator {
  private final SchemasCache schemasCache;
  private final RefFactory refFactory;
  private final NestedSchemaGenerator nestedSchemaGenerator;
  private final ObjectSchema baseType;
  private final SchemaExtension schemaExtension;
  private final TypeFactory typeFactory;

  @Inject
  public SubTypeGenerator(
    @Assisted Referenceable<Schema> baseType,
    @Extension SchemaExtension schemaExtension,
    @Internal TypeFactory typeFactory,
    SchemasCache schemasCache,
    RefFactory refFactory,
    NestedSchemaGenerator nestedSchemaGenerator
  ) {
    this.schemaExtension = schemaExtension;
    this.typeFactory = typeFactory;
    this.baseType = (ObjectSchema) schemasCache.resolve(baseType);
    this.schemasCache = schemasCache;
    this.refFactory = refFactory;
    this.nestedSchemaGenerator = nestedSchemaGenerator;
  }

  public Referenceable<Schema> generate(Class<?> type) throws JsonMappingException {
    JavaType javaType = typeFactory.constructType(type);
    ObjectSchema fields = (ObjectSchema) nestedSchemaGenerator.generateSchema(
      type,
      false
    );
    return schemasCache.putSchema(
      TypeUtils.toTypeName(javaType),
      schemaExtension.modify(
        AllOfSchema
          .builder()
          .addAllOf(baseType)
          .addAllOf(
            fields.withProperties(
              Maps.filterKeys(
                fields.getProperties(),
                Predicates.not(baseType.getProperties()::containsKey)
              )
            )
          )
          .build(),
        javaType
      )
    );
  }

  public interface Factory {
    SubTypeGenerator create(Referenceable<Schema> baseType);
  }
}
