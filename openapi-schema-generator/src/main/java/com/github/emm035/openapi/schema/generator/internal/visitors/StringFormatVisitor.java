package com.github.emm035.openapi.schema.generator.internal.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.schemas.StringSchema;
import com.github.emm035.openapi.schema.generator.internal.Generator;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.util.Set;

public class StringFormatVisitor
  extends JsonStringFormatVisitor.Base
  implements Generator {
  private static final Converter<String, String> CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(
    CaseFormat.LOWER_HYPHEN
  );

  private final StringSchema.Builder stringSchema;

  private final JavaType javaType;

  @Inject
  public StringFormatVisitor(@Assisted JavaType javaType) {
    this.javaType = javaType;
    stringSchema = StringSchema.builder();
  }

  @Override
  public void format(JsonValueFormat format) {
    stringSchema.setFormat(CONVERTER.convert(format.name()));
  }

  @Override
  public void enumTypes(Set<String> enums) {
    stringSchema.setEnum(ImmutableList.copyOf(enums));
  }

  //===============//
  // Generator API //
  //===============//

  @Override
  public Referenceable<Schema> emit(boolean asReference) {
    return stringSchema.build();
  }

  //==================//
  // Assisted Factory //
  //==================//

  public interface Factory {
    StringFormatVisitor create(JavaType javaType);
  }
}
