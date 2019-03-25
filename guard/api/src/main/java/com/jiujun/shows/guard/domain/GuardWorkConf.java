package com.jiujun.shows.guard.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * t_guard_work_conf
 * @author shao.xiang
 * @date 2017-06-13
 *
 */
public class GuardWorkConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8562123767982339134L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 房间守护最大数量
	 */
	private int maxSize;
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 修改时间
	 */
	private Date editTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	
}
