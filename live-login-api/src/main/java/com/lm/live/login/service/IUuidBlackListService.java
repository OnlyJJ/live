package com.lm.live.login.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.login.domain.UuidBlackList;

/**
 * 设备黑名单
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public interface IUuidBlackListService extends ICommonService<UuidBlackList> {
		
	
	/**检查黑名单*/
	public void checkBlackList(String uuid) throws Exception;
		
	
}
