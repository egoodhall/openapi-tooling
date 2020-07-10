package com.github.emm035.openapi.schema.generator.assisted;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.emm035.openapi.core.v3.references.Ref;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.RefPrefix;
import com.github.emm035.openapi.schema.generator.annotations.internal.Internal;
import com.github.emm035.openapi.schema.generator.base.TypeUtils;
import com.google.inject.Inject;
import java.util.regex.Pattern;

public class RefFactory {
  private static final Pattern LEADING_SLASH = Pattern.compile("/+$");
  private static final Pattern TRAILING_SLASH = Pattern.compile("^/+");
  private final String refPrefix;
  private final TypeFactory typeFactory;

  @Inject
  public RefFactory(@RefPrefix String refPrefix, @Internal TypeFactory typeFactory) {
    this.refPrefix = refPrefix.endsWith("/") ? refPrefix : refPrefix + "/";
    this.typeFactory = typeFactory;
  }

  public Ref<Schema> create(Class<?> clazz) {
    return create(typeFactory.constructType(clazz));
  }

  public Ref<Schema> create(JavaType javaType) {
    return create(TypeUtils.toTypeName(javaType));
  }

  public Ref<Schema> create(String schemaName) {
    return Ref.of(join(refPrefix, schemaName));
  }

  private String join(String prefix, String name) {
    return refPrefix + LEADING_SLASH.matcher(name).replaceAll("");
  }
}
