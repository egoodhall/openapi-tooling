package com.github.emm035.openapi.annotation.processor.api.parsers;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedResponse;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;

public interface ResponseParser extends Parser<ExecutableElement, Set<ParsedResponse>> {}
