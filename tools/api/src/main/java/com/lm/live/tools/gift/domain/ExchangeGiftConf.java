package com.lm.live.tools.gift.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_exchange_gift_conf
 * @author shao.xiang 
 * @date 2017-06-29
 */
public class ExchangeGiftConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int id;
	/**
	 * 类型id，每一种礼物对应一个id，使用时使用typeId，不直接使用主键id
	 */
	private int typeId;
	/**
	 * 礼物类型，0-游戏礼物，1-实物
	 */
	private int giftType;
	/**
	 * 根据giftType关联t_exchange_article_conf或者t_gift表的id
	 */
	private Integer giftId;
	/**
	 * 兑换工具id，关联t_tool表id
	 */
	private Integer toolId;
	/**
	 * 兑换礼物数量，默认1个
	 */
	private Integer giftNum;
	/**
	 * 兑换1个礼物所需工具数量
	 */
	private Integer toolNum;
	/**
	 * 是否启用，默认1-启用，0-停用
	 */
	private int inUse;
	/**
	 * 兑换对象，0-主播，1-无限制
	 */
	private int exchangeTarget;
	
	/**
	 * 兑换说明
	 */
	private String comment;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGiftType() {
		return this.giftType;
	}
	
	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	public Integer getGiftid() {
		return this.giftId;
	}
	
	public void setGiftid(Integer giftId) {
		this.giftId = giftId;
	}
	public Integer getToolid() {
		return this.toolId;
	}
	
	public void setToolid(Integer toolId) {
		this.toolId = toolId;
	}
	public Integer getGiftnum() {
		return this.giftNum;
	}
	
	public void setGiftnum(Integer giftNum) {
		this.giftNum = giftNum;
	}
	public Integer getToolnum() {
		return this.toolNum;
	}
	
	public void setToolnum(Integer toolNum) {
		this.toolNum = toolNum;
	}
	public int getInuse() {
		return this.inUse;
	}
	
	public void setInuse(int inUse) {
		this.inUse = inUse;
	}
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getExchangeTarget() {
		return exchangeTarget;
	}

	public void setExchangeTarget(int exchangeTarget) {
		this.exchangeTarget = exchangeTarget;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
