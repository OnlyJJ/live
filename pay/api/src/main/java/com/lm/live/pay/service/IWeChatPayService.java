package com.lm.live.pay.service;

import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.enums.WechatPayBusinessEnum;
import com.lm.live.pay.vo.WechatJSAPIVo;
import com.lm.live.pay.vo.WechatPayVo;

/**
 * 微信支付服务
 * @author shao.xiang
 * @date 2017年8月13日
 *
 */
public interface IWeChatPayService {
	
	/**
	 * 微信支付,统一下单
	 *参考接入流程: <br />
	 *https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1 
	 * @param receiverUserId 实际给加金币的userId
	 * @param total_free 订单总金额，单位为分
	 * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	 * @param clientType 客户端类型  1.www 2.安卓 3.ios
	 * @param channelId 渠道号
	 * @param agentUserId 代充者userId
	 * @param WechatPayBusinessEnum.TradeType(交易类型 app、扫码、公众号)
	 * @param DeviceProperties deviceProperties
	 * @return
	 * @throws Exception
	 */
	ServiceResult<WechatPayVo> payUnifiedorder(String receiverUserId,int total_free,
			String spbill_create_ip,int clientType,String channelId,String agentUserId, 
			String tradeType,DeviceProperties deviceProperties) throws Exception;

	/**
	 * 处理微信支付结果的通知
	 * 参考接入流程: <br />
	 *https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3 
	 * @param xmlData
	 * @param WechatPayBusinessEnum.TradeType(交易类型 app、扫码、公众号)
	 * @param notifyFromIp
	 * @param deviceProperties
	 * @throws Exception
	 */
	ServiceResult<Boolean> dealWechatPayNotify(String xmlData,WechatPayBusinessEnum.TradeType tradeType,
			String notifyFromIp,DeviceProperties deviceProperties) throws Exception ;
	

	/** 
	 * 公众号支付下单接口，流程：校验 —— 生成预定单 —— 生成发起支付的参数 —— 返回给H5
	 * @param receiverUserId
	 * @param buyGold
	 * @param spbill_create_ip
	 * @param clientType
	 * @param channelId
	 * @param tradeType
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 */
	ServiceResult<WechatJSAPIVo> handlePayForWechatJsapi(String openid,String receiverUserId,
			int buyGold,String spbill_create_ip,int clientType,String channelId,
			String tradeType,DeviceProperties deviceProperties) throws Exception;
	
	/**
	 * 处理支付后回调
	 * @param xmlData
	 * @param tradeType
	 * @param notifyFromIp
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 */
	ServiceResult<Boolean> handleWechatJsapiNotify(String xmlData,String notifyFromIp) throws Exception ;
	
}
