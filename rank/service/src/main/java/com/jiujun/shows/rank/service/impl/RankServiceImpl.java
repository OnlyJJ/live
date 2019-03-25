package com.jiujun.shows.rank.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.rank.biz.RankBiz;
import com.jiujun.shows.rank.service.IRankService;
import com.jiujun.shows.rank.vo.Kind;
import com.jiujun.shows.rank.vo.Rank;

/**
 * 榜单服务
 * @author shao.xiang
 * @date 2017-08-05
 *
 */
@Service("rankServiceImpl")
public class RankServiceImpl implements IRankService {

	@Resource
	private RankBiz rankBiz;
	
	@Override
	public JSONObject getRanking(Page page, Rank rank, Kind kind) {
		return rankBiz.getRanking(page, rank, kind);
	}

}
