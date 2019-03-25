package com.jiujun.shows.car.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 停车记录
 * @entity
 * @table t_car_park_record
 * @author shao.xiang
 * @date 2017-06-10
 */

public class CarParkRecord extends BaseVo {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 主播房间 */
	private String roomId;
	/** 主播id */
	private String anchorId;
	/** 用户id */
	private String userId;
	/** 车id */
	private Integer carId;
	/** 车位号 */
	private Integer carport;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 结束时间
	 */
	private String endStatus;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoomId() {
		return this.roomId;
	}
	
	public void setRoomid(String roomid) {
		this.roomId = roomid;
	}
	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userid) {
		this.userId = userid;
	}
	public Integer getCarid() {
		return this.carId;
	}
	
	public void setCarid(Integer carid) {
		this.carId = carid;
	}
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Integer getCarport() {
		return carport;
	}

	public void setCarport(Integer carport) {
		this.carport = carport;
	}

	public String getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}
	
}
