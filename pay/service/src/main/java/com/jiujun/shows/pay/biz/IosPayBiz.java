package com.jiujun.shows.pay.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.constant.SysConfTableEnum;
import com.jiujun.shows.base.domain.SysConf;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.base.service.ISysConfService;
import com.jiujun.shows.base.service.IThirdpartyConfService;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.RandomStringGenerator;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.ApplePayRecordMapper;
import com.jiujun.shows.pay.dao.AppleProductMapper;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.ApplePayRecordDo;
import com.jiujun.shows.pay.domain.AppleProductDo;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IChargeCommonService;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiun.shows.appclient.vo.AppleClientVo;

@Service("IosPayBiz")
public class IosPayBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private ApplePayRecordMapper applePayRecordMapper;
	
	@Resource
	private AppleProductMapper appleProductMapper;
	
	@Resource
	private ISysConfService sysConfService;
	
	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IThirdpartyConfService thirdpartyConfService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IChargeCommonService chargeCommonService;
	
	private static String[] sanboxChargeUserIdArr = null;
	
	
	/**
	 * 初始化沙箱充值用户userId
	 */
	static{
		String sandboxUserIdsStr= SpringContextListener.getContextProValue("ios.pay.sandbox.userIdList", "");
		log.info("###sandboxUserIdList:"+sandboxUserIdsStr);
		if(!StringUtils.isEmpty(sandboxUserIdsStr)){
			sanboxChargeUserIdArr =  sandboxUserIdsStr.split(",");
		}
		log.info("###sanboxChargeUserIdArr:"+sandboxUserIdsStr);
	}
	
	/**
	 * 苹果支付下单
	 * @param receiveUserId
	 * @param productId
	 * @param agentUserId
	 * @param clientIp
	 * @param dp
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月29日
	 */
	public ServiceResult<AppleClientVo> createPayChargeOrder(String receiveUserId, String productId,
			String agentUserId,String clientIp,DeviceProperties dp) throws Exception {
		ServiceResult<AppleClientVo> srt = new ServiceResult<AppleClientVo>();
		AppleClientVo appleClientVo = new AppleClientVo();
		if(StringUtils.isEmpty(receiveUserId)||StringUtils.isEmpty(productId)||StringUtils.isEmpty(clientIp)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		// 本地配置的bundleId
		String localBundleIdListStr = getLocalBundleId();
		if(StringUtils.isEmpty(localBundleIdListStr) ){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5022);
			log.error(e.getMessage(),e);
			throw e;
		}
		// agentUserId不为空,说明是代别人充值,则监测receiverUserId 是否有此用户账户
		if(!StringUtils.isEmpty(agentUserId)){
			UserAccount userAccount = userAccountService.getObjectByUserId(receiveUserId);
			// 为空说明被充账户不存在
			if(userAccount == null){
				PayBizException e = new PayBizException(ErrorCode.ERROR_5029);
				log.error(e.getMessage(),e);
				throw e;
			}
			log.info(String.format("###charge for other user,receiveUserId:%s,agentUserId:%s", receiveUserId,agentUserId));
		}
		//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		String orderId = DateUntil.getNowYyyyMMddHHmmss()+RandomStringGenerator.getRandomStringByLength(10);
		AppleProductDo appleProduct = appleProductMapper.getObjectByProductId(productId);
		if(appleProduct==null){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5028);
			log.error(e.getMessage(),e);
			throw e;
		}else{
			int selectMoney = appleProduct.getMoney();//单位:分 
			int productRateMoney = appleProduct.getRateMoney();
			PayChargeOrder o = new PayChargeOrder();
			o.setOrderId(orderId);
			o.setPay_type(4);//4:苹果支付
			o.setUserId(receiveUserId);
			o.setOrderStatus(1); //订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
			o.setSelectMoney(productRateMoney);//选择充值金额，单位：分
			o.setMoney(0);
			o.setGolds(0);
			o.setChargeTime(new Date());
			o.setRemark("苹果充值");
			o.setCreateType(3); //3 ios
			o.setChannelId(null);
			o.setAgentUserId(agentUserId);
			o.setGenerateOrderIp(clientIp);
			String generateOrderAddr = ipStoreService.getAddressByIp(clientIp);
			o.setGenerateOrderAddr(generateOrderAddr);
			// 2017-09-06，增加统计信息字段
			String douId = "";
			if(dp!=null && null != dp.getChannelId()) {
				douId = dp.getChannelId() + "_";
			}
			if(dp!=null && null != dp.getPackageName()) {
				o.setPkgName(dp.getPackageName());
				douId += dp.getPackageName();
			}
			if(!StringUtils.isEmpty(douId)) {
				o.setDouId(douId);
			}
			UserInfo user = userInfoService.getUserInfoFromCache(receiveUserId);
			if(user != null) {
				int time = DateUntil.getTimeIntervalMinute(user.getAddTime(), new Date());
				int retentionVaild = time / 60 / 24;
				o.setRetentionVaild(retentionVaild);
			}
			payChargeOrderMapper.insert(o);
			appleClientVo.setOrderId(orderId);
		}
		srt.setSucceed(true);
		srt.setData(appleClientVo);
		return srt;
	}
	
	/**
	 * 苹果支付成功-调用加金币
	 * @return
	 * @throws exception
	 * @author shao.xiang
	 * @date 2017年8月27日
	 */
	public ServiceResult<Long> applePayNotice(String orderId,String verifyReceipt,
			String clientIp,DeviceProperties deviceProperties) throws Exception {
		ServiceResult<Long> srt = new ServiceResult<Long>();
		log.info(String.format("###begin-ios-pay-applePayNotice,orderId:%s,verifyReceipt:%s", orderId,verifyReceipt));
		//参数校验
		if(StringUtils.isEmpty(orderId)){
			log.error("###applePay orderId is null");
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		if(StringUtils.isEmpty(verifyReceipt)){
			log.error("###applePay verifyReceipt is null");
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		// 设备信息不能少,用于识别苹果代充
		if(deviceProperties == null){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5027);
			log.error(e.getMessage(),e);
			throw e;
		}
		PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId);
		if(dbPayChargeOrder==null){
			log.info("###本地服务端订单不存在,orderId:"+orderId);
			PayBizException e = new PayBizException(ErrorCode.ERROR_5012);
			log.error(e.getMessage(),e);
			throw e;
		}
		//根据商户系统的订单号(与统一下单时发送给微信接口的一致)查询已成功的充值记录
		int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
		if(dbPayChargeOrderStatus == 4){ //状态为4,说明之前已处理过这个通知
			log.info("###苹果支付通知,此订单:"+orderId+"已处理成功(无需重复处理)");
		}else{
			// 历史原因,苹果客户端把uuid设置在DeviceProperties的u_imei = "c"
			String clientAppleUUID = deviceProperties.getImei();
			String clientWifiMac = deviceProperties.getMac();
			// 处理
			doNewAppleChargeBusiness(orderId,verifyReceipt,clientIp,clientAppleUUID,clientWifiMac,deviceProperties);
		}
		long payUserReamainGolds = 0;
		String agentUserId = dbPayChargeOrder.getAgentUserId();
		String receiveUserId = dbPayChargeOrder.getUserId();
		// 订单没有代理人充值
		if(StringUtils.isEmpty(agentUserId)){
			UserAccount userAccount  = userAccountService.getObjectByUserId(receiveUserId);
			payUserReamainGolds = userAccount.getGold().intValue();
		}else{
			UserAccount userAccount  = userAccountService.getObjectByUserId(agentUserId);
			payUserReamainGolds = userAccount.getGold().intValue();
		}
		srt.setSucceed(true);
		srt.setData(payUserReamainGolds);
		return srt;
	}
	
	/**
	 * 充值失败,更改订单状态
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月29日
	 */
	public ServiceResult<Long> applePayFailNotice(String orderId,
			String remarkSuffix, String clientIp) throws Exception {
		ServiceResult<Long> srt = new ServiceResult<Long>();
		log.info(String.format("###begin-ios-pay-applePayFailNotice,orderId:%s,,remarkSuffix:%s",orderId,remarkSuffix));
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(remarkSuffix)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5027);
			log.error(e.getMessage(),e);
			throw e;
		}
		String remark = "苹果支付失败:"+remarkSuffix;
		chargeCommonService.upateOrderStatus2Fail(orderId, remark,clientIp);
		PayChargeOrder payChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId);
		String agentUserId = payChargeOrder.getAgentUserId();
		String receiveUserId = payChargeOrder.getUserId();
		long payUserReamainGolds = 0;
		// 订单没有代理人充值
		if(StringUtils.isEmpty(agentUserId)) {
			UserAccount  userAccount  = this.userAccountService.getObjectByUserId(receiveUserId);
			payUserReamainGolds = userAccount.getGold().intValue();
		}else{
			UserAccount  userAccount  = this.userAccountService.getObjectByUserId(agentUserId);
			payUserReamainGolds = userAccount.getGold().intValue();
		}
		log.info(String.format("###end-ios-pay-applePayFailNotice,orderId:%s,,remarkSuffix:%s",orderId,remarkSuffix));
		srt.setSucceed(true);
		srt.setData(payUserReamainGolds);
		return srt;
	}
	
	/**
	 * 苹果支付,获取本地配置的bundleId
	 * @return
	 */
	private String getLocalBundleId() {
		String localhostBundleId = null;
		String qryCode = SysConfTableEnum.Code.IOS_PAY_BUNDLE_ID_CONF.getValue();
		SysConf sysConf = null;
		ServiceResult<SysConf> srt = sysConfService.getByCode(qryCode);
		if(srt.isSucceed()) {
			sysConf = srt.getData();
		}
		if(sysConf != null){
			localhostBundleId = sysConf.getConfValue();
		}else{
			log.info(String.format("###苹果支付,在表t_sys_conf中没有code=%s且启用中的配置", qryCode));
		}
		log.info(String.format("###苹果支付,在表t_sys_conf中code=%s且启用中的配置bundleId:%s", qryCode,localhostBundleId));
		return localhostBundleId;
	}
	
	private void doNewAppleChargeBusiness(String paramOrderId,String verifyReceipt,String clientIp,String clientAppleUUID,String clientWifiMac,DeviceProperties deviceProperties) throws Exception {
		log.info(String.format("###begin-ios-pay-doNewAppleChargeBusinessWithSyn,orderId:%s,,reqUrl:%s,verifyReceipt:%s",paramOrderId,Constants.APPLEPAY_REALURL,verifyReceipt));
		//参数校验
		if(StringUtils.isEmpty(paramOrderId)||StringUtils.isEmpty(verifyReceipt) || StringUtils.isEmpty(clientIp)){
			log.error("###applePay orderId is null");
			Exception e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage() ,e);
			throw e;
		}
		//参数校验
		if(StringUtils.isEmpty(clientAppleUUID) ||StringUtils.isEmpty(clientWifiMac)){
			Exception e = new PayBizException(ErrorCode.ERROR_5027);
			log.error(e.getMessage() ,e);
			throw e;
		}
		String clientAddr = ipStoreService.getAddressByIp(clientIp) ;
		String responseStr = HttpUtils.postToAppleServer(Constants.APPLEPAY_REALURL,verifyReceipt);
		//String responseStr = "{\"status\":0, \"environment\":\"Sandbox\", \"receipt\":{\"receipt_type\":\"ProductionSandbox\", \"adam_id\":0, \"app_item_id\":0, \"bundle_id\":\"com.jiujun.peach\", \"application_version\":\"1\", \"download_id\":0, \"version_external_identifier\":0, \"receipt_creation_date\":\"2016-04-07 03:44:39 Etc/GMT\", \"receipt_creation_date_ms\":\"1460000679000\", \"receipt_creation_date_pst\":\"2016-04-06 20:44:39 America/Los_Angeles\", \"request_date\":\"2016-04-07 03:44:42 Etc/GMT\", \"request_date_ms\":\"1460000682088\", \"request_date_pst\":\"2016-04-06 20:44:42 America/Los_Angeles\", \"original_purchase_date\":\"2013-08-01 07:00:00 Etc/GMT\", \"original_purchase_date_ms\":\"1375340400000\", \"original_purchase_date_pst\":\"2013-08-01 00:00:00 America/Los_Angeles\", \"original_application_version\":\"1.0\", \"in_app\":[{\"quantity\":\"1\", \"product_id\":\"peach001\", \"transaction_id\":\"1000000203950244\", \"original_transaction_id\":\"1000000203950244\", \"purchase_date\":\"2016-04-07 03:44:39 Etc/GMT\", \"purchase_date_ms\":\"1460000679000\", \"purchase_date_pst\":\"2016-04-06 20:44:39 America/Los_Angeles\", \"original_purchase_date\":\"2016-04-07 03:44:39 Etc/GMT\", \"original_purchase_date_ms\":\"1460000679000\", \"original_purchase_date_pst\":\"2016-04-06 20:44:39 America/Los_Angeles\", \"is_trial_period\":\"false\"}]}}";
		String strLog = String.format("###ios-pay,server-responseStr:%s", responseStr) ;
		log.info(strLog);
		PayChargeOrder reqPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(paramOrderId);
		if(reqPayChargeOrder==null){
			Exception e = new PayBizException(ErrorCode.ERROR_5012);
			log.error(e.getMessage() ,e);
			throw e;
		}
		String userId = reqPayChargeOrder.getUserId();
		String agentUserId = reqPayChargeOrder.getAgentUserId();
		JSONObject appleResJson =JsonUtil.strToJsonObject(responseStr);
		int status = appleResJson.getIntValue("status");
		String environment = null;
		String environmentJsonKey = "environment";
		if(appleResJson.containsKey(environmentJsonKey)){
			 environment = appleResJson.getString(environmentJsonKey);
		}
		//标志:是否允许用户的充值允许想沙箱服务发起认证
		boolean isSanBoxUser = testIfSanBoxUser(userId);
		//订单是否通过沙箱服务认证充值成功
		boolean isPayBySanBox = false;
		// appleUserRealPayMoney=商品的实际定价
		int appleUserRealPayMoney = 0;
		//非0,表示充值无效
		if(status!=0){
			String payFailLog = String.format("###ios-pay:首次验证status:%s不通过(通过苹果正式服获得status!=0),userId:%s,status:%s,responseStr:%s,verifyReceipt:%s",status,userId,status, responseStr,verifyReceipt) ;
			log.error(payFailLog) ;
			log.warn(String.format("###ios-pay:首次验证status:%s不通过(通过苹果正式服获得status!=0),检测蜜桃官方是否配置用户可以用沙盒充值,userId:%s,isSanBoxUser:%s",status,userId,isSanBoxUser));
			//如果是允许请求沙箱服务进行认证(如:苹果官方审核所用的user)
			if(isSanBoxUser){
				log.warn(String.format("###ios-pay:首次验证status:%s不通过(通过苹果正式服获得status!=0),蜜桃官方配置用户可以用沙河充值,再次进入苹果沙盒服务器验证,userId:%s,isSanBoxUser:%s",status,userId,isSanBoxUser));
				//苹果官方审核,用的是请求的是沙箱服务,所以再请求沙箱地址
				responseStr = HttpUtils.postToAppleServer(Constants.APPLEPAY_SANDBOX_URL,verifyReceipt);
				strLog = String.format("###ios-pay-sanbox,userId:%s,server-responseStr:%s",userId, responseStr) ;
				log.info(strLog);
				appleResJson =JsonUtil.strToJsonObject(responseStr);
				status = appleResJson.getIntValue("status");
				if(appleResJson.containsKey(environmentJsonKey)){
					 environment = appleResJson.getString(environmentJsonKey);
				}
				strLog = String.format("###ios-pay-sanbox,userId:%s,status:%s,environment:%s",userId, status,environment) ;
				log.info(strLog);
				//沙箱服务认证也是失败,则抛出异常
				if(status!=0){
					String remark = String.format("###苹果支付失败:status:%s非0",status);
					chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
					//沙箱认证也不成功,则抛出异常
					String errorMsg = "苹果返回错误码:"+status;
					Exception e = new PayBizException(ErrorCode.ERROR_5008,errorMsg);
					log.error(e.getMessage() ,e);
					throw e;
				}
				isPayBySanBox = true;
			}else{
				String remark = String.format("###苹果支付失败:status:%s非0",status);
				chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
				String errorMsg = "苹果返回错误码:"+status;
				Exception e = new PayBizException(ErrorCode.ERROR_5008,errorMsg);
				log.error(e.getMessage() ,e);
				throw e;
			}
		}
		// 本地配置的bundleId
		String localBundleIdListStr = getLocalBundleId();
		// 校验包名
		String bundle_id = appleResJson.getJSONObject("receipt").getString("bundle_id");
		if(!StringUtils.isEmpty(localBundleIdListStr) ){
			String[] bundleArr = localBundleIdListStr.split(",");
			Boolean flagBundleCheckOk = Boolean.FALSE;
			for(int i=0;i<bundleArr.length;i++){
				String localBuneleItem = bundleArr[i];
				if(bundle_id.equals(localBuneleItem)){
					flagBundleCheckOk = Boolean.TRUE;
					break;
				}else{
					flagBundleCheckOk = Boolean.FALSE;
				}
			}
			if(!flagBundleCheckOk){
				String remark = String.format("###苹果支付失败:验证bundle_id不通过,本地配置bundle_id：%s,返回bundle_id:%s",localBundleIdListStr ,bundle_id);
				chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
				log.error(remark);
				Exception e = new PayBizException(ErrorCode.ERROR_5032);
				throw e;
			}else{
				log.info(String.format("###苹果支付:验证bundle_id已通过,本地配置bundle_id：%s,苹果服务器返回bundle_id:%s",localBundleIdListStr ,bundle_id));
			}
		}else{
			log.error("###苹果支付通知处理,服务端没有配置bundleId,请通知系统管理员") ;
			Exception e = new PayBizException(ErrorCode.ERROR_5022);
			throw e;
		}
		JSONObject receiptJson = appleResJson.getJSONObject("receipt");
		JSONArray inAppJsonArray = receiptJson.getJSONArray("in_app");
		if(!"Production".equals(environment)){
			log.info("#####苹果充值,充值认证返回的environment不是Production,返回的environment:"+environment);
			//不是沙盒测试用户
			if(!isSanBoxUser){
				String remark = String.format("###苹果支付失败:发现使用沙河测试的支付凭证且非沙盒测试用户,userId:%s,environment:%s", userId,environment);
				chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
				log.error(remark);
				return;
				
			}else{
				log.info(String.format("###苹果支付:发现系统配置了该用户允许使用沙河测试的支付凭证,userId:%s,environment:%s", userId,environment));
			}
		}
		if(inAppJsonArray==null || inAppJsonArray.size() <= 0){
			String remark = String.format("###苹果支付失败:根据用户%s的支付凭证请求苹果服务器后没商品返回,苹果返回数据%s", userId,responseStr);
			chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
			log.error(remark);
			return;
		}
		// 商品对应的苹果服务器端的transactionId
		String transactionId = null;
		// 苹果商品按比例转换后的价值(xx分)
		int applePayRateMoney = 0;
		 // 返回的商品信息长度
		int verifyRetProductSize = inAppJsonArray.size();
		for(int i=0;i<verifyRetProductSize;i++){
			JSONObject inAppJson = inAppJsonArray.getJSONObject(i);
			String productId = inAppJson.getString("product_id");
			int quantity = inAppJson.getIntValue("quantity");
			transactionId = inAppJson.getString("transaction_id");
			String purchaseDate =  inAppJson.getString("purchase_date");
			log.info(String.format("#####applePay-transactionId:%s,productId:%s",transactionId,productId)); 
			ApplePayRecordDo applePayRecord = applePayRecordMapper.getObjectByTransactionId(transactionId);
			if(applePayRecord==null){
				applePayRecord = new ApplePayRecordDo();
				applePayRecord.setProductId(productId);
				applePayRecord.setQuantity(quantity); //一次只买一个
				applePayRecord.setTransactionId(transactionId);
				applePayRecord.setPurchaseDdate(purchaseDate);
				applePayRecord.setDataXml(responseStr);
				applePayRecord.setRecordTime(new Date());
				applePayRecord.setUserId(userId);
				applePayRecordMapper.insert(applePayRecord);
			}
			// 根据支付系统端的订单号查db(此transactionId之约订单成功后才update到pay_charge_order)
			PayChargeOrder dbPayChargeOrder = this.payChargeOrderMapper.getPcoByTransactionId(transactionId);
			if(dbPayChargeOrder != null){
				if(i >= (verifyRetProductSize-1)){//已经验证到最后一条,则抛出业务异常
					int rowId = dbPayChargeOrder.getId();
					String dbOrderId = dbPayChargeOrder.getOrderId();
					String remark = String.format("###reqOrderId:%s,苹果支付失败:transactionId:%s不能重复使用,已有pay_charge_order与之对应,rowId:%s,orerId:%s,苹果服务器返回商品列表:%s,长度为:%s,目前遍历到第%s次",paramOrderId,transactionId,rowId,dbOrderId,JsonUtil.arrayToJsonString(inAppJsonArray),verifyRetProductSize,i+1);
					chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
					log.info(remark); 
					log.info(String.format("#####苹果充值失败-苹果支付,该订单已处理过,直接返回,不加金币,userId:%s,agentUserId:%s,ip:%s,地址:%s,transactionId:%s,productId:%s,pay_charge_order-rowId:%s",userId,agentUserId,clientIp,clientAddr,transactionId,productId,rowId)); 
					return ;
				}else{
					int rowId = dbPayChargeOrder.getId();
					String dbOrderId = dbPayChargeOrder.getOrderId();
					String remark = String.format("###苹果支付处理-reqOrderId:%s,苹果支付失败:transactionId:%s不能重复使用,已有pay_charge_order与之对应,rowId:%s,orerId:%s,苹果服务器返回商品列表长度%s,continue",paramOrderId,transactionId,rowId,dbOrderId,verifyRetProductSize);
					log.info("###苹果支付,业务单已被本地处理过,continue,继续验证下一条商品");
					continue;
				}
			}
			log.info(String.format("#####苹果支付通知,transactionId校验通过,继续走后面的流程,transactionId:%s,paramOrderId:%s",transactionId,paramOrderId)); 
			//订单里面保存的选择金额
			int dbPayChargeOrderSelectMoney = reqPayChargeOrder.getSelectMoney();
			AppleProductDo appleProductDo = this.appleProductMapper.getObjectByProductId(productId);
			if(appleProductDo==null){
				if(i >= (verifyRetProductSize-1)){
					log.info("###苹果充值失败-苹果商品不存在,productId:"+productId+",orderId:"+paramOrderId);
					Exception e = new PayBizException(ErrorCode.ERROR_5018);
					log.error(e.getMessage() ,e);
					throw e;
				}else{
					log.info("###苹果支付,本地不存在支付返回的商品,continue,继续验证下一条商品");
					continue;
				}
			}
			//商品设置需支付的金额(单位:分)
			int productMoney = appleProductDo.getMoney();
			// appleUserRealPayMoney=商品的实际定价
			appleUserRealPayMoney = appleProductDo.getMoney();
			//商品对应的按苹果设置的分成比例拿到的金额(单位:分)
			applePayRateMoney = appleProductDo.getRateMoney();
			//比较订单金额与此次拿到的商品金额
			if(dbPayChargeOrderSelectMoney!=applePayRateMoney){
				if(i >= (verifyRetProductSize-1)){
					String remark = String.format("###苹果充值失败-苹果商品金额与订单金额不匹配,商品金额(分成后):%s,订单金额:%s,本地orderId:%s,苹果服务器返回商品列表长度:%s,已循环校验次数:%s",applePayRateMoney,dbPayChargeOrderSelectMoney,paramOrderId,verifyRetProductSize,i+1);
					log.error(remark);
					chargeCommonService.upateOrderStatus2Fail(paramOrderId, remark,clientIp);
					Exception e = new PayBizException(ErrorCode.ERROR_5019);
					throw e;
				}else{
					log.info("###苹果支付,商品金额与本地订单金额不一致,continue,继续验证下一条商品");
					continue;
				}
			}
			break; //前面基本验证都没问题,break：跳出循环(比如:第一条item,就通过了前面几步校验)
		}
		boolean isApplePay = true; 
		// 非法ip或地址,充值存在异常行为
		boolean flagInValidIp = false;
		// 非法uuid
		boolean flagInValidUUID = false;
		// 非法wifiMac
		boolean flagInValidWifiMac  = false;
		String sysConfCode = SysConfTableEnum.Code.IOS_PAY_INVALID_IP_CONF.getValue();
		// 从数据库表t_sys_conf中读取配置
		SysConf sysConf = null;
		ServiceResult<SysConf> srt = sysConfService.getByCode(sysConfCode);
		if(srt.isSucceed()) {
			sysConf = srt.getData();
		}
		String clientAddress = ipStoreService.getAddressByIp(clientIp);
		if(sysConf != null ){
			log.info(String.format("###iosPaySuccessNotice,clientIp:%s,clientAddress:%s,sysConf:%s",clientIp,clientAddress, JsonUtil.beanToJsonString(sysConf)));
			if(sysConf.getIsUse() == SysConfTableEnum.IsUse.Yes.getValue()){
				JSONObject jsonConf = JsonUtil.strToJsonObject(sysConf.getConfValue()) ;
				String confInValidIpOrAddrs = jsonConf.getString("inVaildIpOrAddr");
				String confInVaildAppleUUIDs = jsonConf.getString("inVaildAppleUUID");
				String confInValidWifiMac = jsonConf.getString("inVaildWifiMac");
				// 校验wifiMac
				if(!StringUtils.isEmpty(confInValidWifiMac)){
					String[] wifiMacArr = confInVaildAppleUUIDs.split(",");
					if(wifiMacArr != null && wifiMacArr.length > 0){
						for(int i=0;i<wifiMacArr.length;i++){
							String wifiMacItem = wifiMacArr[i];
							if(!StringUtils.isEmpty(wifiMacItem) && wifiMacItem.equals(clientWifiMac)){ // 根据wifiMac检验 
								flagInValidWifiMac = true;
								log.info(String.format("###iosPaySuccessNotice,该wifiMac:%s(ip:%s,地址:%s)苹果支付存在异常行为,已被列为黑名单,sysConfCode:%s,黑名单arr:%s,orderUserId:%s,agentUserId:%s",clientWifiMac,clientIp,clientAddress,sysConfCode,confInValidIpOrAddrs,userId,agentUserId));
								break;
							}
						}
					}
				}
				// 校验uuid
				if(!StringUtils.isEmpty(confInVaildAppleUUIDs)){
					String[] uuidArr = confInVaildAppleUUIDs.split(",");
					if(uuidArr != null && uuidArr.length > 0){
						for(int i=0;i<uuidArr.length;i++){
							String uuidItem = uuidArr[i];
							if( !StringUtils.isEmpty(uuidItem) && uuidItem.equals(clientAppleUUID)){ // 根据uuid检验 
								flagInValidUUID = true;
								log.info(String.format("###iosPaySuccessNotice,该uuid:%s(ip:%s,地址:%s)苹果支付存在异常行为,已被列为黑名单,sysConfCode:%s,黑名单arr:%s,orderUserId:%s,agentUserId:%s",clientAppleUUID,clientIp,clientAddress,sysConfCode,confInValidIpOrAddrs,userId,agentUserId));
								break;
							}
						}
					}
				}
				// 校验ip
				if(!StringUtils.isEmpty(confInValidIpOrAddrs)){
					String[] ipArr = confInValidIpOrAddrs.split(",");
					if(ipArr != null && ipArr.length > 0){
						for(int i=0;i<ipArr.length;i++){
							String ipItem = ipArr[i];
							//if(!StringUtils.isEmpty(ipItem) && ipItem.equals(clientIp)){ // 根据ip检验 
							if(!StringUtils.isEmpty(ipItem) && clientAddress.contains(ipItem)){ //根据地方名字是否包含黑名单的关键字校验 
								flagInValidIp = true;
								log.info(String.format("###iosPaySuccessNotice,该地址:%s(ip:%s)苹果支付存在异常行为,已被列为黑名单,sysConfCode:%s,黑名单arr:%s,orderUserId:%s,agentUserId:%s",clientAddress,clientIp,sysConfCode,confInValidIpOrAddrs,userId,agentUserId));
								break;
							}
							if(!StringUtils.isEmpty(ipItem) && ipItem.equals(clientIp)){ // 根据ip检验 
								flagInValidIp = true;
								log.info(String.format("###iosPaySuccessNotice,该ip:%s(地址:%s)苹果支付存在异常行为,已被列为黑名单,sysConfCode:%s,黑名单arr:%s,orderUserId:%s,agentUserId:%s",clientIp,clientAddress,sysConfCode,confInValidIpOrAddrs,userId,agentUserId));
								break;
							}
						}
					}
				}
			}else{
				log.info(String.format("###iosPaySuccessNotice,sysConf 非法充值拦截配置已停用,sysConfCode:%s",sysConfCode));
			}
		}else{
			log.info(String.format("###iosPaySuccessNotice,sysConf 非法充值拦截配置不存在,sysConfCode:%s",sysConfCode));
		}
		String failRemark = null ;
		if(flagInValidWifiMac){
			  log.info(String.format("###at-ios-pay-doNewAppleChargeBusinessWithSyn,(userId:%s,agentUserId:%s)该ip:%s充值存在异常行为,验证成功但不加金币,orderId:%s,,reqUrl:%s,verifyReceipt:%s",userId,agentUserId,clientIp,paramOrderId,Constants.APPLEPAY_REALURL,verifyReceipt));
			 failRemark = String.format("苹果充值失败-该userId:%s,clientWifiMac:%s(ip:%s,地址:%s)充值存在异常行为,验证成功但不加金币",userId,clientWifiMac,clientIp,clientAddress);
			  log.info(failRemark);
		}
		if(flagInValidIp){
			log.info(String.format("###at-ios-pay-doNewAppleChargeBusinessWithSyn,(userId:%s,agentUserId:%s)该ip:%s充值存在异常行为,验证成功但不加金币,orderId:%s,,reqUrl:%s,verifyReceipt:%s",userId,agentUserId,clientIp,paramOrderId,Constants.APPLEPAY_REALURL,verifyReceipt));
			 failRemark = String.format("苹果充值失败-该userId:%s,ip:%s(地址:%s)充值存在异常行为,验证成功但不加金币",userId,clientIp,clientAddress);
			 log.info(failRemark);
		}
		if(flagInValidUUID){
			log.info(String.format("###at-ios-pay-doNewAppleChargeBusinessWithSyn,(userId:%s,agentUserId:%s)该uuid:%s充值存在异常行为,验证成功但不加金币,orderId:%s,,reqUrl:%s,verifyReceipt:%s",userId,agentUserId,clientAppleUUID,paramOrderId,Constants.APPLEPAY_REALURL,verifyReceipt));
			failRemark = String.format("苹果充值失败-该userId:%s,uuid:%s(ip:%s,地址:%s)充值存在异常行为,验证成功但不加金币",userId,clientAppleUUID,clientIp,clientAddress);
			log.info(failRemark);
		}
		if(flagInValidUUID || flagInValidIp || flagInValidWifiMac){
			// 非法的ip、uuid、wifiMac，算充值失败
			chargeCommonService.upateOrderStatus2Fail(paramOrderId, failRemark,clientIp) ;
		}else{
			//  充值加金币： 到账的是按苹果设置的分成比例拿到的金额
			chargeCommonService.updateChargeOrderAndGold(paramOrderId, applePayRateMoney,isApplePay,isPayBySanBox,appleUserRealPayMoney,clientIp,transactionId,deviceProperties);
		}
		// 24小时内,达到一次次数发邮箱通知系统管理员
		//sendEmail2NoticeSystemadmin(userId,clientAppleUUID);		
		log.info(String.format("###end-ios-pay-doNewAppleChargeBusinessWithSyn,orderId:%s,,reqUrl:%s,verifyReceipt:%s",paramOrderId,Constants.APPLEPAY_REALURL,verifyReceipt));
	}

	/**
	 * 检测用户充值回调可以调沙箱地址
	 * @param userId
	 * @return
	 */
	private boolean testIfSanBoxUser(String userId) {
		boolean flag = false;
		if(sanboxChargeUserIdArr != null && sanboxChargeUserIdArr.length > 0){
			for(int i=0;i<sanboxChargeUserIdArr.length;i++){
				String str = sanboxChargeUserIdArr[i];
				if(userId.equals(str)){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
}
