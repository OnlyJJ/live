package com.lm.live.appclient.constant;

/**
 * 缓存时间
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
public class CacheTime {
	
	private CacheTime() {}
	
	/** app公告信息 ,12h*/
	public static final int APP_ARTICLE_INFO_DB_CACHE = 60 * 60 * 12;
	
	/** 记录渠道号的设备保存时长  */
	public static final int APP_HAVE_INSTALL_DEVICE_IN_CACHE_TIMEOUTSECOND = 60*60*3;
}