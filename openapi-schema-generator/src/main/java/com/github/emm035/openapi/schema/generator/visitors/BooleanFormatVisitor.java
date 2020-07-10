package com.github.emm035.openapi.schema.generator.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.BooleanSchema;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.Extension;
import com.github.emm035.openapi.schema.generator.base.Generator;
import com.github.emm035.openapi.schema.generator.extension.SchemaExtension;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class BooleanFormatVisitor
  extends JsonBooleanFormatVisitor.Base
  implements Generator {
  private final JavaType javaType;
  private final SchemaExtension schemaExtension;
  private final BooleanSchema.Builder booleanSchema;

  @Inject
  public BooleanFormatVisitor(
    @Assisted JavaType javaType,
    @Extension SchemaExtension schemaExtension
  ) {
    this.javaType = javaType;
    this.schemaExtension = schemaExtension;
    booleanSchema = BooleanSchema.builder();
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) {
    return schemaExtension.modify(booleanSchema.build(), javaType);
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    BooleanFormatVisitor create(JavaType javaType);
  }
}
