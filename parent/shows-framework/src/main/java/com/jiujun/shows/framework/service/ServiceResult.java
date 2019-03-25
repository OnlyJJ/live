package com.jiujun.shows.framework.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务接口返回结果<br>
 * 	字段说明：<br>
 * 	 	succeed		处理结果，true/false<br>
 * 	 	resultCode	结果code，该code由各自业务模块自行维护，但需保证唯一<br>
 * 		resultMsg	结果说明
 * 		
 * @author shao.xiang
 * @date 2017-05-28
 * @param <T>
 */
public final class ServiceResult<T> implements Serializable {
	private static final long serialVersionUID = -9100629359484097704L;

	private boolean succeed = true;
	private int resultCode = -1;
	private String resultMsg;
	private T data;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public ServiceResult() {
	}

	public ServiceResult(T data)
	  {
	    this.data = data;
	  }

	  public ServiceResult(boolean succeed, int resultCode, String resultMsg) {
	    this.succeed = succeed;
	    this.resultCode = resultCode;
	    this.resultMsg = resultMsg;
	  }

	  public ServiceResult(boolean succeed, T data, String resultMsg) {
	    this.succeed = succeed;
	    this.data = data;
	    this.resultMsg = resultMsg;
	  }

	  public ServiceResult(boolean succeed, T data, int resultCode, String resultMsg) {
	    this.succeed = succeed;
	    this.data = data;
	    this.resultCode = resultCode;
	    this.resultMsg = resultMsg;
	  }

	  public ServiceResult(boolean succeed, String resultMsg) {
	    this.succeed = succeed;
	    this.resultMsg = resultMsg;
	  }
	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
	
}
