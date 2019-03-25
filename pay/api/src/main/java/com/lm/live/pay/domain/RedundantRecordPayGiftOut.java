package com.lm.live.pay.domain;

import java.util.Date;


import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_redundant_record_pay_gift_out
 * @date 2017-8-2
 * @author shao.xiang
 */
public class RedundantRecordPayGiftOut extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** orderId */
	private String orderId;
	/** userId */
	private String userId;
	/** toUserId */
	private String toUserId;
	/** giftId */
	private Integer giftId;
	/** number */
	private Integer number;
	/** diamond */
	private int diamond;
	/** resultTime */
	private Date resultTime;
	/**判断resultTime，大于或等于 */
	private Date	gtResultTime;
	/**判断resultTime，小于或等于 */
	private Date	ltResultTime;
	/** remark */
	private String remark;
	/** price */
	private Integer price;
	/** userPoint */
	private Integer userPoint;
	/** anchorPoint */
	private Integer anchorPoint;

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
	
	public void setToUserId(String toUserId){
		this.toUserId = toUserId;
	}
	
	public String getToUserId() {
		return this.toUserId;
	}
	
	public void setGiftId(Integer giftId){
		this.giftId = giftId;
	}
	
	public Integer getGiftId() {
		return this.giftId;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setDiamond(int diamond){
		this.diamond = diamond;
	}
	
	public int getDiamond() {
		return this.diamond;
	}
	
	public void setResultTime(Date resultTime){
		this.resultTime = resultTime;
	}
	
	public Date getResultTime() {
		return this.resultTime;
	}
	
	public void setGtResultTime(Date gtResultTime){
		this.gtResultTime = gtResultTime;
	}
	
//	@JsonIgnore
	public Date getGtResultTime() {
		return this.gtResultTime;
	}
	
	public void setLtResultTime(Date ltResultTime){
		this.ltResultTime = ltResultTime;
	}
	
//	@JsonIgnore
	public Date getLtResultTime() {
		return this.ltResultTime;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public void setPrice(Integer price){
		this.price = price;
	}
	
	public Integer getPrice() {
		return this.price;
	}
	
	public void setUserPoint(Integer userPoint){
		this.userPoint = userPoint;
	}
	
	public Integer getUserPoint() {
		return this.userPoint;
	}
	
	public void setAnchorPoint(Integer anchorPoint){
		this.anchorPoint = anchorPoint;
	}
	
	public Integer getAnchorPoint() {
		return this.anchorPoint;
	}
	

}