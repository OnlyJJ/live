package com.lm.live.login.service.impl;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.login.dao.QQConnectUserInfoDoMapper;
import com.lm.live.login.domain.QQConnectUserInfoDo;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.QQAccessService;
import com.lm.live.login.vo.QQConnectUserInfo;

/**
 * 
 * ＱＱ接入相关业务
 *
 */
@Service
public class QQAccessServiceImpl extends CommonServiceImpl<QQConnectUserInfoDoMapper, QQConnectUserInfoDo>
	implements QQAccessService{

	@Override
	public QQConnectUserInfo getUserinfo(String accessToken, String openid) throws Exception {
		String reqUrl = SpringContextListener.getContextProValue("url_QQConnect_userinfo", "https://graph.qq.com/user/get_user_info");
		StringBuffer sbfUrl = new StringBuffer();
		sbfUrl.append(reqUrl).append("?")
		// my-todo***，这个key不知道是啥玩意，要替换
		.append("oauth_consumer_key=").append(100330589)
		.append("&access_token=").append(accessToken)
		.append("&openid=").append(openid).
		append("&format=").append("json");
		String url = sbfUrl.toString();
		String responseJsonString = HttpUtils.get(url);
		JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonString);
		QQConnectUserInfo qqConnectUserInfo =  new QQConnectUserInfo();
		BeanUtils.copyProperties(qqConnectUserInfo, responseJson);
		return qqConnectUserInfo;
	}

	@Override
	public JSONObject getAppOpenidByAccessToken(String accessToken) throws Exception{
		JSONObject o = null;
		String responseJsonString = null;
		try {
			String reqUrl = SpringContextListener.getContextProValue("url_QQConnect_getOpenid", "https://graph.qq.com/oauth2.0/me");
			StringBuffer sbfUrl = new StringBuffer();
			sbfUrl.append(reqUrl).append("?")
			.append("access_token=").append(accessToken).append("&").append("unionid=1");
			String url = sbfUrl.toString();
			//请求微信服务器，获取到json字符串
			responseJsonString = HttpUtils.get(url);
			LogUtil.log.info(String.format("###qq登录,通过token:%s从qq服务器获取的信息:%s",accessToken,responseJsonString));
			responseJsonString = responseJsonString.substring(responseJsonString.indexOf("{"), responseJsonString.lastIndexOf("}")+1);
			LogUtil.log.info(String.format("###qq登录,通过token:%s从qq服务器获取的信息,json字符串:%s",accessToken,responseJsonString));
			o = JsonUtil.strToJsonObject(responseJsonString);
		} catch (Exception e) {
			LogUtil.log.error("###qq登录通过token获取数据出错:"+responseJsonString);
			throw new LoginBizException(ErrorCode.ERROR_105);
		}
		return o;
	}

}
