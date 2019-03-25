package com.jiun.shows.others.push.service;

import java.util.List;
import java.util.Map;

import com.jiun.shows.others.push.domain.PushDev;

/**
 * Service - 客户端推送消息设备信息记录表
 * @author shao.xiang
 * @date 2017-06-15
 */
public interface IPushDevService {
	/**
	 * 保存用户注册信鸽信息
	 * @param userId
	 * @param token
	 * @param appType
	 * @param pckName
	 * @throws Exception
	 */
	void savePushDev(String userId, String token, int appType, String pckName) throws Exception;
	
	/**
	 * 向关注主播的粉丝推送开播消息
	 * @param anchorId
	 * @param custom
	 */
	void pushLiveStartMsg(String anchorId, Map<String,Object> custom);
	
	/**
	 * 管理后台自定义推送的消息
	 * @param configId
	 * @throws Exception
	 */
	void pushMSGByConfig(int configId) throws Exception;
	
	/**
	 * 从缓存中获取注册信息
	 * @param userId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	PushDev getPushDevFromCache(String userId, String token) throws Exception;
	
	/**
	 * 推送单个账户，android使用
	 * @param deviceType
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	void pushAndroidSingleAccount(int deviceType, String userId,  long accessId, String secretKey,
			final String title, final String content);
	
	/**
	 * 推送单个账户，IOS使用
	 * @param deviceType
	 * @param userId 
	 * @param message
	 * @param environment IOSENV_DEV：开发环境，IOSENV_PROD：生产环境
	 * @return
	 * @throws Exception
	 */
	void pushIOSSingleAccount(int deviceType, String userId, long accessId, String secretKey,
			final String title, final String content);
	
	/**
	 * 推送多个账户，10000个以内，android使用<br>
	 * @param parms 根据需要封装参数(key固定如下，需要按照本约定传参)：<br>
	 * 			accountList 接受消息的账户列表<br>
	 *  		accessId    应用注册信鸽的授权id<br>
	 *  		secretKey   应用注册信鸽服务的key<br>
	 * 			title       消息标题<br>
	 * 			content     消息内容<br>
	 * 			type        打开类型，0：打开到首页，1：打开到房间，2：打开到活动页面<br>
	 * @param custom 封装房间参数<br>
	 * 		参数列表说明(key固定如下，需要按照本约定传参)：：<br>
	* 			roomId		房间id<br>
	 *			targetId	房间主播id<br>
	 *			avatar		主播头像<br>
	 *			nickname	主播昵称<br>
	 *			userLevel	主播用户等级<br>
	 *			anchorLevel	主播等级<br>
	 *			uri			活动uri
	 */
	void pushAndroidAccountList(final List<String> accountList, final Map<String, Object> parms,final Map<String, Object> custom);
	
	/**
	 *  推送多个账户，10000个以内，IOS使用
	 *  @param parms 根据需要封装参数(key固定如下，需要安装本约定传参)：
	 * 			accountList 接受消息的账户列表
	 *  		accessId    应用注册信鸽的授权id
	 *  		secretKey   应用注册信鸽服务的key
	 * 			title       消息标题
	 * 			content     消息内容
	 * 			uri			活动uri
	 * 			type        打开类型，0：打开到首页，1：打开到房间，2：打开到活动页面，该参数封装到custom中
	 * @param custom 封装房间参数
	 * 		参数列表说明(key固定如下，需要按照本约定传参)：
	 * 			type 		值使用上面的parms，因为android只需判断类型，不需要封装到custom，因此type在入口放入了parms，ios必须封装到custom中
	 * 			roomId		房间id
	 *			targetId	房间主播id
	 *			avatar		主播头像
	 *			nickname	主播昵称
	 *			userLevel	主播用户等级
	 *			anchorLevel	主播等级
	 */
	void pushIOSAccountList(final List<String> accountList, final Map<String, Object> parms,final Map<String, Object> custom);
	
}
