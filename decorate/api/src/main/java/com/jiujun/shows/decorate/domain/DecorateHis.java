package com.jiujun.shows.decorate.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 勋章获取历史
 * @entity
 * @table t_decorate_his
 * @author shao.xiang
 * @date 2017-07-08
 */

public class DecorateHis extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String userId;
	private Integer decorateId;
	private Date addTime;
	private Integer number;
	private String sourceKey;
	private String descs;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getDecorateId() {
		return decorateId;
	}
	public void setDecorateId(Integer decorateId) {
		this.decorateId = decorateId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	
	
}
