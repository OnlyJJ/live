package com.jiujun.shows.decorate.service;


import java.util.List;

import com.jiujun.shows.decorate.domain.MedalInfo;

/**
 * 徽章信息服务
 * @author shao.xiang
 * @date 2017-06-08
 *
 */
public interface IMedalInfoService {
	
	public List<MedalInfo> getList(String roomId,int medalId);
}
