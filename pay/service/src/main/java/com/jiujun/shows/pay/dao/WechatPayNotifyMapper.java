package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.WechatPayNotifyDo;

public interface WechatPayNotifyMapper extends ICommonMapper<WechatPayNotifyDo> {

	WechatPayNotifyDo getByOutTradeNo(String orderId);
}
