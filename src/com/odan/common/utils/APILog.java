package com.odan.common.utils;

public class APILog {

	private APILogType type;
	private String message;
	private Exception exception;

	public APILog() {
		this.type = APILogType.WARNING;
		this.message = "Unknown error";
	}

	public APILog(APILogType t, String m) {
		this.type = t;
		this.message = m;
	}

	public APILog(APILogType t, String m, Exception e) {
		this.type = t;
		this.message = m;
		this.exception = e;
	}

	public APILogType getType() {
		return type;
	}

	public void setType(APILogType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
//	@JSON(serialize=false, deserialize=false)
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}	
	
	public String getExceptionMessage() {
		String message = "";
		if(this.getException() != null) {
			message = this.getException().getMessage();
		}
		return message;
	}
	
}
