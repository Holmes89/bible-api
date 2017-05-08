package com.joeldholmes.exceptions;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public APIException(String errorCode, String message, Throwable t) {
		super(message, t);
		this.errorCode = errorCode;
	}
	
	public APIException(String errorCode,  Throwable t) {
		super(errorCode, t);
		this.errorCode = errorCode;
	}
	
	public APIException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public APIException(String errorCode, String message, HttpStatus status) {
		super(message);
		this.errorCode = errorCode;
		this.status = status;
	}

	public APIException(String errorCode) {
		this(errorCode, errorCode);
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
