package com.lm.live.login.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.login.domain.WeiboUserInfoDo;
import com.lm.live.login.vo.WeiboUserInfo;

/**
 * 新浪微博接入
 *
 */
public interface WeiboAccessService extends ICommonService<WeiboUserInfoDo>{
	
	/**
	 * 获取新浪微博用户信息
	 * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得。
	 * @param uid 需要查询的用户ID。
	 * @return
	 * @throws Exception
	 */
	public WeiboUserInfo getInfo(String accessToken,String uid) throws Exception;

}
