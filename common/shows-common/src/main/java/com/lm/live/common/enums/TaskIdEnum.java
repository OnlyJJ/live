package com.lm.live.common.enums;

/**
 * taskId枚举，此类禁止扩展，如需增加，请自行在子模块中增加
 * @author shao.xiang
 * @date 2017-06-02
 *
 */
public enum TaskIdEnum {
	/** 充值送礼*/
	pay(5),
	
	/** 摘蜜桃 */
	peach(6),
	
	/** 宝箱抽奖得到的礼物  */
	BOX_GIFT(7);
	
	
	
	private final int value;
	
	TaskIdEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
