package com.jiujun.shows.pay.dao;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.PayChargeOrder;

public interface PayChargeOrderMapper extends ICommonMapper<PayChargeOrder> {

	PayChargeOrder getPcoByOrderId(String orderId);
	
	PayChargeOrder getPcoByTransactionId(String transactionId);
	
	PayChargeOrder getSuccessOrderByOutTradeNo(String outTradeNo);
	
	int sumUserChargeTotal(@Param("userId") String userId, @Param("beginTime") String beginTime, @Param("endTime")String endTime);

	void updateFail(PayChargeOrder pco);
}
