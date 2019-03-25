package com.jiujun.shows.pay.biz;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.MD5Util;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.common.utils.StrUtil;
import com.jiujun.shows.common.utils.TransactionClient;
import com.jiujun.shows.common.utils.TransactionClientSdk1;
import com.jiujun.shows.common.utils.XMLConverUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.EcoPayNotifyRecordMapper;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.EcoPayNotifyRecord;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IChargeCommonService;
import com.jiujun.shows.pay.vo.DataRequest;
import com.jiujun.shows.pay.vo.EcoPay;
import com.jiujun.shows.pay.vo.PayOrder;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiun.shows.appclient.service.IAppInstallChannelService;
import com.payeco.tools.Xml;

public class UnionPayBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private EcoPayNotifyRecordMapper ecoPayNotifyRecordMapper;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;

	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IChargeCommonService chargeCommonService;
	
	/**
	 * 移动网银支付
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月23日
	 */
	public ServiceResult<EcoPay> doPay(DataRequest data) throws Exception {
		//传递的参数
		/**
		 * 【SDK标准版】和【纯SDK密码键盘版】的商户订单下单接口
		 * @param merchantId:		商户代码
		 * @param merchOrderId	:	商户订单号
		 * @param amount		:	商户订单金额
		 * @param orderDesc		:	商户订单描述	  字符最大128，中文最多40个；参与签名：采用UTF-8编码提交参数：采用UTF-8的base64格式编码
		 * @param tradeTime		:	商户订单提交时间
		 * @param expTime		:	交易超时时间； 超过订单超时时间未支付，订单作废；不提交该参数，采用系统的默认时间（从接收订单后超时时间为30分钟）
		 * @param notifyUrl		:	异步通知URL ； 提交参数时，做URLEncode处理
		 * @param extData		:	商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
		 * @param miscData		:	订单扩展信息   根据不同的行业，传送的信息不一样；参与签名：采用UTF-8编码，提交参数：采用UTF-8的base64格式编码
		 * @param notifyFlag	:	订单通知标志    0：成功才通知，1：全部通知（成功或失败）  不填默认为“1：全部通知”
		 * @param priKey		:	商户签名的私钥
		 * @param pubKey        :   易联签名验证公钥
		 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
		 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
		 * @return 				:  处理状态码： 0000 : 处理成功， 其他： 处理失败
		 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
		 */
		String extData = "";
		String miscData = "";
		String merchOrderId = "";  //订单号
		String merchantId = Constants.MERCHANT_ID;
		String notifyUrl = Constants.MERCHANT_NOTIFY_URL;  //需要做URLEncode
		String tradeTime =  DateUntil.getFormatDate("yyyyMMddHHmmss", new Date());
		String notifyFlag = "0";
		String expTime = "";
		String amount = "" ;
		String orderDesc ="" ;
		//返回报文xml对象
		Xml retXml = new Xml();
		EcoPay ecoPay = new EcoPay();
		if(data != null){
			if(data.getPayOrder()!= null){
				//选择充值金额(单位:分)
				int chargeSelectMoneyFen  = data.getPayOrder().getMoney();
				String testUserList = SpringContextListener.getContextProValue("charge.testUserList","");
				//非测试人员要检测是否达到最少金额
				if(!testUserList.contains(data.getUserBaseInfo().getUserId())){
					String minChargeMoneyStr = SpringContextListener.getContextProValue("charge.minSelectMoney", "500");
					int minChargeMoney = Integer.parseInt(minChargeMoneyStr);
					if(chargeSelectMoneyFen < minChargeMoney){ //限制最低充值5元
						PayBizException e = new PayBizException(ErrorCode.ERROR_5017);
						log.error(e.getMessage(),e);
						throw e;
					}
				}
				PayChargeOrder pco = getPco(data);
				String agentUserId = pco.getAgentUserId();
				String receiveUserId= pco.getUserId();
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
				
				merchOrderId = pco.getOrderId();		
				if(data.getPayOrder().getMoney() != 0){
					amount = data.getPayOrder().getMoney()/100f+"";
					log.info("amount:"+amount);
				}else{
					PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
					log.error(e.getMessage(),e);
					throw e;
				}
				if(data.getPayOrder().getRemark()!= null){
					try {
						pco.setRemark("易联支付充值");
						orderDesc = URLDecoder.decode(data.getPayOrder().getRemark(), "utf-8");
						log.info(orderDesc);
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}
				}			
				try {
					log.info(merchantId+" "+merchOrderId+" "+orderDesc+" "+tradeTime+" "+expTime+" "+extData+" "+miscData+" "+notifyFlag+" "+Constants.PAYECO_URL);
					String ret = TransactionClientSdk1.MerchantOrder(merchantId,
							merchOrderId, amount, orderDesc, tradeTime, expTime,
							notifyUrl, extData, miscData, notifyFlag,
							Constants.MERCHANT_RSA_PRIVATE_KEY, Constants.PAYECO_RSA_PUBLIC_KEY, Constants.PAYECO_URL, retXml);
					log.info("ret:" +ret);
					if(!"0000".equals(ret)){
						PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
						log.error(e.getMessage(),e);
						throw e;
					}else{
						payChargeOrderMapper.insert(pco);
					}
				} catch (Exception e) {
					String errCode  = e.getMessage();
					if("E101".equalsIgnoreCase(errCode)){
						PayBizException e1 = new PayBizException(ErrorCode.ERROR_5031);
						log.error(e1.getMessage(),e1);
						throw e1;
					}else if("E102".equalsIgnoreCase(errCode)){
						PayBizException e1 = new PayBizException(ErrorCode.ERROR_5032);
						log.error(e1.getMessage(),e1);
						throw e1;
					}else if("E103".equalsIgnoreCase(errCode)){
						PayBizException e1 = new PayBizException(ErrorCode.ERROR_5033);
						log.error(e1.getMessage(),e1);
						throw e1;
					}else{
						PayBizException e1 = new PayBizException(ErrorCode.ERROR_5030);
						log.error(e1.getMessage(),e1);
						throw e1;
					}
				}
			}
		}	
		ecoPay.setAmount(data.getPayOrder().getMoney());
		ecoPay.setMerchantId(retXml.getMerchantId());
		ecoPay.setMerchOrderId(retXml.getMerchOrderId());
		ecoPay.setNotifyUrl(notifyUrl);
		ecoPay.setOrderId(retXml.getOrderId());
		ecoPay.setSign(retXml.getSign());
		ecoPay.setTradeTime(retXml.getTradeTime());
		ecoPay.setVersion("2.0.0");
		ServiceResult<EcoPay> srt = new ServiceResult<EcoPay>();
		srt.setSucceed(true);
		srt.setData(ecoPay);
		return srt;
	}
	
	/**
	 * web网银支付
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月23日
	 */
	public ServiceResult<JSONObject> doWebPay(DataRequest data) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		JSONObject jsonRet = new JSONObject();
		PayOrder payOrder = new PayOrder();
		EcoPay ecoPay = new EcoPay();		
		String responseDesc = "" ; 
		String orderId = "" ; 
		String AcqSsn = new SimpleDateFormat("HHmmss").format(new Date());
		String remark = "" ; 
		String amount = "";
		if(data.getPayOrder() != null){
			if(data.getPayOrder().getRemark()!= null){
				try {
					remark = URLDecoder.decode(data.getPayOrder().getRemark(), "utf-8");
					log.info(remark);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			}
			String noticeUrlOfAsynAddress = Constants.MERCHANT_NOTIFY_URL_WEB;
			//选择充值金额(单位:分)
			int chargeSelectMoneyFen  = data.getPayOrder().getMoney();
			String testUserList = SpringContextListener.getContextProValue("charge.testUserList","");
			//非测试人员要检测是否达到最少金额
			if(!testUserList.contains(data.getUserBaseInfo().getUserId())){
				String minChargeMoneyStr = SpringContextListener.getContextProValue("charge.minSelectMoney", "500");
				int minChargeMoney = Integer.parseInt(minChargeMoneyStr);
				if(chargeSelectMoneyFen < minChargeMoney){ //限制最低充值5元
					PayBizException e = new PayBizException(ErrorCode.ERROR_5017);
					log.error(e.getMessage(),e);
					throw e;
				}
			}
			PayChargeOrder pco = getPco(data);
			String agentUserId = pco.getAgentUserId();
			String receiveUserId= pco.getUserId();
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
			pco.setRemark(remark);
			orderId = pco.getOrderId(); 
			payChargeOrderMapper.insert(pco);
			amount = chargeSelectMoneyFen/100f+"";
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("Version", "2.0.0");
			map.put("ProcCode", "0200"); // 交易类型
			map.put("ProcessCode", "190011"); // 处理码
			map.put("Amount", amount); // 交易金额
			map.put("MerchantNo", Constants.MERCHANTNO); // 易联商户号
			map.put("MerchantOrderNo", orderId); // 商户系统订单号
			map.put("AsynAddress", noticeUrlOfAsynAddress); // 异步返回地址
			map.put("SynAddress", Constants.MERCHANT_RETURN_URL); // 同步返回地址 
			map.put("OrderFrom", "02"); // 订单来源
			if(remark.length() >50) { // 易联要求不超过50
				remark = remark.substring(0, 50);
			}
			map.put("Description", remark); // 订单描述
			map.put("AcqSsn", AcqSsn);
			map.put("Reference", "Reference");
			map.put("TransDatetime", DateUntil.getFormatDate("yyyyMMddHHmmss", new Date()));
			map.put("TerminalNo", Constants.TERMINALNO);
			log.info("Amount="+amount+"AsynAddress="+noticeUrlOfAsynAddress+"MerchantOrderNo="+ orderId
					+"Description="+remark+"ProcCode=0200"+"ProcessCode=190011"+"AcqSsn="+AcqSsn);
			String postText = getXmlStr(map);
			log.info("postText:"+postText);
			String base64Text = "";	
			String urlPost = "";
			try {
				//base64Text = Base64.encode(postText.getBytes("utf-8"));
				base64Text = new sun.misc.BASE64Encoder().encode(postText.getBytes("utf-8"));
				String urlEncodeText = URLEncoder.encode(base64Text, "utf-8");
				urlPost = Constants.UNION_URL +"?request_text="+urlEncodeText;
				responseDesc =URLEncoder.encode( HttpUtils.post(urlPost, ""),"utf-8");
				log.info("responseDesc:"+responseDesc);			
			} catch (Exception e) {
				log.error("加密过程出错", e);
				PayBizException e1 = new PayBizException(ErrorCode.ERROR_5002);
				log.error(e1.getMessage(),e1);
				throw e1;
			}
		}
		payOrder.setOrderId(orderId);
		ecoPay.setSign(responseDesc);
		jsonRet.put(payOrder.getShortName(), payOrder.buildJson());
		jsonRet.put(ecoPay.getShortName(), ecoPay.buildJson());
		srt.setSucceed(true);
		srt.setData(jsonRet);
		return srt;
	}
	
	/**
	 * 处理app端易联支付回调通知
	 * @param version 通讯版本号
	 * @param merchantId 商户号
	 * @param merchOrderId 本地订单号
	 * @param amount 商户订单金额  : 元
	 * @param extData 商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
	 * @param orderId 易联订单号
	 * @param status  订单状态
	 * @param payTime 订单支付时间
	 * @param settleDate 订单结算日期
	 * @param sign  签名数据
	 * @param allParamStr 通知的全部参数和值
	 * @return
	 */
	public ServiceResult<String> dealPaySuccessNotify(String version, String merchantId,
			String merchOrderId, String amount, String extData, String orderId,
			String status, String payTime, String settleDate, String sign,
			String allParamStr,String notifyFromIp,DeviceProperties dev) throws Exception {
		ServiceResult<String> srt = new ServiceResult<String>();
		srt.setSucceed(true);
		log.info("###begin-dealEcoPayNotify:"
				+"Version=" + version 
				+ "&MerchantId=" + merchantId
				+ "&MerchOrderId=" + merchOrderId 
				+ "&Amount=" + amount
				+ "&ExtData=" + extData 
				+ "&OrderId=" + orderId 
				+ "&Status=" + status 
				+ "&PayTime=" + payTime 
				+ "&SettleDate=" + settleDate
				+ "&sign="+sign);
		//默认返回错误信息
		String retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
		//本地配置的商户号
		String localMerchantId = Constants.MERCHANT_ID;
		//校验商户号对不对得上本地配置的商户号
		if(StringUtils.isEmpty(merchantId) || !merchantId.equals(localMerchantId)){
			log.info("### dealEcoPayNotify:商户号校验失败!,merchOrderId:"+merchantId+",localMerchantId:"+localMerchantId+",allParamStr:"+allParamStr);
			retMsgJson = "{\"RetCode\":\"E101\",\"RetMsg\":\"商户号校验失败!\"}";
			srt.setData(retMsgJson);
			return srt;
		}
		//验证订单结果通知的签名
		boolean verifyResult = TransactionClient.bCheckNotifySign(version, merchantId, merchOrderId, 
				amount, extData, orderId, status, payTime, settleDate, sign, 
				Constants.PAYECO_RSA_PUBLIC_KEY);
		log.info("###dealEcoPayNotify-verifyResult:"+verifyResult+",merchOrderId:"+merchOrderId);
		//服务端金额格式转换
		//double amountD = StrUtil._Double(amount); //单位:元
		//int money = (int)(amountD * 100); // 单位:分
		BigDecimal b1 = new BigDecimal(amount);
		BigDecimal b2 = new BigDecimal(100);
		int money = b1.multiply(b2).intValue();//转成分
		if (!verifyResult) {
			log.info("### dealEcoPayNotify:验证签名失败!,merchOrderId:"+merchOrderId+",allParamStr:"+allParamStr);
			retMsgJson = "{\"RetCode\":\"E101\",\"RetMsg\":\"验证签名失败!\"}";
			srt.setData(retMsgJson);
			return srt;
		}else{
			//保存记录(有记录忽略)
			EcoPayNotifyRecord ecoPayNotifyRecord = ecoPayNotifyRecordMapper.getObjectByOrderId(merchOrderId);
			if(ecoPayNotifyRecord != null){
				log.info("### dealEcoPayNotify:已存在步请求记录 , orderId = " + merchOrderId );
			}else{
				if("02".equals(status)){
					ecoPayNotifyRecord = getEcoPayNotifyRecord(version, merchantId, merchOrderId, amount, extData, orderId, status, payTime, settleDate, sign , allParamStr);
					if(ecoPayNotifyRecord == null){
						log.info("### dealEcoPayNotify:异步请求数据存在异常 ， orderId = " + merchOrderId);
					}else{
						ecoPayNotifyRecordMapper.insert(ecoPayNotifyRecord);
					}
				}else{
					log.info("###dealAliPayNotify:易联支付通知返回非已付款状态(02),不保存通知,status:"+status );
				}
			}
			//订单业务处理
			//获取本地服务器订单信息
			PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(merchOrderId);
			if(dbPayChargeOrder == null){//本地服务器不存在该订单
				retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
				log.info("### dealEcoPayNotify-本地服务端订单不存在,merchOrderId:"+merchOrderId+",orderId:"+orderId);
				Exception e = new PayBizException(ErrorCode.ERROR_5012);
				log.error(e.getMessage() ,e);
				throw e;
			}else{
				//订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
				int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
				// 签名验证成功后，需要对订单进行后续处理
				if ("02".equals(status)) {
					// 订单已支付;
					// 1、检查Amount和商户系统的订单金额是否一致
					if(money == dbPayChargeOrder.getSelectMoney()){
						// 2、订单支付成功的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理）；
						// 3、返回响应内容
						retMsgJson = "{\"RetCode\":\"0000\",\"RetMsg\":\"订单已支付\"}";
						log.info("###dealEcoPayNotify-addGold,orderId:"+merchOrderId);
						boolean isApplePay = false;
						boolean isPayBySanBox = false;
						int appleUserRealPayMoney = 0;
						String transactionId = orderId  ;
						chargeCommonService.updateChargeOrderAndGold(merchOrderId, money,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIp,transactionId,dev);
					}else{
						log.info("充值金额存在异常");
						retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
					}
				} else {
					// 1、订单支付失败的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理，避免成功后又修改为失败）；
					// 2、返回响应内容
					log.info("###dealEcoPayNotify:订单支付失败!status="+status);
					String remark = "易联支付失败(Status应=02):,Status="+status;
					chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIp);
					retMsgJson = "{\"RetCode\":\"E102\",\"RetMsg\":\"订单支付失败+"+status+"\"}";
				}
			}
			srt.setData(retMsgJson);
			return srt;
		}
	}
	
	/**
	 * 处理web端易联支付回调通知
	 * @param xmlDataStr 通知的全部参数和值
	 * @param dev
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<String> dealPaySuccessNotifyFromWeb(String xmlDataStr,String notifyFromIp,
			DeviceProperties dev) throws Exception {
		ServiceResult<String> srt = new ServiceResult<String>();
		srt.setSucceed(true);
		log.info("###begin-dealWebEcoPayNotify-xmlDataStr:"+xmlDataStr);
		//默认返回错误信息
		String retMsgJson = "{0000}";
		srt.setData(retMsgJson);
		if(StringUtils.isEmpty(xmlDataStr)){
//			retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"通知数据报文为空\"}";
			log.info("###dealPaySuccessNotifyFromWeb-xmlDataStr return fail, is empty....");
			srt.setData(retMsgJson);
			return srt;
		}
		
		String version = getString(getValue(xmlDataStr , "Version"));
		String merchantId = getString(getValue(xmlDataStr , "MerchantNo"));
		String merchOrderId = getString(getValue(xmlDataStr , "MerchantOrderNo"));
		String amount = getString(getValue(xmlDataStr , "Amount"));
		String extData = getString(getValue(xmlDataStr , "Description"));
		String orderId = getString(getValue(xmlDataStr , "OrderNo"));
		String status = getString(getValue(xmlDataStr , "OrderState"));
		String payTime = getString(getValue(xmlDataStr , "TransDatetime"));
		String settleDate = getString(getValue(xmlDataStr , "SettleDate"));
		String reqSign = getString(getValue(xmlDataStr , "MAC"));
		
		//本地配置的web商户号
		String localWebMerchantId = Constants.MERCHANTNO;
		//校验商户号对不对得上本地配置的商户号
		if(StringUtils.isEmpty(merchantId) || !merchantId.trim().equals(localWebMerchantId.trim())){
			log.info("### dealEcoPayNotify:web商户号校验失败!,merchOrderId:"+merchantId+",localWebMerchantId:"+localWebMerchantId+",allParamStr:"+xmlDataStr);
//			retMsgJson = "{\"RetCode\":\"E101\",\"RetMsg\":\"商户号校验失败!\"}";
			return srt;
		}
		Map paramMap = XMLConverUtil.xmlStr2Map(xmlDataStr);
		//本地生成的签名
		String newSign = getSign(paramMap);
		//验证订单结果通知的签名
		boolean verifyResult = false;
		if(newSign.trim().equals(reqSign.trim())){
			verifyResult = true;
		}
		log.info("###dealEcoPayNotify-verifyResult:"+verifyResult+",merchOrderId:"+merchOrderId);
		//服务端金额格式转换
		//double amountD = StrUtil._Double(amount); //单位:元
		//int money = (int)(amountD * 100); // 单位:分
		BigDecimal b1 = new BigDecimal(amount);
		BigDecimal b2 = new BigDecimal(100);
		int money = b1.multiply(b2).intValue();//转成分
		
		if (!verifyResult) {
			log.info("### dealEcoPayNotify:验证签名失败!,merchOrderId:"+merchOrderId+",allParamStr:"+xmlDataStr);
//			retMsgJson = "{\"RetCode\":\"E101\",\"RetMsg\":\"验证签名失败!\"}";
			return srt;
		}else{
			//保存记录(有记录忽略)
			EcoPayNotifyRecord ecoPayNotifyRecord = ecoPayNotifyRecordMapper.getObjectByOrderId(merchOrderId);
			if(ecoPayNotifyRecord != null){
				log.info("### dealEcoPayNotify:已存在步请求记录 , orderId = " + merchOrderId );
			}else{
				if("02".equals(status)){
					ecoPayNotifyRecord = getEcoPayNotifyRecord(version, merchantId, merchOrderId, amount, extData, orderId, status, payTime, settleDate, reqSign , xmlDataStr);
					if(ecoPayNotifyRecord == null){
						log.info("### dealEcoPayNotify:异步请求数据存在异常 ， orderId = " + merchOrderId);
					}else{
						ecoPayNotifyRecordMapper.insert(ecoPayNotifyRecord);
					}
				}else{
					log.info("###dealAliPayNotify:易联支付通知返回非已付款状态(02),不保存通知,status:"+status );
				}
			}
			//订单业务处理
			//获取本地服务器订单信息
			PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(merchOrderId);
			if(dbPayChargeOrder == null){//本地服务器不存在该订单
//				retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
				log.info("### dealEcoPayNotify-本地服务端订单不存在,merchOrderId:"+merchOrderId+",orderId:"+orderId);
				Exception e = new PayBizException(ErrorCode.ERROR_5012);
				log.error(e.getMessage() ,e);
				throw e;
			}else{
				//订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
				int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
				// 签名验证成功后，需要对订单进行后续处理
				if ("02".equals(status)) {
					// 订单已支付;
					// 1、检查Amount和商户系统的订单金额是否一致
					if(money == dbPayChargeOrder.getSelectMoney()){
						// 2、订单支付成功的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理）；
						// 3、返回响应内容
//						retMsgJson = "{\"RetCode\":\"0000\",\"RetMsg\":\"订单已支付\"}";
						log.info("###dealEcoPayNotify-addGold,orderId:"+merchOrderId);
						boolean isApplePay = false;
						boolean isPayBySanBox = false;
						int appleUserRealPayMoney = 0;
						String transactionId = orderId;
						chargeCommonService.updateChargeOrderAndGold(merchOrderId, money,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIp,transactionId,dev);
					}else{
						log.info("充值金额存在异常");
//						retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
					}
				} else {
					// 1、订单支付失败的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理，避免成功后又修改为失败）；
					// 2、返回响应内容
					log.info("###dealEcoPayNotify:订单支付失败!status="+status);
					String remark = "易联支付失败(Status应=02):,Status="+status;
					chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIp);
//					retMsgJson = "{\"RetCode\":\"E102\",\"RetMsg\":\"订单支付失败+"+status+"\"}";
				}
			}
		}
		log.info(String.format("###end-dealWebEcoPayNotify-xmlDataStr:%s,retMsgJson:%s",xmlDataStr,retMsgJson));
		return srt;
	}

	
	/**
	 * 设置订单对象
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月23日
	 */
	private PayChargeOrder getPco(DataRequest data){
		//生成订单号
		String orderId = StrUtil.getOrderId();
		PayChargeOrder pco = new PayChargeOrder();
		pco.setOrderId(orderId);
		if(data.getPayOrder().getMoney() != 0 ){
			pco.setMoney(0);
			pco.setSelectMoney(data.getPayOrder().getMoney());
			log.info("money:" + data.getPayOrder().getMoney());
		}else{
			log.info("money is null");
		}
		pco.setChargeTime(new Date());
		pco.setResultTime(new Date());
		pco.setSyncTime(new Date());
		if(data.getPayOrder().getPay_type() != 0){
			pco.setPay_type(data.getPayOrder().getPay_type());
			log.info("payType:"+data.getPayOrder().getPay_type());
		}else{
			log.info("payType is null");
		}
		if(data.getPayOrder().getPay_type() != 0){
			pco.setCreateType(data.getPayOrder().getClientType());
			log.info("clientType:"+data.getPayOrder().getClientType());
		}else{
			log.info("clientType is null");
		}
		pco.setGolds(0);
		pco.setOrderStatus(1);
		String channelId = null;
		/*if(data.getDeviceProperties()!=null){
			channelId = data.getDeviceProperties().getChannelId();
		}*/
		//代充时,金币接受者
		String receiverUserId = data.getPayOrder().getReceiverUserId();
		String agentUserId = null;
		String loginUserId = data.getUserBaseInfo().getUserId(); 
		//若果receiverUserId不为空,说明是给别人充值
		if(StringUtils.isNotEmpty(receiverUserId)){
			//当前登陆用户为代理人
			agentUserId = loginUserId;
		}else{
			//当前登录用户是给自己充值,金币接受者是自己
			receiverUserId = loginUserId;
		}
		pco.setUserId(receiverUserId);
		pco.setAgentUserId(agentUserId);
		//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
		if(data.getDeviceProperties()!=null){
			if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36)) {
				ServiceResult<String> asrt = appInstallChannelService.getChannelIdByImei(data.getDeviceProperties().getImei());
				if(asrt.isSucceed()) {
					channelId = asrt.getData();
				}
			}
			if(channelId==null) {
				channelId=data.getDeviceProperties().getChannelId();
			}
			// 2017-09-06，增加统计信息字段
			DeviceProperties dp = data.getDeviceProperties();
			String douId = "";
			if(null != channelId) {
				douId = channelId + "_";
			}
			if(dp!=null && null != dp.getPackageName()) {
				pco.setPkgName(dp.getPackageName());
				douId += dp.getPackageName();
			}
			log.error("myorder_douId=" + douId);
			if(!StringUtils.isEmpty(douId)) {
				pco.setDouId(douId);
			}
			UserInfo user = userInfoService.getUserInfoFromCache(receiverUserId);
			if(user != null) {
				int time = DateUntil.getTimeIntervalMinute(user.getAddTime(), new Date());
				int retentionVaild = time / 60 / 24;
				pco.setRetentionVaild(retentionVaild);
			}
		}
		pco.setChannelId(channelId);
		String generateOrderIp = HttpUtils.getUserReallyIp(data);
		String generateOrderAddr = ipStoreService.getAddressByIp(generateOrderIp);
		pco.setGenerateOrderIp(generateOrderIp);
		pco.setGenerateOrderAddr(generateOrderAddr);
		return pco;
	}
	
	private String getXmlStr(Map<String,Object> map){
		/*
		 * Version 2.1.0
		 *消息类型	ProcCode	n4	M	交易类型, 0200
		 *处理码ProcessCode	n6	M	处理码 190011:银联在线 190012:DNA在线
		 *交易金额	Amount	n12	M	交易金额, 例如：99.99，99.9, 99
		 *交易币种	Currency	a3	C	交易币种, CNY：人民币. 参照字段类型.
		 *异步返回地址	AsynAddress	ans(LLVAR)	C	
		 *商户订单编号	MerchantOrderNo	ans(LLVAR)	M 商户系统订单号
		 *订单描述	Description	ans(VAR)	M	编码：UTF-8, 暂不要超过50位
		 *商户号	MerchantNo	ans(LLVAR) 	M
		 *订单来源	OrderFrom	n2	M	“02”	商户OrderNo，网页自助下单（WEB）
										“03”	商户OrderNo，客服电话下单（CallCenter）
										“04”	商户OrderNo，电话自助下单（IVR）
										“05”	商户OrderNo，手机自助下单（WAP）
		 *系统跟踪号	AcqSsn	an(6)	M序号	域名
		 **/
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<x:NetworkRequest xmlns:x=\"http://www.payeco.com\" xmlns:xsi=\"http://www.w3.org\">\n\t")
			.append("<Version>2.1.0</Version>\n\t")
			.append("<ProcCode>0200</ProcCode>\n\t")
			.append("<ProcessCode>190011</ProcessCode>\n\t")
			.append("<Amount>"+map.get("Amount")+"</Amount>\n\t")
			.append("<Currency>CNY</Currency>\n\t")
			.append("<AsynAddress>"+map.get("AsynAddress")+"</AsynAddress>\n\t")
			.append("<MerchantOrderNo>"+map.get("MerchantOrderNo")+"</MerchantOrderNo>\n\t")
			.append("<Description>"+map.get("Description")+"</Description>\n\t")
			.append("<AcqSsn>"+map.get("AcqSsn")+"</AcqSsn>")
			.append("<MerchantNo>"+Constants.MERCHANTNO+"</MerchantNo>")
			.append("<OrderFrom>02</OrderFrom>")
			.append("<AccountNo></AccountNo><AccountType></AccountType><MobileNo></MobileNo><SynAddress>"+map.get("SynAddress")+"</SynAddress>")
			.append("<Remark></Remark><TerminalNo>"+ map.get("TerminalNo") +"</TerminalNo><OrderNo></OrderNo><Language>00</Language>")
			.append("<OrderType>00</OrderType><Reference>"+map.get("Reference")+"</Reference><TransDatetime>"+map.get("TransDatetime")+"</TransDatetime><MerchantName></MerchantName>")
			.append("<TransData></TransData><IDCardName></IDCardName><IDCardNo></IDCardNo><BankAddress></BankAddress><IDCardType>01</IDCardType><BeneficiaryName></BeneficiaryName>")
			.append("<BeneficiaryMobileNo>null</BeneficiaryMobileNo><DeliveryAddress>null</DeliveryAddress><IpAddress>127.0.0.1</IpAddress><Location></Location><UserFlag></UserFlag>");
		String sign = getSign(map);
		if( null != sign) sb.append("<MAC>"+sign+"</MAC>\n\t");
		sb.append("</x:NetworkRequest>\n");
		return sb.toString();
	}
	
	/**
	 * mac 加密
	 */
	private String getSign(Map<String, Object> map){
		StringBuffer sb = new StringBuffer();
		if( null != map){
			if( null != map.get("ProcCode") && map.get("ProcCode").toString().length() > 0 ){
				sb.append(map.get("ProcCode").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("AccountNo") && map.get("AccountNo").toString().length() > 0 ){
				sb.append(map.get("AccountNo").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("ProcessCode") && map.get("ProcessCode").toString().length() > 0 ){
				sb.append(map.get("ProcessCode").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("Amount") && map.get("Amount").toString().length() > 0 ){
				sb.append(map.get("Amount").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("TransDatetime") && map.get("TransDatetime").toString().length() > 0 ){
				sb.append(map.get("TransDatetime").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("AcqSsn") && map.get("AcqSsn").toString().length() > 0 ){
				sb.append(map.get("AcqSsn").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("OrderNo") && map.get("OrderNo").toString().length() > 0 ){
				sb.append(map.get("OrderNo").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("TransData") && map.get("TransData").toString().length() > 0 ){
				sb.append(map.get("TransData").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("Reference") && map.get("Reference").toString().length() > 0 ){
				sb.append(map.get("Reference").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("RespCode") && map.get("RespCode").toString().length() > 0 ){
				sb.append(map.get("RespCode").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("TerminalNo") && map.get("TerminalNo").toString().length() > 0 ){
				sb.append(map.get("TerminalNo").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("MerchantNo") && map.get("MerchantNo").toString().length() > 0 ){
				sb.append(map.get("MerchantNo").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("MerchantOrderNo") && map.get("MerchantOrderNo").toString().length() > 0 ){
				sb.append(map.get("MerchantOrderNo").toString().trim().toUpperCase()+" ");
			}
			if( null != map.get("OrderState") && map.get("OrderState").toString().length() > 0 ){
				sb.append(map.get("OrderState").toString().trim().toUpperCase()+" ");
			}
			sb.append(Constants.MD5_KEY);
			log.info("mac:" + sb.toString());
			return MD5Util.md5(sb.toString()).toUpperCase();
		}
		return null;
	}
	
	private static EcoPayNotifyRecord  getEcoPayNotifyRecord(String version, String merchantId,
			String merchOrderId, String amount, String extData, String orderId,
			String status, String payTime, String settleDate, String sign,String allParamStr){
		EcoPayNotifyRecord ecoPayNotifyRecord = new EcoPayNotifyRecord();
		ecoPayNotifyRecord.setRecordDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if(StrUtil.isNullOrEmpty(version) ||StrUtil.isNullOrEmpty(merchantId) ||StrUtil.isNullOrEmpty(merchOrderId)
				||StrUtil.isNullOrEmpty(amount)||StrUtil.isNullOrEmpty(orderId)
				||StrUtil.isNullOrEmpty(status)||StrUtil.isNullOrEmpty(payTime)||StrUtil.isNullOrEmpty(settleDate)
				||StrUtil.isNullOrEmpty(sign)){
			return null;
		}
		//double amountD = StrUtil._Double(amount);
		//int money = (int)(amountD * 100);
		
		BigDecimal b1 = new BigDecimal(amount);
		BigDecimal b2 = new BigDecimal(100);
		int money = b1.multiply(b2).intValue();//转成分
		
		ecoPayNotifyRecord.setVersion(version);
		ecoPayNotifyRecord.setMerchantId(merchantId);
		ecoPayNotifyRecord.setMerchOrderId(merchOrderId);
		ecoPayNotifyRecord.setAmount(money);
		ecoPayNotifyRecord.setExtData(extData);
		ecoPayNotifyRecord.setOrderId(orderId);
		ecoPayNotifyRecord.setStatus(status);
		ecoPayNotifyRecord.setPayTime(payTime);
		ecoPayNotifyRecord.setSettleDate(settleDate);
		ecoPayNotifyRecord.setSign(sign);
		ecoPayNotifyRecord.setNotifyAllParamStr(allParamStr);
		return ecoPayNotifyRecord;
	}
	
	private String getValue(String xml, String name){
		if(xml==null || "".equals(xml.trim()) 
				|| name == null || "".equals(name.trim())){
			return "";
		}
		String tag = "<" + name + ">";
		String endTag = "</" + name + ">";
		if(!xml.contains(tag) || !xml.contains(endTag)){
			return "";
		}
		String value = xml.substring(xml.indexOf(tag) + tag.length(), xml.indexOf(endTag));
		if(value != null && !"".equals(value)){
			return value;
		}
		return "";
	}
	
	private String getString(String src) {
	    return (isNullOrEmpty(src) ? "" : ( src.trim()));
	}
	
	private boolean isNullOrEmpty(String src){
		return src == null || "".equals(src.trim());
	}
	
}
