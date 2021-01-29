package com.github.emm035.openapi.annotation.processor.api.models;

public interface BaseParameter<T> {
  String getName();
  String getIn();
  T getType();
}
