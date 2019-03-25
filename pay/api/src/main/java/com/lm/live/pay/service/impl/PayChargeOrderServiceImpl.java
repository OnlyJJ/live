package com.lm.live.pay.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.dao.PayChargeOrderMapper;
import com.lm.live.pay.domain.PayChargeOrder;
import com.lm.live.pay.enums.ErrorCode;
import com.lm.live.pay.exception.PayBizException;
import com.lm.live.pay.service.IPayChargeOrderService;


/**
 * @serviceimpl
 * @table t_pay_type
 * @date 2015-12-01 16:32:40
 * @author charge
 */
@Service
public class PayChargeOrderServiceImpl extends CommonServiceImpl<PayChargeOrderMapper,PayChargeOrder> implements IPayChargeOrderService {

	@Override
	public ServiceResult<Integer> getPayCountByUser(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		ServiceResult<Integer> srt = new ServiceResult<Integer>();
		srt.setSucceed(true);
		int chargeNum = dao.getChargeNum(userId);
		srt.setData(chargeNum);
		return srt;
	}

	@Override
	public ServiceResult<PayChargeOrder> getPcoByOrderId(String orderId) {
		if(StringUtils.isEmpty(orderId)) {
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		ServiceResult<PayChargeOrder> srt = new ServiceResult<PayChargeOrder>();
		srt.setSucceed(false);
		PayChargeOrder order = dao.getPcoByOrderId(orderId);
		if(order != null) {
			srt.setSucceed(true);
			srt.setData(order);
		}
		return srt;
	}

	
	
	
}
