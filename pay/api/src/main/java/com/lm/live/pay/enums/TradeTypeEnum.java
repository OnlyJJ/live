package com.lm.live.pay.enums;

/**
 * 微信支付相关的业务枚举
 * 
 * @author Administrator
 *
 */
public enum TradeTypeEnum {

	/** app支付 */
	Ali(1),

	/** 微信 */
	Wechat(2);

	private final int value;

	TradeTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
