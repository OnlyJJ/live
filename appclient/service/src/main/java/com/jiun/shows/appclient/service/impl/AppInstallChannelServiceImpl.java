package com.jiun.shows.appclient.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiun.shows.appclient.biz.AppInstallChannelBiz;
import com.jiun.shows.appclient.domain.AppInstallChannelDo;
import com.jiun.shows.appclient.service.IAppInstallChannelService;

/**
 * 
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
@Service("appInstallChannelServiceImpl")
public class AppInstallChannelServiceImpl implements IAppInstallChannelService {

	@Resource
	private AppInstallChannelBiz appInstallChannelBiz;
	
	@Override
	public ServiceResult<Boolean> testIfExistsAndCache(String mac, String imei) {
		return appInstallChannelBiz.testIfExistsAndCache(mac, imei);
	}

	@Override
	public ServiceResult<Boolean> recordChannel(DeviceProperties device) {
		return appInstallChannelBiz.recordChannel(device);
	}

	@Override
	public ServiceResult<String> getChannelIdByImei(String imei) {
		return appInstallChannelBiz.getChannelIdByImei(imei);
	}

	@Override
	public ServiceResult<AppInstallChannelDo> getByImei(String imei) {
		return appInstallChannelBiz.getByImei(imei);
	}
}
