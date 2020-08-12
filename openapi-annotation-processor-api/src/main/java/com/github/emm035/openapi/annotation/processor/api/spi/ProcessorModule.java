package com.github.emm035.openapi.annotation.processor.api.spi;

import com.github.emm035.openapi.annotation.processor.api.MethodMatcher;
import com.github.emm035.openapi.annotation.processor.api.parsers.ConsumesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.MethodParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ParamParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.PathParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ProducesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.api.parsers.ResponseParser;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;

public abstract class ProcessorModule extends AbstractModule {

  @Override
  protected void configure() {
    super.configure();
  }

  @SafeVarargs
  protected final void bindParamParsers(
    Binder binder,
    Class<? extends ParamParser>... parsers
  ) {
    Multibinder<ParamParser> multibinder = Multibinder.newSetBinder(
      binder,
      ParamParser.class
    );
    for (Class<? extends ParamParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }

  @SafeVarargs
  protected final void bindMethodMatchers(
    Binder binder,
    Class<? extends MethodMatcher>... matchers
  ) {
    Multibinder<MethodMatcher> multibinder = Multibinder.newSetBinder(
      binder,
      MethodMatcher.class
    );
    for (Class<? extends MethodMatcher> matcher : matchers) {
      multibinder.addBinding().to(matcher);
    }
  }

  @SafeVarargs
  protected final void bindResponseParsers(
    Binder binder,
    Class<? extends ResponseParser>... parsers
  ) {
    Multibinder<ResponseParser> multibinder = Multibinder.newSetBinder(
      binder,
      ResponseParser.class
    );
    for (Class<? extends ResponseParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }

  @SafeVarargs
  protected final void bindProducesContentTypeParsers(
    Binder binder,
    Class<? extends ProducesContentTypeParser>... parsers
  ) {
    Multibinder<ProducesContentTypeParser> multibinder = Multibinder.newSetBinder(
      binder,
      ProducesContentTypeParser.class
    );
    for (Class<? extends ProducesContentTypeParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }

  @SafeVarargs
  protected final void bindConsumesContentTypeParsers(
    Binder binder,
    Class<? extends ConsumesContentTypeParser>... parsers
  ) {
    Multibinder<ConsumesContentTypeParser> multibinder = Multibinder.newSetBinder(
      binder,
      ConsumesContentTypeParser.class
    );
    for (Class<? extends ConsumesContentTypeParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }

  @SafeVarargs
  protected final void bindPathParsers(
    Binder binder,
    Class<? extends PathParser>... parsers
  ) {
    Multibinder<PathParser> multibinder = Multibinder.newSetBinder(
      binder,
      PathParser.class
    );
    for (Class<? extends PathParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }

  @SafeVarargs
  protected final void bindMethodParsers(
    Binder binder,
    Class<? extends MethodParser>... parsers
  ) {
    Multibinder<MethodParser> multibinder = Multibinder.newSetBinder(
      binder,
      MethodParser.class
    );
    for (Class<? extends MethodParser> parser : parsers) {
      multibinder.addBinding().to(parser);
    }
  }
}
