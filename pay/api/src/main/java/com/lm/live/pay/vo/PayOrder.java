package com.lm.live.pay.vo;

import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class PayOrder extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String p_orderId = "a" ; 
	private static final String p_money = "b" ;
	private static final String p_golds = "c" ;
	private static final String p_userId = "d" ;
	private static final String p_payType = "e" ;
	private static final String p_clientType = "f" ;
	private static final String p_remark = "g" ; 
	private static final String p_orderStatus = "h";
	private static final String p_selectMoney = "i";
	private static final String p_receiverUserId = "j";
	
	//订单号
	private String orderId;
	//用户id
	private String userId;
	//充值金额
	private int money ; // 分 
	//支付方式
	private int pay_type ;
	//提交订单方式   1.www  2.安卓    3.ios
	private int clientType ; 
	//订单说明
	private String remark;
	//充值金币
	private int golds;
	//订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
	private int orderStatus;
	
	//选择充值金额，单位：分
	private int selectMoney;
	
	/** 替代被人充值时用:金币实际接受者userId */
	private String receiverUserId;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public JSONObject buildJson() {
		JSONObject json =new JSONObject();
		try{
			setString(json, p_orderId, orderId);
			setInt(json, p_money, money);
			setString(json, p_remark, remark);
			setInt(json, p_payType, pay_type);
			setInt(json, p_golds, golds);
			setInt(json,p_orderStatus , orderStatus);
			setString(json, p_userId, userId);
			setInt(json, p_selectMoney, selectMoney);
			setString(json, p_receiverUserId, receiverUserId);
			return json;
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null)
			return ;
		try{
			orderId = getString(json, p_orderId);
			money = getInt(json, p_money);
			pay_type = getInt(json, p_payType);
			clientType = getInt(json, p_clientType);
			remark = getString(json, p_remark);
			userId = getString(json, p_userId);
			orderStatus = getInt(json, p_orderStatus);
			selectMoney = getInt(json, p_selectMoney);
			receiverUserId = getString(json, p_receiverUserId);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(),e);
		}

	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getGolds() {
		return golds;
	}

	public void setGolds(int golds) {
		this.golds = golds;
	}

	public int getSelectMoney() {
		return selectMoney;
	}

	public void setSelectMoney(int selectMoney) {
		this.selectMoney = selectMoney;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	
		
}
