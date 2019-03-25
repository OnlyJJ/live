package com.lm.live.user.service;

import com.lm.live.user.vo.UserCacheInfo;
import com.lm.live.user.vo.UserInfoVo;

/**
 * 用户缓存服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserCacheInfoService {
	
	/**
	 * 从cache中获取用户在房间内的信息
	 * @param uid 用户id
	 * @param roomId 房间id(依据roomId判断用户身份:主播、房管、普通用户),参数为空则返回标示为普通用户
	 * @return
	 * @throws Exception
	 */
	public UserInfoVo getInfoFromCache(String uid,String roomId) throws Exception;
	
	
	/**
	 * 删除用户的缓存信息
	 * @param userId
	 * @throws Exception
	 */
	public void removeUserCacheInfo(String userId) throws Exception;
	
	/**
	 * 获取/设置游客的昵称
	 * @param userId
	 * @param ip 客户端ip
	 * @throws Exception
	 */
	public String getAndSetPesudoUserName(String userId,String ip) throws Exception;

	public UserCacheInfo getOrUpdateUserInfoFromCache(String uid) throws Exception;
}
