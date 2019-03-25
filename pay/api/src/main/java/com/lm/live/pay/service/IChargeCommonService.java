package com.lm.live.pay.service;

import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;


public interface IChargeCommonService {

	/**
	 * 充值成功,更新订单状态、加金币
	 * @param orderId 本地订单号
	 * @param notifyMoney 单位:分(给用户账户加上钱对应的金币,若是苹果支付:指按比例折算之后的)
	 * @param isAppleCharge 是否苹果支付
	 * @param isPayBySanBox 订单是否通过沙箱服务认证充值成功
	 * @param appleUserRealPayMoney 单位:分(若是苹果支付:指用户时间花费的金额)
	 * @param notifyIp 通知来源ip
	 * @param transactionId 对接端服务器的订单号
	 * @param deviceProperties
	 * @throws Exception
	 */
	ServiceResult<Boolean> updateChargeOrderAndGold(String orderId, int notifyMoney,
			boolean isAppleCharge,boolean isPayBySanBox,int appleUserRealPayMoney,
			String notifyIp,String transactionId,DeviceProperties dev) throws Exception;
	

	/**
	 * 充值失败,更改订单状态(如:用户取消订单……)
	 * @param orderId 失败的订单
	 * @param remark 失败描述
	 * @param clientIp
	 * @throws Exception
	 */
	ServiceResult<Boolean> upateOrderStatus2Fail(String orderId,String remark,String clientIp) throws Exception;
	

}

