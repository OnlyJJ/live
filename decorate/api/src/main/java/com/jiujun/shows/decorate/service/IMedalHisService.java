package com.jiujun.shows.decorate.service;


import java.util.List;

import com.jiujun.shows.decorate.domain.MedalHis;

/**
 * 徽章历史记录
 * @author shao.xiang
 * @date 2017-06-08
 *
 */
public interface IMedalHisService {

	/**
	 * 发放徽章
	 * @param userId
	 * @param roomId
	 * @param level
	 */
	public List<MedalHis> findByUserAndSourceKey(String userId, String sourceKey);
	
}
