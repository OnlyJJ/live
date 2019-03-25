package com.lm.live.pay.dao;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.pay.domain.PayChargeOrder;

public interface PayChargeOrderMapper extends ICommonMapper<PayChargeOrder> {
	/**
	 * 查询用户成功充值的次数
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月13日
	 */
	int getChargeNum(String userId);
	
	PayChargeOrder getPcoByOrderId(String orderId);

	/**
	 * 根据商户系统的订单号(与统一下单时发送给微信接口的一致)查询已成功的充值记录
	 * @param outTradeNo
	 * @return
	 */
	PayChargeOrder getSuccessOrderByOutTradeNo(String outTradeNo);
	
	PayChargeOrder getPcoByTransactionId(String transactionId);
	
	
	void updateFail(PayChargeOrder pco);
}
