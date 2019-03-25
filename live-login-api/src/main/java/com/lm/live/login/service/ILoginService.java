package com.lm.live.login.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Session;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.login.vo.QQConnectUserInfoVo;

public interface ILoginService {
	/**
	 * 自动注册
	 * @param clientType
	 * @param clientIp
	 * @param deviceProperties
	 * @param accessToken
	 * @param agc
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月9日
	 */
	public UserBaseInfo autoRegist(String clientType,String clientIp,DeviceProperties deviceProperties,String accessToken) throws Exception;
	
	
	/**
	 * 微信登录
	 * 1.获取token;2.用token获取用户信息;3.保存信息，返回已登录的session
	 * @param code 微信api第一步获取的code参数
	 * @param clientType
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月9日
	 */
	public JSONObject appWechatLogin(String code,String channelId,String clientIp,String clientType,DeviceProperties deviceProperties) throws Exception;
	
	/**
	 * QQ登录(app)
	 * 保存信息，返回已登录的session
	 * @param code 拉起应用，app取到的accessToken
	 * @param qqConnectUserInfoVo app从qq互联拿到的用户信息
	 * @param channelId
	 * @param clientIp
	 * @param clientType
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 */
	public JSONObject appQQConnectLogin(String accessToken,QQConnectUserInfoVo qqConnectUserInfoVo,String channelId,String clientIp,String clientType,DeviceProperties deviceProperties) throws Exception;
	
	/**
	 * 微博登录(app)
	 * @param accessToken
	 * @param uid
	 * @param clientType
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 */
	public JSONObject appWeiboLogin(String accessToken,String uid,String channelId,String clientIp,String clientType,DeviceProperties deviceProperties) throws Exception;
	
	
	/**
	 * QQ登录(web)
	 * 保存信息，返回已登录的session
	 * @param code 拉起应用，app取到的accessToken
	 * @param qqConnectUserInfoVo app从qq互联拿到的用户信息
	 * @return
	 * @throws Exception
	 */
	public JSONObject webQQConnectLogin(String accessToken,QQConnectUserInfoVo qqConnectUserInfoVo,String channelId,String clientIp) throws Exception;
	
	
	/**
	 * 微博登录(web)
	 * @param accessToken
	 * @param uid
	 * @param channelId
	 * @param servername 
	 * @return
	 */
	public JSONObject webWeiboLogin(String accessToken, String uid,String channelId,String clientIp) throws Exception;
	
	/**
	 * 伪登录实现
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月12日
	 */
	public JSONObject pseudoLogin(String sessionId, String uid, String ip, long time) throws Exception;
	
	/**
	 * 保存登录后的session（此接口可不做业务biz的实现，仅供本服务使用）
	 * @param session
	 * @param userId
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	public void setMemcacheToSessionId(Session session,String userId);
	
}
