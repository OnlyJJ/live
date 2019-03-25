package com.jiujun.shows.decorate.service;

import java.util.List;

import com.jiujun.shows.decorate.domain.DecorateHis;

/**
 * Service - 勋章获取历史
 * @author shao.xiang
 * @date 2017-06-08
 */
public interface IDecorateHisService {
	/**
	 * 查询主播最近获得的某类勋章记录
	 * @param userId
	 * @param decorateId
	 * @return
	 * @throws Exception
	 */
	public DecorateHis getLatelyDecorateHis(String userId, int decorateId) throws Exception;
	
	/**
	 * 根据用户和来源key查找
	 * @param userId
	 * @param sourceKey
	 * @return
	 * @throws Exception
	 */
	public List<DecorateHis> findByUserAndSourceKey(String userId,String sourceKey) throws Exception;
	
	/**
	 * 根据用户和来源key查找并按decorateId排序
	 * @param userId
	 * @param sourceKey
	 * @return
	 * @throws Exception
	 */
	public List<DecorateHis> findByUserAndSourceKeyOrder(String userId,String sourceKey) throws Exception;

	/**
	 * 获取用户拿到勋章历史纪录
	 * @param userId 必传
	 * @param decorateId 必传
	 * @param sourceKey 必传
	 * @return List
	 * @throws Exception
	 */
	public List getLatelyDecorateHisBySourcekey(String userId, int decorateId,
			String sourceKey) throws Exception;
	
}
