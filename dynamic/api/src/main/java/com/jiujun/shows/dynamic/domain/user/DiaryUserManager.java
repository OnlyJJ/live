package com.jiujun.shows.dynamic.domain.user;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态用户管理表
 */

public class DiaryUserManager extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	/**
	 * 用户当前使用动态模块功能状态，0-正常，1-禁用动态发布功能，2-禁用动态除查看外其他功能
	 */
	private int powerStatus;
	private Date beginTime;
	private Date endTime;
	/**
	 * 操作者id
	 */
	private String manmgerUserId;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public int getPowerstatus() {
		return this.powerStatus;
	}
	
	public void setPowerstatus(int powerStatus) {
		this.powerStatus = powerStatus;
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
	public String getManmgeruserid() {
		return this.manmgerUserId;
	}
	
	public void setManmgeruserid(String manmgerUserId) {
		this.manmgerUserId = manmgerUserId;
	}
}
