package com.lm.live.pay.vo;

import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 微信公众号支付
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6
 * @author Administrator
 *
 */
public class WechatJSAPIVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String p_appId = "a";
	private static final String p_noncestr = "b";
	private static final String p_signType = "c";
	private static final String p_paySign = "d";
	private static final String p_package_ = "e";
	private static final String p_timestamp = "f";
	private static final String p_receiverUserId = "g";
	private static final String p_buyGold = "h";
	private static final String p_total_fee = "i";
	private static final String p_openid = "j";
	private static final String p_payType = "k";
	private static final String p_mweburl = "l";
	private static final String p_spbill_create_ip = "m";
	private static final String p_pkgname = "o";
	private static final String p_creatType="p";
	private static final String p_channelId = "q";
	
	/** 公众号id */
	private String appId;
	
	//随机字符串
	private String noncestr;
	
	//签名方式：MD5
	private String signType;
	
	// 签名
	private String paySign;
	
	//订单详情扩展字符串 package为java的关键字，所以多加下划线
	private String  package_;
	
	//时间戳
	private long timestamp  ;
	
	/** 金币实际接受者userId */
	private String receiverUserId;
	
	/** 购买的金币 */
	private int buyGold;
	
	/** 需要支付的金额：单位：分 */
	private int total_fee;
	
	/** openid */
	private String openid; 
	
	/** 支付类型，0-公众号，1-H5（微信外浏览器支付） */
	private int payType;
	
	/** 微信外支付调用统一下单接口，返回的url */
	private String mweburl;
	
	/** H5支付客户端上传的ip */
	private String spbill_create_ip;
	/** 包名 */
	private String pkgname;
	/** 订单环境，1.www，2-android，3-ios */
	private int creatType;
	/** 渠道 */
	private String channelId;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, p_noncestr, noncestr);
			setString(json, p_signType, signType);
			setString(json, p_paySign, paySign);
			setString(json, p_package_, package_);
			setLong(json, p_timestamp, timestamp);
			setString(json, p_appId, appId);
			setString(json, p_receiverUserId, receiverUserId);
			setInt(json,p_buyGold, buyGold);
			setInt(json,p_total_fee, total_fee);
			setString(json, p_openid, openid);
			setInt(json, p_payType, payType);
			setString(json, p_mweburl, mweburl);
			setString(json,p_spbill_create_ip,spbill_create_ip);
			setString(json,p_pkgname,pkgname);
			setInt(json,p_creatType,creatType);
			setString(json,p_channelId,channelId);
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
			buyGold = getInt(json, p_buyGold);
			receiverUserId  = getString(json, p_receiverUserId);
			openid = getString(json, p_openid);
			payType = getInt(json, p_payType);
			spbill_create_ip = getString(json,p_spbill_create_ip);
			pkgname = getString(json,p_pkgname);
			creatType = getInt(json, p_creatType);
			channelId = getString(json,p_channelId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getPackage_() {
		return package_;
	}

	public void setPackage_(String package_) {
		this.package_ = package_;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public int getBuyGold() {
		return buyGold;
	}

	public void setBuyGold(int buyGold) {
		this.buyGold = buyGold;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getMweburl() {
		return mweburl;
	}

	public void setMweburl(String mweburl) {
		this.mweburl = mweburl;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	public int getCreatType() {
		return creatType;
	}

	public void setCreatType(int creatType) {
		this.creatType = creatType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	
}
