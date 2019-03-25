package com.lm.live.login.exceptions;

import com.lm.live.login.enums.ErrorCode;


/**
 * 自定义业务异常类
 * 
 */
public class LoginBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	
	private ErrorCode errorCode;
	private String method;
	
	public LoginBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public LoginBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public String getMethod(){
		return method;
	}

}
