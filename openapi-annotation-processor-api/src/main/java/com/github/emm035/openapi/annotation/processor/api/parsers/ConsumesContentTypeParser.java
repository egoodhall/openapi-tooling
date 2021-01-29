package com.github.emm035.openapi.annotation.processor.api.parsers;

import java.util.Set;
import javax.lang.model.element.ExecutableElement;

public interface ConsumesContentTypeParser
  extends Parser<ExecutableElement, Set<String>> {
  Set<String> parse(ExecutableElement method);
}
