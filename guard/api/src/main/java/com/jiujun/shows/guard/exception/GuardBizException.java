package com.jiujun.shows.guard.exception;

import com.jiujun.shows.guard.enums.ErrorCode;



/**
 * 自定义业务异常类
 *
 */
public class GuardBizException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8671093995945814422L;
	private ErrorCode errorCode;
	private String method;
	public GuardBizException(ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
	}
	public GuardBizException(String method,ErrorCode errorCode) {
		super(errorCode.getResultDescr());
		this.errorCode = errorCode;
		this.method=method;
	}
	
	/**
	 * 
	 * @param errorCode 错误码
	 * @param detailMsg 错误详细信息
	 */
	public GuardBizException(ErrorCode errorCode, String detailMsg) {
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
