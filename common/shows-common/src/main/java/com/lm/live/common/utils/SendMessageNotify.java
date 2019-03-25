package com.lm.live.common.utils;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

@Controller("SendMessageNotify")
@RequestMapping("/sendMessageNotify")
public class SendMessageNotify {
	
	@RequestMapping(value="notifyResult" , method={RequestMethod.POST})
	public void sendSMSResult(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonStr = IOUtil.readStream(request.getInputStream(), Charset.forName("utf-8"));
		JSONObject json = JsonUtil.strToJsonObject(jsonStr);
		LogUtil.log.error("### SendMessageNotify:msisdn=" + json.getString("msisdn") +
				",status=" + json.getString("status"));
	}
}
