package com.github.emm035.openapi.annotation.processor.api.exceptions;

public class NoMatchingParserException extends RuntimeException {

  public NoMatchingParserException() {}

  public NoMatchingParserException(String message) {
    super(message);
  }

  public NoMatchingParserException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoMatchingParserException(Throwable cause) {
    super(cause);
  }
}
