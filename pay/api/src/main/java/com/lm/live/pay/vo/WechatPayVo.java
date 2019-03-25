package com.lm.live.pay.vo;

import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 参照微信支付接口的"调起支付接口"定义
 * https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_12&index=2
 * @author Administrator
 *
 */
public class WechatPayVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 4450896206198669122L;
	
	private static final String p_totalFree = "a";
	private static final String p_noncestr = "b";
	private static final String p_sign = "c";
	private static final String p_partnerid = "d";
	private static final String p_prepayid = "e";
	private static final String p_package_ = "f";
	private static final String p_timestamp = "g";
	private static final String p_out_trade_no = "h";
	private static final String p_appid = "i";
	private static final String p_tradeType = "j";
	private static final String p_codeUrl = "k";
	private static final String p_receiverUserId = "l";
	
	/** 支付金额(单位:分) */
	private int totalFree;
	
	//随机字符串
	private String  noncestr;
	
	//签名
	private String  sign;
	
	//商户号
	private String  partnerid;
	
	//预支付交易会话标识
	private String  prepayid;
	
	//固定为 : Sign=WXPay
	//package为java的关键字，所以多加下划线
	private String  package_;
	
	//时间戳
	private long timestamp  ;
	
	// 公众账号ID
	private String appid;
	
	//商户系统内部的订单号,32个字符内、可包含字母
	private String out_trade_no;

	/**交易类型,取值如下：JSAPI，NATIVE，APP，为空时则默认为APP */
	private String tradeType;
	
	/** trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付  */
	private String codeUrl;
	
	/** 替代被人充值时用:金币实际接受者userId */
	private String receiverUserId;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, p_totalFree, totalFree);
			setString(json, p_noncestr, noncestr);
			setString(json, p_sign, sign);
			setString(json, p_partnerid,  partnerid);
			setString(json, p_prepayid, prepayid);
			setString(json, p_package_, package_);
			setLong(json, p_timestamp, timestamp);
			setString(json, p_appid, appid);
			setString(json, p_out_trade_no, out_trade_no);
			setString(json, p_tradeType, tradeType);
			setString(json, p_codeUrl, codeUrl);
			setString(json, p_receiverUserId, receiverUserId);
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
			totalFree = getInt(json, p_totalFree);
			tradeType = getString(json, p_tradeType);
			receiverUserId  = getString(json, p_receiverUserId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public int getTotalFree() {
		return totalFree;
	}

	public void setTotalFree(int totalFree) {
		this.totalFree = totalFree;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
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

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getpTotalfree() {
		return p_totalFree;
	}

	public static String getpNoncestr() {
		return p_noncestr;
	}

	public static String getpSign() {
		return p_sign;
	}

	public static String getpPartnerid() {
		return p_partnerid;
	}

	public static String getpPrepayid() {
		return p_prepayid;
	}

	public static String getpPackage() {
		return p_package_;
	}

	public static String getpTimestamp() {
		return p_timestamp;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
	
}
