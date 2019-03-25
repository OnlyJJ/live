package com.jiujun.shows.decorate.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @entity
 * @table t_medal_info
 * @author shao.xiang
 * @date 2017-06-08
 *
 */
public class MedalInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String userId;
	private String roomId;
	private String name;
	private Date createTime;
	private String lightenImg;
	private int medalId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLightenImg() {
		return lightenImg;
	}
	public void setLightenImg(String lightenImg) {
		this.lightenImg = lightenImg;
	}
	public int getMedalId() {
		return medalId;
	}
	public void setMedalId(int medalId) {
		this.medalId = medalId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	
	
}
