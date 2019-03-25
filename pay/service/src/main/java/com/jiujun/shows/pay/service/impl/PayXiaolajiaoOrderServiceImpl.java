package com.jiujun.shows.pay.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.biz.PayXiaolajiaoOrderBiz;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayXiaolajiaoOrderMapper;
import com.jiujun.shows.pay.domain.PayXiaolajiaoOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IPayXiaolajiaoOrderService;


/**
 * 小辣椒支付服务实现
 * @author shao.xiang
 * @date 2017年8月23日
 *
 */
public class PayXiaolajiaoOrderServiceImpl extends CommonServiceImpl<PayXiaolajiaoOrderMapper,PayXiaolajiaoOrder> implements IPayXiaolajiaoOrderService {
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayXiaolajiaoOrderBiz payXiaolajiaoOrderBiz;
	
	@Override
	public ServiceResult<String> createPayXiaolajiaoOrder(
			PayXiaolajiaoOrder paramPayXiaolajiaoOrder) {
		ServiceResult<String> srt = new ServiceResult<String>();
		try {
			srt = payXiaolajiaoOrderBiz.createPayXiaolajiaoOrder(paramPayXiaolajiaoOrder);
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
	public ServiceResult<Boolean> paySuccessNotifyXiaolajiaoOrder(
			String loginUserId, PayXiaolajiaoOrder paramPayXiaolajiaoOrder,
			String md5Checkcode, String sessionId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = payXiaolajiaoOrderBiz.paySuccessNotifyXiaolajiaoOrder(loginUserId, 
					paramPayXiaolajiaoOrder, md5Checkcode, sessionId);
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
