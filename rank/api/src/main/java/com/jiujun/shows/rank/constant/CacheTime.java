package com.jiujun.shows.rank.constant;

/**
 * 缓存时间
 * @author shao.xiang
 * @date 2017-06-05
 *
 */
public final class CacheTime {
	

	/** 首页、房间(魅力、财富)排行(总、月、周、日)榜的缓存时长*/
	public static final int RANK_TIMEOUTSECOND = 60*3;
	
	/** 首页、房间(魅力、财富)排行(总、月、周、日)榜的二级缓存时长*/
	public static final int RANK_SEC_TIMEOUTSECOND = 60*10;
	
	/** 首页、房间(魅力、财富)排行(总、月、周、日)榜的缓存时长 ， 房间内手动刷新富豪榜,缓存时间不要太长*/
	public static final int RANK_ATROOM_HANDACTIVE_TIMEOUTSECOND = 60;
}
