package com.lm.live.web.controller.login;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.appclient.enums.AppType;
import com.lm.live.appclient.service.IAppInstallChannelService;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.IpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MD5Util;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.Code;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Result;
import com.lm.live.common.vo.Session;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.login.constant.Constants;
import com.lm.live.login.constant.MCPrefix;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.enums.LoginType;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.ILoginService;
import com.lm.live.login.service.IUuidBlackListService;
import com.lm.live.login.vo.QQConnectUserInfoVo;
import com.lm.live.web.vo.DataRequest;

/**
 * 登录业务处理类
 * @author shao.xiang
 * @date 2018年3月9日
 *
 */
@Controller("UserInfoWeb")
public class LoginWeb extends BaseController {
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;
	
	@Resource
	private IUuidBlackListService uuidBlackListServiceImpl;
	
	@Resource
	private ILoginService loginService;
	
	
	/**
	 * L1 
	 * 自动注册（游客登录）
	 * @param data
	 * @return
	 */
	@RequestMapping(value = {"L1/{q}"} , method= {RequestMethod.POST})
	public void autoRegist(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(), ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Code.class.getSimpleName().toLowerCase())) {
				throw new LoginBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			Code code = new Code();
			code.parseJson(data.getData().getJSONObject(code.getShortName()));
			String accessToken = code.getUuid();
			String clientType = HttpUtils.getClientTypeStr(data.getRequest());
			String clientIp = HttpUtils.getUserReallyIp(data);
			// 通过设备信息IMEI校验黑名单
			String uuid = deviceProperties.getUuid();
			if(StringUtils.isNotEmpty(uuid)) {
				uuidBlackListServiceImpl.checkBlackList(uuid);
			}
			
			// 检测是否正常的app请求(即判断有没被刷接口)
			checkIfNormalAppRequest(data.getRequest(), deviceProperties);
			// 自动注册
			UserBaseInfo userBaseInfo  = loginService.autoRegist(clientType, clientIp, deviceProperties, accessToken);
			String userId = userBaseInfo.getUserId();
			Session session = new Session();
			// 自动注册结束,返回登录session
			loginService.setMemcacheToSessionId(session,userId);
			session.setFrom(LoginType.AUTO.getType()); // 1：自动注册
			jsonRes.put(userBaseInfo.getShortName(), userBaseInfo.buildJson());
			jsonRes.put(session.getShortName(), session.buildJson());
		} catch(LoginBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}

	/**
	 * L2
	 * 微信登录
	 * @param data
	 * @return
	 */
	@RequestMapping(value = {"L2/{q}"} , method= {RequestMethod.POST})
	public void appWechatLogin(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(), ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Code.class.getSimpleName().toLowerCase())) {
				throw new LoginBizException(ErrorCode.ERROR_101);
			}
			String clientIp = IpUtils.getClientIp(data.getRequest());
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			String uuid = deviceProperties.getUuid();
			String channelId = deviceProperties.getChannelId();
			if(StringUtils.isNotEmpty(channelId)) {
				if(channelId.length() > 10)
					channelId=channelId.substring(0, 10);
			}
			// 校验设备黑名单
			if (StringUtils.isNotEmpty(uuid)) {
				uuidBlackListServiceImpl.checkBlackList(uuid);
			}
			Code code = new Code();
			code.parseJson(data.getData().getJSONObject(code.getShortName()));
			String clientType = HttpUtils.getClientTypeStr(data.getRequest());
			jsonRes = loginService.appWechatLogin(code.getCode(),channelId,clientIp,clientType,deviceProperties);
		} catch(LoginBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * L3
	 * QQ登录(app)
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	@RequestMapping(value = {"L3/{q}"} , method= {RequestMethod.POST})
	public void appQQLogin(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(), ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(QQConnectUserInfoVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Code.class.getSimpleName().toLowerCase())) {
				throw new LoginBizException(ErrorCode.ERROR_101);
			}
			Code code = new Code();
			code.parseJson(data.getData().getJSONObject(code.getShortName()));
			String accessToken = code.getCode();
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			String uuid = deviceProperties.getUuid();
			String channelId = deviceProperties.getChannelId();
			if(StringUtils.isNotEmpty(channelId)) {
				if(channelId.length() > 10)
					channelId=channelId.substring(0, 10);
			}
			// 校验设备黑名单
			if (StringUtils.isNotEmpty(uuid)) {
				uuidBlackListServiceImpl.checkBlackList(uuid);
			}
			QQConnectUserInfoVo qqvo = new QQConnectUserInfoVo();
			qqvo.parseJson(data.getData().getJSONObject(qqvo.getShortName()));
			String flag = qqvo.getFlag();
			if("web".equals(flag)){
				//web端从参数UserBaseInfo中获取,以免总取到www的ip
				UserBaseInfo userbase = new UserBaseInfo();
				userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
				String clientIp = deviceProperties.getIp();
				jsonRes = loginService.webQQConnectLogin(accessToken, qqvo,channelId,clientIp);
			} else {
				String clientIp = IpUtils.getClientIp(data.getRequest());
				String clientType = HttpUtils.getClientTypeStr(data.getRequest());
				jsonRes = loginService.appQQConnectLogin(accessToken, qqvo,channelId,clientIp, clientType, deviceProperties);
			}
		} catch(LoginBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * L4
	 * 微博登录(web/app)
	 * @param data
	 * @return
	 */
	@RequestMapping(value = {"L4/{q}"} , method= {RequestMethod.POST})
	public void appWeiboLogin(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(), ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Code.class.getSimpleName().toLowerCase())) {
				throw new LoginBizException(ErrorCode.ERROR_101);
			}
			Code code = new Code();
			code.parseJson(data.getData().getJSONObject(code.getShortName()));
			String accessToken = code.getCode();
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			String uuid = deviceProperties.getUuid();
			String channelId = deviceProperties.getChannelId();
			if(StringUtils.isNotEmpty(channelId)) {
				if(channelId.length() > 10)
					channelId=channelId.substring(0, 10);
			}
			// 校验设备黑名单
			if (StringUtils.isNotEmpty(uuid)) {
				uuidBlackListServiceImpl.checkBlackList(uuid);
			}
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			String uid = userbase.getUserId();
			int appType = deviceProperties.getAppType();
			if(appType == AppType.WEB.getValue()) {
				String clientIp = deviceProperties.getIp();
				jsonRes = loginService.webWeiboLogin(accessToken, uid,channelId,clientIp);
			} else {
				String clientIp = IpUtils.getClientIp(data.getRequest());
				String clientType = HttpUtils.getClientTypeStr(data.getRequest());
				jsonRes = loginService.appWeiboLogin(accessToken, uid,channelId,clientIp,clientType,deviceProperties);
			}
		} catch(LoginBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * L5
	 * 伪登录
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月12日
	 */
	@RequestMapping(value = {"L5/{q}"} , method= {RequestMethod.POST})
	public void pseudoLogin(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(), ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Code.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())) {
				throw new LoginBizException(ErrorCode.ERROR_101);
			}
			UserBaseInfo userinfo = new UserBaseInfo();
			userinfo.parseJson(data.getData().getJSONObject(userinfo.getShortName()));
			
			Code code = new Code();
			code.parseJson(data.getData().getJSONObject(code.getShortName()));
			if(code.getCode() != null
					&& code.getCode().equals(Constants.CHECK_MD5CODE)) {
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
			long time = System.currentTimeMillis(); 
			String sessionId =  Constants.PSEUDO_LOGIN_SESSION_KEY+ MD5Util.serverEncode(data.getRequest().getSession().getId() + time);
			String uid = Constants.PSEUDO_LOGIN_SESSION_KEY+MD5Util.md5(data.getRequest().getSession().getId()+time);
			MemcachedUtil.set(MCPrefix.MC_TOKEN_PREFIX+uid, sessionId, MCTimeoutConstants.DEFAULT_TIMEOUT_10M);//秒
			//客户端ip
			DeviceProperties deviceProperties = new DeviceProperties();
			deviceProperties.parseJson(data.getData().getJSONObject(deviceProperties.getShortName()));
			String ip = deviceProperties.getIp();
			if (StrUtil.isNullOrEmpty(ip)) { //ip为空时为app请求
				ip = IpUtils.getClientIp(data.getRequest());
			}
			jsonRes = loginService.pseudoLogin(sessionId, uid, ip, time);
		} catch(LoginBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * 检测是否正常的客户端请求
	 * @param data
	 * @param deviceProperties
	 */
	private void checkIfNormalAppRequest(HttpServletRequest request,
			DeviceProperties deviceProperties) throws Exception{
		// 参数中的appType
		int paramAppTypeInt = deviceProperties.getAppType();
		// 服务端根据http的header判断的客户端
		String serverCheckAppTypeFromHttpHeader = HttpUtils.getClientTypeStr(request);
		if(paramAppTypeInt == AppType.ANDROID.getValue()){ //android
			String paramAppTypeStr = HttpUtils.androidClient;
			if(!paramAppTypeStr.equalsIgnoreCase(serverCheckAppTypeFromHttpHeader)){
				LogUtil.log.warn(String.format("###自动注册可能被攻击,服务端检测请求来源平台:%s与参数中声明的平台:%s不一致,paramAppTypeInt:%s,deviceProperties:%s", serverCheckAppTypeFromHttpHeader,paramAppTypeStr,paramAppTypeInt,JsonUtil.beanToJsonString(deviceProperties)));
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
		}else if(paramAppTypeInt == AppType.WEB .getValue()){// web
			String paramAppTypeStr = HttpUtils.webClient;
			if(!paramAppTypeStr.equalsIgnoreCase(serverCheckAppTypeFromHttpHeader)){
				LogUtil.log.warn(String.format("###自动注册可能被攻击,服务端检测请求来源平台:%s与参数中声明的平台:%s不一致,paramAppTypeInt:%s,deviceProperties:%s", serverCheckAppTypeFromHttpHeader,paramAppTypeStr,paramAppTypeInt,JsonUtil.beanToJsonString(deviceProperties)));
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
		}else{
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
	}
	
}
