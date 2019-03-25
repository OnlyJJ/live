package com.lm.live.pay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.service.IUserAccountService;
import com.lm.live.appclient.enums.AppType;
import com.lm.live.base.domain.ThirdpartyConf;
import com.lm.live.base.enums.ThirdpartyType;
import com.lm.live.base.service.IIpStoreService;
import com.lm.live.base.service.IThirdpartyConfService;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RandomStringGenerator;
import com.lm.live.common.utils.XMLConverUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.constant.Constants;
import com.lm.live.pay.dao.PayChargeOrderMapper;
import com.lm.live.pay.dao.WechatPayNotifyMapper;
import com.lm.live.pay.dao.WechatPayUnifiedorderDoMapper;
import com.lm.live.pay.domain.PayChargeOrder;
import com.lm.live.pay.domain.WechatPayNotifyDo;
import com.lm.live.pay.domain.WechatPayUnifiedorderDo;
import com.lm.live.pay.enums.ErrorCode;
import com.lm.live.pay.enums.PayTypeEnum;
import com.lm.live.pay.enums.TradeTypeEnum;
import com.lm.live.pay.enums.WechatPayBusinessEnum.TradeType;
import com.lm.live.pay.exception.PayBizException;
import com.lm.live.pay.service.IChargeCommonService;
import com.lm.live.pay.service.IWeChatPayService;
import com.lm.live.pay.utils.MchPayNotify;
import com.lm.live.pay.utils.Signature;
import com.lm.live.pay.vo.Unifiedorder;
import com.lm.live.pay.vo.UnifiedorderResult;
import com.lm.live.pay.vo.WechatJSAPIVo;
import com.lm.live.pay.vo.WechatPayVo;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.service.IUserInfoService;

public class WeChatPayServiceImpl implements IWeChatPayService {

	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private WechatPayUnifiedorderDoMapper wechatPayUnifiedorderDoMapper;
	
	@Resource
	private WechatPayNotifyMapper wechatPayNotifyMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IThirdpartyConfService thirdpartyConfService;
	
	@Resource
	private IChargeCommonService chargeCommonService;
	
	@Override
	public ServiceResult<WechatPayVo> payUnifiedorder(String receiverUserId,
			int total_free, String spbill_create_ip, int clientType,
			String channelId, String agentUserId, String tradeType,
			DeviceProperties deviceProperties) throws Exception {
		LogUtil.log.info(String.format("###begin-payUnifiedorder,receiverUserId:%s,total_free:%s,spbill_create_ip：%s,tradeType:%s,deviceProperties:%s",receiverUserId,total_free, spbill_create_ip,tradeType, JsonUtil.beanToJsonString(deviceProperties))) ;
		ServiceResult<WechatPayVo> srt = new ServiceResult<WechatPayVo>();
		srt.setSucceed(false);
		//校验参数
		if(StringUtils.isEmpty(receiverUserId) || total_free <= 0){
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		
		if(!StringUtils.isEmpty(agentUserId)){
			UserAccount userAccount = userAccountService.getObjectByUserId(receiverUserId);
			// 为空说明被充账户不存在
			if(userAccount == null){
				throw new PayBizException(ErrorCode.ERROR_5020);
			}
		}
		
		//选择充值金额(单位:分)
		if(!Constants.OFFICIAL_USER.contains(receiverUserId)){
			if(total_free < Constants.LEAST_MONEY){ //限制最低充值5元
				throw new PayBizException(ErrorCode.ERROR_5014);
			}
		}
		
		UnifiedorderResult unifiedorderResult = null;
		WechatPayVo wechatPayVo = new WechatPayVo ();
		//微信支付统一下单请求地址
		//微信分配的公众账号ID（企业号corpid即为此appId）
		String appid = getWechaAppPayAppid(tradeType, deviceProperties);
		//微信支付分配的商户号
		String mch_id = getWechatPayMchid(tradeType, deviceProperties);
		//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		String key = getWechatPayKey(tradeType, deviceProperties);
		//随机字符串，不长于32位。推荐随机数生成算法
		String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
		String body = "蜜桃直播微信充值";
		String detail = "金额:"+total_free+"分";
		//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		String out_trade_no = DateUntil.getNowYyyyMMddHHmmss()+RandomStringGenerator.getRandomStringByLength(10);
		Unifiedorder unifiedorderXmlObj = new Unifiedorder();
		unifiedorderXmlObj.setAppid(appid);
		unifiedorderXmlObj.setMch_id(mch_id);
		unifiedorderXmlObj.setNonce_str(nonce_str);
		unifiedorderXmlObj.setBody(body);
		unifiedorderXmlObj.setDetail(detail);
		unifiedorderXmlObj.setOut_trade_no(out_trade_no);
		unifiedorderXmlObj.setTotal_fee(total_free);
		
		unifiedorderXmlObj.setSpbill_create_ip(spbill_create_ip);
		
		
		// trade_type,默认取值app支付
		String trade_type = TradeType.App.getValue();
		//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。默认取值app支付对应通知的地址
		String notifyUrl = Constants.SERVICEURL + "/wechat/pay/notifyCallBack";
		
		// 判断扫描支付
		if(tradeType.equals(PayTypeEnum.NATIVE.getValue())){
			// 用本地商户号作为商品id,trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
			String product_id = out_trade_no;
			// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义
			unifiedorderXmlObj.setProduct_id(product_id);
			/*// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
			unifiedorderXmlObj.setDevice_info("WEB");
			*/
			trade_type = TradeType.NATIVE.getValue();
			//扫码支付, 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
			notifyUrl = Constants.SERVICEURL+"/wechat/pay/notifyCallBack/native";
		}
		unifiedorderXmlObj.setTrade_type(trade_type);
		unifiedorderXmlObj.setNotify_url(notifyUrl);
		LogUtil.log.info(String.format("###wechatPayNotifyUrl:%s",notifyUrl));
		//生成微信支付签名
		String sign = Signature.getSign(unifiedorderXmlObj, key);
		unifiedorderXmlObj.setSign(sign);
		LogUtil.log.info("#####payUnifiedorder-unifiedorderXmlObj:"+JsonUtil.beanToJsonString(unifiedorderXmlObj));
		//转换成xml字符串
		String unifiedorderRequestXmlStr = XMLConverUtil.convertToXml(unifiedorderXmlObj);
		LogUtil.log.info("#####payUnifiedorder-unifiedorderRequestXmlStr:"+unifiedorderRequestXmlStr);
		//调用微信支付统一下单api
		String unifiedorderResponseXmlStr = HttpUtils.post(Constants.ORDER_URL, unifiedorderRequestXmlStr,"utf-8");
		LogUtil.log.info("unifiedorderResponseXmlStr:"+unifiedorderResponseXmlStr);
		unifiedorderResult = XMLConverUtil.converyToJavaBean(unifiedorderResponseXmlStr, UnifiedorderResult.class);
		LogUtil.log.info("###unifiedorderResult:"+JsonUtil.beanToJsonString(unifiedorderResult));
		
		if(unifiedorderResult==null||!"SUCCESS".equals(unifiedorderResult.getReturn_code())){
			LogUtil.log.error("####微信支付预下单返回数据:"+unifiedorderResponseXmlStr);
			throw new PayBizException(ErrorCode.ERROR_5003);
		}
		
		// 校验微信返回的签名,商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
		if(!validateWechatpayUnifiedorderSign(unifiedorderResult, key)){
			throw new PayBizException(ErrorCode.ERROR_5004);
		}
		
		// 本地配置的商户号
		String localMchid = getWechatPayMchid(tradeType,deviceProperties);
		//校验是否与本地的商户号一致
		if(unifiedorderResult == null || !localMchid.equals(unifiedorderResult.getMch_id())){
			throw new PayBizException(ErrorCode.ERROR_5016);
		}
		
		// 当字段在return_code 和result_code都为SUCCESS的时候
		if(unifiedorderResult!=null
				&&"SUCCESS".equals(unifiedorderResult.getReturn_code())
				&&"SUCCESS".equals(unifiedorderResult.getResult_code())){
			// 插入充值记录
			int payType = TradeTypeEnum.Wechat.getValue();
			createPayChargeOrderForWechatPay(receiverUserId,payType,total_free,clientType,out_trade_no,channelId,agentUserId,spbill_create_ip,deviceProperties);
			
			// 保存统一下单记录(插表 t_wechat_pay_unifiedorder)
			createWechatPayUnifiedorder(receiverUserId, total_free ,unifiedorderResult,
					unifiedorderResponseXmlStr,out_trade_no,spbill_create_ip);
			
			//生成wechatPayVo，返回给app发起调用"调起支付"接口
			wechatPayVo = generateWechatPayVo(total_free, unifiedorderResult,
					out_trade_no, key,tradeType);
		}else{
			LogUtil.log.error(unifiedorderResponseXmlStr);
			LogUtil.log.error(JsonUtil.beanToJsonString(unifiedorderResult));
			throw new PayBizException(ErrorCode.ERROR_5003);
		}
		LogUtil.log.info(String.format("###end-payUnifiedorder,receiverUserId:%s,total_free:%s,spbill_create_ip：%s,tradeType:%s,deviceProperties:%s",receiverUserId,total_free, spbill_create_ip,tradeType,JsonUtil.beanToJsonString(deviceProperties))) ;
		srt.setSucceed(true);
		srt.setData(wechatPayVo);
		return srt;
	}

	@Override
	public ServiceResult<Boolean> dealWechatPayNotify(String xmlData,
			TradeType tradeType, String notifyFromIp,
			DeviceProperties deviceProperties) throws Exception {
		LogUtil.log.info(String.format("#####begin-dealWechatPayNotify,notify-xmlData:%s,tradeType:%s,notifyFromIp:%s,deviceProperties:%s",xmlData,tradeType.getValue(),notifyFromIp,JsonUtil.beanToJsonString(deviceProperties)));
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(xmlData)){
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		
		//转换数据对象
		MchPayNotify payNotify = XMLConverUtil.converyToJavaBean(xmlData,MchPayNotify.class);
		LogUtil.log.info("###payNotify:"+JsonUtil.beanToJsonString(payNotify));

		//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		String key = getWechatPayKey(tradeType.getValue(),payNotify.getMch_id());
		
		if(!validateWechatPayNotifySign(key,xmlData)){
			throw new PayBizException(ErrorCode.ERROR_5004);
		}
		
		String orderId = payNotify.getOut_trade_no();
		if("SUCCESS".equals(payNotify.getReturn_code())&&"SUCCESS".equals(payNotify.getResult_code())){
			//在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱(已在web层控制)
				WechatPayNotifyDo wechatPayNotifyDo = wechatPayNotifyMapper.getByOutTradeNo(orderId);
				if(wechatPayNotifyDo!=null){
					LogUtil.log.info("#####wechatCharge-have save wechatPayNotifyDo for OutTradeNo:"+orderId); 
				}else{
					//没插入过,则保存支付通知(插表t_wechat_pay_notify)
					createWechatPayNotify(xmlData, payNotify);
				}
				
				PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId);
				//先判断服务器是否已生成有相应订单号的"充值记录"
				if(dbPayChargeOrder == null){
					throw new PayBizException(ErrorCode.ERROR_5001);
				}
				int realChargeMoney = payNotify.getTotal_fee(); //单位:分
				boolean isPayBySanBox = false;
				boolean isApplePay = false;
				int appleUserRealPayMoney = 0;
				String transactionId = payNotify.getTransaction_id();
				synchronized(UserAccount.class) {
					chargeCommonService.updateChargeOrderAndGold(orderId, realChargeMoney,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIp,transactionId,deviceProperties);
				}

		}else{
			LogUtil.log.error(xmlData);
			LogUtil.log.error(JsonUtil.beanToJsonString(payNotify));
			String remark = "微信支付失败:,Return_code="+payNotify.getReturn_code()+",Result_code:"+payNotify.getResult_code();
			chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIp);
			throw new PayBizException(ErrorCode.ERROR_5001);
		
		}
		LogUtil.log.info(String.format("#####end-dealWechatPayNotify,notify-xmlData:%s,tradeType:%s,notifyFromIp:%s,deviceProperties:%s",xmlData,tradeType.getValue(),notifyFromIp,JsonUtil.beanToJsonString(deviceProperties)));
		srt.setSucceed(true);
		return srt;
	}

	@Override
	public ServiceResult<WechatJSAPIVo> handlePayForWechatJsapi(String openid,
			String receiverUserId, int buyGold, String spbill_create_ip,
			int clientType, String channelId, String tradeType,
			DeviceProperties dev) throws Exception {
		if(StringUtils.isEmpty(receiverUserId)) {
			throw new PayBizException(ErrorCode.ERROR_101);
		}
		ServiceResult<WechatJSAPIVo> ret = new ServiceResult<WechatJSAPIVo>();
		ret.setSucceed(false);
		// 校验充值账户是否存在
		UserAccount userAccount = userAccountService.getObjectByUserId(receiverUserId);
		if(userAccount == null){
			throw new PayBizException(ErrorCode.ERROR_5020);
		}
		
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(dev != null){
			packageName = dev.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = AppType.H5.getValue();
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			throw new PayBizException(ErrorCode.ERROR_5021);
		}
		if(thirdpartyConf.getInUsePayCreateOrder() != Constants.STATUS_1){
			throw new PayBizException(ErrorCode.ERROR_5001);
		}
		String appid = thirdpartyConf.getAppId();
		String mch_id = thirdpartyConf.getPayMchId();
		String key = thirdpartyConf.getPayKey();
		synchronized(WechatJSAPIVo.class) {
			WechatJSAPIVo retVo = new WechatJSAPIVo();
			int total_fee = buyGold / 10; // 转化为分
			
			UnifiedorderResult unifiedorderResult = null;
			//微信支付统一下单请求地址
			String reqUrl = Constants.ORDER_URL;
			//随机字符串，不长于32位。推荐随机数生成算法
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			String body = Constants.CHARGE_SUBJECT;
			String detail = "金额:"+ total_fee +"分";
			LogUtil.log.error("###handlePayForWechatJsapi-appid=" + appid + ",mch_id=" + mch_id + ",key=" + key + ",nonce_str="+ nonce_str + ",detail="+detail);
			//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
			String out_trade_no = DateUntil.getNowYyyyMMddHHmmss()+RandomStringGenerator.getRandomStringByLength(10);
			Unifiedorder unifiedorderXmlObj = new Unifiedorder();
			if(!StringUtils.isEmpty(openid)) {
				unifiedorderXmlObj.setOpenid(openid);
			}
			unifiedorderXmlObj.setAppid(appid);
			unifiedorderXmlObj.setMch_id(mch_id);
			unifiedorderXmlObj.setNonce_str(nonce_str);
			unifiedorderXmlObj.setBody(body);
			unifiedorderXmlObj.setDetail(detail);
			unifiedorderXmlObj.setOut_trade_no(out_trade_no);
			unifiedorderXmlObj.setTotal_fee(total_fee);
			unifiedorderXmlObj.setSpbill_create_ip(spbill_create_ip);
			
			String trade_type = tradeType;
			//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。默认取值app支付对应通知的地址
			String notifyUrl = Constants.SERVICEURL+"/wechat/pay/jsapi/notifyCallBack";
			
			unifiedorderXmlObj.setTrade_type(trade_type);
			unifiedorderXmlObj.setNotify_url(notifyUrl);
			//生成微信支付签名
			String sign = Signature.getSign(unifiedorderXmlObj, key);
			unifiedorderXmlObj.setSign(sign);
			LogUtil.log.info("#####handlePayForWechatJsapi-unifiedorderXmlObj:"+JsonUtil.beanToJsonString(unifiedorderXmlObj));
			//转换成xml字符串
			String unifiedorderRequestXmlStr = XMLConverUtil.convertToXml(unifiedorderXmlObj);
			LogUtil.log.info("#####handlePayForWechatJsapi-unifiedorderRequestXmlStr:"+unifiedorderRequestXmlStr);
			//调用微信支付统一下单api
			String unifiedorderResponseXmlStr = HttpUtils.post(reqUrl, unifiedorderRequestXmlStr,"utf-8");
			LogUtil.log.info("###handlePayForWechatJsapi-unifiedorderResponseXmlStr:"+unifiedorderResponseXmlStr);
			unifiedorderResult = XMLConverUtil.converyToJavaBean(unifiedorderResponseXmlStr, UnifiedorderResult.class);
			LogUtil.log.info("###handlePayForWechatJsapi-unifiedorderResult:"+JsonUtil.beanToJsonString(unifiedorderResult));
			
			if(unifiedorderResult == null || !"SUCCESS".equals(unifiedorderResult.getReturn_code())){
				LogUtil.log.error("####handlePayForWechatJsapi-微信支付预下单返回数据:"+unifiedorderResponseXmlStr);
				throw new PayBizException(ErrorCode.ERROR_5003);
			}
			
			// 校验微信返回的签名,商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
			if(!validateWechatpayUnifiedorderSign(unifiedorderResult, key)){
				throw new PayBizException(ErrorCode.ERROR_5003);
			}
			
			//校验是否与本地的商户号一致
			if(unifiedorderResult == null || !mch_id.equals(unifiedorderResult.getMch_id())){
				throw new PayBizException(ErrorCode.ERROR_5004);
			}
			String package_ = "";
			if(unifiedorderResult != null
					&& "SUCCESS".equals(unifiedorderResult.getReturn_code())
					&& "SUCCESS".equals(unifiedorderResult.getResult_code())){
				// return和result都为success才返回preapay_id
				String prepay_id = unifiedorderResult.getPrepay_id();
				package_ = prepay_id;
				// 插入充值记录
				createPayChargeOrderForWechatPay(receiverUserId,TradeTypeEnum.Wechat.getValue(),total_fee,clientType,out_trade_no,channelId,null,spbill_create_ip,dev);
				// 保存统一下单记录(插表 t_wechat_pay_unifiedorder)
				createWechatPayUnifiedorder(receiverUserId, total_fee ,unifiedorderResult,
						unifiedorderResponseXmlStr,out_trade_no,spbill_create_ip);
				// 生成参数vo，返回给H5
				retVo = createWechatJSAPIVo(appid, mch_id, total_fee, nonce_str, out_trade_no, key, prepay_id);
				if(trade_type.equals(TradeType.MWEB.getValue())) {
					retVo.setMweburl(unifiedorderResult.getMweb_url());
					retVo.setPackage_(prepay_id);
				} 
			} else {
				throw new PayBizException(ErrorCode.ERROR_5003);
			}
			if(StringUtils.isEmpty(package_)) {
				throw new PayBizException(ErrorCode.ERROR_5004);
			}
		}
		return ret;
	}

	@Override
	public ServiceResult<Boolean> handleWechatJsapiNotify(String xmlData,
			String notifyFromIp) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int convertClientType(String tradeType) {
		// 默认取通用的type
		int clientTypeInt = AppType.WEB.getValue();;
		if(TradeType.App.getValue().equalsIgnoreCase(tradeType)){
			clientTypeInt = AppType.ANDROID.getValue();
		}
		return clientTypeInt;
	}
	
	/**
	 * 获取微信支付appid
	 * @return
	 */
	private String getWechaAppPayAppid(String tradeType,DeviceProperties deviceProperties) throws Exception{
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convertClientType(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			throw new PayBizException(ErrorCode.ERROR_5021);
		}
		if(thirdpartyConf.getInUsePayCreateOrder() != Constants.STATUS_1){
			throw new PayBizException(ErrorCode.ERROR_5001);
		}
		String appid_wechat_pay = thirdpartyConf.getAppId();
		return appid_wechat_pay ;
	}

	/**
	 * 微信支付分配的商户号
	 * @return
	 */
	private String getWechatPayMchid(String tradeType, DeviceProperties deviceProperties) throws Exception{
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convertClientType(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			throw new PayBizException(ErrorCode.ERROR_5021);
		}
		if(thirdpartyConf.getInUsePayCreateOrder() != Constants.STATUS_1){
			throw new PayBizException(ErrorCode.ERROR_5001);
		}
		String mch_id_wechat_pay = thirdpartyConf.getPayMchId();
		return mch_id_wechat_pay;
	}
	
	/**
	 * 获取微信支付密钥
	 * @return
	 */
	private String getWechatPayKey(String tradeType, DeviceProperties deviceProperties) throws Exception{
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convertClientType(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			throw new PayBizException(ErrorCode.ERROR_5021);
		}
		if(thirdpartyConf.getInUsePayCreateOrder() != Constants.STATUS_1){
			throw new PayBizException(ErrorCode.ERROR_5001);
		}
		String payKey = thirdpartyConf.getPayKey();
		return payKey ;
	}
	
	/**
	 * 获取微信支付密钥
	 * @return
	 */
	private String getWechatPayKey(String tradeType,String mchid) throws Exception{
		//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		/*String key = SpringContextListener.getContextProValue("key_wechat_pay", "tx4no0a8ax1c9yrhwdrov2xwupvjooke");
		if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.App.getValue())){
			key = SpringContextListener.getContextProValue("key_wechat_pay", "tx4no0a8ax1c9yrhwdrov2xwupvjooke");
		}else if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.NATIVE.getValue())){
			key = SpringContextListener.getContextProValue("key_wechat_pay_mp", "767deef133c932e38e84a970f7c25bdc");
		}*/
		
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();

		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convertClientType(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf1(thirdpartyType, mchid, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			throw new PayBizException(ErrorCode.ERROR_5021);
		}
		if(thirdpartyConf.getInUsePayCreateOrder() != Constants.STATUS_1){
			throw new PayBizException(ErrorCode.ERROR_5001);
		}
		String payKey = thirdpartyConf.getPayKey();
		return payKey ;
	}
	
	/**
	 * 校验微信支付统一下单返回结果的签名
	 * @param unifiedorderResult
	 * @param key
	 * @throws Exception
	 * @throws SystemDefinitionException
	 */
	private boolean validateWechatpayUnifiedorderSign(
			UnifiedorderResult unifiedorderResult, String key)throws Exception{
		boolean flag = true;
		UnifiedorderResult signUnifiedorderResult = unifiedorderResult;
		String wechatResponseSign = unifiedorderResult.getSign();
		signUnifiedorderResult.setSign(null);//
		LogUtil.log.info("signUnifiedorderResult:"+JsonUtil.beanToJsonString(signUnifiedorderResult));
		String serverSign = Signature.getSign(signUnifiedorderResult, key);
		//签名验证
		if(!wechatResponseSign.equals(serverSign)){
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 校验微信支付通知的签名
	 * @param key
	 * @param xmlData
	 * @return
	 * @throws Exception
	 */
	private boolean validateWechatPayNotifySign(String key, String xmlData) throws Exception{
		boolean flag = true;

        Map<String,Object> map = XMLConverUtil.xmlStr2Map(xmlData);
        String wechatReturnSign=(String)map.get("sign");
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign","");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String serverGenerateSign=Signature.getSign(map,key);
        
		//签名验证
		if(!serverGenerateSign.equals(wechatReturnSign)){
			flag = false;
		}
		return flag;
	}
	
	/**
	 *  创建充值记录(微信支付)
	 * @param userId
	 * @param total_free
	 * @param clientType
	 * @param orderId
	 * @param channelId
	 * @param agentUserId
	 */
	private void createPayChargeOrderForWechatPay(String userId,int payType,int total_free,int clientType ,String orderId,String channelId,String agentUserId,String clientIp,DeviceProperties dp) {
		PayChargeOrder o = new PayChargeOrder();
		o.setOrderId(orderId);
		o.setPay_type(payType);//3 :微信支付 
		o.setUserId(userId);
		o.setOrderStatus(1); //订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功
		o.setSelectMoney(total_free);//选择充值金额，单位：分
		o.setMoney(0);
		o.setGolds(0);
		o.setChargeTime(new Date());
		o.setRemark("微信充值金币");
		o.setCreateType(clientType);
		o.setChannelId(channelId);
		o.setAgentUserId(agentUserId);
		o.setGenerateOrderIp(clientIp);
		String generateOrderAddr = ipStoreService.getAddressByIp(clientIp);
		o.setGenerateOrderAddr(generateOrderAddr);
		
		UserInfoDo user = userInfoService.getUserInfoFromCache(userId);
		if(user != null) {
			int time = DateUntil.getTimeIntervalMinute(user.getAddTime(), new Date());
			int retentionVaild = time / 60 / 24;
			o.setRetentionVaild(retentionVaild);
		}
		payChargeOrderMapper.insert(o);
	}
	
	/**
	 *  保存统一下单记录
	 * @param userId
	 * @param totalFee
	 * @param unifiedorderResult
	 * @param unifiedorderResultXmlStr
	 */
	private void createWechatPayUnifiedorder(String userId,int totalFee,
			UnifiedorderResult unifiedorderResult,
			String unifiedorderResultXmlStr,String out_trade_no,String spbill_create_ip) {
		WechatPayUnifiedorderDo wechatPayUnifiedorderDo = new WechatPayUnifiedorderDo();
		wechatPayUnifiedorderDo.setUserId(userId);
		wechatPayUnifiedorderDo.setAppid(unifiedorderResult.getAppid());
		wechatPayUnifiedorderDo.setBody(unifiedorderResult.getBody());
		wechatPayUnifiedorderDo.setDetail(unifiedorderResult.getDetail());
		wechatPayUnifiedorderDo.setMchId(unifiedorderResult.getMch_id());
		wechatPayUnifiedorderDo.setNonceStr(unifiedorderResult.getNonce_str());
		wechatPayUnifiedorderDo.setNotifyUrl(unifiedorderResult.getNotify_url());
		wechatPayUnifiedorderDo.setOutTradeNo(out_trade_no);
		wechatPayUnifiedorderDo.setSign(unifiedorderResult.getSign());
		wechatPayUnifiedorderDo.setSpbillCreateIp(spbill_create_ip);
		wechatPayUnifiedorderDo.setTotalFee(totalFee);
		wechatPayUnifiedorderDo.setTradeType(unifiedorderResult.getTrade_type());
		wechatPayUnifiedorderDo.setRecordDateTime(new Date());
		wechatPayUnifiedorderDo.setXmlData(unifiedorderResultXmlStr);
		wechatPayUnifiedorderDoMapper.insert(wechatPayUnifiedorderDo);
	}
	
	/**
	 * 生成wechatPayVo，返回给app发起调用"调起支付"接口
	 * @param total_free
	 * @param unifiedorderResult
	 * @param out_trade_no
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private WechatPayVo generateWechatPayVo(int total_free,
			UnifiedorderResult unifiedorderResult, 
			String out_trade_no, String key,String tradeType) throws Exception {
		WechatPayVo wechatPayVo = new WechatPayVo();
		String sign;
		String noncestr = RandomStringGenerator.getRandomStringByLength(32);
		String package_ = "Sign=WXPay" ;
		long timestamp = System.currentTimeMillis();
		// 标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)
		// 原来的问题: 1.android不除已1000,可以拉起,ios不行; 2.除以1000,ios可以,android不行
		timestamp = timestamp/1000 ; // 改成除以1000:ios、android的兼容问题客户端已解决
		//生成调起支付接口的签名
		sign = generateCallWechatPaySign(unifiedorderResult.getAppid(),unifiedorderResult.getMch_id(),unifiedorderResult.getPrepay_id(), noncestr, package_,timestamp, key);
		//wechatPayVo.setAppid(unifiedorderResult.getAppid());
		wechatPayVo.setPartnerid(unifiedorderResult.getMch_id());
		wechatPayVo.setPrepayid(unifiedorderResult.getPrepay_id());
		//暂填写固定值Sign=WXPay (https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_12&index=2)
		wechatPayVo.setPackage_(package_);
		wechatPayVo.setNoncestr(noncestr);
		wechatPayVo.setTimestamp(timestamp);
		wechatPayVo.setSign(sign);
		wechatPayVo.setTotalFree(total_free);
		wechatPayVo.setOut_trade_no(out_trade_no);
		// trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
		if(tradeType.equals(TradeType.NATIVE.getValue())){
			wechatPayVo.setCodeUrl(unifiedorderResult.getCode_url());
		}
		return wechatPayVo;
	}
	
	/**
	 *  生成调起支付接口的签名
	 * @param appid
	 * @param mchId
	 * @param prepayId
	 * @param noncestr
	 * @param package_
	 * @param timestamp
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private String generateCallWechatPaySign(String appid,String mchId,String prepayId,
			String noncestr, String package_, long timestamp, String key)
			throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("appid", appid);
		m.put("partnerid", mchId);
		m.put("prepayid", prepayId);
		m.put("package",package_ );
		m.put("noncestr", noncestr);
		m.put("timestamp", timestamp);
		String sign = Signature.getSign(m, key);
		return sign;
	}

	private WechatJSAPIVo createWechatJSAPIVo(String appid,String mch_id, int total_fee,
			String noncestr, String out_trade_no, String key,String prepay_id) throws Exception {
		WechatJSAPIVo wechatPayVo = new WechatJSAPIVo();
		String sign;
		String package_ = "prepay_id=" + prepay_id ;
		long timestamp = System.currentTimeMillis();
		timestamp = timestamp / 1000;
		//生成调起支付接口的签名
		sign = generatePaySign(appid,prepay_id, noncestr, package_,timestamp, key);
		wechatPayVo.setAppId(appid);
		wechatPayVo.setPackage_(package_);
		wechatPayVo.setNoncestr(noncestr);
		wechatPayVo.setTimestamp(timestamp);
		wechatPayVo.setPaySign(sign);
		wechatPayVo.setTotal_fee(total_fee);
		wechatPayVo.setSignType(Constants.PUBLIC_NUMBER_SIGNTYPE);
		return wechatPayVo;
	}
	
	/**
	 * 公众号支付，生成签名
	 * @param appid
	 * @param mchId
	 * @param noncestr
	 * @param package_
	 * @param timestamp
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private String generatePaySign(String appid,String mchId,
			String noncestr, String package_, long timestamp, String key)
			throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("appId", appid);
		m.put("signType", Constants.PUBLIC_NUMBER_SIGNTYPE);
		m.put("package",package_ );
		m.put("nonceStr", noncestr);
		m.put("timeStamp", timestamp);
		String sign = Signature.getSign(m, key);
		return sign;
	}
	
	/**
	 * 保存支付通知
	 * @param xmlData
	 * @param payNotify
	 */
	private void createWechatPayNotify(String xmlData, MchPayNotify payNotify) {
		WechatPayNotifyDo d = new WechatPayNotifyDo();
		d.setAppid(payNotify.getAppid());
		d.setAttach(payNotify.getAttach());
		d.setBankType(payNotify.getBank_type());
		d.setFeeType(payNotify.getFee_type());
		d.setIsSubscribe(payNotify.getIs_subscribe());
		d.setMchId(payNotify.getMch_id());
		d.setNonceStr(payNotify.getNonce_str());
		d.setOpenid(payNotify.getOpenid());
		d.setOutTradeNo(payNotify.getOut_trade_no());
		d.setResultCode(payNotify.getResult_code());
		d.setReturnCode(payNotify.getReturn_code());
		d.setSign(payNotify.getSign());
		//d.setSubMchId(payNotify.getsu);
		d.setTimeEnd(payNotify.getTime_end());
		d.setTotalFee(payNotify.getTotal_fee());
		d.setTransactionId(payNotify.getTransaction_id());
		d.setRecordDateTime(new Date());
		d.setXmlData(xmlData);
		wechatPayNotifyMapper.insert(d);
	}
}
