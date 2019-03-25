package com.jiujun.shows.guard.service;

import java.util.List;

import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.domain.GuardConf;


/**
 * Service - 守护配置服务
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardConfService {
	/**
	 * 根据守护类型和价格类型获取守护配置信息
	 * @param guardType
	 * @param priceType
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<GuardConf> getGuardConfData(int guardType, int priceType);
	
	/**
	 * 获取所有守护配置信息
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<List<GuardConf>> getGuardConfAllDataCache();
	
}
