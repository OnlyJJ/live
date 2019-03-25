package com.jiujun.shows.guard.service;

import com.jiujun.shows.guard.domain.GuardPunchConf;

/**
 * Service - 
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardPunchConfService {
	/**
	 * 根据类型获取配置信息
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public GuardPunchConf getConfData(int type) throws Exception;
}
