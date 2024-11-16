package com.ssafyhome.common.exception;

public class SQLInsertException extends RuntimeException {
	public SQLInsertException(Exception e) {
		super(e);
	}
}
