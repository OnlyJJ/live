package com.jiujun.shows.pay.biz;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.constant.ThirdpartyConfTableEnum;
import com.jiujun.shows.base.domain.ThirdpartyConf;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.base.service.IThirdpartyConfService;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.RandomStringGenerator;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.common.utils.XMLConverUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayChargeOrderMapper;
import com.jiujun.shows.pay.dao.WechatPayNotifyMapper;
import com.jiujun.shows.pay.dao.WechatPayUnifiedorderMapper;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.domain.WechatPayNotifyDo;
import com.jiujun.shows.pay.domain.WechatPayUnifiedorderDo;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.enums.LockTarget;
import com.jiujun.shows.pay.enums.PayTypeEnum;
import com.jiujun.shows.pay.enums.WechatPayBusinessEnum;
import com.jiujun.shows.pay.enums.WechatPayBusinessEnum.TradeType;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.pay.service.IChargeCommonService;
import com.jiujun.shows.pay.utils.MchPayNotify;
import com.jiujun.shows.pay.utils.Signature;
import com.jiujun.shows.pay.vo.Unifiedorder;
import com.jiujun.shows.pay.vo.UnifiedorderResult;
import com.jiujun.shows.pay.vo.WechatJSAPIVo;
import com.jiujun.shows.pay.vo.WechatPayVo;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserInfoService;

@Service("weChatPayBiz")
public class WeChatPayBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private WechatPayUnifiedorderMapper wechatPayUnifiedorderMapper;
	
	@Resource
	private PayChargeOrderMapper payChargeOrderMapper;
	
	@Resource
	private WechatPayNotifyMapper wechatPayNotifyMapper;
	
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
	
	/**
	 * 微信支付,统一下单
	 * 参考接入流程: <br />
	 * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1 
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
	public ServiceResult<WechatPayVo> payUnifiedorder(String receiverUserId,
			int total_free, String spbill_create_ip, int clientType,
			String channelId, String agentUserId, TradeType tradeType,
			DeviceProperties deviceProperties) throws Exception {
		//校验参数
		if(StringUtils.isEmpty(receiverUserId)||total_free<=0){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		// agentUserId不为空,说明是代别人充值,则监测receiverUserId 是否有此用户账户
		if(!StringUtils.isEmpty(agentUserId)){
			UserAccount userAccount = userAccountService.getObjectByUserId(receiverUserId);
			// 为空说明被充账户不存在
			if(userAccount == null) {
				PayBizException e = new PayBizException(ErrorCode.ERROR_5029);
				log.error(e.getMessage(),e);
				throw e;
			}
			log.info(String.format("###charge for other user,receiveUserId:%s,agentUserId:%s", receiverUserId,agentUserId));
		}
		//选择充值金额(单位:分)
		String testUserList = SpringContextListener.getContextProValue("charge.testUserList","");
		//非测试人员要检测是否达到最少金额
		if(!testUserList.contains(receiverUserId)){
			String minChargeMoneyStr = SpringContextListener.getContextProValue("charge.minSelectMoney", "500");
			int minChargeMoney = Integer.parseInt(minChargeMoneyStr);
			if(total_free < minChargeMoney){ //限制最低充值5元
				PayBizException e = new PayBizException(ErrorCode.ERROR_5017);
				log.error(e.getMessage(),e);
				throw e;
			}
		}
		UnifiedorderResult unifiedorderResult = null;
		WechatPayVo wechatPayVo = new WechatPayVo ();
		String lockname = LockTarget.LOCK_PAY_WCH.getLockName() + receiverUserId;
		try {
			RdLock.lock(lockname);
			//微信支付统一下单请求地址
			String reqUrl  = SpringContextListener.getContextProValue("url_wechat_pay_unifiedorder", "https://api.mch.weixin.qq.com/pay/unifiedorder");
			//微信分配的公众账号ID（企业号corpid即为此appId）
			String appid = getWechaAppPayAppid(tradeType,deviceProperties);
			//微信支付分配的商户号
			String mch_id = getWechatPayMchid(tradeType,deviceProperties);
			//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
			String key = getWechatPayKey(tradeType,deviceProperties);
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
			String notifyUrl = Constants.SERVICEURL+"/wechat/pay/notifyCallBack";
			// 判断扫描支付
			if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.NATIVE.getValue())){
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
			log.info(String.format("###wechatPayNotifyUrl:%s",notifyUrl));
			//生成微信支付签名
			String sign = Signature.getSign(unifiedorderXmlObj, key);
			unifiedorderXmlObj.setSign(sign);
			log.info("#####payUnifiedorder-unifiedorderXmlObj:"+JsonUtil.beanToJsonString(unifiedorderXmlObj));
			//转换成xml字符串
			String unifiedorderRequestXmlStr = XMLConverUtil.convertToXml(unifiedorderXmlObj);
			log.info("#####payUnifiedorder-unifiedorderRequestXmlStr:"+unifiedorderRequestXmlStr);
			//调用微信支付统一下单api
			String unifiedorderResponseXmlStr = HttpUtils.post(reqUrl, unifiedorderRequestXmlStr,"utf-8");
			log.info("unifiedorderResponseXmlStr:"+unifiedorderResponseXmlStr);
			unifiedorderResult = XMLConverUtil.converyToJavaBean(unifiedorderResponseXmlStr, UnifiedorderResult.class);
			log.info("###unifiedorderResult:"+JsonUtil.beanToJsonString(unifiedorderResult));
			
			if(unifiedorderResult==null||!"SUCCESS".equals(unifiedorderResult.getReturn_code())){
				log.error("####微信支付预下单返回数据:"+unifiedorderResponseXmlStr);
				PayBizException e = new PayBizException(ErrorCode.ERROR_5006);
				log.error(e.getMessage(),e);
				throw e;
			}
			// 校验微信返回的签名,商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
			if(!validateWechatpayUnifiedorderSign(unifiedorderResult, key)){
				PayBizException e = new PayBizException(ErrorCode.ERROR_5004,"处理微信支付统一下单的返回结果,签名验证失败");
				log.error(e.getMessage() ,e);
				throw e;
			}
			// 本地配置的商户号
			String localMchid = getWechatPayMchid(tradeType,deviceProperties);
			//校验是否与本地的商户号一致
			if(unifiedorderResult == null || !localMchid.equals(unifiedorderResult.getMch_id())){
				PayBizException e = new PayBizException(ErrorCode.ERROR_5021);
				log.error(String.format("###支付包含的商户号与本地的商户号不匹配,localMchid:%s,payNotify:%s",JsonUtil.beanToJsonString(unifiedorderResult) )) ;
				log.error(e.getMessage() ,e);
				throw e;
			}
			// 当字段在return_code 和result_code都为SUCCESS的时候
			if(unifiedorderResult!=null
					&&"SUCCESS".equals(unifiedorderResult.getReturn_code())
					&&"SUCCESS".equals(unifiedorderResult.getResult_code())){
				// 插入充值记录
				int payType = PayTypeEnum.Type.Wechat.getValue();
				createPayChargeOrderForWechatPay(receiverUserId,payType,total_free,clientType,out_trade_no,channelId,agentUserId,spbill_create_ip,deviceProperties);
				// 保存统一下单记录(插表 t_wechat_pay_unifiedorder)
				createWechatPayUnifiedorder(receiverUserId, total_free ,unifiedorderResult,
						unifiedorderResponseXmlStr,out_trade_no,spbill_create_ip);
				//生成wechatPayVo，返回给app发起调用"调起支付"接口
				wechatPayVo = generateWechatPayVo(total_free, unifiedorderResult,
						out_trade_no, key,tradeType);
			}else{
				log.error(unifiedorderResponseXmlStr);
				log.error(JsonUtil.beanToJsonString(unifiedorderResult));
				PayBizException e = new PayBizException(ErrorCode.ERROR_5003);
				log.error(e.getMessage() ,e);
				throw e;
			}
		} catch(PayBizException e) {
			throw e;
		} catch(Exception e) {
			throw e;
		} finally {
			RdLock.unlock(lockname);
		}
		ServiceResult<WechatPayVo> srt = new ServiceResult<WechatPayVo>();
		srt.setSucceed(true);
		srt.setData(wechatPayVo);
		return srt;
	}

	/**
	 * 处理扫码支付回调
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月29日
	 */
	public ServiceResult<Boolean> dealWechatPayNotify(String xmlData,
			TradeType tradeType, String notifyFromIp,
			DeviceProperties deviceProperties) throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info(String.format("#####begin-dealWechatPayNotify,notify-xmlData:%s,tradeType:%s,notifyFromIp:%s,deviceProperties:%s",xmlData,tradeType.getValue(),notifyFromIp,JsonUtil.beanToJsonString(deviceProperties)));
		if(StringUtils.isEmpty(xmlData)){
			Exception e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage() ,e);
			throw e;
		}
		//转换数据对象
		MchPayNotify payNotify = XMLConverUtil.converyToJavaBean(xmlData,MchPayNotify.class);
		log.info("###payNotify:"+JsonUtil.beanToJsonString(payNotify));

		//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		String key = getWechatPayKey(tradeType,payNotify.getMch_id());
		
		//校验签名,防止数据泄漏导致出现“假通知”，造成资金损失。
		if(!validateWechatPayNotifySign(key,xmlData)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5004);
			log.error(e.getMessage() ,e);
			throw e;
		}
		String orderId = payNotify.getOut_trade_no();
		if("SUCCESS".equals(payNotify.getReturn_code())&&"SUCCESS".equals(payNotify.getResult_code())){
			//在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱(已在web层控制)
				WechatPayNotifyDo wechatPayNotifyDo = wechatPayNotifyMapper.getByOutTradeNo(orderId);
				if(wechatPayNotifyDo!=null){
					log.info("#####wechatCharge-have save wechatPayNotifyDo for OutTradeNo:"+orderId); 
				}else{
					//没插入过,则保存支付通知(插表t_wechat_pay_notify)
					createWechatPayNotify(xmlData, payNotify);
				}
				PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId);
				//先判断服务器是否已生成有相应订单号的"充值记录"
				if(dbPayChargeOrder==null){
					Exception e = new PayBizException(ErrorCode.ERROR_5007,orderId);
					log.error(JsonUtil.beanToJsonString(payNotify));
					log.error(e.getMessage() ,e);
					throw e ;
				}
				//加金币、更新订单状态(@Transactional独立开来回滚才有效)
				//weChatChargeAddGoldService.wechatChargeAddGoldAndUpdateOrderStatus(payNotify);
				int realChargeMoney = payNotify.getTotal_fee(); //单位:分
				boolean isPayBySanBox = false;
				boolean isApplePay = false;
				int appleUserRealPayMoney = 0;
				String transactionId = payNotify.getTransaction_id();
				chargeCommonService.updateChargeOrderAndGold(orderId, realChargeMoney,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIp,transactionId,deviceProperties);
		}else{
			log.error(xmlData);
			log.error(JsonUtil.beanToJsonString(payNotify));
			String remark = "微信支付失败:,Return_code="+payNotify.getReturn_code()+",Result_code:"+payNotify.getResult_code();
			chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIp);
			Exception e = new PayBizException(ErrorCode.ERROR_5005);
			log.error(e.getMessage() ,e);
			throw e ;
		
		}
		log.info(String.format("#####end-dealWechatPayNotify,notify-xmlData:%s,tradeType:%s,notifyFromIp:%s,deviceProperties:%s",xmlData,tradeType.getValue(),notifyFromIp,JsonUtil.beanToJsonString(deviceProperties)));
		srt.setSucceed(true);
		return srt;
	}

	/**
	 * 公众号支付下单接口，流程：校验 —— 生成预定单 —— 生成发起支付的参数 —— 返回给H5
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月30日
	 */
	public ServiceResult<WechatJSAPIVo> handlePayForWechatJsapi(String openid,
			String receiverUserId, int buyGold, String spbill_create_ip,
			int clientType, String channelId, TradeType tradeType,
			DeviceProperties deviceProperties) throws Exception {
		ServiceResult<WechatJSAPIVo> srt = new ServiceResult<WechatJSAPIVo>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(receiverUserId)) {
			PayBizException e = new PayBizException(ErrorCode.ERROR_5029);
			log.error(e.getMessage() ,e);
			throw e;
		}
		// 校验充值账户是否存在
		UserAccount userAccount = userAccountService.getObjectByUserId(receiverUserId);
		if(userAccount == null){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5029);
			log.error(e.getMessage() ,e);
			throw e;
		}
		log.error("###handlePayForWechatJsapi-公众号支付，receiverUserId="+ receiverUserId + ",buygold="+buyGold + ",begin...");
		// 获取第三方配置信息
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		int clientTypeInt = ThirdpartyConfTableEnum.ClientType.H5.getValue();
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> tsrt = 
				thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(tsrt.isSucceed()) {
			thirdpartyConf = tsrt.getData();
		}
		if(thirdpartyConf == null
				|| thirdpartyConf.getInUsePayCreateOrder() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5037);
			log.error(e.getMessage() ,e);
			throw e;
		}
		String appid = thirdpartyConf.getAppId();
		String mch_id = thirdpartyConf.getPayMchId();
		String key = thirdpartyConf.getPayKey();
		String lockname = LockTarget.LOCK_PAY_WCH.getLockName() + receiverUserId;
		try {
			RdLock.lock(lockname);
			WechatJSAPIVo retVo = new WechatJSAPIVo();
			int total_fee = buyGold / 10; // 转化为分
			UnifiedorderResult unifiedorderResult = null;
			//微信支付统一下单请求地址
			String reqUrl = Constants.ORDER_URL;
			//随机字符串，不长于32位。推荐随机数生成算法
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			String body = "蜜桃直播微信充值";
			String detail = "金额:"+ total_fee +"分";
			log.error("###handlePayForWechatJsapi-appid=" + appid + ",mch_id=" + mch_id + ",key=" + key + ",nonce_str="+ nonce_str + ",detail="+detail);
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
			
			String trade_type = tradeType.getValue();
			//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。默认取值app支付对应通知的地址
			String notifyUrl = Constants.SERVICEURL+"/wechat/pay/jsapi/notifyCallBack";
			
			unifiedorderXmlObj.setTrade_type(trade_type);
			unifiedorderXmlObj.setNotify_url(notifyUrl);
			//生成微信支付签名
			String sign = Signature.getSign(unifiedorderXmlObj, key);
			unifiedorderXmlObj.setSign(sign);
			log.info("#####handlePayForWechatJsapi-unifiedorderXmlObj:"+JsonUtil.beanToJsonString(unifiedorderXmlObj));
			//转换成xml字符串
			String unifiedorderRequestXmlStr = XMLConverUtil.convertToXml(unifiedorderXmlObj);
			log.info("#####handlePayForWechatJsapi-unifiedorderRequestXmlStr:"+unifiedorderRequestXmlStr);
			//调用微信支付统一下单api
			String unifiedorderResponseXmlStr = HttpUtils.post(reqUrl, unifiedorderRequestXmlStr,"utf-8");
			log.info("###handlePayForWechatJsapi-unifiedorderResponseXmlStr:"+unifiedorderResponseXmlStr);
			unifiedorderResult = XMLConverUtil.converyToJavaBean(unifiedorderResponseXmlStr, UnifiedorderResult.class);
			log.info("###handlePayForWechatJsapi-unifiedorderResult:"+JsonUtil.beanToJsonString(unifiedorderResult));
			
			if(unifiedorderResult == null || !"SUCCESS".equals(unifiedorderResult.getReturn_code())){
				log.error("####handlePayForWechatJsapi-微信支付预下单返回数据:"+unifiedorderResponseXmlStr);
				Exception e = new PayBizException(ErrorCode.ERROR_5006);
				log.error(e.getMessage() ,e);
				throw e;
			}
			
			// 校验微信返回的签名,商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
			if(!validateWechatpayUnifiedorderSign(unifiedorderResult, key)){
				PayBizException e = new PayBizException(ErrorCode.ERROR_5004,"处理微信支付统一下单的返回结果,签名验证失败");
				log.error(e.getMessage() ,e);
				throw e;
			}
			
			//校验是否与本地的商户号一致
			if(unifiedorderResult == null || !mch_id.equals(unifiedorderResult.getMch_id())){
				PayBizException e = new PayBizException(ErrorCode.ERROR_5021);
				log.error(String.format("###handlePayForWechatJsapi-支付包含的商户号与本地的商户号不匹配,localMchid:%s,payNotify:%s",JsonUtil.beanToJsonString(unifiedorderResult) )) ;
				log.error(e.getMessage() ,e);
				throw e;
			}
			String package_ = "";
			if(unifiedorderResult != null
					&& "SUCCESS".equals(unifiedorderResult.getReturn_code())
					&& "SUCCESS".equals(unifiedorderResult.getResult_code())){
				// return和result都为success才返回preapay_id
				String prepay_id = unifiedorderResult.getPrepay_id();
				package_ = prepay_id;
				// 插入充值记录
				createPayChargeOrderForWechatPay(receiverUserId,PayTypeEnum.Type.Wechat.getValue(),total_fee,clientType,out_trade_no,channelId,null,spbill_create_ip,deviceProperties);
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
				log.error(unifiedorderResponseXmlStr);
				log.error(JsonUtil.beanToJsonString(unifiedorderResult));
				Exception e = new PayBizException(ErrorCode.ERROR_5003);
				log.error(e.getMessage() ,e);
				throw e ;
			}
			if(StringUtils.isEmpty(package_)) {
				Exception e = new PayBizException(ErrorCode.ERROR_5003);
				log.error(e.getMessage() ,e);
				throw e ;
			}
			log.error("###handlePayForWechatJsapi-公众号支付，receiverUserId="+ receiverUserId + ",buygold="+buyGold + ",end");
			srt.setSucceed(true);
			srt.setData(retVo);
			return srt;
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			RdLock.unlock(lockname);
		}
		return srt;
	}

	/**
	 * 处理公众号支付回调
	 * @param xmlData
	 * @param notifyFromIp
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月30日
	 */
	public ServiceResult<Boolean> handleWechatJsapiNotify(String xmlData,
			String notifyFromIp) throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info(String.format("#####begin-handleWechatJsapiNotify,notify-xmlData:%s,notifyFromIp:%s",xmlData,notifyFromIp));
		if(StringUtils.isEmpty(xmlData)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage() ,e);
			throw e;
		}
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = ThirdpartyConfTableEnum.ClientType.H5.getValue();
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> tsrt = 
				thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(tsrt.isSucceed()) {
			thirdpartyConf = tsrt.getData();
		}
		if(thirdpartyConf == null
				|| thirdpartyConf.getInUsePaySuccessNotify() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			log.info(String.format("###handleWechatJsapiNotify-查询不到相关的第三方支付通知配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			PayBizException e = new PayBizException(ErrorCode.ERROR_5037);
			log.error(e.getMessage() ,e);
			throw e;
		}
		String key = thirdpartyConf.getPayKey();
		//转换数据对象
		MchPayNotify payNotify = XMLConverUtil.converyToJavaBean(xmlData,MchPayNotify.class);
		log.info("###handleWechatJsapiNotify:"+JsonUtil.beanToJsonString(payNotify));
		
		// 本地配置的商户号
		String localMchid = thirdpartyConf.getPayMchId();
		//校验是否与本地的商户号一致
		if(payNotify == null || !localMchid.equals(payNotify.getMch_id())){
			log.error(String.format("###handleWechatJsapiNotify-支付通知包含的商户号与本地的商户号不匹配,localMchid:%s,payNotify:%s",JsonUtil.beanToJsonString(payNotify) )) ;
			PayBizException e = new PayBizException(ErrorCode.ERROR_5021);
			log.error(e.getMessage() ,e);
			throw e;
		}
		
		//校验签名,防止数据泄漏导致出现“假通知”，造成资金损失。
		//if(!validateWechatPayNotifySign(key, payNotify)){
		if(!validateWechatPayNotifySign(key,xmlData)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5004,"处理微信支付通知,签名验证失败");
			log.error(e.getMessage() ,e);
			throw e;
		}
		String orderId = payNotify.getOut_trade_no();
		if("SUCCESS".equals(payNotify.getReturn_code())&&"SUCCESS".equals(payNotify.getResult_code())){
			//在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱(已在web层控制)
				WechatPayNotifyDo wechatPayNotifyDo = wechatPayNotifyMapper.getByOutTradeNo(orderId);
				if(wechatPayNotifyDo!=null){
					log.info("#####handleWechatJsapiNotify-have save wechatPayNotifyDo for OutTradeNo:"+orderId); 
				}else{
					//没插入过,则保存支付通知(插表t_wechat_pay_notify)
					createWechatPayNotify(xmlData, payNotify);
				}
				PayChargeOrder dbPayChargeOrder = payChargeOrderMapper.getPcoByOrderId(orderId);
				//先判断服务器是否已生成有相应订单号的"充值记录"
				if(dbPayChargeOrder==null){
					Exception e = new PayBizException(ErrorCode.ERROR_5007,orderId);
					log.error(JsonUtil.beanToJsonString(payNotify));
					log.error(e.getMessage() ,e);
					throw e ;
				}
				int realChargeMoney = payNotify.getTotal_fee(); //单位:分
				boolean isPayBySanBox = false;
				boolean isApplePay = false;
				int appleUserRealPayMoney = 0;
				String transactionId = payNotify.getTransaction_id();
				chargeCommonService.updateChargeOrderAndGold(orderId, realChargeMoney,isApplePay,isPayBySanBox,appleUserRealPayMoney,notifyFromIp,transactionId,null);
		}else{
			log.error(xmlData);
			log.error(JsonUtil.beanToJsonString(payNotify));
			String remark = "微信支付失败:,Return_code="+payNotify.getReturn_code()+",Result_code:"+payNotify.getResult_code();
			chargeCommonService.upateOrderStatus2Fail(orderId, remark,notifyFromIp);
			Exception e = new PayBizException(ErrorCode.ERROR_5005);
			log.error(e.getMessage() ,e);
			throw e ;
		}
		log.info(String.format("#####end-handleWechatJsapiNotify,notify-xmlData:%s,notifyFromIp:%s",xmlData,notifyFromIp));
		srt.setSucceed(true);
		return srt;
	}
	
	/**
	 * 微信支付,依据tradeType转成相应的客户端类型,web:扫码支付;andorid:app支付
	 * @param tradeType
	 * @return
	 */
	private int convert2ClientTypeInt(TradeType tradeType) {
		// 默认取通用的type
		int clientTypeInt = ThirdpartyConfTableEnum.ClientType.WEB.getValue();;
		if(TradeType.App.getValue() == tradeType.getValue()){
			clientTypeInt = ThirdpartyConfTableEnum.ClientType.ANDROID.getValue();
		}
		return clientTypeInt;
	}


	/**
	 * 获取微信支付appid
	 * @return
	 */
	private String getWechaAppPayAppid(WechatPayBusinessEnum.TradeType tradeType,DeviceProperties deviceProperties) throws Exception{
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convert2ClientTypeInt(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			log.info(String.format("###查询不到相关的第三方支付下单配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5037);
			throw e;
		}
		
		if(thirdpartyConf.getInUsePayCreateOrder() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			log.info(String.format("###查询到相关的第三方支付下单配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5028);
			throw e;
		}
		String appid_wechat_pay = thirdpartyConf.getAppId();
		return appid_wechat_pay ;
	}
	
	/**
	 * 微信支付分配的商户号
	 * @return
	 */
	private String getWechatPayMchid(WechatPayBusinessEnum.TradeType tradeType,DeviceProperties deviceProperties) throws Exception{
		/*String mch_id_wechat_pay = SpringContextListener.getContextProValue("mch_id_wechat_pay", "1313981201");
		if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.App.getValue())){ 
			mch_id_wechat_pay = SpringContextListener.getContextProValue("mch_id_wechat_pay", "1313981201");
		}else if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.NATIVE.getValue())){
			mch_id_wechat_pay = SpringContextListener.getContextProValue("mch_id_wechat_pay_mp", "1420931102");
		}*/
		
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convert2ClientTypeInt(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			log.info(String.format("###查询不到相关的第三方支付下单配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5037);
			throw e;
		}
		
		if(thirdpartyConf.getInUsePayCreateOrder() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			log.info(String.format("###查询到相关的第三方支付下单配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5028);
			throw e;
		}
		String mch_id_wechat_pay = thirdpartyConf.getPayMchId();
		return mch_id_wechat_pay;
	}
	
	/**
	 * 获取微信支付密钥
	 * @return
	 */
	private String getWechatPayKey(WechatPayBusinessEnum.TradeType tradeType,DeviceProperties deviceProperties) throws Exception{
		//key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		/*String key = SpringContextListener.getContextProValue("key_wechat_pay", "tx4no0a8ax1c9yrhwdrov2xwupvjooke");
		if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.App.getValue())){
			key = SpringContextListener.getContextProValue("key_wechat_pay", "tx4no0a8ax1c9yrhwdrov2xwupvjooke");
		}else if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.NATIVE.getValue())){
			key = SpringContextListener.getContextProValue("key_wechat_pay_mp", "767deef133c932e38e84a970f7c25bdc");
		}*/
		
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convert2ClientTypeInt(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			log.info(String.format("###查询不到相关的第三方支付下单配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5037);
			throw e;
		}
		
		if(thirdpartyConf.getInUsePayCreateOrder() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			log.info(String.format("###查询到相关的第三方支付下单配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			Exception e = new PayBizException(ErrorCode.ERROR_5028);
			throw e;
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
		log.info("signUnifiedorderResult:"+JsonUtil.beanToJsonString(signUnifiedorderResult));
		String serverSign = Signature.getSign(signUnifiedorderResult, key);
		//签名验证
		if(!wechatResponseSign.equals(serverSign)){
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
		
		// 2017-09-06，增加统计信息字段
		String douId = "";
		if(null != channelId) {
			douId = channelId + "_";
		}
		if(dp!=null && null != dp.getPackageName()) {
			o.setPkgName(dp.getPackageName());
			douId += dp.getPackageName();
		}
		if(!StringUtils.isEmpty(douId)) {
			o.setDouId(douId);
		}
		UserInfo user = userInfoService.getUserInfoFromCache(userId);
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
		wechatPayUnifiedorderMapper.insert(wechatPayUnifiedorderDo);
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
			String out_trade_no, String key,WechatPayBusinessEnum.TradeType tradeType) throws Exception {
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
		if(tradeType.getValue().equals(WechatPayBusinessEnum.TradeType.NATIVE.getValue())){
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
	
	/**
	 * 生成实体
	 */
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
	 * 生成签名
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
	 * 获取微信支付密钥
	 * @return
	 */
	private String getWechatPayKey(WechatPayBusinessEnum.TradeType tradeType,String mchid) throws Exception{
		int thirdpartyType = ThirdpartyConfTableEnum.ThirdpartyType.WEIXIN.getValue();
		// 微信支付,依据tradeType转成相应的客户端类型typeInt
		int clientTypeInt = convert2ClientTypeInt(tradeType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = 
				thirdpartyConfService.getThirdpartyConf1(thirdpartyType, mchid, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null
				|| thirdpartyConf.getInUsePayCreateOrder() != ThirdpartyConfTableEnum.InUse.Yes.getValue()){
			log.info(String.format("###查询不到相关的第三方支付下单配置,thirdpartyType:%s,mchid:%s,clientTypeInt:%s", thirdpartyType,mchid,clientTypeInt));
			PayBizException e = new PayBizException(ErrorCode.ERROR_5037);
			log.error(e.getMessage(),e);
			throw e;
		}
		String payKey = thirdpartyConf.getPayKey();
		return payKey ;
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
