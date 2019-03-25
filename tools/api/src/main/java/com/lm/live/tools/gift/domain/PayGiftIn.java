package com.lm.live.tools.gift.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_pay_gift_in
 * @author shao.xiang
 * @date 2016-06-25
 */
public class PayGiftIn extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 消费订单号,格式:yyyyMMddHHmmssSSS+rand(0000~9999) */
	private String orderId;
	/** 消费用户ID */
	private String userId;
	/** 购买礼物ID，关联礼物表Id */
	private Integer giftId;
	/** 礼物单价，单位：金币 */
	private Integer price;
	/** 购买数量 */
	private Integer number;
	/** 花费金币 */
	private Integer gold;
	/** 消费信息 */
	private String remark;
	/** 消费时间 */
	private Date resultTime;
	/** 是否有打折，是的时候才有下面字段值 */
	private Integer isOnSale;
	/** 打折信息 */
	private String onSaleMsg;
	/** 打折返回金币 */
	private Integer returnGold;
	/** 实际花费金币 */
	private Integer realGold;

	
	
	/** 关联gift表的字段 */
	/** 礼物名称 */
	private String giftName; 
	
	/**查询条件**/
	/**开始时间 */
	private String startTime;
	/**结束时间 */
	private String endTime;

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	public String getOrderId() {
		return this.orderId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setGiftId(Integer giftId){
		this.giftId = giftId;
	}
	
	public Integer getGiftId() {
		return this.giftId;
	}
	
	public void setPrice(Integer price){
		this.price = price;
	}
	
	public Integer getPrice() {
		return this.price;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setGold(Integer gold){
		this.gold = gold;
	}
	
	public Integer getGold() {
		return this.gold;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public void setResultTime(Date resultTime){
		this.resultTime = resultTime;
	}
	
	public Date getResultTime() {
		return this.resultTime;
	}
	
	public void setIsOnSale(Integer isOnSale){
		this.isOnSale = isOnSale;
	}
	
	public Integer getIsOnSale() {
		return this.isOnSale;
	}
	
	public void setOnSaleMsg(String onSaleMsg){
		this.onSaleMsg = onSaleMsg;
	}
	
	public String getOnSaleMsg() {
		return this.onSaleMsg;
	}
	
	public void setReturnGold(Integer returnGold){
		this.returnGold = returnGold;
	}
	
	public Integer getReturnGold() {
		return this.returnGold;
	}
	
	public void setRealGold(Integer realGold){
		this.realGold = realGold;
	}
	
	public Integer getRealGold() {
		return this.realGold;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}