package com.jiun.shows.guard.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardPunchConf;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardPunchConfMapper extends ICommonMapper<GuardPunchConf> {
	GuardPunchConf getConfData(int type);
}
