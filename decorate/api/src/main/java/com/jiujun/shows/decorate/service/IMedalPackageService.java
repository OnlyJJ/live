package com.jiujun.shows.decorate.service;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.decorate.domain.MedalPackage;

/**
 * 徽章包裹服务
 * @author shao.xiang
 * @date 2017-06-08
 *
 */
public interface IMedalPackageService {


	public void addPackage(String userId, String roomId, int medalId,
			boolean isPeriod, int number, int days, String sendMedalKey,
			 int isAccumulation,String isAdornUserMedal)throws Exception;

	public List<JSONObject> findUserMedal(String userId)throws Exception;

	public void updateIsAdornUserMedal(int medalId,String isAdornUserMedal,String userId);

	public MedalPackage getByType(String userId, String roomId, int medalId);

	public MedalPackage getByType(String uid, int medalId);
	
	public int getCount(Map<String, Object> map);

	public MedalPackage getExpireAdornUserMedal(String userId, String roomId,
			int medalId);

	
}
