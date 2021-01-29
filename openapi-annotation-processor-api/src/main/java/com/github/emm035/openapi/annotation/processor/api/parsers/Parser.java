package com.github.emm035.openapi.annotation.processor.api.parsers;

public interface Parser<T, R> {
  boolean canParse(T element);
  R parse(T element);
}
