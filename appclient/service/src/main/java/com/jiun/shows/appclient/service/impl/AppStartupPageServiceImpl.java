package com.jiun.shows.appclient.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.appclient.biz.AppStartupPageBiz;
import com.jiun.shows.appclient.service.IAppStartupPageService;
import com.jiun.shows.appclient.vo.AppStartupPageVo;


/**
 * 
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appStartupPageServiceImpl")
public class AppStartupPageServiceImpl implements IAppStartupPageService {

	@Resource
	private AppStartupPageBiz appStartupPageBiz;
	

	@Override
	public AppStartupPageVo getAppStartupPage() throws Exception {
		return appStartupPageBiz.getAppStartupPage();
	}

}
