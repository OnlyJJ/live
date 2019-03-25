package com.jiujun.shows.guard.service;

import java.util.List;

import com.jiujun.shows.guard.domain.GuardPunchGive;

/**
 * Service - 
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardPunchGiveService {
	/**
	 * 插入记录
	 * @param vo
	 * @throws Exception
	 */
	public void save(int payHisId, int workId, int priceType, String userId, String roomId, int validate) throws Exception;
	/**
	 * 处理打卡后的，守护相关业务
	 * @param userId
	 * @param roomId
	 * @throws Exception
	 */
	public void handleGuardRelateInfo(String userId, String roomId) throws Exception;
	
	/**
	 * 获取用户某房间的打卡可送时长信息
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public List<GuardPunchGive> getGuardPunchGiveData(String userId, String roomId) throws Exception;
}
