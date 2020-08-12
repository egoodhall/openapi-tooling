package com.github.emm035.openapi.annotation.processor.jaxrs;

import com.github.emm035.openapi.annotation.processor.api.spi.ProcessorModule;
import com.github.emm035.openapi.annotation.processor.jaxrs.content.JaxRsConsumesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.jaxrs.content.JaxRsProducesContentTypeParser;
import com.github.emm035.openapi.annotation.processor.jaxrs.parameter.JaxRsPathParamParser;
import com.github.emm035.openapi.annotation.processor.jaxrs.parameter.JaxRsQueryParamParser;
import com.github.emm035.openapi.annotation.processor.jaxrs.response.JaxRsSimpleResponseParser;
import com.google.auto.service.AutoService;

@AutoService(ProcessorModule.class)
public class JaxRsModule extends ProcessorModule {

  @Override
  protected void configure() {
    bindMethodMatchers(binder(), JaxRsMethodHandler.class);
    bindParamParsers(binder(), JaxRsQueryParamParser.class, JaxRsPathParamParser.class);
    bindProducesContentTypeParsers(binder(), JaxRsProducesContentTypeParser.class);
    bindConsumesContentTypeParsers(binder(), JaxRsConsumesContentTypeParser.class);
    bindResponseParsers(binder(), JaxRsSimpleResponseParser.class);
    bindPathParsers(binder(), JaxRsPathParser.class);
    bindMethodParsers(binder(), JaxRsMethodHandler.class);
  }
}
