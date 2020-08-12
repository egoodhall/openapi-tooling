package com.github.emm035.openapi.annotation.processor.api.models;

import com.fasterxml.jackson.core.type.TypeReference;
import org.immutables.value.Value.Immutable;

@OpenApiProcessorStyle
@Immutable
public interface ParameterIF extends BaseParameter<TypeReference<?>> {}
