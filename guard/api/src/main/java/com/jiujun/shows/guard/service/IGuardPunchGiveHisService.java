package com.jiujun.shows.guard.service;

import com.jiujun.shows.guard.domain.GuardPunchGiveHis;

/**
 * Service - 
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardPunchGiveHisService {
	/**
	 * 根据punchGiveId 获取赠送记录 
	 * @param punchGiveId
	 * @return
	 * @throws Exception
	 */
	public GuardPunchGiveHis getGiveHisData(int punchGiveId) throws Exception;
}
