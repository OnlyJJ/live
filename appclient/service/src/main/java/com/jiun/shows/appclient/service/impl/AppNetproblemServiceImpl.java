package com.jiun.shows.appclient.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.appclient.biz.AppNetproblemBiz;
import com.jiun.shows.appclient.domain.AppNetproblem;
import com.jiun.shows.appclient.service.IAppNetproblemService;
import com.jiun.shows.appclient.vo.AppSubmitPb;


/**
 * app用户网络诊断数据
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appNetproblemServiceImpl")
public class AppNetproblemServiceImpl implements IAppNetproblemService{

	@Resource
	private AppNetproblemBiz appNetproblemBiz;
	
	@Override
	public AppNetproblem findAppNetproblem(String userId) throws Exception {
		return appNetproblemBiz.findAppNetproblem(userId);
	}

	@Override
	public void handleAppSubmitProblem(String userId, AppSubmitPb vo)
			throws Exception {
		appNetproblemBiz.handleAppSubmitProblem(userId, vo);
	}

}
