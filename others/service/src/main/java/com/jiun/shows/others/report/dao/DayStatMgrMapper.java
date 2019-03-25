package com.jiun.shows.others.report.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.others.report.domain.DayStatMgr;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-15
 *
 */
public interface DayStatMgrMapper extends ICommonMapper<DayStatMgr> {
	/**
	 * 新建表
	 * @param tableName
	 */
	void createTable(String tableName);
}
