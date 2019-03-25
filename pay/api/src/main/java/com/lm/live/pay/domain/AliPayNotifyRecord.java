package com.lm.live.pay.domain;

import com.lm.live.common.vo.BaseVo;

public class AliPayNotifyRecord extends BaseVo{
	private static final long serialVersionUID = 8367397668107791474L;
	/** 自增ID*/
	private Integer id;
	/** 服务器记录时间*/
	private String recordDateTime;
	/** 支付宝交易号*/
	private String tradeNo;
	/** 回调通知金额，单位：分*/
	private Integer totalFee;
	/** 服务器订单号*/
	private String outTradeNo;
	/** 回调通知交易状态*/
	private String tradeStatus;
	/** 验证签名信息*/
	private String verifyMap;
	/** SUCCESS表示商户接收通知成功并校验成功*/
	private String returnCode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecordDateTime() {
		return recordDateTime;
	}
	public void setRecordDateTime(String recordDateTime) {
		this.recordDateTime = recordDateTime;
	}

	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getVerifyMap() {
		return verifyMap;
	}
	public void setVerifyMap(String verifyMap) {
		this.verifyMap = verifyMap;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	
}
