package com.lm.live.web.controller.pay;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lm.live.common.utils.HttpServletResponseUtil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.IOUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.XMLConverUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.pay.enums.WechatPayBusinessEnum.TradeType;
import com.lm.live.pay.service.IWeChatPayService;

@Controller("WechatPayWeb")
public class WechatPayWeb {
	
	@Resource
	private IWeChatPayService weChatPayService;
	
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
			LogUtil.log.info(String.format("#####wechat/pay/notifyCallBack,fromIp:%s,xmlData:%s",fromIp,xmlData));
			String clientType = HttpUtils.getClientTypeStr(request);
			DeviceProperties dev = new DeviceProperties();
			dev.setClientType(clientType);
			ServiceResult<Boolean> srt = weChatPayService.dealWechatPayNotify(xmlData,TradeType.App,fromIp,dev);
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
