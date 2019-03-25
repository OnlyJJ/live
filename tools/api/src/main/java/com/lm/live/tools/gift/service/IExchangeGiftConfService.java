package com.lm.live.tools.gift.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.gift.domain.ExchangeGiftConf;

/**
 * 礼物兑换配置服务
 * @author shao.xiang
 * @date 2017-06-29
 */
public interface IExchangeGiftConfService extends ICommonService<ExchangeGiftConf>{
	/**
	 * 兑换物品
	 * @param userId
	 * @param typeId
	 * @throws Exception
	 */
	public void exchangeGift(String userId, int typeId) throws Exception;
}
