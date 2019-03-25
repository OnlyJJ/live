package com.lm.live.login.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.login.constant.Constants;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.WeChatAccessService;
import com.lm.live.login.vo.AccessToken;
import com.lm.live.login.vo.WechatUserInfo;
import com.lm.live.user.service.IUserInfoService;

/**
 * 
 * 微信接入相关业务
 *
 */
@Service
public class WeChatAccessServiceImpl implements WeChatAccessService{
	
	 
	/** NATIVE(微信扫码)支付标示 */
	public static final String TradeTypeNATIVE = "NATIVE";
	
	/** 微信app支付标示  */
	public static final String TradeTypeApp = "APP";
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Override
	public AccessToken getAccessToken(String appid,String secret,String code) throws Exception{
		AccessToken accessToken = null; 
		try {
			String reqUrl = Constants.WECHAT_URL;
			StringBuffer sbfUrl = new StringBuffer();
			sbfUrl.append(reqUrl).append("?")
			.append("appid=").append(appid)
			.append("&secret=").append(secret)
			.append("&code=").append(code)
			.append("&grant_type=authorization_code");
			//请求微信服务器，获取到json字符串
			String responseJsonString = HttpUtils.doHttpGetForWechatLogin(sbfUrl.toString(), "", "") ;
			LogUtil.log.info(String.format("###微信登录,用code从微信服务器获取token,appid:%s,secret:%s,code:%s,取到信息:%s",appid,secret,code,responseJsonString));
			JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonString);
			accessToken = new AccessToken();
			BeanUtils.copyProperties(accessToken, responseJson);
		}catch (IOException e) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}catch (Exception e) {
			throw e;
		}  
		return accessToken;
	}

	@Override
	public WechatUserInfo getUserinfo(String accessToken, String openid) throws Exception {
		if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openid)) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		WechatUserInfo userInfo  = null;
		try {
			String reqUrl = Constants.WECHATINFO_URL;
			StringBuffer sbfUrl = new StringBuffer();
			sbfUrl.append(reqUrl).append("?")
			.append("access_token=").append(accessToken)
			.append("&openid=").append(openid)
			.append("&lang=").append("zh_CN");
			//请求微信服务器，获取到json字符串
			String responseJsonString = HttpUtils.doHttpGetForWechatLogin(sbfUrl.toString(),"utf8","");
			LogUtil.log.info(String.format("###微信登录,从微信服务器获取用户信息,accessToken:%s,openid:%s,取到信息:%s",accessToken,openid,responseJsonString));
			// 字符串中的特殊字符trim掉
			responseJsonString = StrUtil.trimControlCharacter(responseJsonString);
			JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonString);
			userInfo = new WechatUserInfo();
			BeanUtils.copyProperties(userInfo, responseJson);
			
		}catch (IOException e) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}catch (Exception e) {
			throw e;
		}  
		return userInfo;
	}

}
