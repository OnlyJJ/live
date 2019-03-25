package com.lm.live.pay.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.vo.PayOrder;

/**
 * 支付宝支付服务
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
public interface IAliPayService {

	/**
	 * 处理支付宝充值通知 <br />
	 * 参考接入流程: <br />https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.oB6F8C&treeId=58&articleId=103598&docType=1
	 * @param verifyMap 通知请求参数map
	 * @param notifyFromIp
	 * @param dev
	 */
	ServiceResult<Boolean> dealPaySuccessNotify(Map verifyMap,String notifyFromIP,DeviceProperties dev) throws Exception;
	
	/**
	 * 支付宝生成订单 
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月7日
	 */
	ServiceResult<JSONObject> createOrder(PayOrder po, DeviceProperties dv, String userId, String ip) throws Exception;
}
