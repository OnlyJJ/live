package com.lm.live.web.controller.pay;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.ResponseUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.pay.service.IAliPayService;
import com.lm.live.pay.service.IPayChargeOrderService;

@Controller("aliPayNotifyController")
public class AliPayNotifyController extends HttpServlet {

	private static final long serialVersionUID = 4582513677508728286L;
	public static Logger log = Logger.getLogger(AliPayNotifyController.class);
	
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Resource
	private IPayChargeOrderService payChargeOrderService;
	
	@Resource
	private IAliPayService aliPayService;
	
	public AliPayNotifyController() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@RequestMapping(value = "/alipayNotify")
	public void doAlipayNotify(HttpServletRequest request, HttpServletResponse response){
		String returnMsg = SUCCESS;
		try {
			Map<String,String> paramMap = new HashMap<String,String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			LogUtil.log.info("doAlipayNotify-requestParams:" + JsonUtil.beanToJsonString(requestParams));
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
					LogUtil.log.info(name+":"+valueStr);
				}
				paramMap.put(name, valueStr);
			}
			String fromIp = HttpUtils.getIpAddress(request);
			LogUtil.log.info(String.format("doAlipayNotify-fromIP:%s,paramMap:%s",fromIp,JsonUtil.beanToJsonString(paramMap)));
			String clientIp = HttpUtils.getIpAddress(request);
			String clientType = HttpUtils.getClientTypeStr(request);
			DeviceProperties dev = new DeviceProperties();
			dev.setClientType(clientType);
			aliPayService.dealPaySuccessNotify(paramMap,clientIp,dev);
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = FAIL;
		}
		LogUtil.log.info(String.format("##############鏀粯瀹濇敮浠樺鐞嗙粨鏋�retMsgJson:%s", returnMsg));
		ResponseUtil.out(returnMsg, request, response);
	}
	
}