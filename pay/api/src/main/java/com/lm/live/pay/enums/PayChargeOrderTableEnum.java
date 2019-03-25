package com.lm.live.pay.enums;


/** 充值订单表 : t_pay_charge_order  */
public class PayChargeOrderTableEnum {
	
	/** 订单状态  */
	public static enum OrderStatus{

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
		
		OrderStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	/** 充值方式  */
	public static enum PayType{

		/** 1-支付宝 */
		ZHIFUBAO(1),
		/** 2-银联 */
		YINLIAN(2),
		/** 3-微信支付  */
		WEIXIN(3),
		/** 4-苹果支付 */
		APPLEPAY(4);
		
		private final int value;
		
		PayType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	/**
	 * createType
	 * 生成订单的环境
	 */
	public static enum CreateType{
		/**
		 * 1.www
		 */
		WWW(1),
		
		/**
		 *  2.安卓
		 */
		ANDROID(2),
		
		/** 3.ios */
		USER(3);
		
		private final int value;
		
		CreateType(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}

	/** 订单是否通过沙箱服务认证充值成功 */
	public static enum IsPayBySanBox{
		/**
		 * 0:否
		 */
		NO(0),
		
		/**
		 *  1:是
		 */
		YES(1);
		
		private final int value;
		
		IsPayBySanBox(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
}
