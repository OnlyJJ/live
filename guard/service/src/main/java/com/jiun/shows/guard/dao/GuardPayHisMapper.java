package com.jiun.shows.guard.dao;

import java.util.Date;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardPayHis;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardPayHisMapper extends ICommonMapper<GuardPayHis> {
	int getUserGuardCount(String userId, String roomId, Date startTime,
			Date endTime);
}
