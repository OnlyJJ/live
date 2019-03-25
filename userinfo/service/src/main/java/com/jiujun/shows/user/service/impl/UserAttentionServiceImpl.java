package com.jiujun.shows.user.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.user.domain.UserAttentionDo;
import com.jiujun.shows.user.service.IUserAttentionService;

@Service
public class UserAttentionServiceImpl implements IUserAttentionService {

	@Override
	public Page pageFindFans(String toUserId, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAttentionDo findAttentions(String userId, String toUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findAttentionCounts(String toUserId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int find2AttentionCounts(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserAttentionDo> findAttentionUser(String userId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findAttentionAnchor(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserAttentionDo> finUserFans(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
