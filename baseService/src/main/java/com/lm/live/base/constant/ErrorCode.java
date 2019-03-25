package com.lm.live.base.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础服务错误码<10000><br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-06-11
 *
 */
public enum ErrorCode {
	// 10000 基础服务模块使用
	ERROR_10002(10002,"上传文件格式有问题，请上传png或jpg格式"),
	ERROR_10001(10001,"参数错误，请重试！"),
	ERROR_10000(10000,"上传失败，请重试！"),
	
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
