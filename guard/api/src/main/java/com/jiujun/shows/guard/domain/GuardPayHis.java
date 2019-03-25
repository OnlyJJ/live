package com.jiujun.shows.guard.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 守护购买记录表，用户每购买或续期一次，都增加一条记录
 * @table t_guard_pay_his
 * @author shao.xiang
 * @date 2017-06-13
 */

public class GuardPayHis extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6532274537651960249L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 关联守护配置表id
	 */
	private Integer guardId;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 有效天数
	 */
	private Integer validate;
	
	/** 关联work工作表的id */
	private Integer workId;
	
	/** 用户花费金币数  */
	private int price;
	
	/** 主播收到的钻石数  */
	private int diamond;
	
	/** 备注 */
	private String remark;
	
	private String toUserId;
	
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
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getValidate() {
		return this.validate;
	}
	
	public void setValidate(Integer validate) {
		this.validate = validate;
	}

	public Integer getWorkId() {
		return workId;
	}

	public void setWorkId(Integer workId) {
		this.workId = workId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
