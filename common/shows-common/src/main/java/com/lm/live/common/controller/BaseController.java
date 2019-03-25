package com.lm.live.common.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.ResponseUtil;

/**
 * controller基类
 * @author shao.xiang
 * @date 2017-06-05
 */
public class BaseController {
	
	/**
	 * 写回http请求
	 * @param data 服务端处理后的结果
	 * @param request
	 * @param response
	 * @param isPrintLog 是否打印结果（建议结果集较大的不打印，例如查询数据接口）
	 * @author shao.xiang
	 * @date 2017年9月13日
	 */
	public void out(JSONObject data, HttpServletRequest request, HttpServletResponse response,
			String isCompress) {
		boolean flagIsCompress = true;
		if(StringUtils.isEmpty(isCompress) || !"1".equals(isCompress)){
			flagIsCompress = false;
		}
		String responseStr = JsonUtil.beanToJsonString(data);	
		ResponseUtil.out(responseStr, request, response, flagIsCompress);
	}
	
	/**
	 * 输出请求处理结果信息<br>
	 * 	（建议结果较大的，如查询数据接口，避免打印详细处理结果，只输响应时间即可，避免日志过多）
	 * @param log 模块定义的log
	 * @param requestStr 请求参数
	 * @param spendTimes 处理时间
	 * @param responStr 相应结果，json字符串
	 * @param isLogDetileInfo 是否输出详细处理结果
	 * @author shao.xiang
	 * @date 2017年9月13日
	 */
	public static void handleInfo(Logger log, HttpServletRequest request, 
			String requestStr, long spendTimes, String responStr, boolean isLogDetileInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("### ").append(log.getName()).append("-[")
		.append(request.getRequestURL()).append("]")
		.append(", requestStr = ").append(requestStr)
		.append(", clientIp = ").append(HttpUtils.getClientTypeStr(request))
		.append(", clientType = ").append(HttpUtils.getClientTypeStr(request));
		if(!isLogDetileInfo) {
			responStr = "......";
		}
		sb.append(", responStr = ").append(responStr);
		sb.append(", spendTimes = ").append(spendTimes);
		log.info(sb.toString());
	}

}
