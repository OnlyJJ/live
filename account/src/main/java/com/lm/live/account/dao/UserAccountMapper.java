package com.lm.live.account.dao;

import com.lm.live.account.domain.UserAccount;
import com.lm.live.common.dao.ICommonMapper;

public interface UserAccountMapper extends ICommonMapper<UserAccount> {

	UserAccount getUserAccount(String userId);
}
