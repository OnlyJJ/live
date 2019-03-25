package com.jiujun.shows.guard.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 守护服务错误码<12000><br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-10-11
 *
 */
public enum ErrorCode {
	//12000 支付的错误码
	
	ERROR_12002(12002,"守护已经满员了"),
	ERROR_12001(12001,"参数错误"),
	ERROR_12000(12000,"网络不给力，请稍后重试"),
	
	SUCCESS_0(0,"SUCCESS"),;
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
