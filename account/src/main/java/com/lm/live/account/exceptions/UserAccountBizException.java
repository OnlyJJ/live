package com.lm.live.account.exceptions;

import com.lm.live.account.enums.ErrorCode;


/**
 * 自定义业务异常类
 * 
 */
public class UserAccountBizException extends RuntimeException{
	
	private static final long serialVersionUID = -7899801678804581599L;
	
	private ErrorCode errorCode;
	private String method;
	
	public UserAccountBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public UserAccountBizException(String method,ErrorCode errorCode) {
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
