package com.lm.live.pay.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.dao.ServiceLogMapper;
import com.lm.live.base.domain.ServiceLog;
import com.lm.live.base.service.IIpStoreService;
import com.lm.live.common.constant.MCPrefix;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.dao.PayChargeOrderMapper;
import com.lm.live.pay.domain.PayChargeOrder;
import com.lm.live.pay.enums.ErrorCode;
import com.lm.live.pay.enums.TradeTypeEnum;
import com.lm.live.pay.exception.PayBizException;
import com.lm.live.pay.service.IChargeCommonService;
import com.lm.live.user.service.IUserCacheInfoService;

@Service
public class ChargeCommonServiceImpl implements IChargeCommonService{

	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private ServiceLogMapper serviceLogMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public ServiceResult<Boolean> updateChargeOrderAndGold(String orderId, int notifyMoney,
			boolean isAppleCharge,boolean isPayBySanBox,int appleUserRealPayMoney,String notifyIp,String transactionId,DeviceProperties dev)throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		LogUtil.log.info(String.format("###updateChargeOrderAndGold-begin-orderId:%s,money:%s分,isPayBySanBox:%s", orderId,notifyMoney,isPayBySanBox));
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(transactionId)||notifyMoney <=0){
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		
		PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId); 
		
		if(dbPayChargeOrder==null){
			LogUtil.log.info("###本地服务端订单不存在,orderId:"+orderId);
			throw new PayBizException(ErrorCode.ERROR_5009);
		}
		
		//根据商户系统的订单号(与统一下单时发送给微信接口的一致)查询已成功的充值记录
		int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
		// 同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。。如果对应的订单号已标志位充值成功,则直接返回true(已处理成功)。
		if(dbPayChargeOrderStatus==4 ||dbPayChargeOrderStatus==5){ //状态为4或5,说明之前已处理过这个通知
			LogUtil.log.info("###支付通知,此订单(本地订单号orderId):"+orderId+"已处理成功(无需重复处理),直接返回SUCCESS给对方服务器");
			srt.setSucceed(true);
			return srt;
		}
		
		// 根据对接的服务商的商户号查询
		PayChargeOrder dbSuccessOrder = payChargeOrderMapper.getPcoByTransactionId(transactionId);
		if( dbSuccessOrder != null){
			dbPayChargeOrderStatus = dbSuccessOrder.getOrderStatus();
			int rowId= dbSuccessOrder.getId();
			String dbOrderId = dbSuccessOrder.getOrderId() ;
			LogUtil.log.info(String.format("###支付通知,此订单(支付对接方订单号transactionId:%s)已处理成功(无需重复处理),直接返回SUCCESS给对方服务器在本地订单信息rowId:%s,orderId:%s",transactionId,rowId,dbOrderId));
			srt.setSucceed(true);
			return srt;
		}
		
		//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
		int selectMoney = dbPayChargeOrder.getSelectMoney();
		if(selectMoney!=notifyMoney){
			LogUtil.log.info(String.format("###dealPaySuccessNotify-通知返回的金币与本地订单的金币不相等,out_trade_no:%s,selectMoney:%s,notifyMoney:%s",orderId,selectMoney,notifyMoney));
			throw new PayBizException(ErrorCode.ERROR_5013);
		}
		
		String userId = dbPayChargeOrder.getUserId();
		UserAccount userAccount = userAccountService.getObjectByUserId(userId);
		if(userAccount==null){
			LogUtil.log.error("###账户不存在,userId:"+userId);
			throw new PayBizException(ErrorCode.ERROR_5020);
		}
		
		String paySuccessNotifyAddr = ipStoreService.getAddressByIp(notifyIp);
		dbPayChargeOrder.setPaySuccessNotifyIp(notifyIp);
		dbPayChargeOrder.setPaySuccessNotifyAddr(paySuccessNotifyAddr) ;
		dbPayChargeOrder.setTransactionId(transactionId);
		dbPayChargeOrder.setOrderStatus(4);
		dbPayChargeOrder.setResultTime(new Date());
		int golds = notifyMoney * 10; //1分钱给10个金币
		LogUtil.log.info("用户账户修改,加金币之前===订单："+dbPayChargeOrder.getOrderId() +",===RMB(分):"+notifyMoney+",需加金币数："+golds
				+ " ,充值用户-userId:"+userId+",账户剩余金币:" + userAccount.getGold());	
		dbPayChargeOrder.setGolds(golds);
		dbPayChargeOrder.setMoney(notifyMoney);
		int dbOrderPayType = dbPayChargeOrder.getPay_type();
		String successRemark = "充值成功";
		if(dbOrderPayType == TradeTypeEnum.Ali.getValue()){
			successRemark = "支付宝支付:"+successRemark;
		}else if(dbOrderPayType == TradeTypeEnum.Wechat.getValue()){
			successRemark = "微信支付:"+successRemark;
		}else{
			LogUtil.log.info(String.format("###未提前约定的配置方式,本地支付订单orderId:%s,orderInfo:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
		}
		dbPayChargeOrder.setRemark(successRemark+transactionId);
		if(isPayBySanBox){
			dbPayChargeOrder.setIsPayBySanBox(Constants.STATUS_1);
		}else{
			dbPayChargeOrder.setIsPayBySanBox(Constants.STATUS_0);
		}
		
		payChargeOrderMapper.update(dbPayChargeOrder);
		
		// 账户明细
		UserAccountBook userAccountBook = new UserAccountBook();
		userAccountBook.setUserId(userId);
		userAccountBook.setChangeGolds(golds);
		userAccountBook.setSourceId(orderId);
		userAccountBook.setSourceDesc("sourceId为t_pay_charge_order订单id");
		userAccountBook.setContent("充值，增加金币");
		userAccountBook.setRecordTime(new Date());
		
		userAccountService.addGolds(userId, golds,userAccountBook);
		//重新查一次db
		userAccount = userAccountService.getObjectByUserId(userId);
		
		//每次成功,都记录历史
		Date nowDate = new Date();
		String serviceLogInfo = successRemark;
		ServiceLog serviceLog = new ServiceLog();
		if(dev != null){
			serviceLog.setClientType(dev.getClientType());
			serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(dev));
		}
		serviceLog.setUserId(userId);
		serviceLog.setInfo(serviceLogInfo);
		serviceLog.setIp(notifyIp);
		serviceLog.setActTime(nowDate);
		String userName = userCacheInfoService.getOrUpdateUserInfoFromCache(userId).getNickname();
		serviceLog.setUserName(userName);
		String clientType = null;
		if(isAppleCharge){
			clientType = HttpUtils.iosClient;
		}
		serviceLog.setClientType(clientType);
		serviceLogMapper.insert(serviceLog);
		
		LogUtil.log.info("用户账户修改,加金币成功 ===订单："+dbPayChargeOrder.getOrderId() +",===RMB(分):"+notifyMoney+",已加金币数："+dbPayChargeOrder.getGolds()
				+ " ,充值用户-userId:"+userId+",账户剩余金币:" + userAccount.getGold());	
		LogUtil.log.info(String.format("###updateChargeOrderAndGold-addGoldSuccess-orderId:%s,money:%s分,isPayBySanBox:%s", orderId,notifyMoney,isPayBySanBox));
		try {
			// m-todo，这个缓存有时间要重新设计一下
			// 充值成功，清空一下用户缓存
			String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
			MemcachedUtil.delete(userCacheKey);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
		LogUtil.log.info(String.format("###updateChargeOrderAndGold-end-orderId:%s,money:%s分,isPayBySanBox:%s", orderId,notifyMoney,isPayBySanBox));
		srt.setSucceed(true);
		return srt;
	}
	
	@Override
	public ServiceResult<Boolean> upateOrderStatus2Fail(String orderId, String remark,String clientIp)
			throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		LogUtil.log.info(String.format("###upateOrderStaues2Fail-begin-orderId:%s,remark:%s", orderId,remark));
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(remark)){
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId); 
		if(dbPayChargeOrder==null){
			LogUtil.log.error("###本地服务端订单不存在,orderId:"+orderId);
			throw new PayBizException(ErrorCode.ERROR_5020);
		}else{
			LogUtil.log.info(String.format("###修改本地订单状态为失败,订单id:%s,本地订单:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
			int dbOrderStatus = dbPayChargeOrder.getOrderStatus();
			if(dbOrderStatus == 4 || dbOrderStatus ==5){
				LogUtil.log.info(String.format("###本地服务端订单%s已被改成成功状态,不能重复修改状态(return),本地订单:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
				srt.setSucceed(true);
				return srt;
			}
			// 订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
			dbPayChargeOrder.setOrderStatus(3);
			dbPayChargeOrder.setRemark(remark);
			dbPayChargeOrder.setResultTime(new Date());
			String paySuccessNotifyAddr = ipStoreService.getAddressByIp(clientIp);
			
			dbPayChargeOrder.setPaySuccessNotifyIp(clientIp);
			dbPayChargeOrder.setPaySuccessNotifyAddr(paySuccessNotifyAddr);
			payChargeOrderMapper.updateFail(dbPayChargeOrder);
		}
		LogUtil.log.info(String.format("###upateOrderStaues2Fail-end-orderId:%s,remark:%s", orderId,remark));
		srt.setSucceed(true);
		return srt;
	}

}
