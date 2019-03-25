package com.jiujun.shows.pay.biz;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.MD5Util;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.common.utils.StrUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.AliPayNotifyRecordMapper;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.domain.AliPayNotifyRecord;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IChargeCommonService;
import com.jiujun.shows.pay.utils.AlipayConfig;
import com.jiujun.shows.pay.utils.RSA;
import com.jiujun.shows.pay.vo.Alipay;
import com.jiujun.shows.pay.vo.DataRequest;
import com.jiujun.shows.pay.vo.PayOrder;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiun.shows.appclient.service.IAppInstallChannelService;

/**
 * 阿里支付业务处理
 * @author shao.xiang
 * @date 2017年8月23日
 *
 */
@Service("aliPayBiz")
public class AliPayBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private AliPayNotifyRecordMapper aliPayNotifyRecordMapper;
	
	@Resource
	private AlipayNotifyBiz alipayNotifyBiz;
	
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
	
	private static String CHARGE_SUBJECT = "蜜桃直播金币充值";
	
	/**
	 * 创建订单
	 * @param data
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月23日
	 */
	public ServiceResult<JSONObject> createOrder(DataRequest data) throws Exception {
		JSONObject json = new JSONObject();
		PayOrder payOrder = new PayOrder();
		Alipay alipay = new Alipay();
		String remark ="";
		if(null != data.getPayOrder()) {
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
			if(data.getPayOrder().getRemark()!= null){
				try {
					remark = URLDecoder.decode(data.getPayOrder().getRemark(), "utf-8");
					log.info(remark);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
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
			
			//pco.setRemark(remark);
			pco.setRemark("支付宝充值");
			payChargeOrderMapper.insert(pco);
			payOrder.setOrderId(pco.getOrderId());
			payOrder.setMoney(data.getPayOrder().getMoney()/100);// 分
			payOrder.setRemark(pco.getRemark());
			// 1-支付宝
			if(pco.getPay_type() == 1) {
				/**   设置订单签名   */
				String key = MD5Util.md5(AlipayConfig.SELLER_EMAIL);
				StringBuffer orderInfo = new StringBuffer();
				orderInfo.append("")
				.append("partner=").append("\"").append(AlipayConfig.PARTNER_ID).append("\"")
				.append("&seller_id=").append("\"").append(AlipayConfig.SELLER_EMAIL).append("\"")
//				"&seller_id=" + "\"" + AlipayConfig.PARTNER_ID + "\""
				.append("&out_trade_no=").append("\"").append(pco.getOrderId()).append("\"")
				.append("&subject=").append("\"").append(CHARGE_SUBJECT).append("\"")
				.append("&body=").append("\"").append(remark).append("\"")
				.append("&total_fee=").append("\"").append(data.getPayOrder().getMoney()/100f).append("\"")
				.append("&notify_url=").append("\"").append(AlipayConfig.NOTIFY_URL).append("\"")
				.append("&service=\"mobile.securitypay.pay\"")
//				"&service=\"alipay.wap.create.direct.pay.by.user\""
				.append("&payment_type=\"1\"")
				.append("&_input_charset=\"utf-8\"")
				.append("&it_b_pay=\"30m\"");
				String sign = RSA.sign(orderInfo.toString() , AlipayConfig.RSA_PRIVATE , "utf-8");		
				log.info("alipay sign orderInfo=" + orderInfo + " RSA=" + sign);		
				alipay.setPartnerId(AlipayConfig.PARTNER_ID);
				alipay.setSellerEmail(AlipayConfig.SELLER_EMAIL);
				alipay.setBody(remark);
				alipay.setSubject(CHARGE_SUBJECT);
				alipay.setNotifyUrl(AlipayConfig.NOTIFY_URL);
				alipay.setSign(sign);
				json.put(alipay.getShortName(), alipay.buildJson());
				log.info("支付方式为支付宝");
			}else{
				log.info("支付方式不正确");
			}
		} else {
			PayBizException e = new PayBizException(ErrorCode.ERROR_5000);
			log.error(e.getMessage(),e);
			throw e;
		}
		json.put(payOrder.getShortName(), payOrder.buildJson());
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(true);
		srt.setData(json);
		return srt;
	}
	
	/**
	 * 处理支付成功后业务
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月30日
	 */
	public ServiceResult<Boolean> dealPaySuccessNotify(Map verifyMap,
			String notifyFromIP, DeviceProperties dev) throws Exception {
		if(verifyMap == null){
			Exception e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(), e);
			throw e;
		}
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		//验证签名及notify_id合法性校验
		//boolean verifyResult = AlipayNotify.verify(verifyMap);
		boolean verifyResult = alipayNotifyBiz.verify(verifyMap);
		log.info("###dealAliPayNotify-verifyResult:"+JsonUtil.beanToJsonString(verifyMap));
		if(verifyResult){ //验证成功
			 //  out_trade_no 商户订单号
			String out_trade_no = verifyMap.get("out_trade_no").toString();
			 //  trade_no 支付宝交易号
			String trade_no = verifyMap.get("trade_no").toString();
			//  trade_status 交易状态
			String trade_status= verifyMap.get("trade_status").toString(); 
			//  total_fee 交易金额(单位:分)
			String total_fee = "";
			if(verifyMap.get("total_fee")!=null)
				total_fee=verifyMap.get("total_fee").toString();
			else if (verifyMap.get("total_amount")!=null)
				total_fee=verifyMap.get("total_amount").toString();
			//Double amountD1 = StrUtil._Double(total_fee);//支付宝返回的total_fee的单位是元
			//amountD1 = amountD1 * 100 ;
			//int money = amountD1.intValue();//转成分
			BigDecimal b1 = new BigDecimal(total_fee);
			BigDecimal b2 = new BigDecimal(100);
			int money = b1.multiply(b2).intValue();//转成分
			
			String notifySellerId = verifyMap.get("seller_id").toString();
			String serverSelleerId =  AlipayConfig.PARTNER_ID;
			//判断请求时的seller_id与通知时获取的seller_id为一致的
			if(notifySellerId==null||!notifySellerId.equals(serverSelleerId)){
				log.info("###dealAliPayNotify-seller_id验证不通过,serverSelleerId:"+serverSelleerId+"notifySellerId:"+notifySellerId);
				Exception e = new PayBizException(ErrorCode.ERROR_5015);
				log.error(e.getMessage() ,e);
				throw e;
			}
			
			log.info(String.format("###begin-dealAliPayNotify,out_trade_no:%s,trade_no:%s,trade_status:%s,money:%s,verifyMap:%s", out_trade_no,trade_no,trade_status,money,JsonUtil.beanToJsonString(verifyMap))) ;
			// (记录通知(已有则忽略))
			AliPayNotifyRecord  aliPayNotifyRecord = (AliPayNotifyRecord)aliPayNotifyRecordMapper.getObjectByOrderId(out_trade_no);
			if(aliPayNotifyRecord != null){
				log.info("###dealAliPayNotify:异步回调请求记录已经存在,out_trade_no:"+out_trade_no );
			}else{
				if((trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS"))){
					AliPayNotifyRecord newAliPayNotifyRecord = getAliPayNotifyRecord(out_trade_no , trade_no , trade_status , money , JsonUtil.beanToJsonString(verifyMap));
					if(newAliPayNotifyRecord != null){
						log.info("###dealAliPayNotify,getAliPayNotifyRecord,newAliPayNotifyRecord != null,insert begin... ");
						aliPayNotifyRecordMapper.insert(newAliPayNotifyRecord);
						log.info("###dealAliPayNotify:异步回调请求记录保存成功,out_trade_no:"+out_trade_no  );
					}else{
						log.info("###dealAliPayNotify:异步回调请求数据出现异常,out_trade_no:"+out_trade_no  );
					}
				}else{
					log.info("###dealAliPayNotify:支付宝通知返回非已付款状态(TRADE_FINISHED或TRADE_SUCCESS),不保存通知,trade_status:"+trade_status );
				}
				
			}
			
			String orderId = out_trade_no;//订单号
			//请在这里加上商户的业务逻辑程序代码
			PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId); 
			if(dbPayChargeOrder==null){
				log.info("###dealAliPayNotify-本地服务端订单不存在,out_trade_no:"+out_trade_no+",trade_no:"+trade_no);
				Exception e = new PayBizException(ErrorCode.ERROR_5012);
				log.error(e.getMessage() ,e);
				throw e;
			}else{
				log.info("###dealAliPayNotify-out_trade_no:"+out_trade_no+",trade_status:"+trade_status);
				//订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
				int dbPayChargeOrderStatus = dbPayChargeOrder.getOrderStatus();
				if(dbPayChargeOrderStatus!=4){//不等于4,说明本地订单未成功处理过
					if(trade_status.equals("TRADE_FINISHED")){
						//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						log.info("###alipay-doChargeSucessBusiness,orderId:"+orderId+",money:"+money+",trade_status:"+trade_status);
						boolean isApplePay = false;
						boolean isPayBySanBox = false;
						int appleUserRealPayMoney = 0;
						String transactionId = verifyMap.get("trade_no").toString();
						chargeCommonService.updateChargeOrderAndGold(orderId, money,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIP,transactionId,dev);
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						//注意：
						//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					} else if (trade_status.equals("TRADE_SUCCESS")){
						log.info("###alipay-doChargeSucessBusiness,orderId:"+orderId+",money:"+money+",trade_status:"+trade_status);
						boolean isApplePay = false;
						boolean isPayBySanBox = false;
						int appleUserRealPayMoney = 0;
						String transactionId = verifyMap.get("trade_no").toString();
						chargeCommonService.updateChargeOrderAndGold(orderId, money,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIP,transactionId,dev);
					}else{
						log.info("###dealAliPayNotify-返回的订单状态为非完成,out_trade_no:"+out_trade_no+",trade_no:"+trade_no+",trade_status:"+trade_status);
						String remark="支付宝支付失败:trade_status="+trade_status;
						chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIP);
						Exception e = new PayBizException(ErrorCode.ERROR_5014,trade_status);
						log.info(e.getMessage() ,e);
						throw e;
					}
				}else{ //状态等于4,说明已成功处理
					log.info("###dealAliPayNotify-订单已处理过,out_trade_no:"+out_trade_no+",trade_no:"+trade_no);
					srt.setSucceed(true);
					return srt;
				}
			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			log.info(String.format("###end-dealAliPayNotify,out_trade_no:%s,trade_no:%s,trade_status:%s,money:%s,verifyMap:%s", out_trade_no,trade_no,trade_status,money,JsonUtil.beanToJsonString(verifyMap))) ;
				
		}else{//验证失败
			log.info("###dealAliPayNotify-验证不通过,verifyMap:"+JsonUtil.beanToJsonString(verifyMap));
			Exception e = new PayBizException(ErrorCode.ERROR_5015);
			log.error(e.getMessage() ,e);
			throw e;
		}
		srt.setSucceed(true);
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
		pco.setGolds(0);
		pco.setOrderStatus(1);
		String channelId = null;
		/*if(data.getDeviceProperties()!=null){
			channelId = data.getDeviceProperties().getChannelId();
		}*/
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
	
	/**
	 * 设置异步请求记录实体
	 * */
	private AliPayNotifyRecord getAliPayNotifyRecord(String out_trade_no,
			String trade_no, String trade_status, int money, String verifyMapStr) {
		AliPayNotifyRecord aliPayNotifyRecord = new AliPayNotifyRecord();
		if(StrUtil.isNullOrEmpty(out_trade_no) || StrUtil.isNullOrEmpty(trade_no) ||
				 StrUtil.isNullOrEmpty(trade_status)||money<=0){
			return null;
		}
		aliPayNotifyRecord.setRecordDateTime(DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()));
		aliPayNotifyRecord.setOutTradeNo(out_trade_no);
		aliPayNotifyRecord.setTotalFee(money);
		aliPayNotifyRecord.setTradeNo(trade_no);
		aliPayNotifyRecord.setTradeStatus(trade_status);
		aliPayNotifyRecord.setVerifyMap(verifyMapStr);
		aliPayNotifyRecord.setReturnCode("SUCCESS");
		return aliPayNotifyRecord;
	}

}
