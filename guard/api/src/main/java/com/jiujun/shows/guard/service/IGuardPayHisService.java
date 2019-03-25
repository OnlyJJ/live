package com.jiujun.shows.guard.service;


import com.jiujun.shows.framework.service.ServiceResult;

/**
 * Service - 守护购买记录表，用户每购买或续期一次，都增加一条记录
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardPayHisService {
	/**
	 * 购买守护，异常回滚,该方法只处理首次购买
	 * @param userId
	 * @param roomId
	 * @param type
	 * @param priceType
	 * @throws Exception
	 */
	// my-service，如果不抛出异常，在biz中定义的事务是否可行？此处先去掉异常外抛
	public ServiceResult<Boolean> payForGuard(String userId, String anchorId, String roomId,int workId, int guardType, int priceType);
	
}
