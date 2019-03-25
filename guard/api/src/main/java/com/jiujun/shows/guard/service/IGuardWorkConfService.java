package com.jiujun.shows.guard.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.domain.GuardWorkConf;

/**
 * Service - 
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardWorkConfService extends ICommonService<GuardWorkConf> {
	
	/**
	 * 获取房间守护配置信息,缓存
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<GuardWorkConf> getGuardWorkConfDataCache(String roomId) throws Exception;
}
