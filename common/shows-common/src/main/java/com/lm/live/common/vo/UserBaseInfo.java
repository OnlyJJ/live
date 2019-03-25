package com.lm.live.common.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 父类
 * 用户基础信息，需要此信息的子类继承此类即可
 * @author shao.xiang
 * @date 2018年3月13日
 *
 */
public class UserBaseInfo extends JsonParseInterface implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private String userId;
	/** 房间号**/
	private String roomId;
	/** 昵称 */
	private String nickName;
	/** 用户头像 */
	private String icon;
	/** 用户身份，0-普通用户，1-主播 */
	private int identity;
	/** 性别，男：m，女：f，未知：n */
	private String sex;
	
	
	// 字段key
	private static final String u_userId = "b1";
	private static final String u_roomId = "b2";
	private static final String u_nickName = "b3";
	private static final String u_icon = "b4";
	private static final String u_identity = "b5";
	private static final String u_sex = "b6";

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, u_roomId, roomId);
			setString(json, u_userId, userId);
			setString(json, u_nickName, nickName);
			setInt(json, u_identity, identity);
			setString(json, u_sex, sex);
			if( null != icon ){
				setString(json, u_icon, BaseConstants.cdnPath + BaseConstants.ICON_IMG_FILE_URI+File.separator+icon);
			}
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
			roomId = getString(json, u_roomId);
			userId = getString(json, u_userId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}
