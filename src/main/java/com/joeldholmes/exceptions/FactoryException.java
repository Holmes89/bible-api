package com.joeldholmes.exceptions;

public class FactoryException extends APIException {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	
	public FactoryException(String errorCode, String message, Throwable t) {
		super(message, t);
		this.errorCode = errorCode;
	}
	
	public FactoryException(String errorCode,  Throwable t) {
		super(errorCode, t);
		this.errorCode = errorCode;
	}
	
	public FactoryException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public FactoryException(String errorCode) {
		this(errorCode, errorCode);
	}
	
	public String getErrorCode() {
		return errorCode;
	}
}
