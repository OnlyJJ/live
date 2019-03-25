package com.lm.live.user.service;

import com.lm.live.user.domain.Level;

/**
 * 用户等级服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserLevelService {

	Level getObjectByUserPoint(long point);
	
	Level getObjectByAnchorPoint(long point);
	
	String getUserLevel(String userId);
	
	String getAnchorLevel(String userId);

	Level getUserLevelByLevel(int level);
	
	String getRenqLevel(String userId);
}
