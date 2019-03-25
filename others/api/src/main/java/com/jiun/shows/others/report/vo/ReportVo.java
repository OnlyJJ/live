package com.jiun.shows.others.report.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class ReportVo extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// 字段key
	private static final String r_uuid = "a";
	private static final String r_channelId = "b";
	private static final String r_type = "c";
	private static final String r_douId = "d";
	private static final String r_userId = "e";
	
	private String uuid;
	private String channelId;
	private int type; // 状态，0-打开应用，1-登录，2-注册
	private int douId; // 打包id
	private String userId;

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, r_uuid, uuid);
			setString(json, r_channelId, channelId);
			setInt(json, r_type, type);
			setInt(json, r_douId, douId);
			setString(json, r_userId, userId);
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
			uuid = getString(json, r_uuid);
			channelId = getString(json, r_channelId);
			type = getInt(json, r_type);
			douId = getInt(json, r_douId);
			userId = getString(json, r_userId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	public int getDouId() {
		return douId;
	}

	public void setDouId(int douId) {
		this.douId = douId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
}
