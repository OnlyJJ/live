package com.jiujun.shows.decorate.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @entity
 * @table t_medal
 * @author sha0.xiang
 * @date 2017-06-08
 *
 */
public class Medal implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String userId;
	private Date createTime;
	private String lightenImg;
	private String isAdornUserMedal;
	private int medalId;
	private String roomId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getIsAdornUserMedal() {
		return isAdornUserMedal;
	}
	public void setIsAdornUserMedal(String isAdornUserMedal) {
		this.isAdornUserMedal = isAdornUserMedal;
	}
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
	public int getMedalId() {
		return medalId;
	}
	public void setMedalId(int medalId) {
		this.medalId = medalId;
	}
}
