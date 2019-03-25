package com.jiujun.shows.guard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.guard.domain.GuardWork;
import com.jiujun.shows.guard.vo.GuardVo;

/**
 * Service - 
 * @author shao.xiang
 * @date 2017-06-13
 */
public interface IGuardWorkService extends ICommonService<GuardWork> {
	/**
	 * 获取用户所有房间的守护信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<GuardWork> getGuardInfoDataAll(String userId) throws Exception;
	
	
	/**
	 * 获取守护列表信息，游客和非守护为一类缓存，守护为一类
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public List<GuardVo> getGuardWorkData(String userId, String roomId) throws Exception;
	
	/**
	 * 增加或更新守护使用记录
	 * @param userId
	 * @param roomId
	 * @param guardId
	 * @param isPeriod
	 * @param validate
	 * @throws Exception
	 */
	public GuardWork addOrUpdateWorkHis(String userId, String roomId, int workId, int guardId, int isPeriod, int validate, boolean isContinue) throws Exception;
	
	/**
	 * 获取用户所有房间的守护信息，缓存
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map> getUserGuardInfoAllRoomCache(String userId) throws Exception;
	
	/**
	 * 查询用户在当前房间内守护信息
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public List<Map> getUserGuardInfoByRoomCache(String userId, String roomId) throws Exception;
	
	/**
	 * 获取用户守护类型的最长结束时间，返回一条
	 * @param userId
	 * @param guardType
	 * @return
	 * @throws Exception
	 */
	public GuardWork getGuardEndTimeByUser(String userId, String roomId, int guardType) throws Exception;

	/**
	 * 查询主播房间内开通守护的用户信息
	 * @param anchorUserId
	 * @return
	 */
	public List qryRoomGuradList(String anchorUserId) throws Exception;
	
	/**
	 * 获取用户所有房间的守护信息，nickname，avatar为主播的信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<GuardVo> getUserGuardAllData(String userId) throws Exception;
	
	/**
	 * 设置排序字段为null
	 * @param roomId
	 * @throws Exception
	 */
	public void setGuardWorkSortIndex2Null(String roomId) throws Exception;
	
	/**
	 * 查询房间守护
	 * @param roomId
	 * @param guardType
	 * @return
	 * @throws Exception
	 */
	public List<GuardWork> findRoomGuardWorkList(String roomId,int guardType) throws Exception;
	
	/**
	 * 更新金守护排序
	 * @param statisticsGiftBeginTime
	 * @param statisticsGiftEndTime
	 * @throws Exception
	 */
	public void updateAllRoomGoldGuardWorkSortIndex(String statisticsGiftBeginTime,String statisticsGiftEndTime)  throws Exception;
	
	/**
	 * 获取房间所有守护id,去重
	 * 	摘蜜桃使用，其他业务如需使用，请具体看实现是否满足
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	List<String> getRoomGuradDataFromCache(String roomId) throws Exception;
	
	/**
	 * 处理全站守护榜单排行
	 * @return
	 * @throws Exception
	 */
	JSONObject listGuardRankData() throws Exception;
	
	/**
	 * 获取所有守护，按房间分组，守护数排序
	 * @return
	 * @throws Exception
	 */
	Map<String,Integer> listAllGuard(int size) throws Exception;
	
	/**
	 * 获取所有黄金守护，按房间分组，守护数排序
	 * @return
	 * @throws Exception
	 */
	Map<String,Integer> listGoldGuard(int size) throws Exception;

	public int getUserGuardCount(String userId, String roomId,
			Date beginTime, Date endTime);

	/**
	 * 获取护卫队等级虎狼大队以上守护个数
	 * @return
	 */
	public Map<String, Integer> listAllGuardByHaving();

	/**
	 * 获取护所有黄金守护个数
	 * @return
	 */
	public Map<String, Integer> listGoldGuardByHaving();

	
	/**
	 * 查询房间内所有守护用户id
	 * @param roomId
	 * @return
	 */
	public List<String> getRoomGuardUser(String roomId);
	
	
}
