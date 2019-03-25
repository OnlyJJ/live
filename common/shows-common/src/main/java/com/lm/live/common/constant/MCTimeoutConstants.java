package com.lm.live.common.constant;

/**
 * memcache 
 * 超时常量配置，此常量维护的是常用的，如需详细设置，则各个业务自行维护
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class MCTimeoutConstants {
	
	/**
	 * 默认缓存时间,5main
	 */
	public final static int DEFAULT_TIMEOUT_5M = 60 * 5;
	
	/**
	 * 默认缓存时间,10main
	 */
	public final static int DEFAULT_TIMEOUT_10M = 60 * 10;
	
	/**
	 * 默认缓存时间,30main
	 */
	public final static int DEFAULT_TIMEOUT_30M = 60 * 30;
	
	/**
	 * 默认缓存时间, 2H
	 */
	public final static int DEFAULT_TIMEOUT_2H = 60 * 60 * 2;
	
	/**
	 * 默认缓存时间, 8H
	 */
	public final static int DEFAULT_TIMEOUT_8H = 60 * 60 * 8;
	
	/**
	 * 默认缓存时间, 24H
	 */
	public final static int DEFAULT_TIMEOUT_24H = 60 * 60 * 24;
	
}
