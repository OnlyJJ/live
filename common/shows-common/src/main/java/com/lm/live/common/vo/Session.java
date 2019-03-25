package com.lm.live.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Session extends JsonParseInterface implements Serializable {
	private static final long serialVersionUID = 7116595169671066562L;
	// 字段key
	private static final String u_time = "a";
	private static final String u_sessionid = "b";
	private static final String u_from = "c";
	private static final String u_token="d";
	
	private long time;
	private String sessionid;
	private int from;
	private String token;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setLong(json, u_time, time);
			setString(json, u_sessionid, sessionid);
			setInt(json, u_from, from);
			setString(json, u_token, token);
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
			time = getLong(json, u_time);
			sessionid = getString(json, u_sessionid);
			from = getInt(json, u_from);
			token=getString(json, u_token);
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
		return "Result [time=" + time + ", sessionid=" + sessionid+ ",from="+from+", token=" + token+ "]";
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
