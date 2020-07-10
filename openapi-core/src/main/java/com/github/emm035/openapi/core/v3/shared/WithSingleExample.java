package com.github.emm035.openapi.core.v3.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

public interface WithSingleExample {
  @JsonInclude(value = NON_ABSENT, content = ALWAYS)
  Optional<Object> getExample();
}
