package com.ssafyhome.common.exception;

public class InvalidJwtException extends RuntimeException {
  public InvalidJwtException(String message) {
    super(message);
  }
}
