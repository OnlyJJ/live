package com.lm.live.user.service;

import java.util.List;

import com.lm.live.common.vo.Page;
import com.lm.live.user.domain.UserAttentionDo;

/**
 * 用户关注服务
 * @author shao.xiang
 * @date 2017-06-17
 *
 */
public interface IUserAttentionService {
	
	public Page pageFindFans(String toUserId, int pageNo, int pageSize);
	
	public UserAttentionDo findAttentions(String userId,String toUserId);
	
	/**
	 * 查询用户粉丝数
	 * @param userId
	 * @return
	 */
	public int getFansounts(String userId);

	/**
	 * 查询用户已关注的人数
	 * @param userId
	 * @return
	 */
	public int getAttentionCounts(String userId);
	
	/**
	 * 获取用户关注的用户
	 * @param userId
	 * @return
	 */
	List<UserAttentionDo> findAttentionUser(String userId) throws Exception;
	
	/**
	 * 获取用户关注的所有主播
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<String> findAttentionAnchor(String userId) throws Exception;
	
	/**
	 * 获取用户粉丝
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<UserAttentionDo> finUserFans(String userId) throws Exception;
}
