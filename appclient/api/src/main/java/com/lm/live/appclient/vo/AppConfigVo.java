package com.lm.live.appclient.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class AppConfigVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	private int type;//控制类型
	private String isOpen;//是否开启 
	private int level;//控制等级
	
	
	private String a_type = "a";//控制类型
	private String a_isOpen = "b";//是否开启
	private String a_level = "c";//控制等级
	

	@Override
	public JSONObject buildJson() {
		try {
			JSONObject json = new JSONObject();
			setInt(json,a_level,level);
			setInt(json,a_type,type);
			setString(json,a_isOpen,isOpen);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public void parseJson(JSONObject json) {
		
	}
	
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

}
