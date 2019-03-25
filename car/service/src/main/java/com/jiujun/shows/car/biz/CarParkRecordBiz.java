package com.jiujun.shows.car.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarParkIncomeConfMapper;
import com.jiujun.shows.car.dao.CarParkIncomeRecordMapper;
import com.jiujun.shows.car.dao.CarParkRecordMapper;
import com.jiujun.shows.car.dao.SysCarMapper;
import com.jiujun.shows.car.dao.UserCarPortMapper;
import com.jiujun.shows.car.domain.CarParkIncomeConf;
import com.jiujun.shows.car.domain.CarParkIncomeRecord;
import com.jiujun.shows.car.domain.CarParkRecord;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.domain.UserCarPortDo;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.enums.LockTarget;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.car.vo.CarportVo;
import com.jiujun.shows.common.enums.IMBusinessEnum;
import com.jiujun.shows.common.enums.IMBusinessEnum.ImTypeEnum;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.IMutils;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.domain.UserAttentionDo;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserAttentionService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;
import com.mysql.jdbc.StringUtils;

/**
 * 停车记录业务
 * @author shao.xiang
 * @date 2017-06-21
 * 
 */
@Service("carParkRecordBiz")
public class CarParkRecordBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private CarParkRecordMapper carParkRecordMapper;
	
	@Resource
	private CarParkIncomeConfMapper carParkIncomeConfMapper;

	@Resource
	private CarParkIncomeRecordMapper carParkIncomeRecordMapper;
	
	@Resource
	private UserCarPortMapper userCarPortMapper;
	
	@Resource
	private SysCarMapper sysCarMapper;
	
	/** 用户账户服务 */
	@Resource
	private IUserAccountService userAccountService;

	@Resource
	private IUserAttentionService userAttentionService;

	/** 主播服务 */
	@Resource
	private IUserAnchorService userAnchorService;

	/** 用户信息缓存服务 */
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	/** 用户信息服务 */
	@Resource
	private IUserInfoService userInfoService;
	
	/** 抢车位 成功*/
	private static final int CARPORT_FIRST = 1;
	/** 停车产生收益 */
	private static final int CARPORT_SEC = 2;
	private static final String SOURCE = "停车收益";
	private static final String CONTENT = "停车收益记录，sourceId关联t_car_park_income_record";
	
	/** 定时任务处理停车收益状态，默认未处理 */
	static boolean HANDLECARPORTTASK_STATUS = false;

	/**
	 * 根据房间号查询该房间内是否有停靠的车位
	 * @return 已经停靠的车位，没有则为空
	 * @author shao.xiang
	 * @date 2017年7月15日
	 */
	public ServiceResult<List<CarportVo>> findCarportByRoomId(String roomId) {
		ServiceResult<List<CarportVo>> srt = new ServiceResult<List<CarportVo>>();
		srt.setSucceed(false);
		if (StringUtils.isNullOrEmpty(roomId)) {
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		try {
			List<Map> list = carParkRecordMapper.findCarportByRoomId(roomId);
			List<CarportVo> vo = new ArrayList<CarportVo>();
			if (list != null && list.size()>0) {
				for (Map m : list) {
					int carportId = 0;
					int carId = -1;
					int carport = -1;
					String beginTime = null;
					String userId = null;
					String status = null;
					String nickname = "";
					Date beTime = null;
					long time = 0;
					
					String userIdKey = "userId";
					String carIdKey = "carId";
					String carportKey = "carport";
					String beginTimeKey = "beginTime";
					String statusKey = "endStatus";
					String carportIdKey = "id";
					
					if (m.containsKey(carportIdKey) && m.get(carportIdKey) != null) {
						carportId = Integer.parseInt(m.get(carportIdKey).toString());
					}
					if (m.containsKey(userIdKey) && m.get(userIdKey) != null) {
						userId = m.get(userIdKey).toString();
					}
					if (m.containsKey(carIdKey) && m.get(carIdKey) != null) {
						carId = Integer.parseInt(m.get(carIdKey).toString());
					}
					if (m.containsKey(carportKey) && m.get(carportKey) != null) {
						carport = Integer.parseInt(m.get(carportKey).toString());
					}
					if (m.containsKey(beginTimeKey) && m.get(beginTimeKey) != null) {
						beginTime = m.get(beginTimeKey).toString();
						beTime = (Date) m.get(beginTimeKey);
					}
					if(m.containsKey(statusKey) && m.get(statusKey) != null) {
						status = m.get(statusKey).toString();
					}
					
					UserInfo userInfo = userInfoService.getUserByUserId(userId);
					if(userInfo != null) {
						nickname = userInfo.getNickName();
					}
					
					CarParkIncomeConf conf = carParkIncomeConfMapper.getParkTime(carId);
					if(conf != null && conf.getParktime() != null && beTime != null) {
						long pktime = conf.getParktime() * 60;
						int golds = conf.getReceivegold();
						int confId = conf.getId();
						time = DateUntil.getCurrentTime(beTime,pktime);
						if(time <= 0) {
							time = 0;
							String lockname = LockTarget.LOCK_CAR_ADDGOLD.getLockName() + userId;
							try {
								RdLock.lock(lockname);
								String key = "carport_" + userId + "_" + carportId;
								Object cache = RedisUtil.get(key);
								if(cache != null) {
									log.error("####findCarportByRoomId-处理停车收益，已经加过金币，不重复加，userId="+userId + ",carportId="+ carportId);
								} else {
										// 加金币
										log.error("####findCarportByRoomId-addgold,加金币，userId="+userId + ",carportId="+ carportId + ",confId="+confId+",golds="+golds);
										addCarportGold(userId, roomId, carportId, confId, conf.getParktime(), golds, carId);
										RedisUtil.set(key, "1", 60*5);
								}
							} catch(Exception e) {
								log.error(e.getMessage(),e);
							} finally {
								RdLock.unlock(lockname);
							}
						}
					}
					SysCarDo car = sysCarMapper.getObjectById(carId);
					CarportVo port = new CarportVo();
					port.setCarName(car.getCarName());
					port.setCarImg(Constants.CAR_CDN_URL + car.getImage());
					port.setCarId(carId);
					port.setUserId(userId);
					port.setCarport(carport);
					port.setBeginTime(beginTime);
					port.setStatus(status);
					port.setNickname(nickname);
					port.setTime(time);
					vo.add(port);
				}
			}
			srt.setSucceed(true);
			srt.setData(vo);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		}
		return srt;
	}
	
	/**
	 * 抢车位
	 * @param roomId 主播房间
	 * @param anchorId 主播id
	 * @param userId 抢车位用户id
	 * @param anchorLev 主播等级
	 * @param carport 所抢车位号
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月15日
	 */
	public ServiceResult<Boolean> grapCarport(String roomId, String anchorId, String userId,String anchorLev, int carport) {
		 ServiceResult<Boolean> srt = new  ServiceResult<Boolean>(false);
		if (StringUtils.isNullOrEmpty(roomId)
				|| StringUtils.isNullOrEmpty(userId)
				|| StringUtils.isNullOrEmpty(anchorLev)) {
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}

		// 主播等级小于3，不能抢车位---ok
		UserAccount anchor = this.userAccountService.getObjectByUserId(anchorId);
		if (anchor != null) {
			String anchorLevl = anchor.getAnchorLevel();
			String level = anchorLevl.substring(1);
			if(Integer.parseInt(level) <3){
				srt.setResultCode(ErrorCode.ERROR_11010.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11010.getResultDescr());
				return srt;
			}
		} else {
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}

		// 必须关注了主播才可以停靠 ---ok
		UserAttentionDo attens = userAttentionService.findAttentions(userId, anchorId);
		if (attens == null) {
			srt.setResultCode(ErrorCode.ERROR_11011.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11011.getResultDescr());
			return srt; 
		}

		// 当前使用的座驾是否有效 --ok
		UserCarPortDo userCar = userCarPortMapper.getInUseUserCarPort(userId);
		int carId = -1;
		if (userCar != null) {
			if (userCar.getStatus() == 2) {
				srt.setResultCode(ErrorCode.ERROR_11008.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11008.getResultDescr());
				return srt; 
			} else if (userCar.getStatus() == 3) {
				srt.setResultCode(ErrorCode.ERROR_11007.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11007.getResultDescr());
				return srt; 
			}
			carId = userCar.getCarId();
		} else {
			srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
			return srt; 
		}

		boolean grapFlag = false; // 是否抢成功，成功后再发通知
		// 抢车位 --ok
		String lockname = LockTarget.LOCK_CAR_GRABPORT.getLockName() + roomId;
		try{
			RdLock.lock(lockname);
			// 是否已停靠--ok
			CarParkRecord record = findCarportByUserId(userId);
			if (record != null) {
				// 同一房间已经停靠，不能再抢其他位
				srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
				return srt; 
			}
			// 当天是否已经停靠了3次
			List<CarParkIncomeRecord> recordList = carParkIncomeRecordMapper.findCarParkIncomeRecord(userId);
			if (recordList != null && recordList.size() > 2) {
				srt.setResultCode(ErrorCode.ERROR_11013.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11013.getResultDescr());
				return srt; 
			}
			
			CarParkRecord carpark = carParkRecordMapper.getCarportRecord(roomId, carport);
			if (carpark != null) {
				srt.setResultCode(ErrorCode.ERROR_11014.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11014.getResultDescr());
				return srt; 
			}
			
			CarParkRecord vo = new CarParkRecord();
			vo.setRoomid(roomId);
			vo.setAnchorId(anchorId);
			vo.setUserid(userId);
			vo.setCarid(carId);
			vo.setCarport(carport);
			vo.setBegintime(new Date());
			vo.setEndStatus("0");
			carParkRecordMapper.insert(vo);
			grapFlag = true;
			srt.setSucceed(true);
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		} finally {
			RdLock.unlock(lockname);
		}
		
		try{
			if(grapFlag) {
				// 用户昵称＋土豪已经为他心爱的座驾＋座驾图标＋停靠在xxx的房间里
				// 抢车位用户信息
				UserInfoVo fromUser = userCacheInfoService.getInfoFromCache(userId, null);
				// 主播信息
				UserInfoVo toUser = userCacheInfoService.getInfoFromCache(anchorId, roomId);
				JSONObject imData = new JSONObject();
				JSONObject contentJson = new JSONObject();
				contentJson.put("id", carId);
				SysCarDo car = sysCarMapper.getObjectById(carId);
				contentJson.put("carName", car.getCarName());
				contentJson.put("carImg", Constants.CAR_CDN_URL +  car.getImage());
				
				contentJson.put("type", CARPORT_FIRST);
				imData.put("msgtype", 2);
				imData.put("targetid", roomId);
				imData.put("type", IMBusinessEnum.ImTypeEnum.IM_11001_qiangchewei.getValue());
//						imData.put("user", JsonUtil.beanToJsonString(fromUser));
//						imData.put("to", toUser.getUid());
				imData.put("content", contentJson);
				
				int funID = 11001;
				int seqID = 1;
				try{
					boolean imSuccesFlag = IMutils.sendMsg2IM(funID, seqID, imData,userId);
					log.info("##### 抢车位成功后发送通知:"+imSuccesFlag);
					if(!imSuccesFlag){
						log.error("###### 抢车位成功后发送通知失败,roomId:"+roomId);
					}
				} catch(Exception e) {
					log.error("### grapCarport-抢车位成功后，发送通知失败，userId=" + userId);
					log.error(e.getMessage(),e);
				}
			}
		} catch(Exception e) {
			log.error("### grapCarport-抢车位成功后，发送通知失败，userId=" + userId);
		}
		// 抢车位成功后，发送通知
		return srt;
	}
	
	/**
	 * 定时任务使用，处理车位停靠后，停用时间及金币收益
	 * @throws Exception
	 */
	public void handlCarportUsed() throws Exception {
		try {
			log.error("###handlCarportUsed-isHandleCarportTask=" + HANDLECARPORTTASK_STATUS);
			if(HANDLECARPORTTASK_STATUS) {
				log.error("###handlCarportUsed-其他任务正在处理，本次不处理，直接返回。。。。");
				return;
			}
			HANDLECARPORTTASK_STATUS = true; // 设置当前状态，正在处理
			List<CarParkRecord> list = carParkRecordMapper.findCarportAll();
			Date nowDate = new Date();
			if (list != null && list.size() >0) {
				int carId = 0;
				int parkTime = 0;
				int golds = 0;
				int def = 0;// 停车开始时间与当前时间的秒数差
				int confTime = 0; 
				String userId = "";
				String roomId = "";
				String anchorId = "";
				for (CarParkRecord carport : list) {
					Date beginTime = carport.getBegintime();
					// DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", beginTime);
					def = DateUntil.getTimeIntervalSecond(beginTime,nowDate);
					carId = carport.getCarid();
					userId = carport.getUserid();
					roomId = carport.getRoomId();
					anchorId = carport.getAnchorId();
					parkTime = getDefDateTime(beginTime); 
					if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(roomId)) {
						Exception e = new CarBizException(ErrorCode.ERROR_11001);
						log.error(e.getMessage(), e);
						throw e;
					}
					
					CarParkIncomeConf conf = carParkIncomeConfMapper.getReciveGold(carId);
					if (conf == null) {
						Exception e = new CarBizException(ErrorCode.ERROR_11015);
						log.error(e.getMessage(), e);
						throw e;
					} 
					confTime = conf.getParktime() * 60; // second
//				def = confTime - parkTime;
					// 停靠时间到达，增加金币并设置结束状态
					log.info("### handlCarportUsed-userId:" + userId + ",停靠时间与当前时间差：def="+def + ",配置时间:confTime="+confTime);
					if(def >= confTime) {
						String key = "carport_" + userId + "_" + carId;
						Object cache = MemcachedUtil.get(key);
						if(cache != null) {
							log.error("####handlCarportUsed-已经加过金币，不重复加，userId="+userId + ",carportId="+ carId);
							continue;
						}
						// 加金币
						RedisUtil.set(key, "1", 60*5);
						golds = conf.getReceivegold();
						// 账户明细
						UserAccountBook userAccountBook = new UserAccountBook();
						userAccountBook.setUserId(userId);
						userAccountBook.setChangeGolds(golds);
						
						// 增加金币后，设置结束状态
						//				carport.setEndStatus("1");
						carParkRecordMapper.updateCarportEndStatus(userId);
						// 插入收益记录
						CarParkIncomeRecord vo = new CarParkIncomeRecord();
						vo.setUserid(userId);
						vo.setParkrecordid(carport.getId());
						vo.setPrakincomconfid(conf.getId());
						vo.setRecordtime(nowDate);
						carParkIncomeRecordMapper.insert(vo);
						// 金币流水账
						String sourceId = Integer.toString(vo.getId());
						String content_sys = "gift为金币数 ,sourceId关联t_car_park_income_record的id";
						
						userAccountBook.setSourceId(sourceId);
						userAccountBook.setSourceDesc(CONTENT);
						userAccountBook.setContent(SOURCE + ",加金币");
						userAccountBook.setRecordTime(nowDate);
						
						// 为用户增加金币
						this.userAccountService.addGolds(userId, golds,userAccountBook);
						
						// 停靠产生收益后，通知房间，格式：
						// 用户昵称＋土豪的座驾＋座驾图标＋已经停靠了xxx分钟，获得收益：xxx金币
						JSONObject imData = new JSONObject();
						JSONObject contentJson = new JSONObject();
						contentJson.put("id", carId);
						contentJson.put("type", CARPORT_SEC);
						contentJson.put("parkTime", conf.getParktime()); // 停靠时间
						contentJson.put("golds", golds); // 金币收益
						SysCarDo car = sysCarMapper.getObjectById(carId);
						contentJson.put("carName", car.getCarName());
						contentJson.put("carImg", Constants.CAR_CDN_URL +  car.getImage());
						
						imData.put("msgtype", 2);
						imData.put("targetid", roomId);
						imData.put("type", ImTypeEnum.IM_11001_qiangchewei.getValue());
//					imData.put("user", JsonUtil.beanToJsonString(fromUser));
						imData.put("to", userId);
						imData.put("content", contentJson);
						
						int funID = 11001;
						int seqID = 1;
						
						String systemUserIdOfIM  = Constants.SYSTEM_USERID_OF_IM;
						try {
							boolean imSuccesFlag = IMutils.sendMsg2IM(funID, seqID, imData,systemUserIdOfIM);
							log.info("##### 车辆停靠到达收益时间后发送通知:"+imSuccesFlag);
							if(!imSuccesFlag){
								log.error("###### 车辆停靠到达收益时间后发送通知失败,roomId:"+roomId + " 发送者userId:" +userId);
							}
						} catch(Exception e) {
							log.error("###### 车辆停靠到达收益时间后发送通知失败,roomId:"+roomId + " 发送者userId:" +userId);
							log.error(e.getMessage(),e);
						}
					}
					
				}
			}
			log.error("###handlCarportUsed-处理停车收益完毕，设置处理状态未false。。。。");
			HANDLECARPORTTASK_STATUS = false; // 设置当前状态，处理完毕
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			HANDLECARPORTTASK_STATUS = false;
		}
	}

	// 获取到当前时间为止的停靠时间，秒数，更精确些
	protected int getDefDateTime(Date date) {
		int def = 0;
		def = DateUntil.getMinute(date);
		return def;
	}

	/**
	 * 处理停车收金币
	 * @param userId
	 * @param roomId
	 * @param carportId
	 * @param confId
	 * @param pktime
	 * @param golds
	 * @param carId
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年9月26日
	 */
	private void addCarportGold(String userId,String roomId,int carportId,int confId,long pktime,int golds,int carId) throws Exception {
		Date nowDate = new Date();
		// 账户明细
		UserAccountBook userAccountBook = new UserAccountBook();
		userAccountBook.setUserId(userId);
		userAccountBook.setChangeGolds(golds);
		
		// 增加金币后，设置结束状态
		carParkRecordMapper.updateCarportEndStatus(userId);
		// 插入收益记录
		CarParkIncomeRecord vo = new CarParkIncomeRecord();
		vo.setUserid(userId);
		vo.setParkrecordid(carportId);
		vo.setPrakincomconfid(confId);
		vo.setRecordtime(nowDate);
		carParkIncomeRecordMapper.insert(vo);
		// 金币流水账
		String sourceId = Integer.toString(vo.getId());
		String content_sys = "gift为金币数 ,sourceId关联t_car_park_income_record的id";
		
		userAccountBook.setSourceId(sourceId);
		userAccountBook.setSourceDesc(CONTENT);
		userAccountBook.setContent(SOURCE + ",加金币");
		userAccountBook.setRecordTime(nowDate);
		
		// 为用户增加金币
		this.userAccountService.addGolds(userId, golds,userAccountBook);
		
		JSONObject imData = new JSONObject();
		JSONObject contentJson = new JSONObject();
		contentJson.put("id", carId);
		contentJson.put("type", CARPORT_SEC);
		contentJson.put("parkTime", pktime); // 停靠时间
		contentJson.put("golds", golds); // 金币收益
		
		SysCarDo car = sysCarMapper.getObjectById(carId);
		contentJson.put("carName", car.getCarName());
		contentJson.put("carImg", Constants.CAR_CDN_URL +  car.getImage());
		imData.put("msgtype", 2);
		imData.put("targetid", roomId);
		imData.put("type", ImTypeEnum.IM_11001_qiangchewei.getValue());
		imData.put("to", userId);
		imData.put("content", contentJson);
		
		int funID = 11001;
		int seqID = 1;
		
		String systemUserIdOfIM  = Constants.SYSTEM_USERID_OF_IM;
		try {
			boolean imSuccesFlag = IMutils.sendMsg2IM(funID, seqID, imData,systemUserIdOfIM);
			log.info("##### 车辆停靠到达收益时间后发送通知:"+imSuccesFlag);
			if(!imSuccesFlag){
				log.error("###### 车辆停靠到达收益时间后发送通知失败,roomId:"+roomId + " 发送者userId:" +userId);
			}
		} catch(Exception e) {
			log.error("###### 车辆停靠到达收益时间后发送通知失败,roomId:"+roomId + " 发送者userId:" +userId);
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 根据用户id，查询该用户下正在使用的车辆是否正在停靠
	 * 	该服务也提供给座驾使用
	 * @param userId
	 * @return	该用户正在停靠的车，没有为空
	 * @throws Exception
	 */
	private CarParkRecord findCarportByUserId(String userId) throws Exception {
		if (StringUtils.isNullOrEmpty(userId)) {
			Exception e = new CarBizException(ErrorCode.ERROR_11001);
			log.error(e.getMessage(), e);
			throw e;
		}
		CarParkRecord carpark = carParkRecordMapper.findCarportByUserId(userId);
		return carpark;
	}
}
