package com.jiun.shows.appclient.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.appclient.domain.AppInstallChannelDo;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-19
 *
 */
public interface AppInstallChannelMapper extends ICommonMapper<AppInstallChannelDo> {
	AppInstallChannelDo getByImei(String imei);
	
	String getChannelIdByImei(String imei);
}
