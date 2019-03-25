package com.lm.live.common.utils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCPrefix;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;

/**
 * 跟im交互的工具类
 * @author Administrator
 *
 */
public class IMutils {
	
	
	
	private static final String imUrl = SpringContextListener.getContextProValue("httpURL_IM", ""); ;

	/**
	 * 根据im返回的字符串判断是否im发送信息成功
	 * @param imResponseMsg  im返回的json字符串
	 * @return
	 * @throws Exception
	 */
	public static boolean checkIfDoImSuccess(String imResponseMsg) throws Exception{
		boolean flag = true;
		if(StringUtils.isEmpty(imResponseMsg)){
			/*
		    Exception e = new SystemDefinitionException(ErrorCode.ERROR_404);
			LogUtil.log.error(e.getMessage(), e);
			throw e;
			*/
			return flag;
		}else{
			JSONObject imResponseJson = JsonUtil.strToJsonObject(imResponseMsg);
			String imStatusJsonKey = "status";
			String successCode = "2000";
			if(!imResponseJson.containsKey(imStatusJsonKey)||imResponseJson.get(imStatusJsonKey)==null
					||!successCode.equals(imResponseJson.get(imStatusJsonKey).toString())){
				/*
				  "{
					 status:2000,    //2000为成功，其它为失败
					 decr:""描述""
					}"
				 */
				//flag = false;
				String imResDesc = imResponseJson.get("decr").toString();
				LogUtil.log.error("#####checkIfDoImSuccess-fail:"+imResponseMsg);
				//Exception e = new SystemDefinitionException(ErrorCode.ERROR_3088,imResDesc,true);
				Exception e = new SystemDefinitionException(ErrorCode.ERROR_404);
				LogUtil.log.error(e.getMessage(), e);
				throw e;
			}
		}
		
		return flag;
	}
	
	/**
	 * 向IM系统发送消息,token在工具类里指定(请参照IM协议文档) <br />
	 * (当json中的targetid为Constants.WHOLE_SITE_NOTICE_ROOMID(100000)时发全站通知)
	 * @param funID 功能号
	 * @param seqID 序列号
	 * @param imDataBodyJson 请求数据
	 * @param senderUserId 发起者userId(必填)
	 * @return 是否发送成功
	 * @throws Exception
	 */
	public static boolean sendMsg2IM(int funID,int seqID,JSONObject imDataBodyJson,String senderUserId) throws Exception{
		if(imDataBodyJson==null || StringUtils.isEmpty(senderUserId) ){
			Exception e1 = new SystemDefinitionException(ErrorCode.ERROR_101);
			LogUtil.log.error(e1.getMessage(), e1);
			throw e1;
		}
		String imToken = getImtoken(senderUserId);
		imDataBodyJson.put("token", imToken);
		
		boolean imSuccesFlag = false;
		LogUtil.log.info(String.format("#####发送im消息-systemSendAvoidCheatMsg2Room,funID:%s,seqID:%s,data:%s",funID,seqID,imDataBodyJson.toString()));
		JSONObject imAllDataBodyJson = new JSONObject();
		imAllDataBodyJson.put("funID", funID); 
		imAllDataBodyJson.put("seqID", seqID); 
		imAllDataBodyJson.put("data", imDataBodyJson.toString());
		
		imSuccesFlag = sendMsg2IM(imAllDataBodyJson,senderUserId);
		
		return imSuccesFlag;
	}
	
	/**
	 * 向IM系统发送消息,token在工具类里指定(请参照IM协议文档) <br />
	 * (当json中的targetid为Constants.WHOLE_SITE_NOTICE_ROOMID(100000)时发全站通知)
	 * @param imAllDataBodyJson 请求数据体的全部内容
	 * @param senderUserId 发起者userId(必填)
	 * @return 是否发送成功
	 * @throws Exception
	 */
	public static boolean sendMsg2IM(JSONObject imAllDataBodyJson,String senderUserId) throws Exception{
		if(imAllDataBodyJson==null || StringUtils.isEmpty(senderUserId)){
			Exception e1 = new SystemDefinitionException(ErrorCode.ERROR_101);
			LogUtil.log.error(e1.getMessage(), e1);
			throw e1;
		}
		
		String imToken = getImtoken(senderUserId);
		String keyDataJson = "data";
		JSONObject dataJsonObj =  imAllDataBodyJson.getJSONObject(keyDataJson);
		dataJsonObj.put("token", imToken);
		imAllDataBodyJson.put(keyDataJson, dataJsonObj);
		
		boolean imSuccesFlag = false;
		LogUtil.log.info(String.format("#####发送im消息-systemSendAvoidCheatMsg2Room,imAllDataBodyJson:%s",imAllDataBodyJson.toString()));
		Map paramMap = new HashMap();
		String imAllDataBodyJsonStr = imAllDataBodyJson.toString();
		if(!StringUtils.isEmpty(imAllDataBodyJsonStr)){
			//将值编码(避免有空格等特殊字符时请求失败)
			imAllDataBodyJsonStr = URLEncoder.encode(imAllDataBodyJsonStr,"utf-8");
		}
		paramMap.put("p", imAllDataBodyJsonStr);
		//这个请求方法,im接收到的是乱码,还未解决
		//HttpUtils.post(imUrl.toString(), paramMap);
		//将值编码(避免有空格等特殊字符时请求失败)
		//imUrlSbf .append("?p=").append(URLEncoder.encode(imAllDataBodyJson.toString(),"utf-8"));
		//String imResponseMsg = HttpUtils.post(imUrl.toString());
		try {
			LogUtil.log.info(String.format("#####begin-sendMsg2IM-request-url:%s,param:%s", imUrl,JsonUtil.beanToJsonString(paramMap)));
			String imResponseMsg = HttpUtils.post2IM(imUrl,paramMap);
			LogUtil.log.info(String.format("#####end-sendMsg2IM-request-url:%s", imUrl));
			LogUtil.log.info("#####im服务器响应信息-response:"+imResponseMsg);
			imSuccesFlag = checkIfDoImSuccess(imResponseMsg);
		} catch (SystemDefinitionException e1) {
			LogUtil.log.error("###IMutils向im服务器发送Http请求发生异常");
			throw e1;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			Exception e1 = new SystemDefinitionException(ErrorCode.ERROR_404);
			LogUtil.log.error(e1.getMessage(), e1);
			throw e1;
		}
		return imSuccesFlag;
	}

	/**
	 * 根据userId生成imToken(service与im之间通过内存交互token)
	 * @param userId
	 * @return
	 */
	private static String getImtoken(String userId) {
		String imToken = Md5CommonUtils.getMD5String(userId);
		String cacheKey = MCPrefix.IM_MC_SESSION_+imToken;
		int timeoutSecond = MCTimeoutConstants.DEFAULT_TIMEOUT_5M;
		MemcachedUtil.set(cacheKey, userId, timeoutSecond);
		return imToken;
	}

	/**
	 * 赠送免费礼物茄子,所用的方法 <br />
	 * http连接、读超时设置的更短
	 * @param imAllDataBodyJson
	 * @param senderUserId
	 */
	public static boolean sendMsg2IMQiezi(JSONObject imAllDataBodyJson,
			String senderUserId) throws Exception{
		if(imAllDataBodyJson==null || StringUtils.isEmpty(senderUserId)){
			Exception e1 = new SystemDefinitionException(ErrorCode.ERROR_101);
			LogUtil.log.error(e1.getMessage(), e1);
			throw e1;
		}
		
		String imToken = getImtoken(senderUserId);
		String keyDataJson = "data";
		JSONObject dataJsonObj =  imAllDataBodyJson.getJSONObject(keyDataJson);
		dataJsonObj.put("token", imToken);
		imAllDataBodyJson.put(keyDataJson, dataJsonObj);
		
		boolean imSuccesFlag = false;
		LogUtil.log.info(String.format("#####发送im消息sendMsg2IMQiezi-systemSendAvoidCheatMsg2Room,imAllDataBodyJson:%s",imAllDataBodyJson.toString()));
		Map paramMap = new HashMap();
		String imAllDataBodyJsonStr = imAllDataBodyJson.toString();
		if(!StringUtils.isEmpty(imAllDataBodyJsonStr)){
			//将值编码(避免有空格等特殊字符时请求失败)
			imAllDataBodyJsonStr = URLEncoder.encode(imAllDataBodyJsonStr,"utf-8");
		}
		paramMap.put("p", imAllDataBodyJsonStr);
		//这个请求方法,im接收到的是乱码,还未解决
		//HttpUtils.post(imUrl.toString(), paramMap);
		//将值编码(避免有空格等特殊字符时请求失败)
		//imUrlSbf .append("?p=").append(URLEncoder.encode(imAllDataBodyJson.toString(),"utf-8"));
		//String imResponseMsg = HttpUtils.post(imUrl.toString());
		try {
			LogUtil.log.info(String.format("#####begin-sendMsg2IMQiezi-request-url:%s,param:%s", imUrl,JsonUtil.beanToJsonString(paramMap)));
			String imResponseMsg = HttpUtils.post2IMQiezi(imUrl,paramMap);
			LogUtil.log.info(String.format("#####end-sendMsg2IMQiezi-request-url:%s", imUrl));
			LogUtil.log.info("#####im服务器响应信息sendMsg2IMQiezi-response:"+imResponseMsg);
			imSuccesFlag = checkIfDoImSuccess(imResponseMsg);
		} catch (SystemDefinitionException e1) {
			LogUtil.log.error("###IMutils向im服务器发送sendMsg2IMQiezi-Http请求发生异常");
			throw e1;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			Exception e1 = new SystemDefinitionException(ErrorCode.ERROR_404);
			LogUtil.log.error(e1.getMessage(), e1);
			throw e1;
		}
		return imSuccesFlag;
		
	}

}
