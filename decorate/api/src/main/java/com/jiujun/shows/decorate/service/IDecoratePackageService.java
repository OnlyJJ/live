package com.jiujun.shows.decorate.service;

import java.util.Date;
import java.util.List;

import com.jiujun.shows.decorate.domain.DecoratePackage;
import com.jiujun.shows.decorate.vo.DecoratePackageVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * Service - 勋章包裹
 * @author shao.xiang
 * @date 2017-06-08
 */
public interface IDecoratePackageService {
	/**
	 * 查询主播勋章点亮状态，返回所有勋章状态
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public List<DecoratePackageVo> getDecoratePackageList(String userId) throws Exception;
	
	/**
	 * 处理定时任务，根据条件插入勋章记录
	 * 本方法只处理t_decorate表中配置的，固定的点亮时间和点亮条件的勋章，不处理动态条件的勋章
	 * @throws Exception
	 */
	public void insertDecoratePackageByCondition() throws Exception;
	
	/**
	 * 主播在房间摘蜜桃获取女神勋章
	 * @param userId
	 * @param roomId
	 */
	public void addPackageByRoomPeach(String userId,String roomId) throws Exception;
	
	/**
	 * 加勋章包裹(1:默认不累计有效时间;2:默认为普通用户勋章(非守护勋章),即guardType=0)
	 * @param userId 用户Id
	 * @param roomId 冗余字段(若是主播,则为对应的房间号)
	 * @param decorateId 勋章id
	 * @param isPeriod 是否有时间限制
	 * @param number 数量
	 * @param days 有效天数
	 * @throws Exception
	 */
	public ServiceResult<Boolean> addPackage(String userId,String roomId,int decorateId,boolean isPeriod,int number,int days);
	
	/**
	 * 加勋章包裹(默认为普通用户勋章(非守护勋章),即guardType=0)
	 * @param userId 用户Id
	 * @param roomId 冗余字段(若是主播,则为对应的房间号)
	 * @param decorateId 勋章id
	 * @param isPeriod 是否有时间限制
	 * @param number 数量
	 * @param days 有效天数
	 * @param sourceKey 来源key
	 * @param desc 
	 * @param isAccumulation 是否时间累加,1:累加;   0：不累加
	 * @throws Exception
	 */
	public void addPackage(String userId,String roomId,int decorateId,boolean isPeriod,int number,int days,String sourceKey,String desc,int isAccumulation) throws Exception;
	
	/**
	 * 发送七夕活动用户勋章
	 * @throws Exception
	 */
	public void sendQixiUserDecorate() throws Exception;
	
	/**
	 * 发送七夕活动主播勋章
	 * @throws Exception
	 */
	public void sendQixiAnchorDecorate() throws Exception;
	
	/**
	 * 七夕送礼,检测是否发消息通知
	 * @param giftId
	 * @param senderUserId
	 * @param receiveUserId
	 * @throws Exception
	 */
	public void checkAndSendImMsgForQixiGift(int giftId,String senderUserId,String receiveUserId) throws Exception;
	
	/**
	 * 获取用户某一类勋章背包记录
	 * @param userId
	 * @param decorateId
	 * @return
	 * @throws Exception
	 */
	public DecoratePackage getDecoratePackageByUserIdAndDercoateId(String userId,int decorateId) throws Exception;
	
	/**
	 * 活动：满足送礼额度后发送对应勋章勋章、发通知
	 * @param giftId
	 * @param sendUserId
	 * @param anchorUserId
	 * @throws Exception
	 */
	// my-todo-service
//	public void sendLoveSeptDecroate(int giftId, SysConf sysConf, String jsonKey ,String senderUserId, String anchorUserId,String roomId) throws Exception;
	
	/**
	 * 加勋章包裹(默认不累计有效时间)
	 * @param userId 用户Id
	 * @param roomId 冗余字段(若是主播,则为对应的房间号)
	 * @param decorateId 勋章id
	 * @param isPeriod 是否有时间限制
	 * @param number 数量
	 * @param days 有效天数
	 * @param guardType  守护类型，0:非守护 ,1-白狼，2-黄金
	 * @throws Exception
	 */
	public void addPackage(String userId,String roomId,int decorateId,boolean isPeriod,int number,int days,int guardType) throws Exception;
	
	public DecoratePackage getDecoratePackageByType(String userId, int decorateId) throws Exception;
	
	public void handleSpecialDecorate(String userId,String roomId, int decorateId, boolean isPeriod, int number, Date endTime, String sourceKey, String desc) throws Exception;
	
	/**
	 * 获取用户某类有效勋章（在有效期内的勋章）
	 * @param userId
	 * @param decorateId
	 * @return
	 * @throws Exception
	 */
	public DecoratePackage findValidDecoratePackage(String userId, int decorateId) throws Exception;
	
	/**
	 * 统计主播收到礼物,给主播发日常勋章
	 * @param anchorUserId
	 * @param giftId
	 * @param num
	 * @throws Exception
	 */
	public void prizeDecorate2AnchorByReceiveGift(String anchorUserId,int giftId,int num) throws Exception;

	/**
	 * 查询用户所有获得的且有效的勋章
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<DecoratePackageVo> findUserAllReceiveAndEffectDecorate(
			String userId) throws Exception;

	
	/**
	 * 更新用户勋章佩戴与否
	 * @param userId
	 * @param decorateId
	 * @param isAdornUserDecorate y or n
	 * @throws Exception
	 */
	public void updateIsAdornUserDecorate(String userId, int decorateId,String isAdornUserDecorate) throws Exception;
	
	/**
	 * 判断用户是否可以继续佩戴用户勋章
	 * @param userId
	 * @param decorateId
	 * @param isAdornUserDecorate y or n
	 * @throws Exception
	 */
	public boolean checkIfUserCanAdornUserDecorate(String userId) throws Exception;
	
	/**
	 * 同步砸蛋勋章(金、银、铜的佩戴状态)
	 * @param userId
	 * @param decorateId
	 * @param isAdornUserDecorate
	 * @throws Exception
	 */
	public void synZadanDecorate(String userId, int decorateId,
			String isAdornUserDecorate) throws Exception;
	
	/**
	 * 处理同类型勋章，不同有效期的勋章
	 * @param userId
	 * @param roomId
	 * @param decorateId 勋章id
	 * @param isPeriod 是否时间限制，ture-有，false-无
	 * @param number 数量
	 * @param days 有效期
	 * @param sourceKey his表中的sourceKey
	 * @param desc 描述
	 * @param isAccumulation 是否结束时间累加1:累加;   0：不累加
	 * @throws Exception
	 */
	void addPackageForSameType(String userId,String roomId,int decorateId,boolean isPeriod,int number,int days,String sourceKey,String desc,int isAccumulation) throws Exception;

	public void prizeDecorate2AnchorByReceiveLuckyGift(String anchorUserId,
			int sendGiftId, int sendNum,String roomId) throws Exception;

	/**
	 * 幸运礼物勋章处理(爆金、爆银、爆铜)
	 * @param anchorUserId
	 * @param sendGiftId
	 * @param sendNum
	 * @param roomId
	 */
	public void prizeDecorate2UserByReceiveLuckyGift(String senderUserId,String roomId) throws Exception;

	public DecoratePackage getDecoratePackageByType(String userId,
			String roomId, int decorateId);

	public void sendLoveDecorate() throws Exception;


			
}
