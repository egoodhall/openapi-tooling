package com.github.emm035.openapi.core.v3.shared;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;

public interface WithSingleExample {
  @JsonInclude(value = NON_ABSENT, content = ALWAYS)
  Optional<Object> getExample();
}
