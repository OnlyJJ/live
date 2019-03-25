package com.jiun.shows.guard.service.impl;


import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.guard.domain.GuardPunchGiveHis;
import com.jiujun.shows.guard.service.IGuardPunchGiveHisService;
import com.jiun.shows.guard.dao.GuardPunchGiveHisMapper;


/**
 * Service -
 */
@Deprecated
public class GuardPunchGiveHisServiceImpl extends CommonServiceImpl<GuardPunchGiveHisMapper, GuardPunchGiveHis> implements IGuardPunchGiveHisService{

	@Override
	public GuardPunchGiveHis getGiveHisData(int punchGiveId) throws Exception {
		return this.dao.getObjectByPunchGiveId(punchGiveId);
	}
}
