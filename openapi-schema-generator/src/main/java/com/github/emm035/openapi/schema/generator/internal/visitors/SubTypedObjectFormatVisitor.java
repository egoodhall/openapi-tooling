package com.github.emm035.openapi.schema.generator.internal.visitors;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Discriminator;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.OneOfSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.annotations.SchemaProperty;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.schema.generator.internal.Generator;
import com.github.emm035.openapi.schema.generator.internal.RefFactory;
import com.github.emm035.openapi.schema.generator.internal.SchemasCache;
import com.github.emm035.openapi.schema.generator.internal.TypeUtils;
import com.github.emm035.openapi.schema.generator.internal.generators.NestedSchemaGenerator;
import com.github.emm035.openapi.schema.generator.internal.generators.SubTypeGenerator;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.util.Optional;

public class SubTypedObjectFormatVisitor
  extends JsonObjectFormatVisitor.Base
  implements Generator {
  private final JavaType javaType;
  private final SchemasCache schemasCache;
  private final SchemaExtension schemaExtension;
  private final NestedSchemaGenerator nestedSchemaGenerator;
  private final SubTypeGenerator.Factory subTypeGeneratorFactory;
  private final ObjectSchema.Builder baseSchemaBuilder;
  private final PropertyExtension propertyExtension;
  private final RefFactory refFactory;

  @Inject
  public SubTypedObjectFormatVisitor(
    @Assisted JavaType javaType,
    SchemasCache schemasCache,
    @Extension SchemaExtension schemaExtension,
    @Extension PropertyExtension propertyExtension,
    NestedSchemaGenerator nestedSchemaGenerator,
    SubTypeGenerator.Factory subTypeGeneratorFactory,
    RefFactory refFactory
  ) {
    this.propertyExtension = propertyExtension;
    this.refFactory = refFactory;
    this.baseSchemaBuilder = ObjectSchema.builder();
    this.javaType = javaType;
    this.schemasCache = schemasCache;
    this.schemaExtension = schemaExtension;
    this.nestedSchemaGenerator = nestedSchemaGenerator;
    this.subTypeGeneratorFactory = subTypeGeneratorFactory;
  }

  @Override
  public void optionalProperty(BeanProperty prop) throws JsonMappingException {
    String typeName = TypeUtils.toTypeName(TypeUtils.unwrap(prop.getType()));

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
      this.baseSchemaBuilder.putProperties(
          getPropertyName(prop),
          schemasCache.putSchema(prop.getName(), modifiedSchema)
        );
    } else {
      this.baseSchemaBuilder.putProperties(prop.getName(), modifiedSchema);
    }
  }

  @Override
  public void property(BeanProperty prop) throws JsonMappingException {
    baseSchemaBuilder.addRequired(prop.getName());
    optionalProperty(prop);
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
    OneOfSchema.Builder builder = OneOfSchema
      .builder()
      .setDiscriminator(buildDiscriminator());

    Schema baseSchema = schemaExtension.modify(baseSchemaBuilder.build(), javaType);
    Referenceable<Schema> baseSchemaRef = schemasCache.putSchema(
      TypeUtils.toTypeName(javaType),
      baseSchema
    );
    SubTypeGenerator subTypeGenerator = subTypeGeneratorFactory.create(baseSchemaRef);
    for (Class<?> clazz : TypeUtils.getTypeImplementations(javaType)) {
      builder.addOneOf(subTypeGenerator.generate(clazz));
    }

    return schemaExtension.modify(builder.build(), javaType);
  }

  private Discriminator buildDiscriminator() {
    Optional<JsonTypeInfo> typeInfo = TypeUtils.getTypeInfo(javaType);
    if (typeInfo.isPresent()) {
      if (
        typeInfo.get().include() == JsonTypeInfo.As.EXISTING_PROPERTY ||
        typeInfo.get().include() == JsonTypeInfo.As.PROPERTY
      ) {
        return Discriminator
          .builder()
          .setPropertyName(
            Optional
              .of(typeInfo.get().property())
              .filter(Predicates.not(String::isEmpty))
              .orElseGet(() -> typeInfo.get().use().getDefaultPropertyName())
          )
          .setMappings(
            Maps.transformValues(
              TypeUtils.getTypeMappings(javaType),
              clazz -> refFactory.create(clazz).getRef()
            )
          )
          .build();
      }
    }
    return Discriminator.builder().setPropertyName("@type").build();
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    SubTypedObjectFormatVisitor create(JavaType javaType);
  }
}
