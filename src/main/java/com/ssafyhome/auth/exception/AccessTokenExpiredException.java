package com.ssafyhome.auth.exception;

public class AccessTokenExpiredException extends RuntimeException {
  public AccessTokenExpiredException(String message) {
    super(message);
  }
}
