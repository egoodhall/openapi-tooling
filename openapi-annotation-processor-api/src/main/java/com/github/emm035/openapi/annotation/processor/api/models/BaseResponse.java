package com.github.emm035.openapi.annotation.processor.api.models;

public interface BaseResponse<T> {
  int getStatusCode();
  T getType();
}
