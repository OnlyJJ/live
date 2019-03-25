package com.jiujun.shows.decorate.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 徽章包裹
 * @entity
 * @table t_medal_package
 * @author shao.xiang
 * @date 2017-06-08
 */

public class MedalPackage extends BaseVo {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String userId;
	private String roomId;
	private Integer medalId;
	/**
	 * 是否具备时间限制：0-否，1-是
	 */
	private int isPeriod;
	private Integer number;
	private Date beginTime;
	private Date endTime;
	private int isAccumulation;
	
	/** 是否佩戴用户徽章,y 或 n  */
	private String isAdornUserMedal;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	
	public Integer getMedalId() {
		return medalId;
	}

	public void setMedalId(Integer medalId) {
		this.medalId = medalId;
	}

	public int getIsperiod() {
		return this.isPeriod;
	}
	
	public void setIsperiod(int isPeriod) {
		this.isPeriod = isPeriod;
	}
	public Integer getNumber() {
		return this.number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndtime() {
		return this.endTime;
	}
	
	public void setEndtime(Date endTime) {
		this.endTime = endTime;
	}

	public int getIsAccumulation() {
		return isAccumulation;
	}

	public void setIsAccumulation(int isAccumulation) {
		this.isAccumulation = isAccumulation;
	}

	public String getIsAdornUserMedal() {
		return isAdornUserMedal;
	}

	public void setIsAdornUserMedal(String isAdornUserMedal) {
		this.isAdornUserMedal = isAdornUserMedal;
	}
	
}
