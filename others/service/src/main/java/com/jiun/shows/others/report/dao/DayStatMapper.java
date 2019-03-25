package com.jiun.shows.others.report.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.others.report.domain.DayStat;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-15
 *
 */
public interface DayStatMapper extends ICommonMapper<DayStat> {
	void insertBath(DayStat ds);
}
