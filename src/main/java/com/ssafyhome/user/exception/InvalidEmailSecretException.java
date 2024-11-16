package com.ssafyhome.user.exception;

public class InvalidEmailSecretException extends RuntimeException {
	public InvalidEmailSecretException(String message) {
		super(message);
	}
}
