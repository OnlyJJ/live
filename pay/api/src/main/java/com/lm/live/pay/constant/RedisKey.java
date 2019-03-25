package com.lm.live.pay.constant;

/**
 * 缓存key
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
public class RedisKey {
	
	private RedisKey() {}
	
	/** app公告信息缓存 */
	public static final String APP_ARTICLE_INFO_DB_CACHE = "app_article_info_db_cache";
	
	/** 已安装的渠道信息 */
	public static final String APPINSTALL_CHANNEL_INFO_CACHE = "app_cha_info_";
	
}
