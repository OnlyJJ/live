package com.jiujun.shows.pay.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.UnionPayBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IUnionPayService;
import com.jiujun.shows.pay.vo.DataRequest;
import com.jiujun.shows.pay.vo.EcoPay;

public class UnionPayServiceImpl implements IUnionPayService {

	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private UnionPayBiz unionPayBiz;

	@Override
	public ServiceResult<EcoPay> doPay(DataRequest data) {
		ServiceResult<EcoPay> srt = new ServiceResult<EcoPay>();
		srt.setSucceed(false);
		try {
			srt = unionPayBiz.doPay(data);
		} catch (PayBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		}
		return srt;
	}

	@Override
	public ServiceResult<JSONObject> doWebPay(DataRequest data) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = unionPayBiz.doWebPay(data);
		} catch (PayBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		}
		return srt;
	}

	@Override
	public ServiceResult<String> dealPaySuccessNotify(String version,
			String merchantId, String merchOrderId, String amount,
			String extData, String orderId, String status, String payTime,
			String settleDate, String sign, String allParamStr,
			String notifyFromIp, DeviceProperties dev) {
		ServiceResult<String> srt = new ServiceResult<String>();
		try {
			srt = unionPayBiz.dealPaySuccessNotify(version, merchantId,
					merchOrderId, amount, extData, orderId, status, payTime,
					settleDate, sign, allParamStr, notifyFromIp, dev);
		} catch (PayBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		}
		return srt;
	}

	@Override
	public ServiceResult<String> dealPaySuccessNotifyFromWeb(String xmlDataStr,
			String notifyFromIp, DeviceProperties dev) {
		ServiceResult<String> srt = new ServiceResult<String>();
		try {
			srt = unionPayBiz.dealPaySuccessNotifyFromWeb(xmlDataStr, notifyFromIp, dev);
		} catch (PayBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		}
		return srt;
	}

}
