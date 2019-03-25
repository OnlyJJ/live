package com.jiun.shows.guard.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardConf;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardConfMapper extends ICommonMapper<GuardConf> {
	
	GuardConf getGuardConfData(int guardType, int priceType);
	
	List<GuardConf> getGuardConfAllData();
}
