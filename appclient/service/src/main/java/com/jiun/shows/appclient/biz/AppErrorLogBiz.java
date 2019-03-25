package com.jiun.shows.appclient.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.appclient.dao.AppErrorLogMapper;
import com.jiun.shows.appclient.domain.AppErrorLog;
import com.jiun.shows.appclient.vo.AppInfo;


/**
 * app错误日志业务
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service
public class AppErrorLogBiz {

	@Resource
	private AppErrorLogMapper appErrorLogMapper;
	
	public void addErrorLog(int appType,AppInfo appInfo, DeviceProperties device) {
		AppErrorLog errorLog = new AppErrorLog();
		errorLog.setErrorMsg(appInfo.getErrorMsg());
		if(device!=null){
			errorLog.setDeviceMsg(JsonUtil.beanToJsonString(device));
		}
		Date nowDate = new Date();
		String appVersion = appInfo.getVersion();
		String errorMsg = appInfo.getErrorMsg();
		errorLog.setRecordTime(nowDate);
		errorLog.setAppVersion(appVersion);
		errorLog.setErrorMsg(errorMsg);
		errorLog.setAppType(appType);
		appErrorLogMapper.insert(errorLog);
	}

}
