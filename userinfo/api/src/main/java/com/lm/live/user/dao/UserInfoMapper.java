package com.lm.live.user.dao;


import org.apache.ibatis.annotations.Param;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.vo.UserInfo;

public interface UserInfoMapper extends ICommonMapper<UserInfoDo> {
	
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfo getUserDetailInfo(String userId);
	
	/**
	 * 获取db中的用户信息
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	UserInfoDo getUserByUserId(String userId);
	
	/**
	 * 关联微信表查询用户
	 * @param openid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	UserInfoDo getByWechatUnionid(@Param("unionid") String unionid);
	
	/**
	 * 关联qq表查询用户
	 * @param unionid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	UserInfoDo getByQQConnectUnionid(@Param("unionid") String unionid);
	
	/**
	 * 根据昵称查询用户
	 * @param nickName
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	UserInfoDo getUserByNickName(String nickName);
	
	/**
	 * 更新用户信息
	 * @param userInfo
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	void updateByUserId(UserInfoDo userInfo);
	
	/**
	 * 关联微博账号查询用户信息
	 * @param uid
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	UserInfoDo getByWeiboUid(@Param("uid") String uid);
}
