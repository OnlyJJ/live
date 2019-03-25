package com.jiun.shows.appclient.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.appclient.domain.AppNetproblem;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-19
 *
 */
public interface AppNetproblemMapper extends ICommonMapper<AppNetproblem> {
	AppNetproblem getAppNetproblem(String userId);
}
