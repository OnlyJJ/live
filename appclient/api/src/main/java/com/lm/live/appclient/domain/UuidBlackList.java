package com.lm.live.appclient.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * UUID黑名单
 * @author HCY
 */
public class UuidBlackList extends BaseVo {

	private static final long serialVersionUID = 1L;

	/** 主键自增ID */
	private Integer id;
	
	/** 设备信息UUID*/
	private String uuid;
	
	/** 添加时间*/
	private Date addTime;
	
	/** 结束时间*/
	private Date endTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
}
