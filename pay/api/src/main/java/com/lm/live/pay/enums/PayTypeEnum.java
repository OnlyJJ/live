package com.lm.live.pay.enums;

/**
 * 微信支付相关的业务枚举
 * 
 * @author Administrator
 *
 */
public enum PayTypeEnum {

	/** app支付 */
	App("APP"),

	/** 原生扫码支付 */
	NATIVE("NATIVE"),

	/** 公众号支付 */
	JSAPI("JSAPI"),

	/** 微信外H5支付 */
	MWEB("MWEB");

	private final String value;

	PayTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
