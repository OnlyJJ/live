package com.jiujun.shows.dynamic.constant;

/**
 * 缓存key
 * @author shao.xiang
 * @date 2017-06-05
 *
 */
public class CacheKey {
	
	private CacheKey() {}
	
	/** 动态信息，缓存所有动态,格式：key 更新该缓存key时，需要同步修改admin端*/
	public static final String DYNAMIC_ALL_INFOS_CACHE = "diary_all_infos";
	
	/** 用户个人所有动态信息，格式：key + userId */
	public static final String DYNAMIC_USER_ALL_INFOS_CACHE = "diary_user_all_infos_";
	
	/** 用户使用动态功能权限缓存，当后台管理员进行操作时，需要后台删除该缓存 */
	public static final String USER_POWER_STATUS_CACHE = "user_power_status_";
	
	/** 动态的评论缓存 ，格式：key + diaryInfoId 更新该缓存key时，需要同步修改admin端*/
	public static final String DIARYINFO_COMMENT_CACHE = "diary_comment_id_";
	
	/** 动态的评论总数缓存 ,格式： key + userId + "_" + diaryInfoId ，更新该缓存key时，需要同步修改admin端*/
	public static final String DIARYINFO_COMMENT_TOTAL_CACHE = "diary_comment_total_";
	
	/** 个人动态喜好操作缓存，每个人，每条动态点赞或踩各一次 */
	public static final String DIARY_FAVOURATE_CACHE = "diary_favourate_";
	
	/** 动态的喜好缓存 ,格式： key + diaryInfoId + "_" + type (点赞或踩)*/
	public static final String DIARY_COMMENT_FAVOURATE_TOTAL_CACHE = "diary_comment_favourate_total_";
	
	/** 动态图片集格式： key + diaryInfoId */
	public static final String DIARY_IMAGES_CACHE = "diary_images_";
	
	/** 用户动态或评论被评论后，收到的消息缓存，格式：key + userId */
	public static final String DIARY_COMMENT_MSG_CACHE = "diary_comment_msg_";
	
	/** 用户所有评论消息缓存，格式： key + userId + "_" + pageNum(分页页码)*/
	public static final String DIARY_ALL_COMMENT_CACHE = "diary_all_comments_";
	
	/** 动态所有点赞用户缓存，格式：key + diaryInfoId */
	public static final String DIARY_ALL_FAVOURATE_USER_CACHE = "prize_all_users_";
}
