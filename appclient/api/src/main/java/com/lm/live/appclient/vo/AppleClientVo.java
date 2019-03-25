package com.lm.live.appclient.vo;

import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 苹果客户端请求实体
 *
 */
public class AppleClientVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 475916436119715324L;
	
	/** 苹果支付所需的 verifyReceipt */
	private String verifyReceipt;
	
	/** 支付请求地址,1:沙盒(https://sandbox.itunes.apple.com/ );2:正式地址(https://buy.itunes.apple.com) */
	private int payReqAddr ;
	
	/** 苹果商品id  */
	private String productId ;
	
	/** 订单号 */
	private String orderId;
	
	/** 替代被人充值时用:金币实际接受者userId */
	private String receiverUserId;
	
	/** remark后缀 : remarkSuffix */
	private String remarkSuffix;
	
	// 字段key
	private static final String a_verifyReceipt = "a";
	private static final String a_payReqAddr = "c";
	private static final String a_productId = "d";
	private static final String a_orderId = "e";
	private static final String a_receiverUserId = "f";
	private static final String a_remarkSuffix = "g";

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, a_verifyReceipt, verifyReceipt);
			setInt(json, a_payReqAddr, payReqAddr); 
			setString(json, a_orderId, orderId);
			setString(json, a_productId, productId); 
			setString(json, a_receiverUserId, receiverUserId); 
			setString(json, a_remarkSuffix, remarkSuffix); 
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			this.verifyReceipt = getString(json, a_verifyReceipt);
			this.payReqAddr = getInt(json, a_payReqAddr);
			orderId = getString(json, a_orderId);
			productId = getString(json, a_productId);
			receiverUserId =	getString(json, a_receiverUserId);
			remarkSuffix =	getString(json, a_remarkSuffix);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public String getVerifyReceipt() {
		return verifyReceipt;
	}

	public void setVerifyReceipt(String verifyReceipt) {
		this.verifyReceipt = verifyReceipt;
	}

	public int getPayReqAddr() {
		return payReqAddr;
	}

	public void setPayReqAddr(int payReqAddr) {
		this.payReqAddr = payReqAddr;
	}

	

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public String getRemarkSuffix() {
		return remarkSuffix;
	}

	public void setRemarkSuffix(String remarkSuffix) {
		this.remarkSuffix = remarkSuffix;
	}
	
}
