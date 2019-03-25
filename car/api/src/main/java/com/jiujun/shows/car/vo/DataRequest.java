package com.jiujun.shows.car.vo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.common.utils.GZipUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.StrUtil;
import com.jiujun.shows.common.vo.BaseDataRequest;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.common.vo.Session;

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

	/** 用户登录token  **/
	private Session session = null;
	
	private AnchorInfo anchorInfo;
	
	private UserBaseInfo userBaseInfo;
	
	/**列表**/
	List<Object> list = null;
	
	List<Object> dataList = null;
	
	/** 设备参数 **/
	DeviceProperties deviceProperties = null;
	
	private CarVo carVo;

	private CarportVo carportVo;
	
	/** 分页信息 **/
	private Page page = null;
	
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
			
			if(!StringUtils.isEmpty(requestStr)){
				JSONObject jsons;
				try {
					jsons = JSONObject.parseObject(requestStr);
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage() ,e);
					LogUtil.log.error(String.format("######reqUri:%s,convertJson-error:%s",uri,requestStr));
					Exception e1 = new CarBizException(ErrorCode.ERROR_11000);
					throw e1;
				}
				//解析数组
				if (jsons.containsKey("visit")) {
					JSONArray jsona = jsons.getJSONArray("visit");
					LogUtil.log.info(" 数组："+jsona.toString());
					list = new ArrayList<Object>();
					for(int i=0 ; i < jsona.size() ; i++ ){
						AnchorInfo abi = new AnchorInfo();
						abi.parseJson(jsona.getJSONObject(i));
						list.add(abi);
					}
				}
				
				//解析数组
				if (jsons.containsKey("dataList")) {
					JSONArray jsona = jsons.getJSONArray("dataList");
					dataList = new ArrayList<Object>();
					for(int i=0 ; i < jsona.size() ; i++ ){
						dataList.add(jsona.getJSONObject(i));
					}
				}
				
				// 解析协议Bean
				if (jsons.containsKey(Result.class.getSimpleName().toLowerCase())) {
					result = new Result();
					result.parseJson(jsons.getJSONObject(result.getShortName()));
				}
	
				if (jsons.containsKey(AnchorInfo.class.getSimpleName().toLowerCase())) {
					anchorInfo = new AnchorInfo();
					anchorInfo.parseJson(jsons.getJSONObject(anchorInfo.getShortName()));
				}
				
				if (jsons.containsKey(DeviceProperties.class.getSimpleName().toLowerCase())) {
					deviceProperties = new DeviceProperties();
					deviceProperties.parseJson(jsons.getJSONObject(deviceProperties.getShortName()));
				}
				
				if (jsons.containsKey(Page.class.getSimpleName().toLowerCase())) {
					page = new Page();
					page.parseJson(jsons.getJSONObject(page.getShortName()));
				}
				
				if (jsons.containsKey(CarVo.class.getSimpleName().toLowerCase())) {
					carVo = new CarVo();
					carVo.parseJson(jsons.getJSONObject(carVo.getShortName()));
				}
				
				if (jsons.containsKey(CarportVo.class.getSimpleName().toLowerCase())) {
					carportVo = new CarportVo();
					carportVo.parseJson(jsons.getJSONObject(carportVo.getShortName()));
				}
				
				
			}else{
				if(request.getRequestURI().indexOf("F") != -1){
					String userid = request.getHeader("userid");
					String sessionid = request.getHeader("sessionid");
					if(!StringUtils.isEmpty(userid)){
						userBaseInfo = new UserBaseInfo();
						userBaseInfo.setUserId(userid);
					}
					//if( null != userid && userid.length() >0 ){
					if(!StringUtils.isEmpty(sessionid)){
						session = new Session();
						session.setSessionid(sessionid);
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

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public List<Object> getDataList() {
		return dataList;
	}

	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}

	public DeviceProperties getDeviceProperties() {
		return deviceProperties;
	}

	public void setDeviceProperties(DeviceProperties deviceProperties) {
		this.deviceProperties = deviceProperties;
	}

	public UserBaseInfo getUserBaseInfo() {
		return userBaseInfo;
	}

	public void setUserBaseInfo(UserBaseInfo userBaseInfo) {
		this.userBaseInfo = userBaseInfo;
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

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}


	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public AnchorInfo getAnchorInfo() {
		return anchorInfo;
	}

	public void setAnchorInfo(AnchorInfo anchorInfo) {
		this.anchorInfo = anchorInfo;
	}

	public CarVo getCarVo() {
		return carVo;
	}

	public void setCarVo(CarVo carVo) {
		this.carVo = carVo;
	}

	public CarportVo getCarportVo() {
		return carportVo;
	}

	public void setCarportVo(CarportVo carportVo) {
		this.carportVo = carportVo;
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
