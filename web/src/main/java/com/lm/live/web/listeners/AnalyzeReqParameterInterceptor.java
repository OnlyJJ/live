package com.lm.live.web.listeners;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.IpUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.utils.ResponseUtil;
import com.lm.live.common.vo.Result;
import com.lm.live.web.vo.DataRequest;

/**
 * 瑙ｆ瀽璇锋眰鍙傛暟(杩欎釜鎷︽埅鍣ㄥ簲璇ユ斁鍦ㄦ墍鏈夋嫤鎴櫒鍒楄〃鐨勯)
 * @author shao.xiang
 * @date 2017锟�锟�7锟�
 *
 */
public class AnalyzeReqParameterInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			String uri = request.getRequestURI();
			String fromIp = IpUtils.getClientIp(request);
			String clientTypeDetail = HttpUtils.getClientTypeStr(request);
			String logInfo = String.format("###AnalyzeReqParameterInterceptor-[%s]-clientType:%s-fromIp:%s", uri,clientTypeDetail,fromIp);
			LogUtil.log.info(logInfo);
			String[] str = uri.split("/");
			String iscompression = str[str.length - 1];
			String sessionId = RequestUtil.getSessionId(request, 24) + "-["
					+ fromIp + "]";
			DataRequest data = new DataRequest(request, response, iscompression);
			data.reciveRequest(sessionId,uri);
			//浠庢祦涓В鏋愬嚭璇锋眰鏁版嵁鏀惧埌request浣滅敤鍩熶腑
			LogUtil.log.info("DataRequest-data.bef=" + data.getRequestStr());
			RequestUtil.setDataRequest(request, response, data);
			LogUtil.log.info("DataRequest-data.aft=" + data.getRequestStr());
			return super.preHandle(request, response, handler);
		}catch (SystemDefinitionException e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(e.getErrorCode().getResultCode(), e.getErrorCode().getResultDescr());
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(ErrorCode.ERROR_100);
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}
	}

	/**
	 * 瑙嗗浘宸插鐞嗗畬鍚庢墽琛岀殑鏂规硶锛岋拷?甯哥敤浜庨噴鏀捐祫婧愶紱
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * 鎺у埗鍣ㄧ殑鏂规硶宸茬粡鎵ц瀹屾瘯锛岃浆鎹㈡垚瑙嗗浘涔嬪墠鐨勫鐞嗭紱
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			LogUtil.log.info("forward jsp : " + modelAndView.getViewName()+ ".jsp");
		}
	}


	protected void out(String data, HttpServletRequest req,
			HttpServletResponse response) {
		 DataRequest dataRequest = (DataRequest) RequestUtil.getDataRequest( req, response);
		 if(dataRequest != null){
			 String requestStr = dataRequest.getRequestStr() ;
			 LogUtil.log.info(String.format("###begin-HttpServletResponse-out(dealResult):,responseStr:%s,requestStr:",data,requestStr));
		 }
		 ResponseUtil.out(data, req, response);
	}

}
