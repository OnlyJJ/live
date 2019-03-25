package com.jiujun.shows.guard.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * t_guard_punch_give
 * @author shao.xiang
 * @date 2017-06-13
 *
 */
public class GuardPunchGive extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6339241434370599738L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 关联t_guard_pay_his表的id
	 */
	private Integer payHisId;
	/**
	 * 关联t_guard_work的id
	 */
	private Integer workId;
	/**
	 * 用户
	 */
	private String userId;
	/**
	 * 房间
	 */
	private String roomId;
	/**
	 * 赠送天数
	 */
	private Integer type;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 截止日期
	 */
	private Date endTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getPayhisid() {
		return this.payHisId;
	}
	
	public void setPayhisid(Integer payHisId) {
		this.payHisId = payHisId;
	}
	public Integer getWorkId() {
		return workId;
	}

	public void setWorkId(Integer workId) {
		this.workId = workId;
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
	public Integer getType() {
		return this.type;
	}
	
	public void setType(Integer type) {
		this.type = type;
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
}
