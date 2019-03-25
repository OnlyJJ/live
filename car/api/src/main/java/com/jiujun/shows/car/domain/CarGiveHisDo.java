package com.jiujun.shows.car.domain;

import java.util.Date;

/**
 * @entity
 * @table t_car_give_his
 * @author shao.xiang
 * @date 2016-06-10
 */
public class CarGiveHisDo {
	/** id */
	private Integer id;
	/** 座驾id */
	private int carId;
	/** 赠送的用户userId */
	private String sendUserId;
	/** 受赠的用户userId */
	private String receiveUserId;
	/** 赠送数量 */
	private int num;
	/** 记录时间 */
	private Date recordDate;
	/** 赠言 */
	private String comment;
	
	private int type ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}