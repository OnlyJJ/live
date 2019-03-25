package com.lm.live.account.service;


/**
 * Service - 用户账户(金币、钻石)加减流水记录汇总 
 */
public interface IUserAccountBookService {
	/**
	 * 发放奖励并记入账户明细
	 * @param userId
	 * @param prizeType 奖励类型，0-金币，1-礼物，2-座驾，3-勋章，4-工具
	 * @param prizeId 奖励物品id
	 * @param isPeriod 是否有时间限制
	 * @param num 数量
	 * @param golds 金币
	 * @param validate 有效时长
	 * @param isAccumulation 是否结束时间累加（勋章）
	 * @param content 备注
	 * @param sourceId 来源id
	 * @param sourceDesc 来源描述
	 * @param sourceKey key（用于勋章)
	 * @param desc 用于勋章描述
	 * @throws Exception
	 */
	public void givePrizeAndRecordBook(String userId, int prizeType, int prizeId,boolean isPeriod,int num,int golds,
			int validate,int isAccumulation,String content,String sourceId, String sourceDesc,String sourceKey,String desc) throws Exception;
	
}
