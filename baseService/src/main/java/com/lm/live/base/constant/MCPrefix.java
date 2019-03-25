package com.lm.live.base.constant;



/**
 * 缓存的key
 * 登录模块使用
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class MCPrefix {
	
	//缓存key
	public static final String PROVINCE_CODE_CACHE = "province:code:";
	
	//每次缓存省市记录时间间隔key,24h
	public static final String PROVINCE_TIME_CACHE = "province:time";
	
	/** 用户ip归属地缓存*/
	public static final String USER_IP_REGION_CACHE = "ip:region:";
}
