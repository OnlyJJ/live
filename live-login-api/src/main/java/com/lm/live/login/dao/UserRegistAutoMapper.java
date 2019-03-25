package com.lm.live.login.dao;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.login.domain.UserRegistAuto;

public interface UserRegistAutoMapper extends ICommonMapper<UserRegistAuto> {

	UserRegistAuto getByUserId(String userId);
	
	UserRegistAuto getByUuid(String uuId);
}
