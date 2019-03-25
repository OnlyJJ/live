package com.lm.live.pay.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.domain.PayChargeOrder;
/**
 * 订单记录服务
 * @author shao.xiang
 * @date 2017-8-01
 */
public interface IPayChargeOrderService extends ICommonService<PayChargeOrder> {
	
	/**
	 * 获取用户的充值次数
	 * @return
	 */
	ServiceResult<Integer> getPayCountByUser(String userId);
	
	/**
	 * 获取订单信息
	 * @param orderId 订单id
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	ServiceResult<PayChargeOrder> getPcoByOrderId(String orderId);
}
