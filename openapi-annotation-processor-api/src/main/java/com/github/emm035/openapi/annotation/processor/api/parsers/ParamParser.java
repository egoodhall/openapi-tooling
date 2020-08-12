package com.github.emm035.openapi.annotation.processor.api.parsers;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedParameter;
import javax.lang.model.element.VariableElement;

public interface ParamParser extends Parser<VariableElement, ParsedParameter> {}
