package com.lm.live.tools.pack.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.pack.domain.UserPackage;

/**
 * 背包服务
 * @author shao.xiang
 * @date 2017-07-02
 */
public interface IUserPackageService extends ICommonService<UserPackage> {
	
	/**
	 * 加包裹
	 * @param userId 用户userId
	 * @param giftId 礼物id(关联表:t_gift)
	 * @param num 礼物数量
	 * @param isPeriod 是否有时间限制
	 * @param endTime isPeriod为true时不能传null
	 * @throws Exception
	 */
	public void addPackage(String userId,int giftId, int num,boolean isPeriod,Date endTime, UserAccountBook userAccountBook) throws Exception;
	
	/**
	 * 加包裹
	 * @param userId 用户userId
	 * @param giftId 礼物id(关联表:t_gift)
	 * @param num 礼物数量
	 * @param isPeriod 是否有时间限制
	 * @param endTime isPeriod为true时不能传null
	 * @param refId 关联来源表的id(用于记录来源,可空)
	 * @param refDesc 关联来源的说明制(用于记录来源,可空)
	 * @param comment 备注(用于记录来源,不可为空)
	 * @throws Exception
	 */
	public void addPackage(String userId,int giftId, int num,boolean isPeriod,Date endTime,String refId,String refDesc,String comment, UserAccountBook userAccountBook) throws Exception;
	
	/*
	
	*//**
	 * 减包裹
	 * @param userId
	 * @param giftId
	 * @param num
	 * @param isPeriod 是否有时间限制
	 * @throws Exception
	 *//*
	public void subtractPackage(String userId,int giftId ,int num,boolean isPeriod) throws Exception;
		
	*/
	
	/**
	 * 合并qq互联用户的背包
	 * @param masterUserId
	 * @param slaveUserId
	 */
	public void combineQQUserPackage(String masterUserId,String slaveUserId);
	
	/**
	 * 统计用户的礼物包裹剩余数量
	 * @param userId
	 * @param giftId
	 * @return
	 */
	public int countUserPackage(String userId,int giftId) throws Exception;

	/**
	 * 查询用户礼物背包(用于在包裹中显示)
	 * @param userId
	 * @return
	 */
	public List<Map> qryUserGiftpackage(String userId);
}
