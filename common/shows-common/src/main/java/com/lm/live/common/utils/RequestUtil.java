package com.lm.live.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.vo.BaseDataRequest;

public class RequestUtil {

	/**
	 * sessionId = EC1DF64DBB58F5C348EF84C8496A2E98
	 * 截取部分内容，总长度32位
	 * @param request
	 * @param beginIndex 开始截取的位置，不能大于31
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request, int beginIndex) {
		try {
			String sessionId = request.getSession().getId();
			sessionId = sessionId.substring(beginIndex);
			return sessionId;
		} catch (Exception e) {
			LogUtil.log.warn("-----session id warning!!!!");
		}
		return "ErrorSessionId";
	}

	
	public static BaseDataRequest getDataRequest(HttpServletRequest request,HttpServletResponse response) {
		BaseDataRequest dataRequest = null;
		Object reqObj =  request.getAttribute(BaseConstants.REQ_ATTR_DATA);
		if(reqObj!=null){
			dataRequest = (BaseDataRequest)reqObj;
		}
		return dataRequest;
	}
	
	
	public static void setDataRequest(HttpServletRequest request,HttpServletResponse response,BaseDataRequest dataRequest) {
		request.setAttribute(BaseConstants.REQ_ATTR_DATA, dataRequest);
	}

}
