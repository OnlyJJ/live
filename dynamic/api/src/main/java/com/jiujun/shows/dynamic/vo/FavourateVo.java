package com.jiujun.shows.dynamic.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class FavourateVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 操作类型，0-点赞，1-踩*/
	private static final String d_type = "a";
	
	private int type;
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setInt(json,d_type,type);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		// TODO Auto-generated method stub
		if(json == null) {
			return;
		}
		try {
			type = getInt(json,d_type);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
}
