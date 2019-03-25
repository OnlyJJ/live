package com.jiujun.shows.car.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_sys_car
 * @date 2017-06-10
 * @author shao.xiang
 */
public class SysCarDo extends BaseVo{
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 座驾名称 */
	private String carName;
	/** 座驾类型 ,0:经典;1:活动  7-守护座驾*/
	private int type;
	/** 金币 */
	private int gold;
	/** 人民币价格,单位:分 */
	private double price;
	/** 有效天数 */
	private int effectiveDays;
	/** 说明 */
	private String comment;

	/** 是否正在使用,0:否,1:是  */
	private int inUse;
	
	/**  座驾状态 ，状态,0:未启用,1:已启用,2:已过期,3:未购买 */
	private int status;

	private String userId;
	
	private String image;
	
	/** 启用时间 */
	private Date beginTime;
	
	private Date endTime;
	
	private int num;
	
	/** 过期 是否需要显示 */
	private int showStatus;
	
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	
	
	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public void setType(int type){
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setGold(int gold){
		this.gold = gold;
	}
	
	public int getGold() {
		return this.gold;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setEffectiveDays(int effectiveDays){
		this.effectiveDays = effectiveDays;
	}
	
	public int getEffectiveDays() {
		return this.effectiveDays;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
	}
	
}