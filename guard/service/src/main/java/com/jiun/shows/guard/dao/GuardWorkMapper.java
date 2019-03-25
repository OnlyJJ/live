package com.jiun.shows.guard.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.guard.domain.GuardWork;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-23
 *
 */
public interface GuardWorkMapper extends ICommonMapper<GuardWork> {
	
	void setGuardWorkSortIndex2Null(String roomId);
	
	Map<String, Integer> listAllGuard(int size);
	
	Map<String, Integer> listGoldGuard(int size);
	
	Map<String, Integer> listAllGuardByHaving();
	
	Map<String, Integer> listGoldGuardByHaving();
	
	GuardWork getGuardEndTimeByUser(String userId,String roomId, int guardType);
	
	int getUserGuardCount(String userId, String roomId, Date beginTime, Date endTime);
	
	List<GuardWork> findRoomGuardWorkList(String roomId, int guardType);
	
	List<GuardWork> getGuardWorkListByRoom(String roomId);
	
	List<GuardWork> getGuardWorkList(String roomId);
	
	List<String> getRoomGuardUser(String roomId);
	
	List<Map> qryRoomGuradList(String anchorUserId);
	
	List<Map> getGuardWorkData(String roomId);
	
	List<GuardWork> getGuardInfoAllData(String userId);
	
	List<Map> getUserGuardAllData(String userId);
	
	List<Map> getUserGuardInfoAllRoom(String userId);
	
	List<Map> getGuardWorkData(String userId, String roomId);
	
	List<String> findRoomIdThatHasGuardWork();
	
	List<GuardWork> listGuardWorkGroupByUser(String roomId);
	
	List<String> findRoomGuardLastWeekGiveTop(String roomId, String beginTime, String endTime, int qrySize);
}
