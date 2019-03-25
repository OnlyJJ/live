package com.lm.live.login.service;

import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.login.vo.AccessToken;
import com.lm.live.login.vo.WechatUserInfo;

/**
 * 微信接入
 *
 */
public interface WeChatAccessService {
	
	/**
	 * 通过code获取access_token
	 * @param appid
	 * @param secret
	 * @param code
	 * @throws Exception
	 */
	public AccessToken getAccessToken(String appid,String secret,String code) throws Exception;
	
	
	/**
	 * 请求腾讯服务器,获取用户个人信息
	 * @param accessToken
	 * @param openid
	 * @throws Exception
	 */
	public WechatUserInfo getUserinfo(String accessToken,String openid) throws Exception;
	
}
