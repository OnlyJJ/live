package com.jiun.shows.guard.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.guard.domain.GuardPunchConf;
import com.jiun.shows.guard.dao.GuardPunchConfMapper;


/**
 * （暂时不用，需要再实现）
 * @author shao.xiang
 * @date 2017-06-25
 */
@Service("guardPunchConfBiz")
public class GuardPunchConfBiz {

	@Resource
	private GuardPunchConfMapper guardPunchConfMapper;
	
	public GuardPunchConf getConfData(int type) throws Exception {
		return guardPunchConfMapper.getConfData(type);
	}
}
