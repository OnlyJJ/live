package com.jiujun.shows.car.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 停车收益配置
 * @entity
 * @table t_car_park_income_conf
 * @author shao.xiang
 * @date 2017-06-10
 */

public class CarParkIncomeConf extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer carId;
	/**
	 * 停靠时长(单位:分钟)
	 */
	private Integer parkTime;
	/**
	 * 停靠收益(金币:个)
	 */
	private Integer receiveGold;
	
	
	public Integer getCarid() {
		return this.carId;
	}
	
	public void setCarid(Integer carId) {
		this.carId = carId;
	}
	public Integer getParktime() {
		return this.parkTime;
	}
	
	public void setParktime(Integer parkTime) {
		this.parkTime = parkTime;
	}
	public Integer getReceivegold() {
		return this.receiveGold;
	}
	
	public void setReceivegold(Integer receiveGold) {
		this.receiveGold = receiveGold;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
