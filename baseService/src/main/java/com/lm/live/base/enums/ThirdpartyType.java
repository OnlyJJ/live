package com.lm.live.base.enums;

/**
 * 第三方配置
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public enum ThirdpartyType {

	/** 第三方类型,0:qq;1:微信;2:微博 */
	/** qq */
	QQ(0),

	/** 微信 */
	WEIXIN(1),

	/** 微博 */
	WEIBO(2);

	private final int value;

	ThirdpartyType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
