package com.odan.common.application;

public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ValidationException(Exception ex) {
		super(ex.getMessage(), ex.getCause());
	}
	
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ValidationException(Throwable cause) {
		super(cause);
	}
	
	public ValidationException(String message) {
		super(message);
	}
}
