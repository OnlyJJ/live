package com.jiujun.shows.pay.service.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.ChargeCommonBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IChargeCommonService;

public class ChargeCommonServiceImpl implements IChargeCommonService {

	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private ChargeCommonBiz chargeCommonBiz;
	
	@Override
	public ServiceResult<Boolean> updateChargeOrderAndGold(String orderId,
			int notifyMoney, boolean isAppleCharge, boolean isPayBySanBox,
			int appleUserRealPayMoney, String notifyIp, String transactionId,
			DeviceProperties dev) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = chargeCommonBiz.updateChargeOrderAndGold(orderId, notifyMoney, isAppleCharge, isPayBySanBox, appleUserRealPayMoney, notifyIp, transactionId, dev);
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

	@Override
	public ServiceResult<Boolean> upateOrderStatus2Fail(String orderId,
			String remark, String clientIp) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = chargeCommonBiz.upateOrderStatus2Fail(orderId, remark, clientIp);
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
