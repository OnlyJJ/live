package com.jiujun.shows.decorate.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 徽章获取历史
 * @entity
 * @table t_medal_his
 * @author shao.xiang
 * @date 2017-06-08
 */

public class MedalHis extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String userId;
	private String roomId;
	private Integer medalId;
	private Date addTime;
	private Integer number;
	private String sourceKey;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public Integer getMedalId() {
		return medalId;
	}
	public void setMedalId(Integer medalId) {
		this.medalId = medalId;
	}
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	
}
