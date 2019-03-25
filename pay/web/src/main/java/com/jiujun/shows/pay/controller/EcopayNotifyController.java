package com.jiujun.shows.pay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.ResponseUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.service.IUnionPayService;
import com.payeco.tools.Log;

/**
 * 接收订单结果通知处理例子；本例子只举例了订单结果的参数获取；签名验证；订单状态的判断。 测试接口可以通过以下URL进行测试：
 * http://127.0.0.18080/Notify.do?Version=2.0.0&MerchantId=302020000058&MerchOrderId=1407893794150&Amount=1.00&ExtData=5rWL6K+V&OrderId=302014081300038222&Status=02&PayTime=20140814111645&SettleDate=20140909&Sign=iDQ6gBAebnh1kzSb4XN0PP3bTIXTkwG9iE8PDnNZBEiTWpBknH4XoBAotC5G/RF4E+HUa7f9esJWEI1mKw84EMDt+gBY2KABe7fejIdzqS8AH5niJEJkWAKwm4qYQTkT4Ate9lshcOZDfcyZ7eqblXXHUYOFBsYtslANOsb+/IA=
 */
@Controller("ecopayNotifyController")
public class EcopayNotifyController extends HttpServlet {

	private static final long serialVersionUID = 842960784184979704L;
	
	@Resource
	private IUnionPayService unionPayService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@RequestMapping(value="/ecopayNotify")
	protected void doEcoNotify(HttpServletRequest request , HttpServletResponse response){
		// 订单结果逻辑处理
		String retMsgJson = "{\"RetCode\":\"0000\",\"RetMsg\":\"订单已支付\"}";
		try {
			LogUtil.log.info("---交易： 订单结果异步通知-------------------------");
			// 设置编码
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			
			Map<String,String> paramMap = new HashMap<String,String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			LogUtil.log.info("doEcoNotify-requestParams:" + JsonUtil.beanToJsonString(requestParams));
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
					LogUtil.log.info(name+":"+valueStr);
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//	valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
				paramMap.put(name, valueStr);
			}
			
			String fromIp = HttpUtils.getIpAddress(request);
			LogUtil.log.info(String.format("doEcoNotify-fromIp:%s,paramMap:%s" ,fromIp, JsonUtil.beanToJsonString(paramMap)));
			
			// 结果通知参数，易联异步通知采用GET提交
			String version = request.getParameter("Version");
			String merchantId = request.getParameter("MerchantId");
			String merchOrderId = request.getParameter("MerchOrderId");
			String amount = request.getParameter("Amount");
			String extData = request.getParameter("ExtData");
			String orderId = request.getParameter("OrderId");
			String status = request.getParameter("Status");
			String payTime = request.getParameter("PayTime");
			String settleDate = request.getParameter("SettleDate");
			String sign = request.getParameter("Sign");
			
			String allParamStr = JsonUtil.beanToJsonString(paramMap);
			String clientIp = HttpUtils.getIpAddress(request);
			String clientType = HttpUtils.getClientTypeStr(request);
			DeviceProperties dev = new DeviceProperties();
			dev.setClientType(clientType);
			ServiceResult<String> srt = 
					 unionPayService.dealPaySuccessNotify(version,merchantId,
							 merchOrderId,amount,extData,orderId,status,payTime,
							 settleDate,sign,allParamStr,clientIp,dev);
			if(srt.isSucceed()) {
				retMsgJson = srt.getData();
			}
			//业务处理
			LogUtil.log.info(String.format("##############易联支付app处理结果,retMsgJson:%s", retMsgJson));
		} catch (Exception e) {
			retMsgJson = "{\"RetCode\":\"E103\",\"RetMsg\":\"处理通知结果异常\"}";
			LogUtil.log.error(e.getMessage(),e);
		} 
		Log.println("-----处理完成----");
		LogUtil.log.info(String.format("##############易联支付app处理结果,retMsgJson:%s", retMsgJson));
		//返回数据
		ResponseUtil.out(retMsgJson, request, response);
		
	}
	
}
