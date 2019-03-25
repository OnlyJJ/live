package com.lm.live.account.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.account.domain.UserAccountBook;
import com.lm.live.common.service.ICommonService;
import com.lm.live.framework.service.ServiceResult;

/**
 * 用户账户服务
 * @author shao.xiang
 * @date 2017-06-06
 *
 */
public interface IUserAccountService extends ICommonService<UserAccount>{

	UserAccount getObjectByUserId(String userId);
	
	/**
	 * 给用户账户加金币
	 * @param userId
	 * @param golds 必须是正数
	 * @param userAccountBook 若传null，则调用该方法的业务必须要处理insert 账户明细UserAccountBook的操作，参考U9送礼
	 */
	ServiceResult<Boolean> addGolds(String userId,int golds,UserAccountBook userAccountBook);
	
	/**
	 * 给用户账户减金币
	 * @param userId
	 * @param golds 必须是正数
	 * @param userAccountBook 若传null，则调用该方法的业务必须要处理insert 账户明细UserAccountBook的操作，参考U9送礼
	 */
	public void subtractGolds(String userId,int golds,UserAccountBook userAccountBook) throws Exception;
	
	/**
	 * 给用户账户加主播经验
	 * @param userId
	 * @param anchorPoint 必须是正数
	 * @throws Exception 
	 */
	public void addAnchorPoint(String userId,int anchorPoint) throws Exception;
	
	/**
	 * 给用户账户加钻石 
	 * @param userId
	 * @param diamond 必须是正数
	 * @throws Exception 
	 */
	public void addDiamond(String userId, int diamond) throws Exception;
	
	/**
	 * 给用户账户减钻石 
	 * @param userId
	 * @param diamond 必须是正数
	 * @throws Exception 
	 */
	public void subtractDiamond(String userId, int diamond) throws Exception;
	
	/**
	 * 赠送礼物
	 * @param senderUserId 发送者userId
	 * @param anchorUserId 主播(收礼者)userId
	 * @param giftId 礼物id
	 * @param sendNum 赠送个数
	 * @param anchorRoomId 主播对应房间号
	 * @param isOnlySubGiftpackage 是否只扣礼物背包(不扣金币)
	 * @param isOnGiftRunway 是否上礼物跑道
	 * @return 送蜜桃时才查剩余包裹数量,其他礼物返回0(以提高效率)
	 * @throws Exception
	 */
	// my-todo-service
//	public Gift doSendGiftBusiness(String senderUserId,String anchorUserId,int giftId,int sendNum,String anchorRoomId,boolean isOnlySubGiftpackage,boolean isOnGiftRunway,double extraRate,boolean isExtraAnchor) throws Exception;
	
	/**
	 * 充值累计到一定值后送礼
	 * @param userId
	 * @param onceChargeMoney  单位:分
	 * @throws Exception
	 */
	public void sendGiftWithCharge(String userId,int onceChargeMoney) throws Exception;
	
	
	/**
	 * 获取房间相应主播的主播等级
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public int getAnchorLevel(String roomId) throws Exception;
	
	
	/**
	 * 减金币,并添加金币支出的流水记录
	 * @param userId
	 * @param golds
	 * @param businessId 关联具体业务id
	 * @param businessModel 业务模块
	 * @param content 业务简述,为方便以后查看，在调用该业务方法时，最好把具体的业务表放入该字段内，如：系统任务，领取金币记录表：t_user_finish_task
	 * @throws Exception
	 */
	public void subtractGoldsAndAddAccountBookOutHis(String userId,int golds,String businessId,String businessModel,String content,UserAccountBook userAccountBook) throws Exception;
	
	/**
	 * 给用户账户加经验等级
	 * @param userId
	 * @param anchorPoint 必须是正数
	 * @throws Exception 
	 */
	public void addUserPoint(String userId,int userPoint) throws Exception;


	/**
	 * 送礼权限校验(没有权限则抛出异常)
	 * @param senderUserId
	 * @param anchorUserId
	 * @param sendGiftId
	 * @param sendNum
	 * @param imNotifyRoomId
	 */
	void checkSendGiftAccess(String senderUserId, String anchorUserId,String reciveUserId, int sendGiftId, int sendNum, String imNotifyRoomId) throws Exception;
	
	/***
	 * 用户升级、升级消息推送,并记录升级历史
	 * @param userId
	 * @param strLevel 送礼前等级
	 * @param endLevel 送礼后等级
	 * @param roomId
	 */
	void userUpgradeSendMsg(String userId,String strLevel,String endLevel,String roomId);
	/***
	 * 主播升级、升级消息推送,并记录升级历史
	 * @param userId
	 * @param strLevel 送礼前等级
	 * @param endLevel 送礼后等级
	 * @param roomId
	 */
	void anchorUpgradeSendMsg(String userId,String strLevel,String endLevel,String roomId);
	
	/**
	 * 消费金币后做的一些共同业务,如：活动
	 * @param senderUserId 送出金币者
	 * @param anchorUserId 获得金币者
	 * @param consumeGold 消费金币数量
	 * @param roomId 主播房间
	 * @param isUserGift 是否是用户礼物
	 * @param sendGiftId 礼物ID
	 * @param consumeGold 送礼金币数
	 * @throws Exception
	 */
	public void doCommonBizAfterConsumeGold(String senderUserId, String anchorUserId, boolean isUserGift, String roomId, int sendGiftId, int consumeGold) throws Exception;
	
	/**
	 * 消费礼物后，通用后续业务处理入口
	 * 注：活动过期后，请注释掉
	 * @param senderUserId 送礼者
	 * @param anchorUserId 收礼者
	 * @param roomId 房间
	 * @param sendGiftId 礼物id
	 * @param consumeNum 礼物数量
	 * @throws Exception
	 */
	void doCommonConsumeGift(String senderUserId,String anchorUserId, String roomId, int sendGiftId, int consumeNum) throws Exception;

	void updateUserLevel(String senderUserId, String senderNewUserLevel);

	void updateAnchorLevel(String anchorUserId, String anchorNewAnchorLevel);
	
	/**
	 * 消费礼物后，额外增加经验入口，配置参考t_activity_festival_conf，No.20
	 * @param userId
	 * @param anchorId
	 * @param roomId
	 * @param giftId
	 * @param giftNum
	 * @throws Exception
	 */
	JSONObject doAddExtraExperienceByGift(String userId, String anchorId, String roomId, int giftId, int giftNum) throws Exception;
	
	/**
	 * 消费金币后，额外增加经验入口，不区分礼物，只针对金币消费，配置参考t_activity_festival_conf.NO.21
	 * @param userId
	 * @param anchorId
	 * @param consumGolds
	 * @throws Exception
	 */
	void doAddExtraExperiencsByGold(String userId, String anchorId, int consumGolds) throws Exception;
	
	void doSendUserGiftBiz(String senderUserId, String reciveUserId,String anchorUserId,int giftId,int sendNum,String anchorRoomId,boolean isOnGiftRunway,double extraRate) throws Exception;
}
