package com.jiun.shows.appclient.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.appclient.domain.AppInfo;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-19
 *
 */
public interface AppInfoMapper extends ICommonMapper<AppInfo> {
	
	AppInfo getNewestVersion(Integer appType, String packageName,
			String signatures);
}
