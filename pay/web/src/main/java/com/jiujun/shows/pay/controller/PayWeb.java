package com.jiujun.shows.pay.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.common.utils.HttpServletResponseUtil;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.IOUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.SystemUtil;
import com.jiujun.shows.common.utils.XMLConverUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.domain.PayChargeOrder;
import com.jiujun.shows.pay.domain.PayXiaolajiaoOrder;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.enums.WechatPayBusinessEnum.TradeType;
import com.jiujun.shows.pay.service.IAliPayService;
import com.jiujun.shows.pay.service.IIosPayService;
import com.jiujun.shows.pay.service.IPayChargeOrderService;
import com.jiujun.shows.pay.service.IPayXiaolajiaoOrderService;
import com.jiujun.shows.pay.service.IUnionPayService;
import com.jiujun.shows.pay.service.IWeChatPayService;
import com.jiujun.shows.pay.vo.DataRequest;
import com.jiujun.shows.pay.vo.EcoPay;
import com.jiujun.shows.pay.vo.PayOrder;
import com.jiujun.shows.pay.vo.WechatJSAPIVo;
import com.jiujun.shows.pay.vo.WechatPayVo;
import com.jiun.shows.appclient.service.IAppInstallChannelService;
import com.jiun.shows.appclient.vo.AppleClientVo;

/**
 * 支付服务
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
@Controller("PayWeb")
public class PayWeb {
	
	@Resource
	private IAliPayService aliPayService;
	
	@Resource
	private IUnionPayService unionPayService;
	
	@Resource
	private IPayChargeOrderService payChargeOrderService;
	
	@Resource
	private IWeChatPayService weChatPayService;
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;
	
	@Resource
	private IIosPayService iosPayService;
	
	@Resource
	private IPayXiaolajiaoOrderService payXiaolajiaoOrderService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	/**
	 * P1
	 * 支付宝生成订单 ,流程模拟代理支付(应对接入号被封,回调通知地址,用另外的域名),中间多跳了一次
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月7日
	 */
	public JSONObject createOrder(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject json = new JSONObject();
		if(null == data) {
			result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
		}else{
			ServiceResult<JSONObject> srt = aliPayService.createOrder(data);
			if(srt.isSucceed()) {
				json = srt.getData();
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} 
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
	/** 
	 * P2 返回订单列表信息 
	 * (暂时废掉了，三端貌似没用了，以后用再来处理)
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	@Deprecated
	public JSONObject returnPayOrderList(DataRequest data ){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		json.put(result.getShortName(), result.buildJson());
		json.put(Constants.DATA_BODY, jsonArray);
		json.put(data.getPage().getShortName(), data.getPage().buildJson());
		return json;
	}
	
	/**
	 * P3
	 * 移动支付端下单接口
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	public JSONObject doPay(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject json = new JSONObject();
		if(data == null) {
			result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
		} else {
			ServiceResult<EcoPay> srt = unionPayService.doPay(data);
			if(srt.isSucceed()) {
				EcoPay ecoPay = srt.getData();
				json.put(ecoPay.getShortName(), ecoPay.buildJson());
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		}	
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
	/**
	 * P4
	 * web端网银支付
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年9月27日
	 */
	public JSONObject doWebPay(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject json = new JSONObject();
		if(data == null) {
			result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
		} else {
			ServiceResult<JSONObject> srt = unionPayService.doWebPay(data);
			if(srt.isSucceed()) {
				json = srt.getData();
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		}	
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
	/**
	 * P5
	 * 查询订单信息
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	public JSONObject getPcoByOrderId(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject json = new JSONObject();
		PayOrder payOrder = new PayOrder();
		if(data == null || data.getPayOrder() == null
				|| data.getPayOrder().getOrderId() == null) {
			result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
		} else {
			String orderId = data.getPayOrder().getOrderId();
			ServiceResult<PayChargeOrder> srt = payChargeOrderService.getDataByOrderId(orderId);
			if(srt.isSucceed()) {
				PayChargeOrder pco = srt.getData();
				//返回： 订单号 ，订单状态  ， 充值金额(单位：分) ， 到账金币 (1000/元) , 到账账号
				if(pco != null){
					payOrder.setOrderId(pco.getOrderId());
					payOrder.setOrderStatus(pco.getOrderStatus());
					payOrder.setPay_type(pco.getPay_type());
					payOrder.setMoney(pco.getMoney());
					payOrder.setSelectMoney(pco.getSelectMoney());
					if(pco.getGolds()==null||pco.getGolds() == 0){
						payOrder.setGolds(0);
					}else{
						payOrder.setGolds(pco.getGolds());
					}
					payOrder.setUserId(pco.getUserId());
				}
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		}	
		json.put(payOrder.getShortName(),payOrder.buildJson());
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
	/**
	 * P10
	 * 请求生成微信支付(app支付)订单 
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月13日
	 */
	public JSONObject payUnifiedorder(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		WechatPayVo responseWechatPayVo = null;
		try {
			if(data==null||data.getWechatPayVo()==null||data.getUserBaseInfo()==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				WechatPayVo reqWechatPayVo = data.getWechatPayVo();
				String spbill_create_ip = HttpUtils.getIpAddress(data.getRequest());
				int total_free = reqWechatPayVo.getTotalFree();//充值金额(单位:分)
				//代充时,金币接受者
				String receiverUserId = reqWechatPayVo.getReceiverUserId();
				String agentUserId = null;
				String loginUserId = data.getUserBaseInfo().getUserId(); 
				//若果receiverUserId不为空,说明是给别人充值
				if(StringUtils.isNotEmpty(receiverUserId)){
					//当前登陆用户为代理人
					agentUserId = loginUserId;
					// 微信app端代充提示有bug,先屏蔽此功能
					result.setResultCode(ErrorCode.ERROR_5026.getResultCode());
					result.setResultDescr(ErrorCode.ERROR_5026.getResultDescr());
				}else{
					//当前登录用户是给自己充值,金币接受者是自己
					receiverUserId = loginUserId;
				}
				int clientType = HttpUtils.getClientTypeInt(data.getRequest());
				String channelId = null;
				DeviceProperties dev = data.getDeviceProperties();
				//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
				if(data.getDeviceProperties()!=null){
					if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36)) {
						ServiceResult<String> srt = appInstallChannelService.getChannelIdByImei(data.getDeviceProperties().getImei());
						if(srt.isSucceed()) {
							channelId = srt.getData();
						}
					}
					if(channelId==null) {
						channelId=data.getDeviceProperties().getChannelId();
					}
				}
				ServiceResult<WechatPayVo> srt = weChatPayService.payUnifiedorder(receiverUserId,total_free, spbill_create_ip, clientType,channelId,agentUserId,TradeType.App,dev);
				if(srt.isSucceed()) {
					responseWechatPayVo = srt.getData();
					jsonRes.put(responseWechatPayVo.getShortName(), responseWechatPayVo.buildJson());
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P12
	 * 苹果支付,生成订单
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月17日
	 */
	public JSONObject createPayChargeOrder(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getAppleClientVo()==null) {
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				// 设备信息不能少,用于识别苹果代充
				if(data.getDeviceProperties()==null) {
					result.setResultCode(ErrorCode.ERROR_5027.getResultCode());
					result.setResultDescr(ErrorCode.ERROR_5027.getResultDescr());
				} else {
					String productId =  data.getAppleClientVo().getProductId();
					//代充时,金币接受者
					String receiverUserId = data.getAppleClientVo().getReceiverUserId();
					String agentUserId = null;
					String loginUserId = data.getUserBaseInfo().getUserId(); 
					String clientIp = HttpUtils.getIpAddress(data.getRequest());
					//若果receiverUserId不为空,说明是给别人充值
					if(StringUtils.isNotEmpty(receiverUserId)){
						//当前登陆用户为代理人
						agentUserId = loginUserId;
					}else{
						//当前登录用户是给自己充值,金币接受者是自己
						receiverUserId = loginUserId;
					}
					//默认为0
					int reqVersionInt = 0;
					String versionStr = data.getDeviceProperties().getAppVersion();
					reqVersionInt = SystemUtil.parseAppVersion(versionStr);
					// 苹果app新包开始的版本
					int newApplePackStartVersion = SystemUtil.parseAppVersion("1.0");
					if(reqVersionInt == newApplePackStartVersion){ 
						LogUtil.log.info(String.format("###苹果充值(下订单):发现客户端版本(%s)是起始版本",reqVersionInt));
					}else{ // 不是新包的开始版本,要校验版本是否过低
						// 判断版本号，低于3.1.7->生成订单不成功
						int sysConfLowestVersionLimit = SystemUtil.parseAppVersion("3.1.7");
						if(reqVersionInt < sysConfLowestVersionLimit){
							LogUtil.log.error(String.format("###苹果充值(下订单):发现客户端版本(%s)太低,不利于数据跟踪,系统不给下订单,要求不低于版本(%s),",reqVersionInt,sysConfLowestVersionLimit));
							result.setResultCode(ErrorCode.ERROR_5027.getResultCode());
							result.setResultDescr(ErrorCode.ERROR_5027.getResultDescr());
						}
					}
					DeviceProperties dp = data.getDeviceProperties();
					ServiceResult<AppleClientVo> srt = iosPayService.createPayChargeOrder(receiverUserId, productId,agentUserId,clientIp,dp);
					if(srt.isSucceed()) {
						AppleClientVo appleClientVo = srt.getData();
						jsonRes.put(appleClientVo.getShortName(),appleClientVo.buildJson());
					} else {
						result.setResultCode(srt.getResultCode());
						result.setResultDescr(srt.getResultMsg());
					}
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P13
	 * 苹果支付成功后回调(支付成功触发通知,前提是先调用P12(生成订单))
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月17日
	 */
	public JSONObject applePayNotice(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getAppleClientVo()==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				// 设备信息不能少,用于识别苹果代充
				if(data.getDeviceProperties()==null){
					result.setResultCode(ErrorCode.ERROR_5027.getResultCode());
					result.setResultDescr(ErrorCode.ERROR_5027.getResultDescr());
				} else {
					String userId = data.getUserBaseInfo().getUserId();
					String verifyReceipt = data.getAppleClientVo().getVerifyReceipt();
					String orderId =  data.getAppleClientVo().getOrderId();
					long remainGolds = 0;
					String clientIp = HttpUtils.getIpAddress(data.getRequest()) ;
					DeviceProperties dev = data.getDeviceProperties();
					String clientType = HttpUtils.getClientTypeStr(data.getRequest()) ;
					dev.setClientType(clientType);
					ServiceResult<Long> srt = iosPayService.applePayNotice(orderId,verifyReceipt,clientIp,dev);
					if(srt.isSucceed()) {
						remainGolds = srt.getData();
						jsonRes.put("remainGolds",remainGolds);
					} else {
						result.setResultCode(srt.getResultCode());
						result.setResultDescr(srt.getResultMsg());
					}
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P14
	 * 苹果支付充值失败,更改订单状态
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月17日
	 */
	public JSONObject applePayFailNotice(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getAppleClientVo()==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				String clientIp = HttpUtils.getIpAddress(data.getRequest()) ;
				String userId = data.getUserBaseInfo().getUserId();
				String orderId =  data.getAppleClientVo().getOrderId();
				String remarkSuffix = data.getAppleClientVo().getRemarkSuffix();
				long remainGolds = 0;
				ServiceResult<Long> srt = iosPayService.applePayFailNotice(orderId,remarkSuffix,clientIp);
				if(srt.isSucceed()) {
					remainGolds = srt.getData();
					jsonRes.put("remainGolds",remainGolds);
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P15
	 * 请求生成微信支付(扫码支付接入)订单 - 
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月20日
	 */
	public JSONObject payUnifiedorderOfNative(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		WechatPayVo responseWechatPayVo = null;
		try {
			if(data==null||data.getWechatPayVo()==null||data.getUserBaseInfo()==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				WechatPayVo reqWechatPayVo = data.getWechatPayVo();
				String spbill_create_ip = HttpUtils.getUserReallyIp(data);
				int total_free = reqWechatPayVo.getTotalFree();//充值金额(单位:分)
				//代充时,金币接受者
				String receiverUserId = reqWechatPayVo.getReceiverUserId();
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
				int clientType = HttpUtils.getClientTypeInt(data.getRequest());
				String channelId = null;
				DeviceProperties dev = data.getDeviceProperties();
				//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
				if(data.getDeviceProperties()!=null){
					if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36)) {
						ServiceResult<String> srt = appInstallChannelService.getChannelIdByImei(data.getDeviceProperties().getImei());
						if(srt.isSucceed()) {
							channelId = srt.getData();
						}
					}
					if(channelId==null) {
						channelId=data.getDeviceProperties().getChannelId();
					}
				}
				ServiceResult<WechatPayVo> srt = weChatPayService.payUnifiedorder(receiverUserId,total_free, spbill_create_ip, clientType,channelId,agentUserId, TradeType.NATIVE,dev);
				if(srt.isSucceed()) {
					responseWechatPayVo = srt.getData();
					jsonRes.put(responseWechatPayVo.getShortName(), responseWechatPayVo.buildJson());
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P16 - 
	 * 小辣椒支付下单
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月19日
	 */
	public JSONObject createPayXiaolajiaoOrder(DataRequest data) {
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getPayXiaolajiaoVo() == null|| data.getUserBaseInfo() ==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}
			String clientIp = HttpUtils.getIpAddress(data.getRequest());
			String clientAddr = ipStoreService.getAddressByIp(clientIp);
			String productPackId = data.getPayXiaolajiaoVo().getProductPackId() ;
			String loginUserId = data.getUserBaseInfo().getUserId();
			PayXiaolajiaoOrder paramPayXiaolajiaoOrder = new PayXiaolajiaoOrder();
			paramPayXiaolajiaoOrder.setProductPackId(productPackId);
			paramPayXiaolajiaoOrder.setGenerateOrderIp(clientIp);
			paramPayXiaolajiaoOrder.setGenerateOrderAddr(clientAddr);
			paramPayXiaolajiaoOrder.setUserId(loginUserId);
			ServiceResult<String> srt = payXiaolajiaoOrderService.createPayXiaolajiaoOrder(paramPayXiaolajiaoOrder);
			if(srt.isSucceed()) {
				String orderId = srt.getData();
				PayOrder payOrder = new PayOrder();
				payOrder.setOrderId(orderId);
				jsonRes.put(payOrder.getShortName(), payOrder.buildJson());
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);;
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P17- 
	 * 小辣椒支付成功通知处理
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月19日
	 */
	public JSONObject paySuccessNotifyXiaolajiaoOrder(DataRequest data) {
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getPayOrder() == null
					||data.getPayXiaolajiaoVo() ==null
					|| data.getUserBaseInfo() ==null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}
			String orderId = data.getPayOrder().getOrderId();
			String loginUserId = data.getUserBaseInfo().getUserId();
			String clientIp = HttpUtils.getIpAddress(data.getRequest());
			String clientAddr = ipStoreService.getAddressByIp(clientIp);
			String productPackId = data.getPayXiaolajiaoVo().getProductPackId();
			String md5Checkcode = data.getPayXiaolajiaoVo().getMd5Checkcode();
			String sessionId = data.getSession().getSessionid();
			PayXiaolajiaoOrder paramPayXiaolajiaoOrder = new PayXiaolajiaoOrder();
			paramPayXiaolajiaoOrder.setOrderId(orderId);
			paramPayXiaolajiaoOrder.setNotifyIp(clientIp);
			paramPayXiaolajiaoOrder.setNotifyAddr(clientAddr);
			paramPayXiaolajiaoOrder.setProductPackId(productPackId);
			ServiceResult<Boolean> srt =
					payXiaolajiaoOrderService.paySuccessNotifyXiaolajiaoOrder(loginUserId,paramPayXiaolajiaoOrder,md5Checkcode,sessionId);
			if(!srt.isSucceed()) {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);;
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * P18
	 * 微信公众号支付-下单接口(供公共号、微信外H5页面支付共同使用)
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月20日
	 */
	public JSONObject payforWechatJsapi(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		WechatJSAPIVo retVo = null;
		try {
			if(data==null||data.getWechatJSAPIVo() == null){
				result.setResultCode(ErrorCode.ERROR_5025.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_5025.getResultDescr());
			}else{
				WechatJSAPIVo reqVo = data.getWechatJSAPIVo();
				String spbill_create_ip = HttpUtils.getIpAddress(data.getRequest());
				int buyGold = reqVo.getBuyGold(); // 充值金币
				String receiverUserId = reqVo.getReceiverUserId(); // 充值金币账户
				int clientType = HttpUtils.getClientTypeInt(data.getRequest());
				String channelId = null;
				DeviceProperties dev = data.getDeviceProperties();
				//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
				if(data.getDeviceProperties()!=null){
					if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36)) {
						ServiceResult<String> srt = appInstallChannelService.getChannelIdByImei(data.getDeviceProperties().getImei());
						if(srt.isSucceed()) {
							channelId = srt.getData();
						}
					}
					if(channelId==null) {
						channelId=data.getDeviceProperties().getChannelId();
					}
				}
				String openid= data.getWechatJSAPIVo().getOpenid();
				int payType = data.getWechatJSAPIVo().getPayType();
				ServiceResult<WechatJSAPIVo> srt = null;
				if(payType == 1) {
					srt = weChatPayService.handlePayForWechatJsapi(null,receiverUserId,buyGold, spbill_create_ip, clientType,channelId,TradeType.MWEB,dev);
				} else {
					srt = weChatPayService.handlePayForWechatJsapi(openid,receiverUserId,buyGold, spbill_create_ip, clientType,channelId,TradeType.JSAPI,dev);
				}
				if(srt.isSucceed()) {
					retVo = srt.getData();
					jsonRes.put(retVo.getShortName(), retVo.buildJson());
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_5000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_5000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * 接收微信支付(app支付)异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 * @param request
	 * @param response
	 */
	@RequestMapping("wechat/pay/notifyCallBack")
	public void wechatPayNotifyCallBack(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("return_code","FAIL" );
		map.put("return_msg", "ERROR");
		try {
			String fromIp = HttpUtils.getIpAddress(request);
			//获取请求数据
			String xmlData = IOUtil.readStream(request.getInputStream(), Charset.forName("utf-8"));
			//LogUtil.log.info("###notifyCallBack,str:"+xmlData);
			LogUtil.log.info(String.format("#####wechat/pay/notifyCallBack,fromIp:%s,xmlData:%s",fromIp,xmlData));
			String clientType = HttpUtils.getClientTypeStr(request);
			DeviceProperties dev = new DeviceProperties();
			dev.setClientType(clientType);
			ServiceResult<Boolean> srt = weChatPayService.dealWechatPayNotify(xmlData,TradeType.App,fromIp,dev);
			if(srt.isSucceed()) {
				map.put("return_code","SUCCESS" );
				map.put("return_msg", "OK");
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
		String resStr = XMLConverUtil.maptoXml(map);
		LogUtil.log.info("#####wechat/pay/notifyCallBack response:"+resStr);
		HttpServletResponseUtil.out(response, resStr, false);
	}
	
	/**
	 * 接收微信支付(扫码支付)异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 * @param request
	 * @param response
	 */
	@RequestMapping("wechat/pay/notifyCallBack/native")
	public void wechatPayNotifyCallBackOfMp(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("return_code","FAIL" );
		map.put("return_msg", "ERROR");
		try {
			String fromIp = HttpUtils.getIpAddress(request);
			//获取请求数据
			String xmlData = IOUtil.readStream(request.getInputStream(), Charset.forName("utf-8"));
			//LogUtil.log.info("###notifyCallBack,str:"+xmlData);
			LogUtil.log.info(String.format("#####wechatPayNotifyCallBackOfMp,fromIp:%s,xmlData:%s",fromIp,xmlData));
			String clientIp = HttpUtils.getIpAddress(request);
			ServiceResult<Boolean> srt = weChatPayService.dealWechatPayNotify(xmlData,TradeType.NATIVE,clientIp,null);
			if(srt.isSucceed()){
				map.put("return_code","SUCCESS" );
				map.put("return_msg", "OK");
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
		String resStr = XMLConverUtil.maptoXml(map);
		LogUtil.log.info("#####wechat/pay/notifyCallBack response:"+resStr);
		HttpServletResponseUtil.out(response, resStr, false);
	}
	
	/**
	 * 公众号支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("wechat/pay/jsapi/notifyCallBack")
	public void wechatPayJsapiCallBack(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("return_code","FAIL" );
		map.put("return_msg", "ERROR");
		try {
			String fromIp = HttpUtils.getIpAddress(request);
			//获取请求数据
			String xmlData = IOUtil.readStream(request.getInputStream(), Charset.forName("utf-8"));
			LogUtil.log.info(String.format("####wechatPayJsapiCallBack,fromIp:%s,xmlData:%s",fromIp,xmlData));
			ServiceResult<Boolean> srt = weChatPayService.handleWechatJsapiNotify(xmlData, fromIp);
			if(srt.isSucceed()){
				map.put("return_code","SUCCESS" );
				map.put("return_msg", "OK");
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
		}
		String resStr = XMLConverUtil.maptoXml(map);
		LogUtil.log.info("###wechatPayJsapiCallBack-response:"+resStr);
		HttpServletResponseUtil.out(response, resStr, false);
	}

}
