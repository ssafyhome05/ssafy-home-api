package com.ssafyhome.common.exception;

public class AccessTokenExpiredException extends RuntimeException {
  public AccessTokenExpiredException(String message) {
    super(message);
  }
}
