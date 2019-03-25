package com.lm.live.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Result extends JsonParseInterface implements Serializable {
	private static final long serialVersionUID = -6056485111761523757L;
	
	public static final int RESULT_FAILURE = 1;//code 1
	public static final int RESULT_SUCCESS = 2000;//code 0)
	//public static final String RESULT_USERACCOUNT_NOT_EXIST = "用户名不存在";
	//public static final String RESULT_PWD_NOT_CORRECT = "用户名或密码不正确";
	// 字段key
	private static final String u_resultCode = "a";
	private static final String u_resultDescr = "b";
	private static final String u_errMsg = "c";
	
	/** 请求结果码，0-成功，其他为失败  **/
	public int resultCode;
	/** 请求结果说明 **/
	public String resultDescr;
	
	public String errMsg;
	
	public Result(ErrorCode err){
		this.resultCode = err.getResultCode();
		this.resultDescr = err.getResultDescr();
	}
	
	public Result(ErrorCode err,String errMsg){
		this.resultCode = err.getResultCode();
		this.resultDescr = err.getResultDescr();
		this.errMsg = errMsg;
	}
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, u_resultCode, resultCode);
			setString(json, u_resultDescr, resultDescr);
			setString(json, u_errMsg, errMsg);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			resultCode = getInt(json, u_resultCode);
			resultDescr = getString(json, u_resultDescr);
			errMsg = getString(json, u_errMsg);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public String toString() {
		return "Result [resultCode=" + resultCode + ", resultDescr=" + resultDescr+ "]";
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescr() {
		return resultDescr;
	}

	public void setResultDescr(String resultDescr) {
		this.resultDescr = resultDescr;
	}

	public Result(int resultCode, String resultDescr) {
		super();
		this.resultCode = resultCode;
		this.resultDescr = resultDescr;
	}
	
	public Result(){};
	
}
