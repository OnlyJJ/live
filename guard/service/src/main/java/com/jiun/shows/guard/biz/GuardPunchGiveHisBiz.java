package com.jiun.shows.guard.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.guard.domain.GuardPunchGiveHis;
import com.jiun.shows.guard.dao.GuardPunchGiveHisMapper;


/**
 * （暂时不用，需要再实现）
 * @author shao.xiang
 * @date 2017-06-25
 */
@Service("guardPunchGiveHisServiceImpl")
@Deprecated
public class GuardPunchGiveHisBiz {

	@Resource
	private GuardPunchGiveHisMapper guardPunchGiveHisMapper;

	public GuardPunchGiveHis getGiveHisData(int punchGiveId) throws Exception {
		return guardPunchGiveHisMapper.getObjectByPunchGiveId(punchGiveId);
	}
}
