package com.odan.common.application;

public class CommandException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CommandException() {
		
	}
	
	public CommandException(Exception ex) {
		super(ex.getMessage(), ex.getCause());
	}
	
	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CommandException(Throwable cause) {
		super(cause);
	}
	
	public CommandException(String message) {
		super(message);
	}
}
