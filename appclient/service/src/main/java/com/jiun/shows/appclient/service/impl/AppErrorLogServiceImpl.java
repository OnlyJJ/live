package com.jiun.shows.appclient.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.appclient.biz.AppErrorLogBiz;
import com.jiun.shows.appclient.service.IAppErrorLogService;
import com.jiun.shows.appclient.vo.AppInfo;


/**
 * app错误日志实现类
 * @author shao.xiang
 * @date 2017-09-15
 * 
 */
@Service("appErrorLogServiceImpl")
public class AppErrorLogServiceImpl implements IAppErrorLogService {

	@Resource
	private AppErrorLogBiz appErrorLogBiz;
	
	@Override
	public void addErrorLog(int appType, AppInfo appInfo,
			DeviceProperties device) {
		appErrorLogBiz.addErrorLog(appType, appInfo, device);
	}

}
