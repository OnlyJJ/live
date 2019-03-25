package com.jiujun.shows.car.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 停车收益记录
 * @entity
 * @table t_car_park_income_record
 * @author shao.xiang
 * @date 2017-06-10
 */

public class CarParkIncomeRecord extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userId;
	/**
	 * 关联表t_car_park_record表的id
	 */
	private Integer parkRecordId;
	/**
	 * 关联表t_car_park_income_conf表的id
	 */
	private Integer prakIncomConfId;
	private Date recordTime;
	
	
	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public Integer getParkrecordid() {
		return this.parkRecordId;
	}
	
	public void setParkrecordid(Integer parkRecordId) {
		this.parkRecordId = parkRecordId;
	}
	public Integer getPrakincomconfid() {
		return this.prakIncomConfId;
	}
	
	public void setPrakincomconfid(Integer prakIncomConfId) {
		this.prakIncomConfId = prakIncomConfId;
	}
	public Date getRecordtime() {
		return this.recordTime;
	}
	
	public void setRecordtime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
