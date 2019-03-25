package com.lm.live.appclient.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class AppInfo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = -5543491584624221602L;
	/** app类型，1-安卓，2-IOS */
	private Integer appType;
	/** app版本号 */
	private String version;
	/** 下载链接  */
	private String url;
	/** 添加时间  */
	private String addTime;
	/** 版本更新说明*/
	private String message;
	
	/**  错误信息  */
	private String errorMsg;
	
	/** 包名 */
	private String packageName;
	
	/** 签名 */
	private String signatures;
	
	/** 上次使用开始时间 yyyy-MM-dd HH:mm:ss  */
	private String preUseBeginTime;
	
	/** 上次使用结束时间  yyyy-MM-dd HH:mm:ss */
	private String preUseEndTime;
	
	/** 客户端是否需要更新 */
	private Integer state; //是否需要更新，1是、0否、-1服务器异常
	public static final int STATE_NEED_UPDATE = 1;
	public static final int STATE_NO_NEED_UPDATE = 0;
	
	// 字段key
	private static final String a_appType = "a";
	private static final String a_version = "b";
	private static final String a_url = "c";
	private static final String a_addTime = "d";
	private static final String a_state = "e";
	private static final String a_message = "f";
	
	private static final String a_errorMsg = "g";
	
	private static final String a_packageName = "h";
	
	private static final String a_signatures = "i";
	
	private static final String a_preUseBeginTime = "j";
	
	private static final String a_preUseEndTime = "k";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, a_appType, appType); 
			setString(json, a_version, version);
			setString(json,a_url, url);
			setString(json, a_addTime, addTime);
			setInt(json, a_state, state);
			setString(json, a_message, message);
			setString(json, a_errorMsg, errorMsg);
			setString(json, a_packageName, packageName);
			setString(json, a_signatures, signatures);
			setString(json, a_preUseBeginTime, preUseBeginTime);
			setString(json, a_preUseEndTime, preUseEndTime);
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
			this.appType = getInt(json, a_appType);
			this.version = getString(json, a_version);
			this.url = getString(json, a_url);
			this.addTime = getString(json, a_addTime);
			this.errorMsg = getString(json, a_errorMsg);
			this.packageName = getString(json, a_packageName);
			this.signatures = getString(json, a_signatures);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	
	@Override
	public String toString() {
		return "AppInfo [appType=" + appType + ", version=" + version + ", url=" + url + ", addTime=" + addTime + "]";
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public String getSignatures() {
		return signatures;
	}


	public void setSignatures(String signatures) {
		this.signatures = signatures;
	}


	public String getPreUseBeginTime() {
		return preUseBeginTime;
	}


	public void setPreUseBeginTime(String preUseBeginTime) {
		this.preUseBeginTime = preUseBeginTime;
	}


	public String getPreUseEndTime() {
		return preUseEndTime;
	}


	public void setPreUseEndTime(String preUseEndTime) {
		this.preUseEndTime = preUseEndTime;
	}
	
}
