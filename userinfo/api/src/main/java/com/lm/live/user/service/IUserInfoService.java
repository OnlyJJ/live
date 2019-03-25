package com.lm.live.user.service;


import com.lm.live.common.service.ICommonService;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.vo.UserInfo;

/**
 * 用户服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IUserInfoService extends ICommonService<UserInfoDo> {
	
	/**
	 * 获取用户基本信息（个人中心）
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserDetailInfo(String userId) throws Exception;	
	
	/**
	 * 查看别人资料
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserInfo(String userId) throws Exception;	
	
	/**
	 * 获取db中的用户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfoDo getUserByUserId(String userId) throws Exception;	;
	
	/**
	 * 根据unionid获取用户信息
	 * @param openid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	UserInfoDo getByWechatUnionid(String unionid);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	void updateByUserId(UserInfoDo user);
	
	/**
	 * 通过unionid获取用户信息
	 * @param unionid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	UserInfoDo getByQQConnectUnionid(String unionid);
	
	/**
	 * 根据微博uid获取用户信息
	 * @param uid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	UserInfoDo getByWeiboUid(String uid);
	
	/**
	 * 获取用户基本信息，缓存
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月13日
	 */
	UserInfoDo getUserInfoFromCache(String userId);
	
}
