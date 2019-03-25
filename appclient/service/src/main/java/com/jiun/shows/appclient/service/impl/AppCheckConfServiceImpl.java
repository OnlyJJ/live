package com.jiun.shows.appclient.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.appclient.biz.AppCheckConfBiz;
import com.jiun.shows.appclient.service.IAppCheckConfService;


/**
 * app配置相关服务实现
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
@Service("appCheckConfServiceImpl")
public class AppCheckConfServiceImpl implements IAppCheckConfService {

	@Resource
	private AppCheckConfBiz appCheckConfBiz;
	
	@Override
	public boolean appPackageCheck(DeviceProperties deviceProperties) {
		return appCheckConfBiz.appPackageCheck(deviceProperties);
	}

	
}
