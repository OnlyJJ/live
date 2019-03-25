package com.lm.live.appclient.service;

import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.appclient.vo.AppInfo;
/**
 * 
 * @author shao.xiang
 * @date 2017-06-18
 */
public interface IAppErrorLogService {

	public void addErrorLog(int appType,AppInfo appInfo, DeviceProperties device);

}
