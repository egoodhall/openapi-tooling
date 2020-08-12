package com.github.emm035.openapi.annotation.processor;

import com.github.emm035.openapi.annotation.processor.api.MethodMatcher;
import com.github.emm035.openapi.annotation.processor.api.models.ParsedEndpoint;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ProducesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ResponseParser;
import com.github.emm035.openapi.annotation.processor.api.spi.ProcessorModule;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import java.util.ServiceLoader;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class OpenApiProcessorModule extends AbstractModule {
  private final Set<ParsedEndpoint> endpoints = Sets.newConcurrentHashSet();
  private final ProcessingEnvironment processingEnv;

  public OpenApiProcessorModule(ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
  }

  @Override
  protected void configure() {
    // Ensure we have set binders for all of these types
    Multibinder.newSetBinder(binder(), MethodMatcher.class);
    Multibinder.newSetBinder(binder(), ProducesContentTypeParser.class);
    Multibinder.newSetBinder(binder(), ParamParser.class);
    Multibinder.newSetBinder(binder(), ResponseParser.class);

    // Load all processor modules using the SPI
    ServiceLoader
      .load(ProcessorModule.class, OpenApiProcessor.class.getClassLoader())
      .stream()
      .map(ServiceLoader.Provider::get)
      .forEach(this::install);
  }

  @Provides
  public Set<ParsedEndpoint> providesParsedEndpointsSet() {
    return endpoints;
  }

  @Provides
  public Messager providesMessager() {
    return processingEnv.getMessager();
  }

  @Provides
  public Elements providesElements() {
    return processingEnv.getElementUtils();
  }

  @Provides
  public Types providesTypes() {
    return processingEnv.getTypeUtils();
  }

  @Provides
  public Filer providesFiler() {
    return processingEnv.getFiler();
  }
}
