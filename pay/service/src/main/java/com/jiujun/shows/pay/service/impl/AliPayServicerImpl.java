package com.jiujun.shows.pay.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.AliPayBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IAliPayService;
import com.jiujun.shows.pay.vo.DataRequest;

public class AliPayServicerImpl implements IAliPayService {
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private AliPayBiz aliPayBiz;
	
	@Override
	public ServiceResult<JSONObject> createOrder(DataRequest data) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = aliPayBiz.createOrder(data);
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
	public ServiceResult<Boolean> dealPaySuccessNotify(Map verifyMap,
			String notifyFromIP, DeviceProperties dev) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = aliPayBiz.dealPaySuccessNotify(verifyMap, notifyFromIP, dev);
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
