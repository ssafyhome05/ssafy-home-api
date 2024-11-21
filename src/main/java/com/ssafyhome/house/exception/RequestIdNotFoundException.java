package com.ssafyhome.house.exception;

public class RequestIdNotFoundException extends RuntimeException {
  public RequestIdNotFoundException(String message) {
    super(message);
  }
}
