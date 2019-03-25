package com.jiun.shows.guard.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.guard.domain.GuardWork;
import com.jiujun.shows.guard.service.IGuardWorkService;
import com.jiujun.shows.guard.vo.GuardVo;
import com.jiun.shows.guard.biz.GuardWorkBiz;
import com.jiun.shows.guard.dao.GuardWorkMapper;


/**
 * 守护相关服务
 * @author shao.xiang
 * @date 2017-06-30
 */
public class GuardWorkServiceImpl extends CommonServiceImpl<GuardWorkMapper, GuardWork> implements IGuardWorkService{

	@Resource
	private GuardWorkBiz guardWorkBiz;
	
	@Override
	public List<GuardWork> getGuardInfoDataAll(String userId) throws Exception {
		return guardWorkBiz.getGuardInfoDataAll(userId);
	}

	@Override
	public List<GuardVo> getGuardWorkData(String userId, String roomId)
			throws Exception {
		return guardWorkBiz.getGuardWorkData(userId, roomId);
	}

	@Override
	public GuardWork addOrUpdateWorkHis(String userId, String roomId,
			int workId, int guardId, int isPeriod, int validate,
			boolean isContinue) throws Exception {
		return guardWorkBiz.addOrUpdateWorkHis(userId, roomId, workId, guardId, isPeriod, validate, isContinue);
	}

	@Override
	public List<Map> getUserGuardInfoAllRoomCache(String userId)
			throws Exception {
		return guardWorkBiz.getUserGuardInfoAllRoomCache(userId);
	}

	@Override
	public List<Map> getUserGuardInfoByRoomCache(String userId, String roomId)
			throws Exception {
		return guardWorkBiz.getUserGuardInfoByRoomCache(userId, roomId);
	}

	@Override
	public GuardWork getGuardEndTimeByUser(String userId, String roomId,
			int guardType) throws Exception {
		return guardWorkBiz.getGuardEndTimeByUser(userId, roomId, guardType);
	}

	@Override
	public List qryRoomGuradList(String anchorUserId) throws Exception {
		return guardWorkBiz.qryRoomGuradList(anchorUserId);
	}

	@Override
	public List<GuardVo> getUserGuardAllData(String userId) throws Exception {
		return guardWorkBiz.getUserGuardAllData(userId);
	}

	@Override
	public void setGuardWorkSortIndex2Null(String roomId) throws Exception {
		guardWorkBiz.setGuardWorkSortIndex2Null(roomId);
	}

	@Override
	public List<GuardWork> findRoomGuardWorkList(String roomId, int guardType)
			throws Exception {
		return guardWorkBiz.findRoomGuardWorkList(roomId, guardType);
	}

	@Override
	public void updateAllRoomGoldGuardWorkSortIndex(
			String statisticsGiftBeginTime, String statisticsGiftEndTime)
			throws Exception {
		guardWorkBiz.updateAllRoomGoldGuardWorkSortIndex(statisticsGiftBeginTime, statisticsGiftEndTime);
	}

	@Override
	public List<String> getRoomGuradDataFromCache(String roomId)
			throws Exception {
		return guardWorkBiz.getRoomGuradDataFromCache(roomId);
	}

	@Override
	public JSONObject listGuardRankData() throws Exception {
		return guardWorkBiz.listGuardRankData();
	}

	@Override
	public Map<String, Integer> listAllGuard(int size) throws Exception {
		return guardWorkBiz.listAllGuard(size);
	}

	@Override
	public Map<String, Integer> listGoldGuard(int size) throws Exception {
		return guardWorkBiz.listGoldGuard(size);
	}

	@Override
	public int getUserGuardCount(String userId, String roomId, Date beginTime,
			Date endTime) {
		return guardWorkBiz.getUserGuardCount(userId, roomId, beginTime, endTime);
	}

	@Override
	public Map<String, Integer> listAllGuardByHaving() {
		return guardWorkBiz.listAllGuardByHaving();
	}

	@Override
	public Map<String, Integer> listGoldGuardByHaving() {
		return guardWorkBiz.listGoldGuardByHaving();
	}

	@Override
	public List<String> getRoomGuardUser(String roomId) {
		return guardWorkBiz.getRoomGuardUser(roomId);
	}


}
