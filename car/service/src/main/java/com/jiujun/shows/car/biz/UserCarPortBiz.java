package com.jiujun.shows.car.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountBookOutService;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarGiveHisMapper;
import com.jiujun.shows.car.dao.CarParkRecordMapper;
import com.jiujun.shows.car.dao.SysCarMapper;
import com.jiujun.shows.car.dao.UserCarPortMapper;
import com.jiujun.shows.car.domain.CarGiveHisDo;
import com.jiujun.shows.car.domain.CarParkRecord;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.domain.UserCarPortDo;
import com.jiujun.shows.car.enums.CarGiveHisEnum;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.enums.LockTarget;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.enums.IMBusinessEnum.ImTypeEnum;
import com.jiujun.shows.common.enums.TaskIdEnum;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.IMutils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.enums.GuardTableEnum;
import com.jiujun.shows.user.domain.Level;
import com.jiujun.shows.user.service.IUserLevelService;


/**
 * 座驾业务
 * @author shao.xiang
 * @date 2017-09-21
 */
@Service("userCarPortBiz")
public class UserCarPortBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private SysCarMapper sysCarMapper;
	
	@Resource
	private UserCarPortMapper userCarPortMapper;
	
	@Resource
	private CarGiveHisMapper carGiveHisMapper;
	
	@Resource
	private CarParkRecordMapper carParkRecordMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserLevelService userLevelService;
	
	@Resource
	private IUserAccountBookOutService userAccountBookOutService;
	
	private static final String CONTENT = "座驾消费，sourciId为t_car_give_his表的id";
	
	/**
	 * 购买座驾
	 * @return boolean
	 * @author shao.xiang
	 * @date 2017-07-10
	 */
	@Transactional(rollbackFor=Exception.class)
	public ServiceResult<Boolean> buyCar(String userId, int carId) {
		log.info("### UserCarPortBiz-buyCar:userId=" + userId + ",carId=" + carId + ",begin...");
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		if(sysCar==null){
			srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
			return srt;
		}
		//座驾类型, 0:购买型;1:系统赠送型 ，系统赠送型的只能通过完成系统任务后由系统自动发放,不能通过购买、用户赠送的方式获取;
		//座驾类型, 0:经典购买型; 1:活动类型，默认条件下不可以购买，活动可以获取，管理后台设置花费人民币大于零就可以续期购买; 07:守护型 ，开通守护可立即获得该座驾
		if(sysCar.getType() == 1 && sysCar.getPrice() == 0){ 
			srt.setResultCode(ErrorCode.ERROR_11003.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11003.getResultDescr());
			return srt;
		}else{
			UserCarPortDo userCarPortDo = userCarPortMapper.getUserCarPort(userId, carId);
			//对于活动类座驾，配置了花费人民币大于零的座驾且以前没有该座驾记录的用户不许续期购买
			if(userCarPortDo == null && sysCar.getType() == 1){
				srt.setResultCode(ErrorCode.ERROR_11003.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11003.getResultDescr());
				return srt;
			}
		}
		if(sysCar.getType() == 7){ 
			srt.setResultCode(ErrorCode.ERROR_11004.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11004.getResultDescr());
			return srt;
		}
		// 并发锁
		String lock = LockTarget.LOCK_CAR_BUY.getLockName() + userId;
		try {
			RdLock.lock(lock);
			UserAccount userAccount = userAccountService.getObjectByUserId(userId);
			if(userAccount.getGold()<=0){
				srt.setResultCode(ErrorCode.ERROR_11005.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11005.getResultDescr());
				return srt;
			}
			// 座驾价格
			int subtractGolds = sysCar.getGold();
			log.info("### UserCarPortBiz-buyCar:subtractGolds="+subtractGolds);
			if(userAccount.getGold() < subtractGolds) {
				srt.setResultCode(ErrorCode.ERROR_11005.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11005.getResultDescr());
				return srt;
			}
			int addUserPoint = sysCar.getGold();
			handleBuyCar(userAccount, carId, subtractGolds, addUserPoint);
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
			return srt;
		} finally {
			RdLock.unlock(lock);
		}
		String s = null;
		s.toString();
		srt.setSucceed(true);
		log.info("### UserCarPortBiz-buyCar:userId=" + userId + ",carId=" + carId + ",end!");
		return srt;
	}
	
	

	/**
	 * 更新用户等级
	 * @param userAccount
	 * @param addUserPoint
	 */
	private void updateUserLevel(UserAccount userAccount, int addUserPoint) {
		long totalUserPoint = userAccount.getUserPoint()+addUserPoint ;
		userAccount.setUserPoint(totalUserPoint);
		Level level = userLevelService.getObjectByUserPoint(totalUserPoint);
		String newUserLevel = level.getLevel();
		userAccount.setUserLevel(newUserLevel);
		this.userAccountService.update(userAccount);
	}

	/**
	 * 车入库
	 * 加座驾,若原来已有,则更新到期时间;否则加记录
	 * @param userId
	 * @param carId
	 * @throws Exception
	 */
	public ServiceResult<Boolean> addCar2User(String userId, int carId) {
		log.info("###UserCarPortBiz-addCar2User,userId:"+userId+",carId:"+carId+",begin...");
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		Date nowDateTime = new Date();
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		if(sysCar ==null ){
			srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
			return srt;
		}
		UserCarPortDo userCarPortDo =userCarPortMapper.getUserCarPort(userId, carId);
		if(userCarPortDo==null){//没买过,则插入记录
			//加座驾
			UserCarPortDo  userCarPor =new UserCarPortDo();
			userCarPor.setUserId(userId);
			userCarPor.setCarId(carId);
			userCarPor.setInUse(0); //是否正在使用,0:否,1:是
			userCarPor.setNum(1); // 默认一辆
			userCarPortMapper.insert(userCarPor);
		}else{ //之前买过
			Date dbEndTime = userCarPortDo.getEndTime();
			if(dbEndTime!=null){ //启用过
				if(nowDateTime.after(dbEndTime)){//已过期,则重新设置为未启用
					userCarPortDo.setBeginTime(null);
					userCarPortDo.setEndTime(null);
					userCarPortDo.setNum(userCarPortDo.getNum()+1);//数量+1
					userCarPortDo.setInUse(0);
				}else{ //未过期,则延长时间
					Date newEndTime = null;
					newEndTime = DateUntil.addDay(dbEndTime, sysCar.getEffectiveDays());
					userCarPortDo.setEndTime(newEndTime);
				}
				
			}else{ //未启用过,则数量加1
				userCarPortDo.setNum(userCarPortDo.getNum()+1);//数量+1
			}
			userCarPortMapper.update(userCarPortDo);
			
			//清缓存,让修改座驾立刻生效
			String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
			RedisUtil.del(userCacheKey);
		}
		srt.setSucceed(true);
		log.info("###UserCarPortBiz-addCar2User,userId:"+userId+",carId:"+carId+",end!");
		return srt;
	}

	/**
	 * 设置座驾为当前使用
	 * @param carPortId 座驾id
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public ServiceResult<Boolean> inUse(String userId, int carId) {
		log.info("### UserCarPortBiz-inUse:userId=" + userId + ",carId=" + carId + ",begin...");
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		
		//查询在房间车位里面正在摆放的车
		CarParkRecord carParkRecord = carParkRecordMapper.findCarportByUserId(userId);
		//有在抢车位,则不给切换
		if(carParkRecord!=null){
			srt.setResultCode(ErrorCode.ERROR_11006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11006.getResultDescr());
			return srt;
		}
		
		//获取用户要使用的座驾
		UserCarPortDo tobeInUseUserCarPort =  userCarPortMapper.getUserCarPort(userId,carId);
		//验证是否拥有此座驾
		if(tobeInUseUserCarPort == null){//为空说明此座驾不是他的
			srt.setResultCode(ErrorCode.ERROR_11007.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11007.getResultDescr());
			return srt;
		}
		
		//获取用户原来正在使用的座驾
		UserCarPortDo oldInUseUserCarPort = userCarPortMapper.getInUseUserCarPort(userId,1);
		if(oldInUseUserCarPort !=null && oldInUseUserCarPort.getCarId() ==carId){
			// 启用的当前座驾是原来正在使用的，默认为成功
			srt.setSucceed(true);
			return srt;
		}
		
		Date nowDate = new Date();
		Date oldEndTime = tobeInUseUserCarPort.getEndTime();
		SysCarDo sysCar = this.sysCarMapper.getObjectById(carId);
		int guardType = 0;
		if(sysCar != null) {
			guardType = sysCar.getType();
		}
		int num = tobeInUseUserCarPort.getNum();//库存的汽车数量
		//验证座驾是否已过期
		if(oldEndTime!=null){
			if(nowDate.after(oldEndTime)){
				if(num <= 0){  //已过期,且没库存
					srt.setResultCode(ErrorCode.ERROR_11008.getResultCode());
					srt.setResultMsg(ErrorCode.ERROR_11008.getResultDescr());
					return srt;
				}
			}
		}
		
		// 更新座驾信息:开始时间、到期时间、使用状态、库存剩余量
		int totalDays = sysCar.getEffectiveDays()*num;//有效天数=每辆的有效天*库存数量
		if(guardType == 7) { // 守护座驾在获得的时候就已经设置了结束时间，启用的时候，不再重复设置结束时间
			tobeInUseUserCarPort.setBeginTime(nowDate);
		} else {
			if(oldEndTime==null){//第一次启用
				//设置开始时间、到期时间
				tobeInUseUserCarPort.setBeginTime(nowDate);
				Date newEndTime = DateUntil.addDay(nowDate, totalDays);
				tobeInUseUserCarPort.setEndTime(newEndTime);
			}else{
				
				if(num>=1){//启用过(未过期)、有库存,则重新设置到期时间
					Date newEndTime =  DateUntil.addDay(oldEndTime, totalDays);
					tobeInUseUserCarPort.setEndTime(newEndTime);
				}
			}
		}
		tobeInUseUserCarPort.setInUse(1);
		tobeInUseUserCarPort.setNum(0);//库存数量设置为0
		userCarPortMapper.update(tobeInUseUserCarPort);
		
		//设置原来使用的状态为未使用
		if(oldInUseUserCarPort!=null){
			oldInUseUserCarPort.setInUse(0);
			userCarPortMapper.update(oldInUseUserCarPort);
		}
		//清缓存,让修改座驾立刻生效
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
		RedisUtil.del(userCacheKey);
		srt.setSucceed(true);
		log.info("### UserCarPortBiz-inUse:userId=" + userId + ",carId=" + carId + ",end!");
		return srt;
	}

	/**
	 * 用户之间赠送座驾
	 * @param sendUserId 赠送用户
	 * @param receiveUserId 收到用户
	 * @return boolean
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	@Transactional(rollbackFor=Exception.class)
	public ServiceResult<Boolean> giveCar(String sendUserId, String receiveUserId, int carId,String sendComment) {
		log.info("### UserCarPortBiz-giveCar:sendUserId=" + sendUserId
				+ ",receiveUserId=" + receiveUserId + ",carId=" + carId + ",begin...");
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(sendUserId)||StringUtils.isEmpty(receiveUserId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		if(sysCar==null){
			srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
			return srt;
		}
		if(sysCar.getType()==1){ //座驾类型, 0:购买型;1:系统赠送型 ，系统赠送型的只能通过完成系统任务后由系统自动发放,不能通过购买、用户赠送的方式获取
			srt.setResultCode(ErrorCode.ERROR_11003.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11003.getResultDescr());
			return srt;
		}
		log.info(String.format("###giveCar,sendUserId:%s,receiveUserId:%s,carId:%s",sendUserId,receiveUserId,carId));
		
		String lock = LockTarget.LOCK_CAR_GIVE.getLockName() + sendUserId;
		try {
			RdLock.lock(lock);
			UserAccount userAccount = userAccountService.getObjectByUserId(sendUserId);
			if(userAccount.getGold()<=0){
				srt.setResultCode(ErrorCode.ERROR_11005.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_11005.getResultDescr());
				return srt;
			}
			// 赠送座驾
			giveCarWithSyn(userAccount, sendUserId, receiveUserId, carId, sendComment);
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		} finally {
			RdLock.unlock(lock);
		}
		// 发消息
		try {
			JSONObject imReqmsg = new JSONObject();
			JSONObject imData = new JSONObject();
			JSONObject contentJson = new JSONObject();
			contentJson.put("id", carId);
			contentJson.put("carName", sysCar.getCarName());
			contentJson.put("carImg", Constants.CAR_CDN_URL + sysCar.getImage());
			imData.put("msgtype", 1);
			imData.put("targetid", receiveUserId);
			imData.put("type", ImTypeEnum.IM_11001_songzuojia.getValue());
			imData.put("content", contentJson);
			imReqmsg.put("length", 102);
			//	imData.put("user", JsonUtil.beanToJsonString(fromUser));
			imData.put("to", receiveUserId);
			imReqmsg.put("data", imData.toString());
			IMutils.sendMsg2IM(imReqmsg,sendUserId);
		} catch(Exception e) {
			// 消息失败，不影响赠送业务，不回滚
			log.error(e.getMessage(),e);
		}
		srt.setSucceed(true);
		log.info("### UserCarPortBiz-giveCar:sendUserId=" + sendUserId
				+ ",receiveUserId=" + receiveUserId + ",carId=" + carId + ",end!");
		return srt;
	}

	/**
	 * 添加座驾流水记录、发im消息等
	 * @throws Exception
	 */
	private void sysProvideGiftCar(String userId, int carId,int type,String roomId,String comment) throws Exception {
		log.info(String.format("###sysProvideGiftCar,userId:%s,carId:%s,type:%s,comment:%s",userId,carId,type,comment));
		if(StringUtils.isEmpty(userId)){
			Exception e = new CarBizException(ErrorCode.ERROR_11001);
			log.error(e.getMessage() ,e);
			throw e;
		}
		
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		if(sysCar==null){
			Exception e = new CarBizException(ErrorCode.ERROR_11002);
			log.error(e.getMessage() ,e);
			throw e;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append("###sysProvideGiftCar,userId:"+userId+",carId:"+carId+",comment:"+comment);
		log.info(sbf.toString());
//		addCar2User(userId, carId);
		String sendUserId = BaseConstants.SENDUSER_DEFAULT;
		//添加赠送记录
		String sendComment = comment;
		Date nowDate = new Date();
		int defaultNum = 1;//默认数量
		CarGiveHisDo carGiveHisDo = new CarGiveHisDo();
		carGiveHisDo.setCarId(carId);
		carGiveHisDo.setSendUserId(sendUserId);
		carGiveHisDo.setReceiveUserId(userId);
		carGiveHisDo.setRecordDate(nowDate);
		carGiveHisDo.setComment(sendComment);
		carGiveHisDo.setType(type);
		carGiveHisDo.setNum(defaultNum);
		carGiveHisMapper.insert(carGiveHisDo);
		
		if(!StringUtils.isEmpty(roomId)){
			//发送系统通知
			//赠送座驾群发消息
			JSONObject content = new JSONObject();
			content.put("id", carId);
			content.put("carName", sysCar.getCarName());
			content.put("carImg", sysCar.getImage());
			JSONObject imReqmsg = new JSONObject();
			JSONObject imData = new JSONObject();
			imData.put("msgtype", 2);
			imData.put("targetid", roomId);
			imData.put("type", ImTypeEnum.IM_11001_songzuojia.getValue());
			imData.put("content", JsonUtil.beanToJsonString(content));
			imData.put("to", userId);
			imReqmsg.put("length", 102);
			imReqmsg.put("data", imData.toString());
			String systemUserIdOfIM  = BaseConstants.SYSTEM_USERID_OF_IM;
			IMutils.sendMsg2IM(imReqmsg,systemUserIdOfIM);
			
		}
	}

	/**
	 * 查询用户的座驾
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public ServiceResult<Page> pageFindUserCarPort(String userId,Page reqPageVo)  {
		ServiceResult<Page> srt = new ServiceResult<Page>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(userId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		//根据id,名称、类型分页查询
		Page page = new Page();
		page.setPageNum(reqPageVo.getPageNum());
		page.setPagelimit(reqPageVo.getPagelimit());
		int count = userCarPortMapper.countForGetListByPage(userId);
		page.setCount(String.valueOf(count));
		List<Map> list = userCarPortMapper.getListByPage(userId);
		JSONArray jsonArray = new JSONArray();
		if(list!=null){
			for(Map m:list){
				CarVo car = covertMap2CarVo(m);
				if(car!=null){
					jsonArray.add(car.buildJson());
				}
			}
		}
		page.setDataJsonStr(jsonArray.toString());
		srt.setSucceed(true);
		srt.setData(page);
		return srt;
	}

	private CarVo covertMap2CarVo( Map m) {
		if(m==null){
			return null;
		}
		CarVo car = new CarVo();
		Date nowDate = new Date();
		if(m.containsKey("carId")&&m.get("carId")!=null){
			car.setCarId(Integer.parseInt(m.get("carId").toString()));
		}
		if(m.containsKey("carName")&&m.get("carName")!=null){
			car.setCarName(m.get("carName").toString());
		}
		if(m.containsKey("type")&&m.get("type")!=null){
			car.setCarType(Integer.parseInt(m.get("type").toString()));
		}
		if(m.containsKey("gold")&&m.get("gold")!=null){
			car.setCarSpendGolds(Integer.parseInt(m.get("gold").toString()));
		}
		if(m.containsKey("price")&&m.get("price")!=null){
			car.setCarSpendMoney(Double.parseDouble(m.get("price").toString()));
		}
		if(m.containsKey("effectiveDays")&&m.get("effectiveDays")!=null){
			car.setCarEffectiveDays(Integer.parseInt(m.get("effectiveDays").toString()));
		}
		if(m.containsKey("beginTime")&&m.get("beginTime")!=null){
			Object beginTimeObj = m.get("beginTime");
			Date beginTime = (Date)beginTimeObj;
			car.setBeginTime(DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss",beginTime));
		}
		Date endTime = null;
		if(m.containsKey("endTime")&&m.get("endTime")!=null){
			Object endTimeObj = m.get("endTime");
			endTime = (Date)endTimeObj;
			car.setEndTime(DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", endTime));
			// 剩余天数
			try {
				int remainDays = DateUntil.daysBetween(nowDate, endTime);
				// 剩余的时间倒计时
				String remainTimes = DateUntil.getTimeRemains(nowDate, endTime);
				car.setRemainDays(remainDays);
				car.setRemainTimes(remainTimes);
			} catch(Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		if(endTime!=null&&nowDate.after(endTime)){
			car.setStatus(2); // 2 : 过期
		}else{
			if(m.containsKey("status")&&m.get("status")!=null){
				car.setStatus(Integer.parseInt(m.get("status").toString()));
			}
		}
		if(m.containsKey("inUse")&&m.get("inUse")!=null){
			car.setInUse(Integer.parseInt(m.get("inUse").toString()));
		}
		if(m.containsKey("userCarPortId")&&m.get("userCarPortId")!=null){
			car.setUserCarPortId(Integer.parseInt(m.get("userCarPortId").toString()));
		}
		return car;
	}

	/**
	 * 获取当前正在使用的座驾详细信息
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-10
	 */
	public ServiceResult<CarVo> getInUseUserCarPortDetailInfo(String userId) {
		log.info("### car-userId=" + userId + ",begin...");
		//获取用户要使用的座驾
		ServiceResult<CarVo> srt = new ServiceResult<CarVo>();
		srt.setSucceed(true);
		try {
			Map m = userCarPortMapper.getInUseDetailInfo(userId, 1);
			CarVo car = covertMap2CarVo(m);
			srt.setData(car);
		} catch(Exception e) {
			srt.setSucceed(false);
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		}
		log.info("### car-userId=" + userId + ",end!");
		return srt;
	}

	/**
	 * 获取当前正在使用的座驾
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月16日
	 */
	public ServiceResult<UserCarPortDo> getInUseUserCarPort(String userId) {
		ServiceResult<UserCarPortDo> srt = new ServiceResult<UserCarPortDo>();
		srt.setSucceed(true);
		UserCarPortDo upd = userCarPortMapper.getInUse(userId, 1);
		if(upd != null) {
			srt.setData(upd);
		} else {
			srt.setSucceed(false);
		}
		return srt;
	}

	/**
	 * 取消使用
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public ServiceResult<Boolean> cancelInUse(String userId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		//获取用户要取消使用的座驾
		//UserCarPortDo tobeCancelInUseUserCarPort =  userCarPortMapper.getUserCarPort(userId,carId);
		UserCarPortDo tobeCancelInUseUserCarPort =  userCarPortMapper.getInUseUserCarPort(userId,1);
		//验证是否拥有此座驾
		if(tobeCancelInUseUserCarPort==null){ //为空说明已取消使用
			srt.setResultCode(ErrorCode.ERROR_11009.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11009.getResultDescr());
			return srt;
		}
		//设置原来使用的状态为未使用
		if(tobeCancelInUseUserCarPort!=null){
			tobeCancelInUseUserCarPort.setInUse(0);
			userCarPortMapper.update(tobeCancelInUseUserCarPort);
		}
		//清缓存,让修改座驾立刻生效
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
		RedisUtil.del(userCacheKey);
		return srt;
	}

	@Deprecated
	public void allotCar2UserTaskList(TaskIdEnum taskIdEnum,String userId, int carId, int type,String roomId, String comment) throws Exception {
		if(StringUtils.isEmpty(userId)){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
		}
		
		this.sysProvideGiftCar(userId, carId, type, roomId, comment);
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		String prizeName = sysCar.getCarName();
		String remark = comment;
		int number = 1;
//		sysTaskPrizesService.insertSysTaskPrizes(userId, taskIdEnum, GiftTypeBusinessEnum.Car, carId, number, prizeName, remark);
	}


	/**
	 * 下发座驾
	 * @param type 类型,0:购买,1:用户赠送,2:系统赠送(连续登陆7天),3:系统赠送(蜜桃礼物),4:系统赠送(充值赠送),5:其他 6.:系统赠送(分享)
	 * @param roomId 房间号(1:不为空时用于群发消息;2:为空则不发房间消息)
	 * @param flag2taskList 是否需要到任务页面领取(此功能已取消，默认自动下发)
	 * @return
	 * @author shao.xiang
	 * @date 2017年9月26日
	 */
	public ServiceResult<Boolean> sysActiveGiveCar(String userId, int carId, int type,
			String roomId, String comment, boolean flag2taskList) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)){
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		log.info("###begin-sysActiveGiveCar,userId:"+userId);
		flag2taskList = false;
		try {
			if(!flag2taskList) {
				// 车入库
				addCar2User(userId, carId);
				// 添加座驾流水记录、发im消息等
				sysProvideGiftCar(userId, carId, type, roomId, comment);
				srt.setSucceed(true);
			}
		} catch(CarBizException e) {
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		}
		log.info("###end-sysActiveGiveCar,userId:"+userId);
		return srt;
	}

	/**
	 * 查询用户的座驾
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月19日
	 */
	public ServiceResult<List<SysCarDo>> findUserCars(String userId) throws Exception {
		ServiceResult<List<SysCarDo>> srt = new ServiceResult<List<SysCarDo>>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(userId)){
			Exception e = new CarBizException(ErrorCode.ERROR_11001);
			log.error(e.getMessage() ,e);
			throw e;
		}
		List<SysCarDo> data = sysCarMapper.findUserCars(userId);
		srt.setData(data);
		srt.setSucceed(true);
		return srt;
	}

	/**
	 * 为用户加守护座驾
	 * @param isContinue 是否续期
	 * @param validate 座驾有效时间，与守护结束时间一致
	 * @param gkEndTime 守护最长结束时间
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月26日
	 */
	public ServiceResult<Boolean> addCar2User(String userId, int carId, int guardType,
			int priceType, boolean isContinue, int validate, Date gkEndTime) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info("###addCar2User-guard_car,userId:"+userId+",carId:"+carId+",guardType="+guardType + ",priceType=" +priceType);
		Date nowDateTime = new Date();
		// 座驾是否配置
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		if(sysCar == null) {
			srt.setResultCode(ErrorCode.ERROR_11002.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11002.getResultDescr());
			return srt;
		}
		UserCarPortDo userCarPortDo =userCarPortMapper.getUserCarPort(userId, carId);
		if(userCarPortDo==null){//没买过,则插入记录
			log.info("###addCar2User-guard_car,validate="+validate+ ",没买过,则插入记录");
			//加座驾
			UserCarPortDo  userCarPor =new UserCarPortDo();
			userCarPor.setUserId(userId);
			userCarPor.setCarId(carId);
			userCarPor.setInUse(0); //是否正在使用,0:否,1:是
			userCarPor.setNum(1); // 默认一辆
			// 设置启用过，与守护座驾结束时间，与守护有效期一致
			userCarPor.setEndTime(DateUntil.addDatyDatetime(nowDateTime, validate));
			userCarPortMapper.insert(userCarPor);
		}else{ //之前买过
			Date dbEndTime = userCarPortDo.getEndTime();
			if(dbEndTime!=null){ //启用过
				if(nowDateTime.after(dbEndTime)){//已过期,则重新设置为未启用
					userCarPortDo.setBeginTime(null);
					userCarPortDo.setEndTime(DateUntil.addDatyDatetime(nowDateTime, validate));
					userCarPortDo.setInUse(0);
				}else{ //未过期,则延长时间
					Date newEndTime = DateUntil.addDatyDatetime(nowDateTime, validate);
					if(gkEndTime.after(dbEndTime)) { // 新购买守护有效期大于之前购买的，则延长座驾时间
//						if(isContinue) { // 是否续期
//							userCarPortDo.setEndTime(DateUntil.addDatyDatetime(dbEndTime, validate));
//						} else {
//							userCarPortDo.setEndTime(newEndTime);
//						}
						// 对于某一类型的守护座驾，设置结束时间为该类型守护最长的有效期一致
						userCarPortDo.setEndTime(gkEndTime);
					}
				}
			}
			userCarPortDo.setNum(1);//数量+1
			// 更新座驾
			userCarPortMapper.update(userCarPortDo);
		}

		String sendUserId = BaseConstants.SENDUSER_DEFAULT;
		//添加赠送记录
		String sendComment = "购买守护，获得守护专属座驾";
		Date nowDate = new Date();
		int defaultNum = 1;//默认数量
		CarGiveHisDo carGiveHisDo = new CarGiveHisDo();
		carGiveHisDo.setCarId(carId);
		carGiveHisDo.setSendUserId(sendUserId);
		carGiveHisDo.setReceiveUserId(userId);
		carGiveHisDo.setRecordDate(nowDate);
		carGiveHisDo.setComment(sendComment);
		carGiveHisDo.setType(GuardTableEnum.CarType.shouhu.getValue()); // 守护座驾
		carGiveHisDo.setNum(defaultNum);
		carGiveHisMapper.insert(carGiveHisDo);
		
		//清缓存,让修改座驾立刻生效
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
		RedisUtil.del(userCacheKey);
		srt.setSucceed(true);
		return srt;
	}
	
	private void handleBuyCar(UserAccount userAccount, int carId, int gold, int addUserPoint) throws Exception {
		//买座驾加经验，多少金币多少经验；
		updateUserLevel(userAccount, addUserPoint);
		String userId = userAccount.getUserId();
		// 座驾入库
		addCar2User(userId, carId);
		//添加购买记录(赠送者和受赠者设置为同一人)
		Date nowDate = new Date();
		CarGiveHisDo carGiveHisDo = new CarGiveHisDo();
		carGiveHisDo.setCarId(carId);
		carGiveHisDo.setSendUserId(userId);
		carGiveHisDo.setReceiveUserId(userId);
		carGiveHisDo.setRecordDate(nowDate);
		carGiveHisDo.setNum(1);//默认1个
		carGiveHisDo.setComment("购买");
		carGiveHisDo.setType(CarGiveHisEnum.TypeEnum.buy.getType());
		carGiveHisMapper.insert(carGiveHisDo);
		String sourceId = Integer.toString(carGiveHisDo.getId());
		// 流水
		UserAccountBook userAccountBook = new UserAccountBook();
		userAccountBook.setUserId(userId);
		userAccountBook.setChangeGolds(-gold);
		userAccountBook.setSourceId(sourceId);
		userAccountBook.setSourceDesc(CONTENT);
		userAccountBook.setContent("购买座驾，扣金币");
		userAccountBook.setRecordTime(nowDate);
		userAccountService.subtractGolds(userId, gold,userAccountBook);
	}
	
	private void giveCarWithSyn(UserAccount userAccount, String sendUserId, String receiveUserId,
			int carId, String sendComment) throws Exception {
		SysCarDo  sysCar  = sysCarMapper.getObjectById(carId);
		//买座驾加经验，多少金币多少经验；
		int addUserPoint = sysCar.getGold();
		updateUserLevel(userAccount, addUserPoint);
		
		/** 扣赠送者的金币 */ 
		int subtractGolds = sysCar.getGold();
		addCar2User(receiveUserId, carId);
		//添加赠送记录
		Date nowDate = new Date();
		CarGiveHisDo carGiveHisDo = new CarGiveHisDo();
		carGiveHisDo.setCarId(carId);
		carGiveHisDo.setSendUserId(sendUserId);
		carGiveHisDo.setReceiveUserId(receiveUserId);
		carGiveHisDo.setRecordDate(nowDate);
		carGiveHisDo.setNum(1);//默认1个
		carGiveHisDo.setComment(sendComment);
		carGiveHisDo.setType(CarGiveHisEnum.TypeEnum.userGive.getType());
		carGiveHisMapper.insert(carGiveHisDo);
		
		// 金币流水账
		String sourceId = Integer.toString(carGiveHisDo.getId());
		UserAccountBook userAccountBook = new UserAccountBook();
		userAccountBook.setUserId(sendUserId);
		userAccountBook.setChangeGolds(-subtractGolds);
		userAccountBook.setSourceId(sourceId);
		userAccountBook.setSourceDesc(CONTENT);
		userAccountBook.setRecordTime(nowDate);
		userAccountBook.setContent("赠送座驾，扣金币");
		userAccountService.subtractGolds(sendUserId, subtractGolds,userAccountBook);
	}
}
