package com.jiun.shows.guard.service.impl;

import java.util.List;


import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.guard.domain.GuardPunchGive;
import com.jiujun.shows.guard.service.IGuardPunchGiveService;
import com.jiun.shows.guard.dao.GuardPunchGiveMapper;


/**
 * Service -
 */
@Deprecated
public class GuardPunchGiveServiceImpl extends CommonServiceImpl<GuardPunchGiveMapper, GuardPunchGive> implements IGuardPunchGiveService{

	@Override
	public void save(int payHisId, int workId, int priceType, String userId,
			String roomId, int validate) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleGuardRelateInfo(String userId, String roomId)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GuardPunchGive> getGuardPunchGiveData(String userId,
			String roomId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
