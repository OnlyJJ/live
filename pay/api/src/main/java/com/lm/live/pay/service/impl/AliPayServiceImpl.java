package com.lm.live.pay.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import jodd.util.StringUtil;
import jodd.util.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.base.service.IIpStoreService;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MD5Util;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.constant.Constants;
import com.lm.live.pay.dao.PayChargeOrderMapper;
import com.lm.live.pay.domain.PayChargeOrder;
import com.lm.live.pay.enums.ChargeStatusEnum;
import com.lm.live.pay.enums.ErrorCode;
import com.lm.live.pay.exception.PayBizException;
import com.lm.live.pay.service.IAliPayService;
import com.lm.live.pay.utils.AlipayConfig;
import com.lm.live.pay.utils.RSA;
import com.lm.live.pay.vo.Alipay;
import com.lm.live.pay.vo.PayOrder;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.service.IUserInfoService;

public class AliPayServiceImpl extends CommonServiceImpl<PayChargeOrderMapper, PayChargeOrder> implements IAliPayService {
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IIpStoreService ipStoreService;

	@Override
	public ServiceResult<Boolean> dealPaySuccessNotify(Map verifyMap,
			String notifyFromIP, DeviceProperties dev) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResult<JSONObject> createOrder(PayOrder po,
			DeviceProperties dv, String userId, String ip) throws Exception {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		if(po == null || dv == null 
				|| StringUtil.isEmpty(userId)) {
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		PayOrder payOrder = new PayOrder();
		Alipay alipay = new Alipay();
		
		//选择充值金额(单位:分)
		int chargeSelectMoneyFen = po.getMoney();
		if(!Constants.OFFICIAL_USER.contains(userId)){
			if(chargeSelectMoneyFen < Constants.LEAST_MONEY){ //限制最低充值5元
				throw new PayBizException(ErrorCode.ERROR_5014);
			}
		}
		String orderId = StrUtil.getOrderId();
		PayChargeOrder pco = new PayChargeOrder();
		pco.setOrderId(orderId);
		if(chargeSelectMoneyFen != 0 ){
			pco.setMoney(0);
			pco.setSelectMoney(chargeSelectMoneyFen);
			LogUtil.log.info("money:" + chargeSelectMoneyFen);
		}
		Date now = new Date();
		pco.setChargeTime(now);
		pco.setResultTime(now);
		pco.setSyncTime(new Date());
		if(po.getPay_type() != 0){
			pco.setPay_type(po.getPay_type());
			pco.setCreateType(po.getClientType());
		}
		
		//代充时,金币接受者
		String receiverUserId = po.getReceiverUserId();
		String agentUserId = null;
		//若果receiverUserId不为空,说明是给别人充值
		if(!StringUtils.isEmpty(receiverUserId)){
			//当前登陆用户为代理人
			agentUserId = userId;
		}else{
			//当前登录用户是给自己充值,金币接受者是自己
			receiverUserId = userId;
		}
		pco.setUserId(receiverUserId);
		pco.setAgentUserId(agentUserId);
		pco.setGolds(0);
		pco.setOrderStatus(ChargeStatusEnum.CREATE.getValue());
		String channelId = dv.getChannelId();
		if(channelId != null) {
			if(channelId.length() > 10) {
				channelId=channelId.substring(0, 10);
			}
		}
		
		UserInfoDo user = userInfoService.getUserInfoFromCache(receiverUserId);
		if(user != null) {
			int time = DateUntil.getTimeIntervalMinute(user.getAddTime(), new Date());
			int retentionVaild = time / 60 / 24;
			pco.setRetentionVaild(retentionVaild);
		}
		pco.setChannelId(channelId);
		String generateOrderAddr = ipStoreService.getAddressByIp(ip);
		pco.setGenerateOrderIp(ip);
		pco.setGenerateOrderAddr(generateOrderAddr);
		
		if(!StringUtils.isEmpty(agentUserId)) {
			UserAccount userAccount = userAccountService.getObjectByUserId(receiverUserId);
			if(userAccount == null){
				throw new PayBizException(ErrorCode.ERROR_5020);
			}
		}
		
		pco.setRemark(Constants.ALI_PAY_REMARK);
		this.dao.insert(pco);
		payOrder.setOrderId(pco.getOrderId());
		payOrder.setMoney(chargeSelectMoneyFen / 100);// 分
		payOrder.setRemark(pco.getRemark());
		
		JSONObject data = new JSONObject();
		
		String remark = "";
		if(StringUtils.isEmpty(po.getRemark())) {
			remark = URLDecoder.decode(po.getRemark(), Constants.DEFAULT_UNICODE);
		}
		// 1-支付宝
		if(pco.getPay_type() == 1) {
			/**   设置订单签名   */
			String key = MD5Util.md5(AlipayConfig.SELLER_EMAIL);
			String orderInfo = "partner=" + "\"" + AlipayConfig.PARTNER_ID + "\"" 
			+ "&seller_id=" + "\"" + AlipayConfig.SELLER_EMAIL + "\""
			+"&out_trade_no=" + "\"" + pco.getOrderId() + "\"" 
			+ "&subject=" + "\"" + Constants.CHARGE_SUBJECT + "\"" 
			+ "&body=" + "\"" + remark + "\"" 
			+ "&total_fee=" + "\"" + chargeSelectMoneyFen/100f + "\"" 
			+ "&notify_url=" + "\"" + AlipayConfig.NOTIFY_URL+ "\"" 
			+ "&service=\"mobile.securitypay.pay\"" 
			+ "&payment_type=\"1\""
			+ "&_input_charset=\"utf-8\""
			+"&it_b_pay=\"30m\"";				
			String sign = RSA.sign(orderInfo , AlipayConfig.RSA_PRIVATE , Constants.DEFAULT_UNICODE);		
			LogUtil.log.info("alipay sign orderInfo=" + orderInfo + " RSA=" + sign);		
			alipay.setPartnerId(AlipayConfig.PARTNER_ID);
			alipay.setSellerEmail(AlipayConfig.SELLER_EMAIL);
			alipay.setBody(remark);
			alipay.setSubject(Constants.CHARGE_SUBJECT);
			alipay.setNotifyUrl(AlipayConfig.NOTIFY_URL);
			alipay.setSign(sign);
			data.put(alipay.getShortName(), alipay.buildJson());
			LogUtil.log.info("支付方式为支付宝");
		}
		data.put(payOrder.getShortName(), payOrder.buildJson());
		srt.setData(data);
		srt.setSucceed(true);
		return srt;
	}
	
}
