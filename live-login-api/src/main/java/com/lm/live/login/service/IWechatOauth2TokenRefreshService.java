package com.lm.live.login.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.login.domain.WechatOauth2TokenRefresh;

public interface IWechatOauth2TokenRefreshService extends ICommonService<WechatOauth2TokenRefresh> {

	/**
	 * 获取db中微信授权信息
	 * @param code
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	public WechatOauth2TokenRefresh getByCode(String code) throws Exception;
	
	/**
	 * 查询accessToken即将过期的数据
	 * @param soonSecond 倒计时
	 * @return
	 * @throws Exception
	 */
	public List<WechatOauth2TokenRefresh> findExpireSoonList(int soonSecond) throws Exception;
	
	/**
	 * 刷新或续期access_token
	 */
	public void reFreshToken();
	
	/**
	 * 删除所有过期的accessToken
	 */
	public void delExpireAccessToken();
}
