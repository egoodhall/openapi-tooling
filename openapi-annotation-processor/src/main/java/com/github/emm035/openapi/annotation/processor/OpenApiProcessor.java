package com.github.emm035.openapi.annotation.processor;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.util.Collections;
import java.util.function.Supplier;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class OpenApiProcessor extends BasicAnnotationProcessor {
  private final Supplier<Injector> injector = Suppliers.memoize(
    () -> Guice.createInjector(new OpenApiProcessorModule(processingEnv))
  );

  @Override
  protected Iterable<? extends ProcessingStep> initSteps() {
    return Collections.singleton(injector.get().getInstance(OpenApiProcessingStep.class));
  }

  @Override
  protected void postRound(RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      injector.get().getInstance(EndpointWriter.class).writeAll();
    }
  }
}
