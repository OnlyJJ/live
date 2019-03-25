package com.jiujun.shows.guard.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;

public class GuardVo extends JsonParseInterface implements Serializable {
	/**
	 * 守护名称
	 */
	private String name;
	/**
	 * 守护等级
	 */
	private int level;
	/**
	 * 图片
	 */
	private String image;
	/**
	 * 守护有效期
	 */
	private long validate;
	/**
	 * 守护类型，1-白银，2-黄金
	 */
	private int guardType;
	/**
	 * 价格类型，1-月，2-季度，3-年度
	 */
	private int priceType;
	/**
	 * 是否快到期，定为3天
	 */
	private String isExpire;
	
	/**
	 * 用于续期时，获取该用户在work工作表中的记录
	 */
	private int workId;
	/**
	 * userId
	 */
	private String userId;
	/**
	 * name
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 守护价格
	 */
	private int price;
	
	/**
	 * 倒计时，xx天xx时xx分
	 */
	private String timerDown;
	
	/**
	 * 有效期与服务器当前时间差
	 */
	private long time;
	
	/** 分给主播的钻石*/
	private int diamond;
	
	/** 开始时间 */
	private String beginTime;
	
	private String endTime;
	
	private String roomId;
	
	/** 排序 */
	private int sortIndex;
	
	private String userLevel;
	
	private String g_name = "a";
	private String g_level = "b";
	private String g_image = "c";
	private String g_validate = "d";
	private String g_guardType = "e";
	private String g_priceType = "f";
	private String g_isExpire = "g";
	private String g_workId = "h";
	private String g_userId = "i";
	private String g_nickname = "j";
	private String g_avatar="k";
	private String g_price = "l";
	private String g_timerDown = "m";
	private String g_time = "n";
	private String g_diamond = "o";
	private String g_beginTime = "p";
	private String g_endTime = "q";
	private String g_roomId="r";
	private String g_sortIndex="s";
	private String g_userLevel="t";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json,g_name,name);
			setInt(json,g_level,level);
			setString(json,g_image,image);
			setLong(json,g_validate,validate);
			setInt(json,g_guardType,guardType);
			setInt(json,g_priceType,priceType);
			setString(json,g_isExpire,isExpire);
			setInt(json,g_workId,workId);
			setString(json,g_userId,userId);
			setString(json,g_nickname,nickname);
			setString(json,g_avatar,avatar);
			setInt(json,g_price,price);
			setString(json,g_timerDown,timerDown);
			setInt(json,g_diamond,diamond);
			setString(json,g_beginTime,beginTime);
			setLong(json,g_time,time);
			setString(json,g_endTime,endTime);
			setString(json,g_roomId,roomId);
			setInt(json,g_sortIndex,sortIndex);
			setString(json,g_userLevel,userLevel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		try {
			this.name=getString(json,g_name);
			this.level=getInt(json,g_level);
			this.image=getString(json,g_image);
			this.validate=getLong(json,g_validate);
			this.guardType= getInt(json,g_guardType);
			this.priceType = getInt(json,g_priceType);
			this.isExpire = getString(json,g_isExpire);
			this.workId = getInt(json,g_workId);
			this.userId = getString(json,g_userId);
			this.nickname = getString(json,g_nickname);
			this.avatar = getString(json,g_avatar);
			this.timerDown=getString(json,g_timerDown);
			this.time=getLong(json,g_time);
			this.userLevel = getString(json, g_userLevel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getValidate() {
		return validate;
	}

	public void setValidate(long validate) {
		this.validate = validate;
	}

	public int getGuardType() {
		return guardType;
	}

	public void setGuardType(int guardType) {
		this.guardType = guardType;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public String getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
	}

	public int getWorkId() {
		return workId;
	}

	public void setWorkId(int workId) {
		this.workId = workId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTimerDown() {
		return timerDown;
	}

	public void setTimerDown(String timerDown) {
		this.timerDown = timerDown;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	
	
}
