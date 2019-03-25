package com.lm.live.pay.enums;

/**
 * 微信支付相关的业务枚举
 * 
 * @author Administrator
 *
 */
public enum ChargeStatusEnum {

	/** 1-生成订单 */
	CREATE(1),
	/** 2-提交订单 */
	SUBMIT(2),
	/** 3-充值失败 */
	FAIL(3),
	/** 4-充值成功 */
	SUCCESS(4),
	/** 5-同步成功 */
	SYN_SUCCESS(5);

	private final int value;

	ChargeStatusEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
