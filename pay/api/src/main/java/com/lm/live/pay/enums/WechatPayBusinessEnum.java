package com.lm.live.pay.enums;

/**
 * 微信支付相关的业务枚举
 * @author shao.xiang
 * @date 2017-08-02
 *
 */
public class WechatPayBusinessEnum {
	
	/**
	 * 交易类型
	 */
	public  enum TradeType {
		
		/** app支付  */
		App("APP"),
		
		/** 原生扫码支付 */
		NATIVE("NATIVE"),
		
		/** 公众号支付   */
		JSAPI("JSAPI"),
		
		/** 微信外H5支付 */
		MWEB("MWEB");
		
		private final String value;
		
		TradeType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
