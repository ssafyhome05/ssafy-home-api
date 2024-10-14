package com.ssafyhome.exception;

public class InvalidJwtException extends RuntimeException {
  public InvalidJwtException(String message) {
    super(message);
  }
}
