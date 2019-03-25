package com.jiujun.shows.dynamic.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryUserManager;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-04
 *
 */
public interface DiaryUserManagerMapper extends ICommonMapper<DiaryUserManager> {
	
	DiaryUserManager getUserManagerByUserId(String userId);
}
