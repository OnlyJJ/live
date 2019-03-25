package com.lm.live.login.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.ICommonService;
import com.lm.live.login.domain.QQConnectUserInfoDo;
import com.lm.live.login.vo.QQConnectUserInfo;

/**
 * qq互联接入
 * @author shao.xiang
 * @date 2018年3月11日
 *
 */
public interface QQAccessService extends ICommonService<QQConnectUserInfoDo> {
	
	/**
	 * 拉取用户QQ个人信息
	 * @param accessToken
	 * @param openid
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	public QQConnectUserInfo getUserinfo(String accessToken,String openid) throws Exception;
	
	/**
	 * 根据token拉取openid，code等信息
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	public JSONObject getAppOpenidByAccessToken(String accessToken) throws Exception;
	

}
