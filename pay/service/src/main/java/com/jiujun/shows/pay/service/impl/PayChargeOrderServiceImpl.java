package com.jiujun.shows.pay.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.PayChargeOrderBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IPayChargeOrderService;

public class PayChargeOrderServiceImpl extends CommonServiceImpl<PayChargeOrderMapper,  PayChargeOrder>
		implements IPayChargeOrderService {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayChargeOrderBiz payChargeOrderBiz;

	@Override
	public ServiceResult<Integer> getPayCountByUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResult<PayChargeOrder> getDataByOrderId(String orderId) {
		ServiceResult<PayChargeOrder> srt = new ServiceResult<PayChargeOrder>();
		srt.setSucceed(false);
		try {
			srt = payChargeOrderBiz.getDataByOrderId(orderId);
		} catch(PayBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		}
		return srt;
	}


}
