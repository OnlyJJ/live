package com.lm.live.tools.enums;
/**
	 * (礼物类型,根据业务、接口统一约定的)
	 * @author Administrator
	 *
	 */
	public  enum GiftTypeBusinessEnum {
		
		//奖品类型,0:座驾,1:蜜桃,2:金币,3:大喇叭,4:钥匙(开用户等级宝箱),5:宝箱(用户等级)
		
		/** 0:座驾 */
		Car(0),
		
		/** 1:蜜桃 */
		Peach(1),
		
		/** 2:金币 */
		Gold(2),
		
		/** 3:大喇叭 (更名为传送门)*/
		Horn(3),
		
		/** 4:钥匙(开用户等级宝箱) */
		Key(4),
		
		/** 5:宝箱(开用户等级宝箱) */
		BoxUserLevel(5),
		
		/** 6:蜜桃种子 */
		PeachSeed(6),
		
		/** 隔空喊话 */
		AllRoomMSG(7),
		
		/**
		 * 以下为动态购买道具是使用
		 */
		key_3(3),//3:传送门
		
		key_4(4),//4:钥匙(开用户等级宝箱)
		
		key_5(5),//5:宝箱(开用户等级宝箱)
		
		key_6(6),//6:蜜桃种子
		
		key_7(7),//7:隔空喊话
		
		key_8(8),//8:银宝箱
		
		key_9(9),//9:金宝箱
		
		key_10(10);//10:钻石宝箱
		
		private final int value;
		
		GiftTypeBusinessEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}