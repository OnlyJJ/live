package com.lm.live.appclient.service;

import com.alibaba.fastjson.JSONArray;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.appclient.vo.AppInfo;

/**
 * 
 * @author shao.xiang
 * @date 2017-06-18
 */
public interface IAppInfoService {

	/**
	 * 监测更新
	 * @param appInfo
	 * @return
	 */
	public AppInfo  checkUpate(AppInfo appInfo) throws Exception;
	

	/**
	 * 是否开启APP某些功能（1、私聊)
	 * @param userId
	 * @param deviceProperties
	 * @return
	 */
	public JSONArray appFunctionOpenCheck(String userId,
			DeviceProperties deviceProperties) throws Exception;
	

}
