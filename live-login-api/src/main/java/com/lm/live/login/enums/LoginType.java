package com.lm.live.login.enums;


/**
 * 登录方式
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum LoginType {
	/** 伪登录 */
	PESO(7),
	/** WEB微博 */
	WEBWB(6),
	/** APP微博 */
	APPWB(5),
	/** webQQ登录 */
	WEBQQ(4),
	/** appQQ登录 */
	APPQQ(3),
	/** 微信登录 */
	WECHAT(2),
	/** 自动注册 */
	AUTO(1);
	
	private int type;
	
	private LoginType(int type){
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
