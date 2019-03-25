package com.lm.live.tools.tool.service;

import com.jiujun.shows.common.enums.TaskIdEnum;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.tools.tool.domain.UserToolPackage;
import com.lm.live.tools.enums.GiftTypeBusinessEnum;
import com.lm.live.tools.enums.ToolTableEnum;

/**
 * 用户工具背包服务
 * @author shao.xiang
 * @date 2017-06-29
 */
public interface IUserToolPackageService extends ICommonService<UserToolPackage> {
	/**
	 * 增加工具
	 * @param userId
	 * @param toolId
	 * @param num
	 */
	public void save(String userId , GiftTypeBusinessEnum id , int num) throws Exception;
	/**
	 * 增加工具
	 * @param userId
	 * @param toolId
	 * @param num
	 */
	public void save(String userId , ToolTableEnum.Id id, int num) throws Exception;
	
	/**
	 * 增加工具
	 * @param userId
	 * @param toolId
	 * @param num
	 */
	public void save(String userId , int toolId , int num) throws Exception;
	
	/**
	 * 扣除工具箱中的工具
	 * @param userId
	 * @param toolId
	 * @param toolTypeEnum
	 * @throws Exception
	 */
	public void subPackageTool(String userId,GiftTypeBusinessEnum giftTypeEnum,int num) throws Exception;
	
	/**
	 * 根据用户和工具id查找工具
	 * @param userId
	 * @param toolId
	 * @return
	 * @throws Exception
	 */
	public UserToolPackage getUserToolPackage(String userId,GiftTypeBusinessEnum giftTypeEnum) throws Exception;
	 

	/**
	 * 给用户加等级宝箱
	 * @param userId
	 * @param userLevel
	 * @param roomId 送礼当时所在房间号
	 * @throws Exception
	 */
	public void addUserLevelBox(String userId , int userLevel ,String roomId) throws Exception; 
	
	/**
	 * 花费金币购买工具并使用
	 * @param userId
	 * @param toolTypeEnum
	 * @param num
	 * @throws Exception
	 */
	public void useToolBySubGold(String userId,GiftTypeBusinessEnum giftTypeEnum) throws Exception;
	
	/**
	 * 增加开宝箱记录以及将物品放入系统奖励表
	 * @param userId
	 * @param taskIdEnum
	 * @param giftTypeEnum
	 * @param giftId
	 * @param number
	 * @param prizeName
	 * @param remark
	 * @throws Exception
	 */
	public void addSysPrizeForBox(String userId,TaskIdEnum taskIdEnum, GiftTypeBusinessEnum giftTypeEnum,
			int giftId,int number, String prizeName,String remark) throws Exception;
	
	/**
	 * 加工具
	 * @param userId
	 * @param id 工具id
	 * @param num
	 */
	public ServiceResult<Boolean> addToolPackage(String userId , int id , int num);

	/**
	 * 加工具
	 * @param userId
	 * @param toolId
	 * @param toolNum
	 * @param type 0:减少,1:添加
	 * @param refId 来源id(用于记录来源,可为空)
	 * @param refDesc 来源表描述(用于记录来源,可为空)
	 * @param comment 相关说明(用于记录来源,不可为空)
	 */
	public void addToolPackage(String userId, int toolId, int toolNum,
			int type,String refId, String refDesc, String comment) throws Exception;
	
	/**
	 * 获取用户背包工具信息
	 * @param userId
	 * @param toolId
	 * @return
	 * @throws Exception
	 */
	public UserToolPackage getUserToolPackageData(String userId, int toolId) throws Exception;
	
	/**
	 * 扣工具
	 * @param userId
	 * @param toolId
	 * @param num
	 * @throws Exception
	 */
	public void subPackageTool(String userId,int toolId,int num) throws Exception;
}
