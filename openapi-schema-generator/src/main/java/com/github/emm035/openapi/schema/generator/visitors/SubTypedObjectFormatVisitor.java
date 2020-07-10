package com.github.emm035.openapi.schema.generator.visitors;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.github.emm035.openapi.schema.generator.extension.PropertyExtension;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.github.emm035.openapi.schema.generator.assisted.Extension;
import com.github.emm035.openapi.schema.generator.assisted.NestedSchemaGenerator;
import com.github.emm035.openapi.schema.generator.assisted.RefFactory;
import com.github.emm035.openapi.schema.generator.assisted.SubTypeGenerator;
import com.github.emm035.openapi.schema.generator.base.Generator;
import com.github.emm035.openapi.schema.generator.base.Schemas;
import com.github.emm035.openapi.schema.generator.base.TypeUtils;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Discriminator;
import com.github.emm035.openapi.core.v3.schemas.ObjectSchema;
import com.github.emm035.openapi.core.v3.schemas.OneOfSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;

import java.util.Optional;

public class SubTypedObjectFormatVisitor
  extends JsonObjectFormatVisitor.Base
  implements Generator {
  private final JavaType javaType;
  private final Schemas schemas;
  private final SchemaExtension schemaExtension;
  private final NestedSchemaGenerator nestedSchemaGenerator;
  private final SubTypeGenerator.Factory subTypeGeneratorFactory;
  private final ObjectSchema.Builder baseSchemaBuilder;
  private final PropertyExtension propertyExtension;
  private final RefFactory refFactory;

  @Inject
  public SubTypedObjectFormatVisitor(
    @Assisted JavaType javaType,
    Schemas schemas,
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
    this.schemas = schemas;
    this.schemaExtension = schemaExtension;
    this.nestedSchemaGenerator = nestedSchemaGenerator;
    this.subTypeGeneratorFactory = subTypeGeneratorFactory;
  }



  @Override
  public void optionalProperty(BeanProperty prop) throws JsonMappingException {
    String typeName = TypeUtils.toTypeName(TypeUtils.unwrap(prop.getType()));

    Referenceable<Schema> schema;
    if (schemas.exists(typeName)) {
      schema = refFactory.create(typeName);
    } else {
      schema = nestedSchemaGenerator.generateSchema(TypeUtils.unwrap(prop.getType()), false);
    }
    Schema modifiedSchema = propertyExtension.modify(schemas.resolve(schema), prop);

    // Overwrite schema if needed
    if (schema.isReferential()) {
      this.baseSchemaBuilder.putProperties(
        prop.getName(),
        schemas.putSchema(prop.getName(), modifiedSchema)
      );
    } else {
      this.baseSchemaBuilder.putProperties(
        prop.getName(),
        modifiedSchema
      );
    }
  }

  @Override
  public void property(BeanProperty prop) throws JsonMappingException {
    baseSchemaBuilder.addRequired(prop.getName());
    optionalProperty(prop);
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) throws JsonMappingException {
    OneOfSchema.Builder builder = OneOfSchema.builder()
      .setDiscriminator(buildDiscriminator());

    Schema baseSchema = schemaExtension.modify(baseSchemaBuilder.build(), javaType);
    Referenceable<Schema> baseSchemaRef = schemas.putSchema(TypeUtils.toTypeName(javaType), baseSchema);
    SubTypeGenerator subTypeGenerator = subTypeGeneratorFactory.create(baseSchemaRef);
    for (Class<?> clazz : TypeUtils.getTypeImplementations(javaType)) {
      builder.addOneOf(subTypeGenerator.generate(clazz));
    }

    return schemaExtension.modify(builder.build(), javaType);
  }

  private Discriminator buildDiscriminator() {
    Optional<JsonTypeInfo> typeInfo = TypeUtils.getTypeInfo(javaType);
    if (typeInfo.isPresent()) {
      if (typeInfo.get().include() == JsonTypeInfo.As.EXISTING_PROPERTY || typeInfo.get().include() == JsonTypeInfo.As.PROPERTY) {
        return Discriminator.builder()
          .setPropertyName(
            Optional.of(typeInfo.get().property()).filter(Predicates.not(String::isEmpty)).orElseGet(() -> typeInfo.get().use().getDefaultPropertyName())
          ).setMappings(
            Maps.transformValues(TypeUtils.getTypeMappings(javaType), clazz -> refFactory.create(clazz).getRef())
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
