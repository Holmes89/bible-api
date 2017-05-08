package com.joeldholmes.exceptions;

import org.springframework.http.HttpStatus;

public class ApiMultipleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private Iterable<String> messageArray;
	private String code;
	private HttpStatus status;
	
	public ApiMultipleException(String code){
		super();
		this.code = code;
	}
	
	public ApiMultipleException(String code, Iterable<String> messageArray, HttpStatus status){
		this(code);
		this.status = status;
		this.messageArray = messageArray;
	}

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public HttpStatus getStatus() {
		return status;
	}


	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	public Iterable<String> getMessageArray() {
		return messageArray;
	}

	public void setMessageArray(Iterable<String> messageArray) {
		this.messageArray = messageArray;
	}
	
	
}
