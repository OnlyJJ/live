package com.lm.live.login.constant;


/**
 * 缓存的key
 * 登录模块使用
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class MCPrefix {
	
	/** 用户登录后，服务端派发给客户端用于数据加密token的key前缀 */
	public static final String MC_TOKEN_PREFIX = "mc_data_token_";
	public static final String MC_USERINFO_PREFIX = "mc_userinfo_";
	
	/** msgid消息资源映射关系的key前缀 */
	public static final String MC_MSGID_MAP_PREFIX = "mc_msgid_map_";
	
	/** 聊天消息中的url地址缓存的key前缀 */
	public static final String MC_MSG_URL_PREFIX = "mc_msg_url_";
	
	/** im系统中存储token */
	public static final String IM_MC_SESSION_ = "mc_session_";
	
	
	/** 系统帮未注册用户自动生成一个预登录帐号, 记录某ip对应的数量*/
	public static final String AUTO_REGIST_LIMIT_CACHE = "login:auto:ip:";
	/** 自动注册，每天的注册量限制 */
	public static final String AUTO_REGIST_LIMIT_DAY_CACHE = "login:auto:d:";
	/** 用户id库中生成的id缓存 */
	public static final String LOGIN_USERCODE_RONDOM_CACHE = "login:usercode";
}
