package com.lm.live.web.vo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;
import com.lm.live.common.utils.GZipUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.BaseDataRequest;
import com.lm.live.common.vo.Result;
import com.lm.live.common.vo.Session;
import com.lm.live.common.vo.UserBaseInfo;

public class DataRequest extends BaseDataRequest implements Serializable {
	
	private static final long serialVersionUID = -439203251066845356L;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	/** 是否使用gzip 1是 0否 **/
	private String b;
	
	/**
	 * 从请求流中解析到的字符串
	 */
	private String requestStr;

	/** 响应参数 **/
	private Result result = null;
	
	/** 请求流中解析的data，具体的vo参数由具体业务处理 */
	private JSONObject data;

	public DataRequest() {}
	
	public DataRequest(HttpServletRequest request, HttpServletResponse response, String b) {
		this.request = request;
		this.response = response;
		this.b = b;
	}

	@Override
	public void reciveRequest(String sessionId, String uri) throws Exception {
		InputStream in = null;
		try {
			// my-todo-session共享处理
			//解密处理
			this.requestStr = this.analyzeReqStrFromStream();
			
			//登录、注册不打印log,以免被看到密码
			if(uri.indexOf("/U1/")==-1&&uri.indexOf("/U2/")==-1){
				LogUtil.log.info(String.format("%s-%s-requestStr:%s", sessionId,uri,requestStr));
			}
			// 替换回车、换行符,以避免转换成json时报错
			requestStr = StrUtil.replaceNewline(requestStr);
			
			// 字符串中的特殊字符trim掉
			requestStr = StrUtil.trimControlCharacter(requestStr);
			
			LogUtil.log.error("datarequest-requestStr=" + requestStr);
			
			if(!StringUtils.isEmpty(requestStr)){
				try {
					data = JSONObject.parseObject(requestStr);
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage() ,e);
					LogUtil.log.error(String.format("######reqUri:%s,convertJson-error:%s",uri,requestStr));
					throw new SystemDefinitionException(ErrorCode.ERROR_100);
				}
			}else{
				if(request.getRequestURI().indexOf("F") != -1){
					data = new JSONObject();
					String userid = request.getHeader("userid");
					String sessionid = request.getHeader("sessionid");
					if(!StringUtils.isEmpty(userid)){
						UserBaseInfo userBaseInfo = new UserBaseInfo();
						userBaseInfo.setUserId(userid);
						data.put(userBaseInfo.getShortName(), userBaseInfo);
					}
					//if( null != userid && userid.length() >0 ){
					if(!StringUtils.isEmpty(sessionid)){
						Session session = new Session();
						session.setSessionid(sessionid);
						data.put(session.getShortName(), session);
					}
				}
			}
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage(), e);
			throw e;
		} finally {
			try {if (null != in)in.close();} catch (Exception e2) {LogUtil.log.error(e2.getMessage(),e2);}
		}
		
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}


	public String getRequestStr() {
		return requestStr;
	}

	public void setRequestStr(String requestStr) {
		this.requestStr = requestStr;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	/**
	 * 从请求流中解析出请求参数成字符串
	 * @return
	 * @throws IOException
	 */
	private String analyzeReqStrFromStream() throws IOException{
		String requestStr = null;
		InputStream in = request.getInputStream();
			if( this.b.equals("1")){
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
				byte[] b1 = new byte[512];
				int n = 0;
				while((n=in.read(b1, 0, 512))>0){
					swapStream.write(b1, 0, n);
				}
				byte[] out =  swapStream.toByteArray();
				requestStr = GZipUtil.uncompressToString(out, "utf-8");
			}else if(this.b.equals("0")){
				BufferedReader bf =  new BufferedReader(new InputStreamReader(in));
				requestStr = bf.readLine();
			}
		return requestStr;
	}
}
