package com.jiujun.shows.car.domain;

import java.util.Date;

/**
 * @entity
 * @table t_user_carport
 * @author shao.xiang
 * @date 2017-06-10
 */
public class UserCarPortDo {
	/** id */
	private Integer id;
	/** 用户userId */
	private String userId;
	/** 座驾id */
	private int carId;
	/** 启用时间 */
	private Date beginTime;
	/** 到期时间 */
	private Date endTime;
	
	/** 是否正在使用,0:否,1:是  */
	private int inUse;
	
	/** 状态,座驾状态 ，状态,0:未启用,1:已启用,2:已过期,3:未购买   */
	private int status;
	
	/** 数量,启用后会设置为0  */
	private int num;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getInUse() {
		return inUse;
	}
	public void setInUse(int inUse) {
		this.inUse = inUse;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}