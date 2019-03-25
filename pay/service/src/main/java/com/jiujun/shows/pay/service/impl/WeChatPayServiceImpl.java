package com.jiujun.shows.pay.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.WeChatPayBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.enums.WechatPayBusinessEnum.TradeType;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IWeChatPayService;
import com.jiujun.shows.pay.vo.WechatJSAPIVo;
import com.jiujun.shows.pay.vo.WechatPayVo;

public class WeChatPayServiceImpl implements IWeChatPayService {

	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private WeChatPayBiz weChatPayBiz;

	@Override
	public ServiceResult<WechatPayVo> payUnifiedorder(String receiverUserId,
			int total_free, String spbill_create_ip, int clientType,
			String channelId, String agentUserId, TradeType tradeType,
			DeviceProperties deviceProperties) {
		ServiceResult<WechatPayVo> srt = new ServiceResult<WechatPayVo>();
		srt.setSucceed(false);
		try {
			srt = weChatPayBiz.payUnifiedorder(receiverUserId, total_free,
					spbill_create_ip, clientType, channelId, agentUserId,
					tradeType, deviceProperties);
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
	public ServiceResult<Boolean> dealWechatPayNotify(String xmlData,
			TradeType tradeType, String notifyFromIp,
			DeviceProperties deviceProperties) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = weChatPayBiz.dealWechatPayNotify(xmlData, tradeType,
					notifyFromIp, deviceProperties);
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
	public ServiceResult<WechatJSAPIVo> handlePayForWechatJsapi(String openid,
			String receiverUserId, int buyGold, String spbill_create_ip,
			int clientType, String channelId, TradeType tradeType,
			DeviceProperties deviceProperties) {
		ServiceResult<WechatJSAPIVo> srt = new ServiceResult<WechatJSAPIVo>();
		srt.setSucceed(false);
		try {
			srt = weChatPayBiz.handlePayForWechatJsapi(openid, receiverUserId, buyGold, 
					spbill_create_ip, clientType, channelId, tradeType, deviceProperties);
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
	public ServiceResult<Boolean> handleWechatJsapiNotify(String xmlData,
			String notifyFromIp) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = weChatPayBiz.handleWechatJsapiNotify(xmlData, notifyFromIp);
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
