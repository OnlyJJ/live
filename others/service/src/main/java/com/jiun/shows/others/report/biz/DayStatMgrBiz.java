package com.jiun.shows.others.report.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.utils.DateUntil;
import com.jiun.shows.others.report.dao.DayStatMgrMapper;
import com.jiun.shows.others.report.domain.DayStatMgr;


/**
 * 应用统计管理表
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("dayStatMgrBiz")
public class DayStatMgrBiz {
	
	@Resource
	private DayStatMgrMapper dayStatMgrMapper;

	/** 新建表名前缀 */
	private static final String TABLE_ = "t_day_stat_";
	
	public void handleTableTask() throws Exception {
		// 1、创建新表
		Date now = new Date();
		String nowStr = DateUntil.format2Str(now, "yyyyMMdd");
		String tableName = TABLE_ + nowStr;
		dayStatMgrMapper.createTable(tableName);
		
		// 2、插入表记录
		DayStatMgr dm = new DayStatMgr();
		dm.setAddtime(now);
		dm.setTbname(tableName);
		dayStatMgrMapper.insert(dm);
	}
}
