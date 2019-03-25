package com.lm.live.user.dao;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.user.domain.UserAttentionDo;

public interface AttentionMapper extends ICommonMapper<UserAttentionDo> {
	
	/**
	 * 查询用户粉丝数
	 * @param userId
	 * @return
	 */
	int getFansounts(String userId);

	/**
	 * 查询用户已关注的人数
	 * @param userId
	 * @return
	 */
	int getAttentionCounts(String userId);
	
}
