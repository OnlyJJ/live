package com.jiun.shows.others.report.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.others.report.biz.DayStatBiz;
import com.jiun.shows.others.report.service.IDayStatService;
import com.jiun.shows.others.report.vo.ReportVo;


/**
 * 每日统计数据表
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("dayStat20170822ServiceImpl")
public class DayStatServiceImpl implements IDayStatService{

	@Resource
	private DayStatBiz dayStatBiz;
	
	@Override
	public void handleReport(ReportVo vo, DeviceProperties dp) throws Exception {
		dayStatBiz.handleReport(vo, dp);
	}

}
