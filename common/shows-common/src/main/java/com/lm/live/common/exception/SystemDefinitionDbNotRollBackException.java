package com.lm.live.common.exception;

import com.lm.live.common.enums.ErrorCode;


/**
 * 自定义业务异常类(抛出此异常,不回滚事务)
 *
 */
public class SystemDefinitionDbNotRollBackException extends RuntimeException{
	
	private static final long serialVersionUID = -7524346238461923503L;
	private ErrorCode errorCode;
	private String method;
	public SystemDefinitionDbNotRollBackException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public SystemDefinitionDbNotRollBackException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public SystemDefinitionDbNotRollBackException(ErrorCode errorCode, String detailMsg) {
		super(errorCode.getResultDescr()+"@"+detailMsg);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public String getMethod(){
		return method;
	}

}
