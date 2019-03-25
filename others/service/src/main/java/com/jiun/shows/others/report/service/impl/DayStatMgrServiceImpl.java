package com.jiun.shows.others.report.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.report.biz.DayStatMgrBiz;
import com.jiun.shows.others.report.service.IDayStatMgrService;


/**
 * 应用统计管理表
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("dayStatMgrServiceImpl")
public class DayStatMgrServiceImpl implements IDayStatMgrService{

	@Resource
	private DayStatMgrBiz dayStatMgrBiz;
	
	@Override
	public void handleTableTask() throws Exception {
		dayStatMgrBiz.handleTableTask();
	}
}
