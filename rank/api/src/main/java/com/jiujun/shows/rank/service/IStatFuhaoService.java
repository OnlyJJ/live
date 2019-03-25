package com.jiujun.shows.rank.service;

import java.util.Date;
import java.util.List;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.rank.domain.StatFuhaoDo;

/**
 * 用户送礼汇总服务
 * @author shao.xiang
 * @date 2017-07-18
 */
public interface IStatFuhaoService extends ICommonService<StatFuhaoDo>{
	/**
	 * 根据时间获取前几位用户数据
	 * @param timeframe 时间
	 * @param size 长度
	 * @return
	 */
	public List<StatFuhaoDo> getFuhaoSortDataCache(Date timeframe, int size) throws Exception;
}
