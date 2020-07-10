package com.github.emm035.openapi.core.v3.shared;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.emm035.openapi.core.v3.examples.Example;
import java.util.Map;

public interface WithMultipleExamples {
  @JsonInclude(NON_EMPTY)
  Map<String, Example> getExamples();
}
