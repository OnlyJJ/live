package com.jiujun.shows.car.listeners;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.car.vo.DataRequest;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.IpUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.RequestUtil;
import com.jiujun.shows.common.utils.ResponseUtil;
import com.jiujun.shows.common.vo.Result;

/**
 * 解析请求参数
 * @author shao.xiang
 * @date 2017年7月26日
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
			//LogUtil.log.info(logInfo);
			String[] str = uri.split("/");
			String iscompression = str[str.length - 1];
			String sessionId = RequestUtil.getSessionId(request, 24) + "-["
					+ fromIp + "]";
			DataRequest data = new DataRequest(request, response, iscompression);
			data.reciveRequest(sessionId,uri);
			//从流中解析出请求数据�?放到request作用域中
			RequestUtil.setDataRequest(request, response, data);
			return super.preHandle(request, response, handler);
		}catch (SystemDefinitionException e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(e.getErrorCode());
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			Result result = new Result(ErrorCode.ERROR_2008);
			JSONObject json = new JSONObject();
			json.put(result.getShortName(), result.buildJson());
			out(json.toString(), request, response);
			return false;
		}

	}

	/**
	 * 视图已处理完后执行的方法，常用于释放资源；
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * 控制器的方法已经执行完毕，转换成视图之前的处理；
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
