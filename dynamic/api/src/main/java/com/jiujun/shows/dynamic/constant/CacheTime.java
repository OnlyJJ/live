package com.jiujun.shows.dynamic.constant;

/**
 * 缓存时间
 * @author shao.xiang
 * @date 2017-06-05
 *
 */
public class CacheTime {
	
	private CacheTime() {}
	
	/** 默认动态所有功能的缓存时间，30天 */
	public static final int DIARY_DEFAULT_CACHE_TIME = 60 * 60 * 24 * 30;
	
	/** 所有动态信息缓存，1天 */
	public static final int DYNAMIC_ALL_INFOS_TIME = 60 * 60 * 24;
	
	/** 用户使用动态功能权限缓存 时间，30天*/
	public static final int USER_POWER_STATUS_TIME = 60 * 60 * 24 * 30;
	
	/** 动态评论缓存，30天 */
	public static final int DIARYINFO_COMMENT_TIME = 60 * 60 * 24 * 30;
	
	/** 动态消息缓存 */
	public static final int DIARYINFO_COMMENT_MSG_TIME = 60 * 60 * 24 * 30;
	
	/** 动态点赞缓存 缓存30天*/
	public static final int DIARYINFO_FAVOURATE_TIME = 60 * 60 * 24 * 30;
}
