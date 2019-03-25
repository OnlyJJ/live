package com.jiujun.shows.pay.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.domain.ServiceLog;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.base.service.IServiceLogService;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.enums.LockTarget;
import com.jiujun.shows.pay.enums.PayChargeOrderTableEnum;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.user.service.IUserCacheInfoService;

@Service("chargeCommonBiz")
public class ChargeCommonBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);

	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IServiceLogService serviceLogDao ;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	
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
	 */
	public ServiceResult<Boolean> updateChargeOrderAndGold(String orderId, int notifyMoney,boolean isAppleCharge,boolean isPayBySanBox,int appleUserRealPayMoney,String notifyIp,String transactionId,DeviceProperties dev) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info(String.format("###updateChargeOrderAndGold-begin-orderId:%s,money:%s分,isPayBySanBox:%s", orderId,notifyMoney,isPayBySanBox));
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(transactionId)||notifyMoney <=0){
			srt.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5025.getResultDescr());
			return srt;
		}
		String lockname = LockTarget.LOCK_PAYAFTER_BIZ.getLockName() + orderId;
		try {
			RdLock.lock(lockname);
			PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId); 
			if(dbPayChargeOrder==null){
				log.info("###本地服务端订单不存在,orderId:"+orderId);
				srt.setResultCode(ErrorCode.ERROR_5012.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_5012.getResultDescr());
				return srt;
			}
			//根据商户系统的订单号(与统一下单时发送给微信接口的一致)查询已成功的充值记录
			//PayChargeOrder  successOrder = this.payChargeOrderDao.getSuccessOrderByOutTradeNo(orderId);
			int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
			// 同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。。如果对应的订单号已标志位充值成功,则直接返回true(已处理成功)。
			//if(successOrder!=null){
			if(dbPayChargeOrderStatus==4 ||dbPayChargeOrderStatus==5){ //状态为4或5,说明之前已处理过这个通知
				log.info("###支付通知,此订单(本地订单号orderId):"+orderId+"已处理成功(无需重复处理),直接返回SUCCESS给对方服务器");
				srt.setSucceed(true);
				return srt;
			}
			// 根据对接的服务商的商户号查询
			PayChargeOrder dbSuccessOrder = payChargeOrderMapper.getPcoByTransactionId(transactionId);
			if( dbSuccessOrder!= null){
				dbPayChargeOrderStatus = dbSuccessOrder.getOrderStatus();
				int rowId= dbSuccessOrder.getId();
				String dbOrderId = dbSuccessOrder.getOrderId() ;
				log.info(String.format("###支付通知,此订单(支付对接方订单号transactionId:%s)已处理成功(无需重复处理),直接返回SUCCESS给对方服务器在本地订单信息rowId:%s,orderId:%s",transactionId,rowId,dbOrderId));
				srt.setSucceed(true);
				return srt;
			}
			//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
			int selectMoney = dbPayChargeOrder.getSelectMoney();
			if(selectMoney!=notifyMoney){
				log.info(String.format("###dealPaySuccessNotify-通知返回的金币与本地订单的金币不相等,out_trade_no:%s,selectMoney:%s,notifyMoney:%s",orderId,selectMoney,notifyMoney));
				srt.setResultCode(ErrorCode.ERROR_5016.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_5016.getResultDescr());
				return srt;
			}
			String userId = dbPayChargeOrder.getUserId();
			UserAccount userAccount = userAccountService.getObjectByUserId(userId);
			if(userAccount==null){
				log.error("###账户不存在,userId:"+userId);
				srt.setResultCode(ErrorCode.ERROR_5029.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_5029.getResultDescr());
				return srt;
			}
			// 处理数据，失败回滚
			handleData(userId, notifyIp, transactionId, notifyMoney, orderId, isAppleCharge, isPayBySanBox, userAccount, dbPayChargeOrder, dev);
			
			log.info("用户账户修改,加金币成功 ===订单："+dbPayChargeOrder.getOrderId() +",===RMB(分):"+notifyMoney+",已加金币数："+dbPayChargeOrder.getGolds()
					+ " ,充值用户-userId:"+userId+",账户剩余金币:" + userAccount.getGold());	
			try {
				// 充值成功，清空一下用户缓存
				String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
				RedisUtil.del(userCacheKey);
				if(isAppleCharge){ //苹果充值
					//苹果充值,按实际折算前的的钱算(指用户时间花费的金额),充值送礼
					userAccountService.sendGiftWithCharge(userId,appleUserRealPayMoney);
				}else{ 
					//充值送礼
					userAccountService.sendGiftWithCharge(userId,notifyMoney);
				}
			} catch (Exception e) {
				log.error(e.getMessage() ,e);
			}
			log.info(String.format("###updateChargeOrderAndGold-end-orderId:%s,money:%s分,isPayBySanBox:%s", orderId,notifyMoney,isPayBySanBox));
			srt.setSucceed(true);
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5000.getResultDescr());
		} finally {
			RdLock.unlock(lockname);
		}
		return srt;
	}
	
	/**
	 * 充值失败,更改订单状态(如:用户取消订单……)
	 * @param orderId 失败的订单
	 * @param remark 失败描述
	 * @param clientIp
	 */
	public ServiceResult<Boolean> upateOrderStatus2Fail(String orderId, String remark,String clientIp) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info(String.format("###upateOrderStaues2Fail-begin-orderId:%s,remark:%s", orderId,remark));
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(remark)){
			srt.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5025.getResultDescr());
			return srt;
		}
		PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId); 
		if(dbPayChargeOrder==null){
			log.error("###本地服务端订单不存在,orderId:"+orderId);
			srt.setResultCode(ErrorCode.ERROR_5012.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_5012.getResultDescr());
			return srt;
		}else{
			log.info(String.format("###修改本地订单状态为失败,订单id:%s,本地订单:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
			int dbOrderStatus = dbPayChargeOrder.getOrderStatus();
			if(dbOrderStatus == 4 || dbOrderStatus ==5){
				log.info(String.format("###本地服务端订单%s已被改成成功状态,不能重复修改状态(return),本地订单:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
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
			payChargeOrderMapper.update(dbPayChargeOrder);
			srt.setSucceed(true);
		}
		log.info(String.format("###upateOrderStaues2Fail-end-orderId:%s,remark:%s", orderId,remark));
		return srt;
	}

	@Transactional(rollbackFor=Exception.class)
	private void handleData(String userId, String notifyIp, String transactionId, int notifyMoney,
			String orderId, boolean isAppleCharge, boolean isPayBySanBox, 
			UserAccount userAccount ,PayChargeOrder dbPayChargeOrder, DeviceProperties dev) throws Exception {
		String paySuccessNotifyAddr = ipStoreService.getAddressByIp(notifyIp);
		dbPayChargeOrder.setPaySuccessNotifyIp(notifyIp);
		dbPayChargeOrder.setPaySuccessNotifyAddr(paySuccessNotifyAddr) ;
		dbPayChargeOrder.setTransactionId(transactionId);
		dbPayChargeOrder.setOrderStatus(4);
		dbPayChargeOrder.setResultTime(new Date());
		int golds = notifyMoney * 10; //1分钱给10个金币
		log.info("用户账户修改,加金币之前===订单："+dbPayChargeOrder.getOrderId() +",===RMB(分):"+notifyMoney+",需加金币数："+golds
				+ " ,充值用户-userId:"+userId+",账户剩余金币:" + userAccount.getGold());	
		dbPayChargeOrder.setGolds(golds);
		dbPayChargeOrder.setMoney(notifyMoney);
		int dbOrderPayType = dbPayChargeOrder.getPay_type();
		String successRemark = "充值成功";
		if(dbOrderPayType == PayChargeOrderTableEnum.PayType.ZHIFUBAO.getValue()){
			successRemark = "支付宝支付:"+successRemark;
		}else if(dbOrderPayType == PayChargeOrderTableEnum.PayType.YINLIAN.getValue()){
			successRemark = "易联支付:"+successRemark;
		}else if(dbOrderPayType == PayChargeOrderTableEnum.PayType.WEIXIN.getValue()){
			successRemark = "微信支付:"+successRemark;
		}else if(dbOrderPayType == PayChargeOrderTableEnum.PayType.APPLEPAY.getValue()){
			successRemark = "苹果支付:"+successRemark;
		}else{
			log.info(String.format("###未提前约定的配置方式,本地支付订单orderId:%s,orderInfo:%s",orderId,JsonUtil.beanToJsonString(dbPayChargeOrder)));
		}
		dbPayChargeOrder.setRemark(successRemark+transactionId);
		if(isPayBySanBox){
			dbPayChargeOrder.setIsPayBySanBox(PayChargeOrderTableEnum.IsPayBySanBox.YES.getValue());
		}else{
			dbPayChargeOrder.setIsPayBySanBox(PayChargeOrderTableEnum.IsPayBySanBox.NO.getValue());
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
		ServiceResult<Boolean> asrt = userAccountService.addGolds(userId, golds,userAccountBook);
		if(!asrt.isSucceed()) {
			Exception e = new PayBizException(ErrorCode.ERROR_5038);
			throw e;
		}
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
		serviceLogDao.insert(serviceLog);
	}

}
