package com.jiujun.shows.pay.biz;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.PayChargeOrder;

public class PayChargeOrderBiz  {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayChargeOrderMapper PayChargeOrderMapper;

	public ServiceResult<Integer> getPayCountByUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询订单信息
	 * @param orderId
	 * @return
	 * @author shao.xiang
	 * @date 2017年9月28日
	 */
	public ServiceResult<PayChargeOrder> getDataByOrderId(String orderId) {
		ServiceResult<PayChargeOrder> srt = new ServiceResult<PayChargeOrder>();
		srt.setSucceed(true);
		PayChargeOrder pco = PayChargeOrderMapper.getPcoByOrderId(orderId);
		if(pco != null) {
			srt.setData(pco);
		}
		return srt;
	}


}
