package com.jiujun.shows.decorate.service;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.decorate.domain.Medal;

/**
 * 徽章服务
 * @author shao.xiang
 * @date 2017-06-08
 *
 */
public interface IMedalService {

	/**
	 * 发放徽章
	 * @param userId
	 * @param roomId
	 * @param level(护卫队等级)
	 */
	public void sendMedalService(String userId, String roomId, int level) throws Exception;

	public List<Medal> getList(String roomId,int medalId);
	
	public List<Medal> getList(String roomId,String isAdornUserMedal);
	
	public List<Medal> getList(String userId);
	
	/**
	 * 徽章命名后业务处理
	 */
	public void renameMedalFinished(String userId,String roomId,int level) throws Exception;

	/** 
	 * 守护队降级处理徽章
	 */
	public void handleMedal() throws Exception;

	public List<JSONObject> setUserMedal(String uid);
	
	//public List<Badge> setUserMedalService(String userId,String roomId)throws Exception;

	//public void handleMedalInit()throws Exception;
	
}
