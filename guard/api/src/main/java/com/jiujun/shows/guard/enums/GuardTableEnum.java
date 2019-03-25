package com.jiujun.shows.guard.enums;


/**
 * 守护相关枚举
 * @author shao.xiang
 * @date 2017-06-13
 *
 */
public class GuardTableEnum {
	
	/**
	 * 守护座驾id，是car表id
	 * @author Administrator
	 *
	 */
	public static enum CarId{
		
		/**
		 * 白银座驾
		 */
		baiyin(23),
		
		/**
		 * 黄金座驾
		 */
	    huangjin(24);
	    
		
		private final int value;
		
		CarId(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 守护座驾类型，用于购买守护时，区别守护座驾
	 * @author Administrator
	 *
	 */
	public static enum GuardCarType {
		
		/** 白银 */
		baiyin(1),
		
		/** 黄金 */
		huangjin(2);
		
		private final int value;
		GuardCarType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	/**
	 * carType 用于座驾区别其他的座驾类型
	 * @author Administrator
	 *
	 */
	public static enum CarType {
		
		/** 守护座驾  */
		shouhu(7);
		
		private final int value;
		CarType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	public static enum GuardType {
		
		/** 白银守护  */
		baiyin(1),
		/** 黄金守护 */
		huangjin(2);
		
		private final int value;
		
		GuardType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	public static enum GuardName {
		
		/** 默认守护名称，-守护*/
		name_def("守护"),
		/** 忠义小队 */
		name_one("忠义小队"),
		/** 威武中队 */
		name_two("威武中队"),
		/** 虎狼大队 */
		name_thr("虎狼大队"),
		/** 铁血之师 */
		name_fou("铁血之师"),
		/** 王牌军团  */
		name_fiv("王牌军团");
		
		private final String value;
		
		GuardName(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		
	}

}
