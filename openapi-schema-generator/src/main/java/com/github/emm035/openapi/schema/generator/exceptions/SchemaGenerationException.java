package com.github.emm035.openapi.schema.generator.exceptions;

public class SchemaGenerationException extends Exception {

  public SchemaGenerationException(String message) {
    super(message);
  }

  public SchemaGenerationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SchemaGenerationException(Throwable cause) {
    super(cause);
  }

  public SchemaGenerationException(
    String message,
    Throwable cause,
    boolean enableSuppression,
    boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public SchemaGenerationException() {}
}
