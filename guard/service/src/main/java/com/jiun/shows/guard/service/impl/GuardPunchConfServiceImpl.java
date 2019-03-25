package com.jiun.shows.guard.service.impl;

import javax.annotation.Resource;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.guard.domain.GuardPunchConf;
import com.jiujun.shows.guard.service.IGuardPunchConfService;
import com.jiun.shows.guard.biz.GuardPunchConfBiz;
import com.jiun.shows.guard.dao.GuardPunchConfMapper;


/**
 * Service -
 * @author shao.xiang
 * @date 2017-06-29
 */
@Deprecated
public class GuardPunchConfServiceImpl extends CommonServiceImpl<GuardPunchConfMapper, GuardPunchConf> implements IGuardPunchConfService{

	@Resource
	private GuardPunchConfBiz guardPunchConfBiz;

	@Override
	public GuardPunchConf getConfData(int type) throws Exception {
		return guardPunchConfBiz.getConfData(type);
	}
}
