package com.github.emm035.openapi.annotation.processor.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public interface SourceWriter {
  CodeBlock toCode();

  static CodeBlock list(Iterable<? extends SourceWriter> items) {
    CodeBlock.Builder builder = CodeBlock
      .builder()
      .add("$T.unmodifiableList(", Collections.class)
      .add("$T.asList(", Arrays.class)
      .indent();

    Iterator<? extends SourceWriter> iterator = items.iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      builder.add(iterator.next().toCode());
      if (iterator.hasNext()) {
        builder.add(",");
      }
    }

    return builder.unindent().add("))").build();
  }

  static CodeBlock stringList(Iterable<String> items) {
    CodeBlock.Builder builder = CodeBlock
      .builder()
      .add("$T.unmodifiableList(", Collections.class)
      .add("$T.asList(", Arrays.class)
      .indent();

    Iterator<String> iterator = items.iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      builder.add("$S", iterator.next());
      if (iterator.hasNext()) {
        builder.add(",");
      }
    }

    return builder.unindent().add("))").build();
  }

  static TypeName typeReference(TypeName typeName) {
    return ParameterizedTypeName.get(ClassName.get(TypeReference.class), typeName.box());
  }
}
