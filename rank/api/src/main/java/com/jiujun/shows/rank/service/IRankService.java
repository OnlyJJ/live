package com.jiujun.shows.rank.service;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.rank.vo.Kind;
import com.jiujun.shows.rank.vo.Rank;

/**
 * 榜单服务
 * @author shao.xiang
 * @date 2017-08-03
 *
 */
public interface IRankService {
	/**
	 * 获取榜单数据
	 * @param rank
	 * @param kind
	 * @return
	 */
	JSONObject getRanking(Page page, Rank rank, Kind kind);
	
	// 此处增加一个通用的榜单服务，用于处理活动之类的榜单数据
}
