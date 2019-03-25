package com.jiujun.shows.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiujun.shows.user.biz.UserCacheInfoServiceBiz;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserCacheInfo;
import com.jiujun.shows.user.vo.UserInfoVo;

@Service("userCacheInfoServiceImpl")
public class UserCacheInfoServiceImpl implements IUserCacheInfoService {
	
	@Autowired
	private UserCacheInfoServiceBiz userInfoServiceBiz;
	

	@Override
	public UserInfoVo getInfoFromCache(String uid, String roomId)
			throws Exception {
		// TODO Auto-generated method stub
		return userInfoServiceBiz.getInfoFromCache(uid, roomId);
	}

	@Override
	public void removeUserCacheInfo(String userId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAndSetPesudoUserName(String userId, String ip)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserCacheInfo getOrUpdateUserInfoFromCache(String uid)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	


}
