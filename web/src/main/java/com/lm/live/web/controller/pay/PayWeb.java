package com.lm.live.web.controller.pay;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.service.IIpStoreService;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.HttpServletResponseUtil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.IOUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.utils.XMLConverUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Result;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.enums.ErrorCode;
import com.lm.live.pay.enums.PayTypeEnum;
import com.lm.live.pay.enums.WechatPayBusinessEnum.TradeType;
import com.lm.live.pay.exception.PayBizException;
import com.lm.live.pay.service.IAliPayService;
import com.lm.live.pay.service.IPayChargeOrderService;
import com.lm.live.pay.service.IWeChatPayService;
import com.lm.live.web.vo.DataRequest;
import com.lm.live.pay.vo.PayOrder;
import com.lm.live.pay.vo.WechatJSAPIVo;
import com.lm.live.pay.vo.WechatPayVo;
import com.lm.live.appclient.service.IAppInstallChannelService;

/**
 * 支付服务
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
@Controller("PayWeb")
public class PayWeb extends BaseController {
	
	@Resource
	private IAliPayService aliPayService;
	
	@Resource
	private IPayChargeOrderService payChargeOrderService;
	
	@Resource
	private IWeChatPayService weChatPayService;
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;
	
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
	@RequestMapping(value = {"P1/{q}"} , method= {RequestMethod.POST})
	public void createOrder(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(PayOrder.class.getSimpleName().toLowerCase())) {
				throw new PayBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			String userId = userbase.getUserId();
			PayOrder order = new PayOrder();
			order.parseJson(data.getData().getJSONObject(order.getShortName()));
			String ip = HttpUtils.getUserReallyIp(data);
			ServiceResult<JSONObject> srt = aliPayService.createOrder(order, dv, userId, ip);
			if(srt.isSucceed()) {
				jsonRes = srt.getData();
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch(PayBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * P2
	 * 微信支付下单接口
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	@RequestMapping(value = {"P2/{q}"} , method= {RequestMethod.POST})
	public void payWechatOrder(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(WechatPayVo.class.getSimpleName().toLowerCase())) {
				throw new PayBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			WechatPayVo wechat = new WechatPayVo();
			wechat.parseJson(data.getData().getJSONObject(wechat.getShortName()));
			String spbill_create_ip = HttpUtils.getIpAddress(data.getRequest());
			int total_free = wechat.getTotalFree();//充值金额(单位:分)
			//代充时,金币接受者
			String receiverUserId = wechat.getReceiverUserId();
			String agentUserId = null;
			String loginUserId = userbase.getUserId(); 
			//若果receiverUserId不为空,说明是给别人充值
			if(StringUtils.isNotEmpty(receiverUserId)){
				//当前登陆用户为代理人
				agentUserId = loginUserId;
				throw new PayBizException(ErrorCode.ERROR_101);
			}else{
				//当前登录用户是给自己充值,金币接受者是自己
				receiverUserId = loginUserId;
			}
			int clientType = HttpUtils.getClientTypeInt(data.getRequest());
			String channelId = dv.getChannelId();
			if(!StringUtils.isEmpty(channelId)) {
				if(channelId.length()>10)
					channelId=channelId.substring(0, 10);
			}
			
			ServiceResult<WechatPayVo> srt = weChatPayService.payUnifiedorder(receiverUserId, total_free, 
					spbill_create_ip, clientType, channelId, agentUserId, PayTypeEnum.App.getValue(), dv);
			if(srt.isSucceed()) {
				WechatPayVo ret = srt.getData();
				jsonRes.put(ret.getShortName(),  ret.buildJson());
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch(PayBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * P3
	 * 微信扫码充值
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年9月27日
	 */
	@RequestMapping(value = {"P3/{q}"} , method= {RequestMethod.POST})
	public void payWechatNative(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(WechatPayVo.class.getSimpleName().toLowerCase())) {
				throw new PayBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			WechatPayVo wechat = new WechatPayVo();
			wechat.parseJson(data.getData().getJSONObject(wechat.getShortName()));
			String spbill_create_ip = HttpUtils.getUserReallyIp(data);
			int total_free = wechat.getTotalFree();//充值金额(单位:分)
			//代充时,金币接受者
			String receiverUserId = wechat.getReceiverUserId();
			String agentUserId = null;
			String loginUserId = userbase.getUserId(); 
			//若果receiverUserId不为空,说明是给别人充值
			if(StringUtils.isNotEmpty(receiverUserId)){
				//当前登陆用户为代理人
				agentUserId = loginUserId;
			}else{
				//当前登录用户是给自己充值,金币接受者是自己
				receiverUserId = loginUserId;
			}
			int clientType = HttpUtils.getClientTypeInt(data.getRequest());
			String channelId = dv.getChannelId();
			if(!StringUtils.isEmpty(channelId)) {
				if(channelId.length()>10)
					channelId=channelId.substring(0, 10);
			}
			
			ServiceResult<WechatPayVo> srt = weChatPayService.payUnifiedorder(receiverUserId, total_free, 
					spbill_create_ip, clientType, channelId, agentUserId, PayTypeEnum.NATIVE.getValue(), dv);
			if(srt.isSucceed()) {
				WechatPayVo ret = srt.getData();
				jsonRes.put(ret.getShortName(),  ret.buildJson());
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch(PayBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * P4
	 * 公众号支付
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月9日
	 */
	@RequestMapping(value = {"P4/{q}"} , method= {RequestMethod.POST})
	public void payforWechatJsapi(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(WechatJSAPIVo.class.getSimpleName().toLowerCase())) {
				throw new PayBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = null;
			if(data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())) {
				dv = new DeviceProperties();
				dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			}
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			WechatJSAPIVo reqVo = new WechatJSAPIVo();
			reqVo.parseJson(data.getData().getJSONObject(reqVo.getShortName()));
			String channelId = null;
			if(dv != null) {
				channelId = dv.getChannelId();
			} else {
				dv = new DeviceProperties();
				channelId = reqVo.getChannelId();
			}
			if(!StringUtils.isEmpty(channelId)) {
				if(channelId.length() > 10)
					channelId=channelId.substring(0, 10);
			}
			String spbill_create_ip = HttpUtils.getIpAddress(data.getRequest());
			if(reqVo.getSpbill_create_ip() != null) {
				spbill_create_ip = reqVo.getSpbill_create_ip();
			}
			int buyGold = reqVo.getBuyGold(); // 充值金币
			String receiverUserId = reqVo.getReceiverUserId(); // 充值金币账户
			String openid= reqVo.getOpenid();
			int payType = reqVo.getPayType();
			String pkgname = reqVo.getPkgname();
			int creatType = reqVo.getCreatType();
			dv.setPackageName(pkgname);
			ServiceResult<WechatJSAPIVo> srt = null;
			if(payType == 1) {
				srt = weChatPayService.handlePayForWechatJsapi(null, receiverUserId, buyGold, 
						spbill_create_ip, creatType, channelId, TradeType.MWEB.getValue(), dv);
			} else {
				srt = weChatPayService.handlePayForWechatJsapi(openid, receiverUserId, buyGold, 
						spbill_create_ip, creatType, channelId, TradeType.JSAPI.getValue(), dv);
			}
			if(srt != null && srt.isSucceed()) {
				WechatJSAPIVo ret = srt.getData();
				jsonRes.put(ret.getShortName(),  ret.buildJson());
			} 
		} catch(PayBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
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
