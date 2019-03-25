package com.lm.live.common.utils;

public class AppException extends Exception{

	private static final long serialVersionUID = -3188852054371918014L;
	
	private String message;
	private int code;
	
	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
		this.message = message;
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
