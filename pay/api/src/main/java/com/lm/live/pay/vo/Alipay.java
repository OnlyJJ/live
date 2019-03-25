package com.lm.live.pay.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Alipay extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = -7026307232310131034L;
	private static final String p_partnerId = "a" ;
	private static final String p_sellerEmail = "b" ;
	private static final String p_subject = "c" ;
	private static final String p_body = "d" ;
	private static final String p_notifyUrl = "e" ;
	private static final String p_sign = "f" ;
	
	/** 合作身份者ID，以2088开头由16位纯数字组成的字符串 */
	private String partnerId;
	/** 签约支付宝账号或卖家收款支付宝帐户 */
	private String sellerEmail;
	private String subject;
	private String body;
	private String notifyUrl;
	private String sign;

	@Override
	public JSONObject buildJson() {
		JSONObject json =new JSONObject();
		try{
			setString(json, p_partnerId, partnerId);
			setString(json , p_sellerEmail , sellerEmail);
			setString(json, p_subject, subject);
			setString(json , p_body , body);
			setString(json, p_notifyUrl , notifyUrl);
			setString(json, p_sign , sign);
			return json;
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null){
			return ;
		}else{
			
			try{
				partnerId = getString(json, p_partnerId);
				sellerEmail = getString(json, p_sellerEmail);
				subject = getString(json, p_subject);
				body = getString(json, p_body);
				notifyUrl = getString(json, p_notifyUrl);
				sign = getString(json, p_sign);
			}catch(Exception e){
				LogUtil.log.error(e.getMessage(),e);
			}

		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String getpBody() {
		return p_body;
	}

}
