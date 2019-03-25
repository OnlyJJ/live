package com.lm.live.login.dao;

import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.login.domain.WechatOauth2TokenRefresh;

public interface WechatOauth2TokenRefreshMapper extends ICommonMapper<WechatOauth2TokenRefresh> {
	
	WechatOauth2TokenRefresh getByCode(String code);

	List<WechatOauth2TokenRefresh> findExpireSoonList(int soonSecond);

	/**
	 * 删除所有过期的accessToken
	 */
	void delExpireAccessToken();

}
