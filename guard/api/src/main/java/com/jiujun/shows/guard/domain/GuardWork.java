package com.jiujun.shows.guard.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * t_guard_work
 * @author shao.xiang
 * @date 2017-06-13
 *
 */
public class GuardWork extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2225305150822365833L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 关联守护配置表id
	 */
	private Integer guardId;
	/**
	 * 用户
	 */
	private String userId;
	/**
	 * 房间
	 */
	private String roomId;
	/**
	 * 是否有时间限制，0-否，1-是
	 */
	private int isPeriod;
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	
	private int sortIndex;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getGuardid() {
		return this.guardId;
	}
	
	public void setGuardid(Integer guardId) {
		this.guardId = guardId;
	}
	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getRoomid() {
		return this.roomId;
	}
	
	public void setRoomid(String roomId) {
		this.roomId = roomId;
	}
	public int getIsperiod() {
		return this.isPeriod;
	}
	
	public void setIsperiod(int isPeriod) {
		this.isPeriod = isPeriod;
	}
	public Date getEndtime() {
		return this.endTime;
	}
	
	public void setEndtime(Date endTime) {
		this.endTime = endTime;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	
	
}
