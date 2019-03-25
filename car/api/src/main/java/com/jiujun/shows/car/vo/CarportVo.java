package com.jiujun.shows.car.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class CarportVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 65411248556998L;
	/** 房间号 */
	private String roomId;
	/** 停靠车辆所属用户 */
	private String userId;
	/** 用户名 */
	private String nickname;
	/** 停靠车辆id  */ 
	private int carId;
	/** 停靠车位 */
	private int carport;
	/** 开始时间 */
	private String beginTime;
	/** 停靠状态，0，已停靠未结束，1，已结束 */
	private String status;
	/** 开始时间到当前时间时间戳 */
	private long time;
	/** 座驾名称 */
	private String carName;
	/** 座驾图片 */
	private String carImg;
	
	private static final String c_roomId = "a";
	private static final String c_userId = "b";
	private static final String c_carId = "c";
	private static final String c_carport= "d";
	private static final String c_beginTime = "e";
	private static final String c_status = "f";
	private static final String c_nickname = "g";
	private static final String c_time ="h";
	private static final String c_carName = "i";
	private static final String c_carImg = "j";
	
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCarport() {
		return carport;
	}

	public void setCarport(int carport) {
		this.carport = carport;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarImg() {
		return carImg;
	}

	public void setCarImg(String carImg) {
		this.carImg = carImg;
	}

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json,c_roomId,roomId);
			setString(json,c_userId,userId);
			setInt(json,c_carId,carId);
			setInt(json,c_carport,carport);
			setString(json,c_beginTime,beginTime);
			setString(json,c_status,status);
			setString(json,c_nickname,nickname);
			setLong(json,c_time,time);
			setString(json, c_carName, carName);
			setString(json, c_carImg, carImg);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		
		try {
			this.roomId = getString(json,c_roomId);
			this.userId = getString(json,c_userId);
			this.carId = getInt(json,c_carId);
			this.carport = getInt(json,c_carport);
			this.beginTime = getString(json,c_beginTime);
			this.status = getString(json,c_status);
			this.nickname = getString(json,c_nickname);
			this.time = getLong(json,c_time);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
}
