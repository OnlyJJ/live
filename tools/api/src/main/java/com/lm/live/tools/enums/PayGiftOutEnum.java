package com.lm.live.tools.enums;

/**
 * 枚举类<br>
 * 	说明：<br>
 * 		枚举的类型及值应该是固定的，不需要扩展的，如需频繁扩展的，应该使用静态变量类来管理
 * @author shao.xiang
 * @date 2017-06-25
 *
 */
public class PayGiftOutEnum {
	
	/**
	 * 来源类型,默认0:礼物,1:守护
	 *
	 */
	public static enum SourceType{
		/** 礼物 */
		gift(0),
		
		/** 守护  */
		guard(1);
		
		SourceType(int value){
			this.value = value;
		}
		private final int value;
		
		public int getValue() {
			return value;
		}
	} 
	
}
