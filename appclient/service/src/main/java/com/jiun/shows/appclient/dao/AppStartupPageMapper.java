package com.jiun.shows.appclient.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.appclient.domain.AppStartupPage;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-19
 *
 */
public interface AppStartupPageMapper extends ICommonMapper<AppStartupPage> {
	
	AppStartupPage getInuseConf();
	
	List<String> findStartupPageMedia(int startupPageId);
}
