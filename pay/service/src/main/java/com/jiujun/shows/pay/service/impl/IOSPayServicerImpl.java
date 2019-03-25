package com.jiujun.shows.pay.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.IosPayBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IIosPayService;
import com.jiun.shows.appclient.vo.AppleClientVo;

public class IOSPayServicerImpl implements IIosPayService {
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private IosPayBiz iosPayBiz;
	
	@Override
	public ServiceResult<AppleClientVo> createPayChargeOrder(String userId,
			String productId, String agentUserId, String clientIp,
			DeviceProperties dp) {
		ServiceResult<AppleClientVo> srt = new ServiceResult<AppleClientVo>();
		srt.setSucceed(false);
		try {
			srt = iosPayBiz.createPayChargeOrder(userId, productId, agentUserId, clientIp, dp);
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
	public ServiceResult<Long> applePayNotice(String orderId,
			String verifyReceipt, String clientIp,
			DeviceProperties deviceProperties) {
		ServiceResult<Long> srt = new ServiceResult<Long>();
		srt.setSucceed(false);
		try {
			srt = iosPayBiz.applePayNotice(orderId, verifyReceipt, clientIp, deviceProperties);
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
	public ServiceResult<Long> applePayFailNotice(String orderId,
			String remarkSuffix, String clientIp) {
		ServiceResult<Long> srt = new ServiceResult<Long>();
		srt.setSucceed(false);
		try {
			srt = iosPayBiz.applePayFailNotice(orderId, remarkSuffix, clientIp);
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
