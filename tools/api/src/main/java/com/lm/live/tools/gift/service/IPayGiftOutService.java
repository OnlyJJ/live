package com.lm.live.tools.gift.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jiujun.shows.base.domain.SysConf;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.gift.domain.PayGiftOut;

public interface IPayGiftOutService extends ICommonService<PayGiftOut>{
	/**
	 * 获取在有效期内的送礼信息
	 * @param userId
	 * @param giftId
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public PayGiftOut getDecorateGiftInfo(String userId, int giftId, Date beginTime, Date endTime) throws Exception;

	/**
	 * 统计七夕送礼情况
	 * @param qixiGiftId
	 * @return
	 */
	public List<Map> statisticsSendQixiGift(int qixiGiftId) throws Exception;

	
	/**
	 * 统计七夕收礼情况
	 * @param qixiGiftId
	 * @return
	 */
	public List<Map> statisticsReceiveQixiGift(int qixiGiftId) throws Exception;


	/**
	 *  统计七夕送礼量情况(按用户分组)
	 * @param qixiGiftId
	 * @param senderUserId
	 * @return
	 * @throws Exception
	 */
	public int statisticsNumGiftGroupByUser(int qixiGiftId,String senderUserId) throws Exception;
	
	/**
	 *  统计七夕送礼量情况(按主播分组)
	 * @param qixiGiftId
	 * @param receiveUserId
	 * @return
	 * @throws Exception
	 */
	public int statisticsNumGiftGroupByRoom(int qixiGiftId,String receiveUserId) throws Exception;
	
	/**
	 * 统计七夕送礼量情况(按用户、房间分组)
	 * @param giftId
	 * @param senderUserId
	 * @param receiveUserId
	 */
	public int statisticsNumGiftGroupByUserAndRoom(int giftId,String senderUserId, String receiveUserId) throws Exception;
	
	/**
	 * 统计某时间段内某种礼物的用户送礼情况
	 * @param giftId
	 * @param sendUserId
	 * @param sysConfCode 活动配置code(t_sys_conf)
	 * @param jsonKey 活动json配置中，时间的key
	 * @return
	 * @throws Exception
	 */
	public int statisticsSendGiftByValidTime(int giftId,String senderUserId, SysConf sysConf, String jsonKey) throws Exception;
	
	/**
	 * 统计某时间段内某种礼物的主播收礼情况
	 * @param giftId
	 * @param receiveUserId
	 * @param sysConfCode 活动配置code(t_sys_conf)
	 * @param jsonKey 活动json配置中，时间的key
	 * @return
	 * @throws Exception
	 */
	public int statisticsReceiveGiftByValidTime(int giftId,String receiveUserId, SysConf sysConf, String jsonKey) throws Exception;
	
	/**
	 * 统计用户或主播在某时段内礼物情况
	 * @param userId
	 * @param giftId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public int getUserSendGiftNum(String userId,String anchorId, int giftId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计某时间段内用户或主播某种礼物的情况
	 * @param giftId
	 * @param isAnchor
	 * @param sysConf
	 * @return
	 * @throws Exception
	 */
	public List<Map> getUserOrAnchorGiftNum(int giftId,boolean isAnchor, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计截止date为止,幸运礼物产生的所有加入到奖金池的金币数
	 * @param date
	 * @return
	 */
	public long sumLuckygiftTotalGold2Pool(Date date);
	
	/**
	 * 统计startDate开始，截止date为止,幸运礼物产生的所有加入到奖金池的金币数
	 * @param date
	 * @return
	 */
	public long sumLuckygiftTotalGold2Pool(Date startDate,Date date);
	/**
	 * 1、定时任务调用
	 * 2、每次统计富豪榜前几位
	 * 3、给排名发放相应勋章奖励
	 * 4、同一时间段，只显示该类别一种勋章
	 * @throws Exception
	 */
	public void statisticsGiftAndGivePrizes() throws Exception;
	
	
	/**
	 * 统计主播某时间段内收到的礼物个数
	 * @param anchorId
	 * @param giftId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public int sumAnchorReceiveGiftNum(String anchorId, int giftId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 每天凌晨执行一次，年终礼物排名第一的发放奖励
	 * @throws Exception
	 */
	public void statisticsNianZhongSortAndPrize() throws Exception;
	
	/**
	 * 统计每天送礼榜单
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<Map> getStaticsGiftSort(int num,String beginTime, String endTime) throws Exception;
	
	/**
	 * 统计主播某些收到的数量
	 * @param anchorId
	 * @param giftList giftId集合
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public long getAnchorSumForGiftIds(String anchorId, List<Integer> giftList, Date beginTime, Date endTime) throws Exception;
	
	
	public List<Map> getStaticsGiftsForND(List<Integer> giftList,Date beginTime, Date endTime,boolean isAnchor) throws Exception;

	/**
	 * 根据送礼userId、xueshanGiftId、日期,查询每日收到雪山礼物最多的主播
	 * @param userId
	 * @param xueshanGiftId
	 * @param hisDateStr
	 * @param activityBeginTime
	 * @param activityEndTime
	 * @return
	 */
	public String qry210612ActivityXueshanzhiyueTopHisAnchor(
			String userId, int xueshanGiftId, String hisDateStr , Date activityBeginTime,Date activityEndTime) throws Exception;


	/**
	 * 查询每日送出雪山礼物最多的玩家
	 * @param xueshanGiftId
	 * @param qryDateStr
	 * @param activityBeginTime
	 * @param activityEndTime
	 * @return
	 */
	public Map qry210612ActivityueshanzhiyueTopHisUser(int xueshanGiftId,
			String qryDateStr ,Date activityBeginTime,Date activityEndTime) throws Exception;
	
	/**
	 * 查询活动期内，送礼消费的某房间用户信息
	 * @param anchorId
	 * @param giftLists
	 * @param beginTime
	 * @param endTime
	 * @return List<Map> map：userId，golds
	 * @throws Exception
	 */
	List<Map> getUsersSendGiftForActivity(String anchorId, Date beginTime, Date endTime, int consumGold) throws Exception;
	
	/**
	 * 查询活动期内，本房间活动礼物送出数据，按用户分组
	 * @param anchorId
	 * @param giftList
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<Map> getRoomSendGiftNumberForActivity(String anchorId,List<Integer> giftList,Date beginTime, Date endTime) throws Exception;

	/**
	 * 统计房间内所有送礼用户金币数
	 * @param anchorId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	List<Map> getRoomRecGoldForActivity(String anchorId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 获取用户全站送礼金币数
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	long getUserSendAllstationGold(String userId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计用户在某房间送礼金币数
	 * @param userId
	 * @param anchorId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	long getUserSendGoldForAnchor(String userId, String anchorId, Date beginTime,Date endTime) throws Exception;
	
	List<Map> getAllstationGoldForActivity(Date beginTime, Date endTime) throws Exception;
	
	List<Map> getAllstationGoldForAnchor(Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计主播收礼金币
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	long getAnchorReceiveGold(String anchorId, Date beginTime, Date endTime);
	
	/**
	 * 统计主播收到某些礼物的金币数量
	 * @param anchorId 主播id
	 * @param giftList 礼物id集合
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	long getAnchorReceiveGoldsByGifts(String anchorId,List<Integer> giftList,Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计主播收到所有礼物的金币数量
	 * @param anchorId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	long getAnchorReceiveGoldAllGifts(String anchorId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计用户全站送礼数量
	 * @param userId
	 * @param giftList 需要统计的礼物集合
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	int getUserSendGiftNumAll(String userId,List<Integer> giftList, Date beginTime,Date endTime) throws Exception;
	
	/**
	 * 统计用户在某房间送的某些礼物总数量
	 * @param userId
	 * @param anchorId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	int getUserSendGiftNumForAnchor(String userId, String anchorId,List<Integer> giftList, Date beginTime,Date endTime) throws Exception;
	
	/**
	 * 统计某些礼物，主播收到的数量，按主播分组
	 * @param giftList
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	List<Map> getAllAnchorRecGiftNumber(List<Integer> giftList,Date beginTime, Date endTime) throws Exception;

	public Long getSumpriceByUserid(String userid,String date);
	
	/**
	 * 通过主播userId集合去查询距离90天内金币小于1000000的主播ID集合
	 * @param toUserIdList
	 * @return
	 */
	public List<String> getToUserIdByPrice(List<String> toUserIdList);

	public long getSumpriceInWeekByUserid(String userId,String startTime,String endTime);

	public int getLuckyGiftNumForAnchor(String anchorUserId);

	/**
	 * 查询每月送幸运礼物触发特殊动画次数
	 * @param userId
	 * @return
	 */
	public int getSpecialAnimationNum(String userId);

	/**查询用户主播房间礼物送礼总数*/
	public int getGiftNumForUser(String senderUserId,String anchorUserId,List<Integer> giftIdList,
			String beginTime, String endTime);
	
	/**查询用户全站送礼总数*/
	public int getGiftNumForUser(String senderUserId,List<Integer> giftIdList,String beginTime, String endTime);

	/**查询主播房间某种礼物收礼总数*/
	public int getGiftNumForAnchor(String anchorUserId,List<Integer> giftIdList,String beginTime, String endTime);

	/**查询主播多种礼物收礼总数－map*/
	public List<Map<String, Object>> getGiftListForAnchor(
			String anchorUserId,List<Integer> giftIdList, String beginTime, String endTime);

	/**查询用户主播房间多种礼物收礼总数、用户id分组*/
	public List<Map<String,Object>> getUserGiftSumList(String anchorUserId,List<Integer> giftIdList,
			String beginTime, String endTime);
	
	/**查询用户主播房间某种礼物收礼总数、用户id分组*/
	public List<Map<String,Object>> getUserGiftSumByGiftId(String anchorUserId,int giftId,
			String beginTime, String endTime);

	/**查询主播房间某种礼物的送礼数*/
	public int getGiftNumForAnchorByGiftId(String anchorUserId,int giftId,String beginTime, String endTime);

	/**查询全站主播收礼总数*/
	public List<Map<String,Object>> getAllGiftSumForAnchor(List<Integer> giftIdList,String beginTime, String endTime);
	
	/**查询全站用户送礼数*/
	public List<Map<String,Object>> getAllGiftSumForUser(List<Integer> giftIdList,String beginTime, String endTime);
	
	/**查询主播全站某种礼物的送礼数*/
	public int getGiftNumByGiftIdForAnchor(String anchorUserId,int giftId,String beginTime, String endTime);

	/**查询用户主播房间某种礼物的送礼数*/
	public int getGiftNumByGiftIdForUser(String userId, String anchorId,
			int giftId, String beginTime, String endTime);

	/**查询主播某种礼物的送礼数,主播id分组*/
	public List<Map<String,Object>> getGiftNumByGiftIdForAllAnchor(int giftId, String beginTime,
			String endTime);

	
	/**
	 * 统计房间内所有送礼守护用户金币数
	 * @param anchorId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	List<Map> getGuardUserRoomRecGoldForActivity(String anchorId,String roomId, Date beginTime, Date endTime) throws Exception;
	
	/**
	 * 统计用户各房间送礼总数
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map> getUserRecGoldForActivity(String userId, Date beginTime, Date endTime) throws Exception;

	/**
	 * 
	 * @param giftIds 礼物id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getAnchorGiftSumForWoods(List<Integer> giftIds,String startTime,
			String endTime);
}
