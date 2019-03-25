package com.jiujun.shows.pay.controller;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.ResponseUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.service.IUnionPayService;
import com.payeco.tools.Log;

@Controller("ecopayNotifyWeb")
public class EcopayNotifyWebController {

	@Resource
	private IUnionPayService unionPayService;

	@RequestMapping("/*")
	protected void ecopayNotifyWeb(HttpServletRequest request,
			HttpServletResponse response) {
		final String CharSet = "UTF-8";
		response.setContentType("text/html");
		// 订单结果逻辑处理
		String retMsgJson = "0000";
		String response_text = null;
		String response_xml = "";
		try {
			response.setContentType("text/html");
			request.setCharacterEncoding(CharSet);
			response.setCharacterEncoding(CharSet);
			response_text = request.getParameter("response_text");
			LogUtil.log.info("ecopayNotifyWeb-response_text 解码前 : "
					+ response_text);

			String fromIp = HttpUtils.getIpAddress(request);
			String urlText = response_text;
			// 如果解出来的xml是乱码的，请将这个步骤给注释掉，有的服务器程序会自动做UrlDecode
			/*
			 * urlText = URLDecoder.decode(urlText, CharSet);
			 * LogUtil.log.info("ecopayNotifyWeb-response_text UrlDecode解码后:" +
			 * urlText);
			 */

			response_xml = new String(
					new sun.misc.BASE64Decoder().decodeBuffer(urlText), CharSet);
			LogUtil.log
					.info(String
							.format("###ecopayNotifyWeb-fromIp:%s,response_text Base64解码后,xml报文 :%s",
									fromIp, response_xml));
			String clientIp = HttpUtils.getIpAddress(request);
			String clientType = HttpUtils.getClientTypeStr(request);
			DeviceProperties dev = new DeviceProperties();
			dev.setClientType(clientType);
			ServiceResult<String> srt = unionPayService
					.dealPaySuccessNotifyFromWeb(response_xml, clientIp, dev);
			if (srt.isSucceed()) {
				retMsgJson = srt.getData();
			}
			// 业务处理
			LogUtil.log.info(String.format(
					"##############ecopayNotifyWeb处理结果,retMsgJson:%s",
					retMsgJson));
			String urlText2 = request.getParameter("response_text");
			urlText2 = URLDecoder.decode(urlText2, CharSet);
			String response_xml2 = new String(
					new sun.misc.BASE64Decoder().decodeBuffer(urlText2),
					CharSet);
			LogUtil.log
					.info(String
							.format("###ecopayNotifyWeb-fromIp:%s,response_xml2 Base64解码后,xml报文 :%s",
									fromIp, response_xml2));

		} catch (Exception e) {
			// retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
			LogUtil.log
					.error(String
							.format("##############ecopayNotifyWeb-error,response_text:%s,xml:%s",
									response_text, response_xml));
			LogUtil.log.error(e.getMessage(), e);
		}
		Log.println("-----处理完成----");
		LogUtil.log.info(String.format(
				"##############ecopayNotifyWeb易联支付web处理结果,retMsgJson:%s",
				retMsgJson));
		// 返回数据
		retMsgJson = "0000";
		ResponseUtil.out(retMsgJson, request, response);
	}

}
