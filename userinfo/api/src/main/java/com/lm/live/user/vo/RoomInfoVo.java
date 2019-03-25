package com.lm.live.user.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;

/**
 * 房间实体
 * @author shao.xiang
 * @date 2017-09-04
 */
public abstract class RoomInfoVo extends UserBaseInfo implements Serializable {
	private static final long serialVersionUID = -6874129572214793335L;
	
	/** 粉丝数 */
	private int fans;
	
	// 字段key
	private static final String r_fans = "a";
	
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			 setInt(json, r_fans, fans);
			 
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
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	
	
}
