package com.lm.live.appclient.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.appclient.domain.UuidBlackList;

/**
 * UUID黑名单处理
 * @author HCY
 */
public interface IUuidBlackListService extends ICommonService<UuidBlackList> {
		
	public UuidBlackList getBlackListByUuid(String uuid);
	
	/**检查黑名单*/
	public void checkBlackList(String uuid) throws Exception;
		
	
}
