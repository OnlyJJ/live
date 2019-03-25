package com.lm.live.login.dao;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.login.domain.UuidBlackList;

public interface UuidBlackListMapper extends ICommonMapper<UuidBlackList> {
	
	UuidBlackList getBlackListByUuid(String uuid);
}
