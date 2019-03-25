package com.lm.live.tools.gift.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.gift.domain.ExchangeGiftHis;

/**
 * 礼物兑换历史记录
 * @author shao.xiang
 * @date 2017-06-29 
 */
public interface IExchangeGiftHisService extends ICommonService<ExchangeGiftHis>{
	public void save(ExchangeGiftHis vo) throws Exception;
}
