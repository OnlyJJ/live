package com.jiun.shows.appclient.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.appclient.biz.AppInfoBiz;
import com.jiun.shows.appclient.service.IAppInfoService;
import com.jiun.shows.appclient.vo.AppInfo;


/**
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appInfoServiceImpl")
public class AppInfoServiceImpl implements IAppInfoService {

	@Resource
	private AppInfoBiz appInfoBiz;
	
	@Override
	public AppInfo checkUpate(AppInfo appInfo) throws Exception {
		return appInfoBiz.checkUpate(appInfo);
	}

	@Override
	public JSONArray appFunctionOpenCheck(String userId,
			DeviceProperties deviceProperties) throws Exception {
		return appInfoBiz.appFunctionOpenCheck(userId, deviceProperties);
	}


}
