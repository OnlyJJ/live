package com.lm.live.tools.gift.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_exchange_gift_his
 * @author shao.xiang 
 * @date 2017-06-29
 */
public class ExchangeGiftHis extends BaseVo {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4552562267716707646L;
	/**
	 * 兑换人
	 */
	private String userId;
	/**
	 * 对应兑换礼物配置表t_exchange_gift_conf的id
	 */
	private Integer confId;
	/**
	 * 兑换时间
	 */
	private Date addTime;
	/**
	 * 说明
	 */
	private String comment;
	
	
	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public Integer getConfid() {
		return this.confId;
	}
	
	public void setConfid(Integer confId) {
		this.confId = confId;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
