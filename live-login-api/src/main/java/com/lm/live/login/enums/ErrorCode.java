package com.lm.live.login.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录模块使用
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum ErrorCode {
	ERROR_105(105, "QQ接入请求失败，请稍后重试"),
	ERROR_104(104, "登录已停用"),
	/** 账号异常 */
	ERROR_103(103, "账号存在异常，请联系客服"),
	/** 应用未授权 */
	ERROR_102(102, "应用未授权，请开启授权"),
	
	// 以下错误码所有模块通用
	/** 参数错误 */
	ERROR_101(101, "参数错误"),
	/** 不需要重新请求，只提示错误（未知的异常应该统一使用此信息返回客户端）  */
	ERROR_100(100, "网络繁忙，请稍后重试"),
	/** 需要重新请求的错误码 */
	ERROR_1(-1, "网络繁忙，请稍后重试"),
	/** 成功 */
	SUCCESS_0(0, "SUCCESS");
	
	private int resultCode;
	private String resultDescr;
	
	private ErrorCode(int resultCode,String resultDescr){
		this.resultCode = resultCode;
		this.resultDescr = resultDescr;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	public String getResultDescr() {
		return resultDescr;
	}
	
	public void setResultDescr(String resultDescr) {
		this.resultDescr = resultDescr;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("a", this.resultCode);
		r.put("b", this.resultDescr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("r", r);
		return map;
	}
}
