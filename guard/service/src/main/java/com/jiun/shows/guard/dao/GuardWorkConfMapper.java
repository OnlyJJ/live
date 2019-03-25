package com.jiun.shows.guard.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardWorkConf;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardWorkConfMapper extends ICommonMapper<GuardWorkConf> {
	GuardWorkConf getGuardWorkConfData(String roomId);
}
