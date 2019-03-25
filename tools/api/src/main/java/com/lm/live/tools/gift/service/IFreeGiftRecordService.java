package com.lm.live.tools.gift.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.gift.domain.FreeGiftRecord;

/**
 * 免费礼物记录服务
 * @author shao.xiang
 * @date 2017-07-19
 *
 */
public interface IFreeGiftRecordService  extends ICommonService<FreeGiftRecord> {
	
	/**
	 * 获取用户的免费礼物
	 * @param userId
	 * @return
	 */
	public FreeGiftRecord getUserFreeGiftSum(String userId);
	
	/**
	 *  增加礼物(客户端每10min触发，加1朵)
	 * @param userId
	 * @param clientType 1:web;2:app
	 * @return
	 * @throws Exception
	 */
	public int addFreeGift(String userId,int clientType) throws Exception;
	
	
	/**
	 * 赠送免费礼物
	 * 1:用户减,主播加
	 * 2.推送通知给IM
	 * @param userId 赠送者
	 * @param toAnchorId 被赠送者(主播Id)
	 * @param num 数量
	 * @return
	 * @throws Exception
	 */
	public int giveFreeGift(String userId, String toAnchorId, int num,String roomId,String imToken) throws Exception;
	
	
	/**
	 *  用户待在房间,每个2分钟系统奖励茄子免费礼物1个
	 * @param userId
	 * @param clientType 1:web;2:app
	 * @return 返回剩下数量
	 * @throws Exception
	 */
	public int prizeFreeGiftQieziForStayinroom(String userId,String clientType) throws Exception;
	
	
	/**
	 *  新注册的用户,系统赠送茄子免费礼物
	 * @param userId
	 * @param clientType 1:web;2:app
	 * @return 返回剩下数量
	 * @throws Exception
	 */
	public int addFreeGiftQieziForNewRegistUser(String userId,String clientType) throws Exception;
	
	
	/**
	 * 进入直播间,系统赠送茄子免费礼物
	 * @param userId
	 * @param clientType 1:web;2:app
	 * @return 返回剩下数量
	 * @throws Exception
	 */
	public int addFreeGiftQieziForIntoRoom(String userId,String clientType) throws Exception;
	
	/**
	 * 赠送茄子免费礼物
	 * @param userId
	 * @param toAnchorId
	 * @param sendNum
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public int sendFreegiftQiezi(String userId, String toAnchorId, int sendNum, String roomId) throws Exception;
	
	/**
	 * 获取用户的茄子免费礼物的数量
	 * @param userId
	 * @return
	 */
	public FreeGiftRecord getUserFreeGifQiezitSum(String userId);

}
