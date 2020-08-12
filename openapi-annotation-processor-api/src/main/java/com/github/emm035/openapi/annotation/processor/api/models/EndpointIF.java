package com.github.emm035.openapi.annotation.processor.api.models;

import org.immutables.value.Value.Immutable;

@OpenApiProcessorStyle
@Immutable
public interface EndpointIF extends BaseEndpoint<Parameter, Response> {}
