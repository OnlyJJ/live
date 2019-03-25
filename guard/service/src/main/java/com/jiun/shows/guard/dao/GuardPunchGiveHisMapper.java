package com.jiun.shows.guard.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardPunchGiveHis;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardPunchGiveHisMapper extends ICommonMapper<GuardPunchGiveHis> {
	GuardPunchGiveHis getObjectByPunchGiveId(int punchGiveId);
}
