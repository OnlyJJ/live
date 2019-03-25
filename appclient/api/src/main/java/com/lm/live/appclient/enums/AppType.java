package com.lm.live.appclient.enums;

/**
 * 客户端类型
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public enum AppType {

	/** 0:android */
	ANDROID(0),

	/** 1:web */
	WEB(1),
	/** 2,H5 */
	H5(2);

	private final int value;

	AppType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
