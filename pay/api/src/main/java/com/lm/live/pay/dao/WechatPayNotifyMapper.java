package com.lm.live.pay.dao;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.pay.domain.WechatPayNotifyDo;

public interface WechatPayNotifyMapper extends ICommonMapper<WechatPayNotifyDo> {

	WechatPayNotifyDo getByOutTradeNo(String orderId);
}
