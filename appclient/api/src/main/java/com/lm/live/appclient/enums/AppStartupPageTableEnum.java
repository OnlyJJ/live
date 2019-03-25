package com.lm.live.appclient.enums;

/**
 * 开机页(t_app_startup_page字段值-枚举)
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
public class AppStartupPageTableEnum {
	/**
	 * 主题类型，1：单张图片自动滚动 2：多张图片自动滑动  3：多张图片手动滑动 4：渐变 5：单张图片没动画
	 *
	 */
	public static enum ThemeType{
		/**
		 *1-单张图片自动滚动
		 */
		SingleImg_Auto_Marquee(1),
		
		/**
		 *2-多张图片自动滑动
		 */
		MultipleImg_Auto_Marquee(2),
		
		/**
		 *3-多张图片手动滑动
		 */
		MultipleImg_Handle_Marquee(3),
		
		/**
		 * 4-渐变
		 */
		Jianbian(4),
		
		/**
		 * 5-单张图片没动画
		 */
		SingleImg_No_Animation(5);
		
		
		
		
		
		private final int value;
		
		ThemeType(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 跳转类型,0:不跳转;1:URL跳转;2:房间跳转
	 *
	 */
	public static enum JumpType{
		/**
		 * 0:不跳转
		 */
		No_Jump(0),
		
		/**
		 * 1:URL跳转
		 */
		Jump_Url(1),
		
		/**
		 * 2:房间跳转
		 */
		Jump_Room(2);
		
		private final int value;
		
		JumpType(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
	
	
	/**
	 * 是否使用,0:不使用,1:使用
	 *
	 */
	public static enum InUse{
		/**
		 * 0:不使用
		 */
		NO(0),
		
		/**
		 * 1:使用
		 */
		YES(1);
		
		private final int value;
		
		InUse(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
}
