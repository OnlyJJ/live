package com.jiun.shows.guard.biz;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.constant.MCTimeoutConstants;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.Helper;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.decorate.service.IDecorateHisService;
import com.jiujun.shows.decorate.service.IDecoratePackageService;
import com.jiujun.shows.guard.domain.GuardWork;
import com.jiujun.shows.guard.enums.GuardTableEnum;
import com.jiujun.shows.guard.service.IGuardConfService;
import com.jiujun.shows.guard.vo.GuardVo;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;
import com.jiun.shows.guard.dao.GuardWorkMapper;
import com.mysql.jdbc.StringUtils;


/**
 * Service -
 */
@Service("guardWorkBiz")
public class GuardWorkBiz {

	@Resource
	private GuardWorkMapper guardWorkMapper;
	
	@Resource
	private IGuardConfService guardConfService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IDecorateHisService decorateHisService;
	
	/** 到期前几天 */
	private static final int EXPIRE_TIME = 3;
	
	private static final String USER_ICON = SpringContextListener.getContextProValue("cdnPath", "") +"/" + BaseConstants.ICON_IMG_FILE_URI + "/";
	
	
	public List<GuardVo> getGuardWorkData(String userId, String roomId) throws Exception {
		if(StringUtils.isNullOrEmpty(roomId) || StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		List<GuardVo> list = null;
		String key = "";
		boolean isGuard = false;
		// 从缓存中查询当前用户是否是该房间的守护
		List<Map> guardList = getUserGuardInfoByRoomCache(userId, roomId);
		if(guardList == null || guardList.size() == 0) { // 非守护和游客的缓存
			key = MCPrefix.USER_WORK_INFO_DB_CACHE_FOR_ROOM + roomId ;
		} else { // 守护个人缓存
			isGuard = true;
			key = MCPrefix.GUARD_WORK_INFO_DB_CACHE_FOR_ROOM + roomId + userId;
		}
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			list = (List<GuardVo>) obj;
			LogUtil.log.info("### mydebug,从缓存中查询守护列表，key="+key + ",obj.size="+list.size());
		} else {
			list = getGuardWorkDataCache(userId, roomId, key,isGuard);
		}
		return list;
	}
	
	/**
	 * 缓存房间内，守护信息，userId为空时，表示是游客看到是守护信息
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	private List<GuardVo> getGuardWorkDataCache(String userId, String roomId, String key, boolean isGuard) throws Exception {
		// 蛋疼的排序规则，为了避免并发时数据错误，加同步控制
		// optimize,先去掉这个同步，2016-11-30
//		synchronized(GuardWork.class) {
			List<GuardVo> list = new ArrayList<GuardVo>();
			MemcachedUtil.delete(key);
			// 房间所有守护放入缓存，当有人在该房间购买守护时，再删除缓存
			List<Map> listWork = guardWorkMapper.getGuardWorkData(roomId);
			LogUtil.log.info("### mydebug,从db中查询守护列表，userId="+userId +",key="+key + ",roomId="+roomId+",listWork.size="+listWork.size());
			if(listWork != null && listWork.size() >0) {
				Date now = new Date();
				Date endTime = null;
				int workIdTemp = 0;
				String isExpire = "n"; // 是否块到期
				int workId = 0;
				int guardId = 0;
				int level = 0;
				int guardType = 0;
				int priceType = 0;
				String name = "";
				String image = "";
				String gdUserId = "";
				String timerDown = "";
				String userLevel = "";
				
				String gdUserIdKey = "gdUserId";
				String guardIdKey = "guardId";
				String workIdKey = "workId";
				String levelKey = "level";
				String guardTypeKey = "guardType";
				String priceTypeKey = "priceType";
				String nameKey = "name";
				String imageKey = "image";
				String endTimeKey = "endTime";
				
				// 用户等级
				String userLevelKey = "userLevel";
				
				
				// begin- 取消原本看自己守护排在第一位的规则
				/*
				// 查询用户在房间是否是守护,若同一用户在房间内有多个守护身份，则排序时，第一个位置只显示他最高级的守护，其余的与其他守护排序
				// 先加自己最高级的守护
				if(isGuard) {
					List<Map> guardList = getUserGuardInfoByRoomCache(userId, roomId);
					LogUtil.log.info("#### mydebug,查询用户="+userId +"在房间="+roomId +"的守护信息-guardList.size=."+ guardList.size());
					if(guardList != null && guardList.size() > 0) {
						
						Map m = guardList.get(0);
						
						if(m.containsKey(guardIdKey) && m.get(guardIdKey) != null) {
							guardId = Integer.parseInt(m.get(guardIdKey).toString());
						}
						if(m.containsKey(workIdKey) && m.get(workIdKey) != null) {
							workId = Integer.parseInt(m.get(workIdKey).toString());
							workIdTemp = workId;
						}
						if(m.containsKey(levelKey) && m.get(levelKey) != null) {
							level = Integer.parseInt(m.get(levelKey).toString());
						}
						if(m.containsKey(imageKey) && m.get(imageKey) != null) {
							image = m.get(imageKey).toString();
						}
						if(m.containsKey(nameKey) && m.get(nameKey) != null) {
							name = m.get(nameKey).toString();
						}
						if(m.containsKey(guardTypeKey) && m.get(guardTypeKey) != null) {
							guardType = Integer.parseInt(m.get(guardTypeKey).toString());
						}
						if(m.containsKey(endTimeKey) && m.get(endTimeKey) != null) {
							endTime = (Date) m.get(endTimeKey);
						}
						if(m.containsKey(priceTypeKey) && m.get(priceTypeKey) != null) {
							priceType = Integer.parseInt(m.get(priceTypeKey).toString());
						}
					
						GuardVo vo = new GuardVo();
						vo.setWorkId(workId);
						vo.setName(name);
						vo.setLevel(level);
						vo.setImage(image);
						vo.setValidate(DateUntil.getTime(endTime));
						vo.setTimerDown(DateUntil.getTimeRemains2(now, endTime));
						vo.setGuardType(guardType);
						vo.setUserId(userId);
						vo.setPriceType(priceType);
						UserInfo userInfo = userInfoService.getUserByUserId(userId);
						if(userInfo != null) {
							vo.setNickname(userInfo.getNickName());
							vo.setAvatar(USER_ICON + userInfo.getIcon());
						}
						// 判断是否快到期
						if(DateUntil.getTimeIntervalSecond(new Date(),DateUntil.addDatyDatetime(endTime, -EXPIRE_TIME)) < 0) {
							isExpire = "y";
						}
						vo.setIsExpire(isExpire);
						vo.setTime(DateUntil.getTimeIntervalSecond(now,endTime));
						
						list.add(vo);
					}
					LogUtil.log.info("#### mydebug,查询用户="+userId +"在房间="+roomId +"的守护信息 - end..");
				}
				*/
				// end- 取消原本看自己守护排在第一位的规则
				
				// 再加其他的守护
				for(Map m : listWork) {
					GuardVo vo = new GuardVo();
					
					
					if(m.containsKey(workIdKey) && m.get(workIdKey) != null) {
						workId = Integer.parseInt(m.get(workIdKey).toString());
						// begin -取消原本看自己守护排在第一位的规则
						/*
						if(workId == workIdTemp) {
							continue; // 说明已经把用户在本房间的守护信息添加了，这里不重复添加
						}
						*/
						// end- 取消原本看自己守护排在第一位的规则
					}
					
					
					if(m.containsKey(guardIdKey) && m.get(guardIdKey) != null) {
						guardId = Integer.parseInt(m.get(guardIdKey).toString());
					}
					if(m.containsKey(nameKey) && m.get(nameKey) != null) {
						name = m.get(nameKey).toString();
					}
					if(m.containsKey(imageKey) && m.get(imageKey) != null) {
						image = m.get(imageKey).toString();
					}
					if(m.containsKey(levelKey) && m.get(levelKey) != null) {
						level = Integer.parseInt(m.get(levelKey).toString());
					}
					if(m.containsKey(endTimeKey) && m.get(endTimeKey) != null) {
						endTime = (Date) m.get(endTimeKey);
						// 判断是否快到期
						if(DateUntil.getTimeIntervalSecond(new Date(),DateUntil.addDatyDatetime(endTime, -EXPIRE_TIME)) < 0) {
							isExpire = "y";
						}
					}
					if(m.containsKey(guardTypeKey) && m.get(guardTypeKey) != null) {
						guardType = Integer.parseInt(m.get(guardTypeKey).toString());
					}
					if(m.containsKey(priceTypeKey) && m.get(priceTypeKey) != null) {
						priceType = Integer.parseInt(m.get(priceTypeKey).toString());
					}
					if(m.containsKey(gdUserIdKey) && m.get(gdUserIdKey) != null) {
						gdUserId = m.get(gdUserIdKey).toString();
						vo.setUserId(gdUserId);
						UserInfo userInfo = userInfoService.getUserByUserId(gdUserId);
						if(userInfo != null) {
							vo.setNickname(userInfo.getNickName());
							vo.setAvatar(USER_ICON + userInfo.getIcon());
						}
					}
					
					int sortIndex = 0;
					if(m.containsKey("sortIndex")&& m.get("sortIndex") != null) {
						sortIndex = Integer.parseInt(m.get("sortIndex").toString());
					}
					
					if(m.containsKey(userLevelKey)&& m.get(userLevelKey) != null) {
						userLevel =(m.get(userLevelKey).toString());
					}
					
					vo.setName(name);
					vo.setLevel(level);
					vo.setImage(image);
					vo.setValidate(DateUntil.getTime(endTime));
					vo.setTimerDown(DateUntil.getTimeRemains2(now, endTime));
					vo.setIsExpire(isExpire);
					vo.setGuardType(guardType);
					vo.setWorkId(workId);
					vo.setPriceType(priceType);
					vo.setTime(DateUntil.getTimeIntervalSecond(now,endTime));
					vo.setSortIndex(sortIndex);
					vo.setUserLevel(userLevel);//用户等级
					list.add(vo);
				}
				// 放入缓存
				if(list != null) {
					MemcachedUtil.set(key, list, MCTimeoutConstants.USER_GUARD_ROOM_INFO_CACHE);
				}
				LogUtil.log.info("### mydebug,从db中查询守护列表，key="+key);
			} else {
				LogUtil.log.info("#### mydebug-getGuardWorkDataCache,roomId=" + roomId + ",房间内还没有任何守护");
			}
			return list;
//		}
	}

	
	public GuardWork addOrUpdateWorkHis(String userId, String roomId, int workId, int guardId,
			int isPeriod, int validate, boolean isContinue) throws Exception {
		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(roomId) || guardId <0
				|| isPeriod < 0 || validate < 0) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		synchronized(GuardWork.class) {
			boolean isPeriodDB = true; // 是否有时间限制,默认有
			if(isPeriod == 0) {
				isPeriodDB = false;
			}
			// 续期
			if(isContinue) {
				GuardWork gkHis = guardWorkMapper.getObjectById(workId);
				if(!isPeriodDB) { // 没有时间限制的，设置结束时间为null
					gkHis.setIsperiod(isPeriod);
					gkHis.setEndtime(null);
				} else {
					Date oldTime = gkHis.getEndtime();
					Date newTime = DateUntil.addDatyDatetime(oldTime, validate);
					gkHis.setEndtime(newTime);
				}
				guardWorkMapper.update(gkHis);
				return gkHis;
			} else {
				// 没有记录，则新增一条
				GuardWork vo = new GuardWork();
				vo.setGuardid(guardId);
				vo.setUserid(userId);
				vo.setRoomid(roomId);
				vo.setIsperiod(isPeriod);
				vo.setEndtime(DateUntil.addDatyDatetime(new Date(), validate));
				guardWorkMapper.insert(vo);
				return vo;
			}
//			GuardWork guardWork = guardWorkMapper.getGuardWorkByCondition(userId, roomId, guardId);
//			if(guardWork != null) { // 原来已经购买，则增加结束时间
//				if(!isPeriodDB) { // 没有时间限制的，设置结束时间为null
//					guardWork.setIsperiod(isPeriod);
//					guardWork.setEndtime(null);
//				} else {
//					Date oldTime = guardWork.getEndtime();
//					Date newTime = DateUntil.addDatyDatetime(oldTime, validate);
//					guardWork.setEndtime(newTime);
//				}
//				guardWorkMapper.update(guardWork);
//			} else { 
//			}
		}
	}

	
	public List<GuardWork> getGuardInfoDataAll(String userId) throws Exception {
		if(StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		return guardWorkMapper.getGuardInfoAllData(userId);
	}

	
	public List<Map> getUserGuardInfoAllRoomCache(String userId) throws Exception {
		if(StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		List<Map> list = null;
		String key = MCPrefix.USER_GUARD_INFO_IN_ALL_ROOM_CACHE + userId;
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			list = (List<Map>) obj;
		} else {
			list = updateAndSetUserGuardInfoData(userId);
		}
		return list;
	}
	
	private List<Map> updateAndSetUserGuardInfoData(String userId) throws Exception {
		List<Map> list = null;
		String key = MCPrefix.USER_GUARD_INFO_IN_ALL_ROOM_CACHE + userId;
		MemcachedUtil.delete(key);
		list = guardWorkMapper.getUserGuardInfoAllRoom(userId);
		if(list != null) {
			MemcachedUtil.set(key, list, MCTimeoutConstants.USER_GUARD_INFO_ALL_ROOM_CACHE);
		}
		return list;
	}

	
	public List<Map> getUserGuardInfoByRoomCache(String userId,
			String roomId) throws Exception {
		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(roomId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		List<Map> list = null;
		String key = MCPrefix.USER_GUARD_INFO_IN_ROOM_CACHE + roomId + userId;
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			list = (List<Map>) obj;
		} else {
			list = updateAndSetUserSelfGuard(userId, roomId);
		}
		return list;
	}
	
	private List<Map> updateAndSetUserSelfGuard(String userId, String roomId) throws Exception {
		List<Map> list = null;
		String key = MCPrefix.USER_GUARD_INFO_IN_ROOM_CACHE + roomId + userId;
		MemcachedUtil.delete(key);
		list = guardWorkMapper.getGuardWorkData(userId, roomId);
		if(list != null) {
			MemcachedUtil.set(key, list, MCTimeoutConstants.USER_GUARD_INFO_ALL_ROOM_CACHE);
		}
		return list;
	}

	
	public GuardWork getGuardEndTimeByUser(String userId,String roomId, int guardType)
			throws Exception {
		if(StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		
		return guardWorkMapper.getGuardEndTimeByUser(userId, roomId, guardType);
	}

	
	public List qryRoomGuradList(String anchorUserId) throws Exception{
		if(StringUtils.isNullOrEmpty(anchorUserId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		List guardVoList = new ArrayList();
		List<Map>  listMap = guardWorkMapper.qryRoomGuradList(anchorUserId);
		if(listMap != null && listMap.size() > 0){
			for(Map map:listMap){
				GuardVo vo = Helper.map2Object(map,GuardVo.class);
				guardVoList.add(vo.buildJson());
			}
		}
		return guardVoList;
	}

	
	public List<GuardVo> getUserGuardAllData(String userId) throws Exception {
		if(StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		List guardVoList = new ArrayList();
		List<Map> listMap = guardWorkMapper.getUserGuardAllData(userId);
		if(listMap != null && listMap.size() >0) {
			for(Map m : listMap) {
				String roomId = "";
				String anchorId = "";
				String nickname = "";
				String icon = "";
				int validate = 0;
				Date beginTime = null;
				Date endTime = null;
				Date now = new Date();
				GuardVo vo = new GuardVo();
				vo = Helper.map2Object(m,GuardVo.class);
				if(m.containsKey("roomId") && m.get("roomId") != null) {
					roomId = m.get("roomId").toString();
					vo.setRoomId(roomId);
					UserInfo userInfo = userInfoService.getAnchorUserInfoByRoomId(roomId);
					if(userInfo != null) {
						anchorId = userInfo.getUserId();
						nickname = userInfo.getNickName();
						icon = userInfo.getIcon();
					}
				}
				if(m.containsKey("endTime") && m.get("endTime") != null) {
					endTime = (Date) m.get("endTime");
					vo.setEndTime(DateUntil.format2Str(endTime, "yyyy-MM-dd HH:mm:ss"));
					if(endTime.after(now)) {
						vo.setTime(DateUntil.getTimeIntervalSecond(now, endTime));
					}
				}
				if(m.containsKey("beginTime") && m.get("beginTime") != null) {
					beginTime = (Date) m.get("beginTime");
					vo.setBeginTime(DateUntil.format2Str(beginTime, "yyyy-MM-dd HH:mm:ss"));
				}
				if(m.containsKey("validate") && m.get("validate") != null) {
					validate = Integer.parseInt(m.get("validate").toString());
					vo.setValidate(validate);
				}
				vo.setNickname(nickname);
				vo.setAvatar(BaseConstants.cdnPath+File.separator+BaseConstants.ICON_IMG_FILE_URI+File.separator + icon);
				vo.setUserId(anchorId);
				guardVoList.add(vo);
			}
		}
		return guardVoList;
	}

	
	public void setGuardWorkSortIndex2Null(String roomId) throws Exception {
		if(StringUtils.isNullOrEmpty(roomId)){
			throw new SystemDefinitionException(ErrorCode.ERROR_3017);
		}
		guardWorkMapper.setGuardWorkSortIndex2Null(roomId);
	}

	
	public List<GuardWork> findRoomGuardWorkList(String roomId,
			int guardType) throws Exception {
		if(StringUtils.isNullOrEmpty(roomId)){
			throw new SystemDefinitionException(ErrorCode.ERROR_3017);
		}
		return guardWorkMapper.findRoomGuardWorkList(roomId,guardType);
	}
	
	
	public void updateAllRoomGoldGuardWorkSortIndex(String statisticsGiftBeginTime,String statisticsGiftEndTime) throws Exception {
		List<String> roomIdList =  guardWorkMapper.findRoomIdThatHasGuardWork();
		LogUtil.log.info(String.format("###更新金守护四圣兽席位,所有有守护的房间:%s",JsonUtil.arrayToJsonString(roomIdList)));
		if(roomIdList != null && roomIdList.size() > 0){
			for(String roomId:roomIdList){
				LogUtil.log.info(String.format("###begin-更新金守护四圣兽席位,房间:%s",roomId));
				try {
					// 更新金守护sortIndex
					this.updateAllRoomGoldGuardWorkSortIndex(roomId, statisticsGiftBeginTime, statisticsGiftEndTime);
				} catch (Exception e) {
					LogUtil.log.info(String.format("###发生异常-更新金守护四圣兽席位,房间:%s",roomId));
					LogUtil.log.error(e.getMessage(),e);
				}
				LogUtil.log.info(String.format("###end-更新金守护四圣兽席位,房间:%s",roomId));
			}
		}
	}

	private void updateAllRoomGoldGuardWorkSortIndex(String roomId,String statisticsGiftBeginTime,String statisticsGiftEndTime) throws Exception {
		if(StringUtils.isNullOrEmpty(roomId)){
			throw new SystemDefinitionException(ErrorCode.ERROR_3017);
		}
		
		// 设置排序字段为null
		this.setGuardWorkSortIndex2Null(roomId);
		
		// 金守护
		int goldGuardType = GuardTableEnum.GuardCarType.huangjin.getValue();
		List<GuardWork> roomGoldGuardWorkList = this.findRoomGuardWorkList(roomId, goldGuardType);
		
		/**
			若房间内金守护数量＜=4人，则开通金守护自动上四圣兽坐席
    		若房间内金守护数量＞5个人，则根据周榜排序，前四个上四圣兽坐席,第五个以后为普
		 */
		
		int specialSeatNum = 4; //4个席位
		if(roomGoldGuardWorkList !=null && roomGoldGuardWorkList.size() >0){
			int goldGuardLength = roomGoldGuardWorkList.size();
			if(goldGuardLength <= specialSeatNum ){ // 少于4个金守护,随机给
				for(int i=0;i<goldGuardLength;i++){
					GuardWork g = roomGoldGuardWorkList.get(i);
					 g.setSortIndex(i+1); // sortIndex从1开始，
					 guardWorkMapper.update(g);
				}
			}else{ //多于4个金守护
				String beginTime = statisticsGiftBeginTime;
				String endTime = statisticsGiftEndTime;
				int qrySize = specialSeatNum;
				// 按上周贡献排序查询
				List<String> userList =  guardWorkMapper.findRoomGuardLastWeekGiveTop(roomId, beginTime, endTime,qrySize);
				int sortIndex =1; // sortIndex从1开始，
				for(int i=0;i<goldGuardLength;i++){
					GuardWork goldGuardWork = roomGoldGuardWorkList.get(i);
					if(userList!=null){
						for(int j=0;j<userList.size();j++){
							// 前4的给特权
							if(userList.get(j).equals(goldGuardWork.getUserid())){
								 goldGuardWork.setSortIndex(sortIndex); // 设置排序
								 guardWorkMapper.update(goldGuardWork);
								 sortIndex++; //排序++
								 
							}
							if(j+1 > specialSeatNum ){ // 最多给4个席位
								break;
							}
						}
					}
					if(sortIndex > specialSeatNum ){ // 最多给4个席位
						break;
					}
				}
				
				
				// 房间上周没送礼,补刀
				for(;sortIndex<=specialSeatNum;sortIndex++){
					for(int i=0;i<goldGuardLength;i++){
						GuardWork guardWork = roomGoldGuardWorkList.get(i);
						if(guardWork.getSortIndex()==0){
							guardWork.setSortIndex(sortIndex);
							guardWorkMapper.update(guardWork);
							break;
						}else{
							continue;
						}
					}	
				}
			}
			String keyRoomGuard = MCPrefix.USER_WORK_INFO_DB_CACHE_FOR_ROOM + roomId ;
			MemcachedUtil.delete(keyRoomGuard);
		}
	}

	
	public List<String> getRoomGuradDataFromCache(String roomId) throws Exception {
		List<String> userList = new ArrayList<String>();
		// 缓存key为房间+当天日期
		String key = MCPrefix.ROOM_GUARD_FOR_PEACH_CACHE + roomId + DateUntil.getFormatDate("yyyyMMdd", new Date());
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			userList = (List<String>) obj;
		} else {
			MemcachedUtil.delete(key);
			List<GuardWork> list = guardWorkMapper.listGuardWorkGroupByUser(roomId);
			if(list != null && list.size() >0) {
				for(GuardWork wk : list) {
					userList.add(wk.getUserid());
				}
				MemcachedUtil.set(key, userList, MCTimeoutConstants.ROOM_GUARD_FOR_PEACH_TIME_CACHE);
			}
		}
		return userList;
	}

	
	public JSONObject listGuardRankData() throws Exception {
		JSONObject ret = new JSONObject();
		String cacheKey = MCPrefix.GUARD_RANK_CACHE;
		Object obj = MemcachedUtil.get(cacheKey);
		if(obj != null) {
			ret = (JSONObject) obj;
		} else {
			// 1、查询所有守护，按房间分类
			int size = 90; // 榜单默认20个
			int goldSize = 200; // 
			Map<String, Integer> all = listAllGuard(size);
			if(all != null && all.size() >0) {
				// 2、按房间，查询所有黄金守护
				Map<String,Integer> goldmap = listGoldGuard(goldSize);
				JSONArray wpjt = new JSONArray(); // 王牌军团
				JSONArray txzs = new JSONArray(); // 铁血之师
				JSONArray hldd = new JSONArray(); // 虎狼大队
				JSONArray wwzd = new JSONArray(); // 威武中队
				JSONArray zyxd = new JSONArray(); // 忠义小队
				for(String anchorId : all.keySet()) {
					JSONObject json = new JSONObject();
					int total = Integer.parseInt(all.get(anchorId).toString());
					int goldNum = 0;
					// 黄金守护个数
					if(goldmap != null && goldmap.size() >0) {
						if(goldmap.containsKey(anchorId) && goldmap.get(anchorId) != null) {
							goldNum = Integer.parseInt(goldmap.get(anchorId).toString());
						}
					}
					UserInfoVo vo = this.userCacheInfoService.getInfoFromCache(anchorId, null);
					if(vo == null) {
						LogUtil.log.error("###listGuardRankData-查询骑士守护团，主播查询为空，anchorId=" + anchorId);
						continue;
					}
					
					String avatar = vo.getAvatar();
					json.put("name", vo.getNickname());
					json.put("num", total);
					json.put("avatar", avatar);
					
					if(total >= 50 && goldNum >= 5) {
						if(wpjt.size() < 6) {
							wpjt.add(json);
							continue;
						}
//						if(txzs.size() < 20) {
//							txzs.add(json);
//							continue;
//						}
//						if(hldd.size() < 20) {
//							hldd.add(json);
//							continue;
//						}
//						if(wwzd.size() < 20) {
//							wwzd.add(json);
//							continue;
//						}
//						if(zyxd.size() < 20) {
//							zyxd.add(json);
//						}
					} else if(total >= 30 && goldNum >= 3) {
						if(txzs.size() < 20) {
							txzs.add(json);
							continue;
						}
//						if(hldd.size() < 20) {
//							hldd.add(json);
//							continue;
//						}
//						if(wwzd.size() < 20) {
//							wwzd.add(json);
//							continue;
//						}
//						if(zyxd.size() < 20) {
//							zyxd.add(json);
//						}
					} else if(total >= 10 && goldNum >= 1) {
						if(hldd.size() < 20) {
							hldd.add(json);
							continue;
						}
//						if(wwzd.size() < 20) {
//							wwzd.add(json);
//							continue;
//						}
//						if(zyxd.size() < 20) {
//							zyxd.add(json);
//						}
					} else if(total >= 5) {
						if(wwzd.size() < 20) {
							wwzd.add(json);
							continue;
						}
//						if(zyxd.size() < 20) {
//							zyxd.add(json);
//						}
					} else if(total >= 2) {
						if(zyxd.size() < 20) {
							zyxd.add(json);
						} 
					}
				}
				ret.put("wpjt", wpjt);
				ret.put("txzs", txzs);
				ret.put("hldd", hldd);
				ret.put("wwzd", wwzd);
				ret.put("zyxd", zyxd);
				MemcachedUtil.set(cacheKey, ret);
			}
		}
		return ret;
	}

	
	public Map<String, Integer> listAllGuard(int size) throws Exception {
		return guardWorkMapper.listAllGuard(size);
	}

	
	public Map<String, Integer> listGoldGuard(int size) throws Exception {
		return guardWorkMapper.listGoldGuard(size);
	}

	
	public int getUserGuardCount(String userId, String roomId, Date beginTime,
			Date endTime) {
		return guardWorkMapper.getUserGuardCount(userId,roomId,beginTime,endTime);
	}

	
	public Map<String, Integer> listAllGuardByHaving() {
		return guardWorkMapper.listAllGuardByHaving();
	}

	
	public Map<String, Integer> listGoldGuardByHaving() {
		return guardWorkMapper.listGoldGuardByHaving();
	}

	
	public List<String> getRoomGuardUser(String roomId) {
		String key = MCPrefix.MEDAL_GUARD_USER+roomId;
		Object object = MemcachedUtil.get(key);
		int defaultTime = MCTimeoutConstants.MEDAL_GUARD_USER_CACHETIME;
		List<String> list = null;
		if(object != null){
			list = (List<String>) object;
		}else{
			list = guardWorkMapper.getRoomGuardUser(roomId);
			if(list == null){
				list = new ArrayList<String>();
			}
			MemcachedUtil.set(key, list,defaultTime);
		}
		return list;
	}

	
	public int getRoomGuardLevel(String userId, String roomId) throws Exception {
		int all = 0;
		int goldNum = 0;
		int level = 0;
		List<GuardVo> list = getGuardWorkData(userId, roomId);
		if(list != null && list.size() >0) {
			all = list.size();
			for(GuardVo vo : list) {
				if(vo.getLevel() == GuardTableEnum.GuardType.huangjin.getValue()) {
					goldNum++;
				}
			}
		}
		if(all >= 50 && goldNum >= 5 ) { 
			level = 5;
		} else if(all >= 30 && goldNum >= 3) {
			level = 4;
		} else if(all >= 10 && goldNum >= 1) {
			level = 3;
		} else if(all >= 5) {
			level = 2;
		} else if(all >= 2) {
			level = 1;
		}
		return level; 
	}

}
