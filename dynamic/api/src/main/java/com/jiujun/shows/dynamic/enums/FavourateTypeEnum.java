package com.jiujun.shows.dynamic.enums;

public class FavourateTypeEnum {
	public enum Type {
		/** 踩 */
		belittle(1),
		/** 点赞 */
		prise(0);
		
		private final int value;
		
		Type(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
}
