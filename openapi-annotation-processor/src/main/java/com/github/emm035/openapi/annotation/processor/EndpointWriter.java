package com.github.emm035.openapi.annotation.processor;

import com.github.emm035.openapi.annotation.processor.api.ParsedEndpointsProvider;
import com.github.emm035.openapi.annotation.processor.api.SourceWriter;
import com.github.emm035.openapi.annotation.processor.api.models.Endpoint;
import com.github.emm035.openapi.annotation.processor.api.models.ParsedEndpoint;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

public class EndpointWriter {
  private static final String CLASS_PACKAGE =
    "com.github.emm035.openapi.annotation.processor";
  private static final String CLASS_NAME = "ParsedEndpoints";

  private final Set<ParsedEndpoint> parsedEndpoints;
  private final Messager messager;
  private final Filer filer;

  @Inject
  public EndpointWriter(
    Set<ParsedEndpoint> parsedEndpoints,
    Messager messager,
    Filer filer
  ) {
    this.parsedEndpoints = parsedEndpoints;
    this.messager = messager;
    this.filer = filer;
  }

  public void writeAll() {
    try {
      JavaFile.builder(CLASS_PACKAGE, buildTypeSpec()).build().writeTo(filer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private TypeSpec buildTypeSpec() {
    FieldSpec staticListField = FieldSpec
      .builder(
        ParameterizedTypeName.get(List.class, Endpoint.class),
        "PARSED_ENDPOINTS",
        Modifier.PRIVATE,
        Modifier.STATIC,
        Modifier.FINAL
      )
      .initializer(SourceWriter.list(parsedEndpoints))
      .build();

    MethodSpec fetcherMethod = MethodSpec
      .methodBuilder("get")
      .addModifiers(Modifier.PUBLIC)
      .returns(ParameterizedTypeName.get(List.class, Endpoint.class))
      .addStatement("return PARSED_ENDPOINTS")
      .build();

    return TypeSpec
      .classBuilder(ClassName.get(CLASS_PACKAGE, CLASS_NAME))
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .addAnnotation(
        AnnotationSpec
          .builder(AutoService.class)
          .addMember("value", "$T.class", ParsedEndpointsProvider.class)
          .build()
      )
      .addSuperinterface(ClassName.get(ParsedEndpointsProvider.class))
      .addField(staticListField)
      .addMethod(fetcherMethod)
      .build();
  }
}
