package com.lm.live.tools.enums;

/**
 * 工具枚举(工具,对应表t_tool的type字段)
 * @author Administrator
 *
 */
public class ToolTableEnum {
	
	public static enum ToolBuyableEnum {
		
		/**
		 * 不可购买
		 */
		NO(0),
		
		/** 可购买 */
		Yes(1);
		
		private final int value;
		
		ToolBuyableEnum(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}

	}
	
	
	public static enum Id {
		
		/**
		 * 大喇叭
		 */
		Horn(3),
		
		/**
		 * 钥匙
		 */
		Key(4),
		/**
		 * 铜宝箱
		 */
		BoxBronze(5),
		
		/** 铜桃树种子 */
		TreeSeed(6),
		
		/** 隔空喊话 */
		Gekonghanhua(7),
		
		/** 银宝箱  */
		BoxSilver(8),
		
		/** 金宝箱  */
		BoxGold(9),
		
		/** 钻石宝箱 */
		BoxDiamond(10),
		
		/** 谢谢惠顾 */
		ThankYouCustom(11);
		
		private final int value;
		
		Id(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}

	}
}
