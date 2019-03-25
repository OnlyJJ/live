package com.jiun.shows.guard.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.enums.IMBusinessEnum.ImTypeEnum;
import com.jiujun.shows.common.utils.IMutils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.decorate.enums.DecorateTableEnum;
import com.jiujun.shows.decorate.service.IDecoratePackageService;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.constant.Constants;
import com.jiujun.shows.guard.domain.GuardConf;
import com.jiujun.shows.guard.domain.GuardPayHis;
import com.jiujun.shows.guard.domain.GuardWork;
import com.jiujun.shows.guard.domain.GuardWorkConf;
import com.jiujun.shows.guard.enums.ErrorCode;
import com.jiujun.shows.guard.enums.GuardTableEnum;
import com.jiujun.shows.guard.exception.GuardBizException;
import com.jiujun.shows.guard.service.IGuardWorkService;
import com.jiujun.shows.tools.enums.PayGiftOutEnum;
import com.jiujun.shows.tools.gift.domain.PayGiftOut;
import com.jiujun.shows.tools.gift.service.IPayGiftOutService;
import com.jiujun.shows.user.domain.UserAnchor;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;
import com.jiun.shows.guard.dao.GuardConfMapper;
import com.jiun.shows.guard.dao.GuardPayHisMapper;
import com.jiun.shows.guard.dao.GuardWorkConfMapper;
import com.jiun.shows.guard.dao.GuardWorkMapper;
import com.mysql.jdbc.StringUtils;


/**
 * Service -守护购买记录表，用户每购买或续期一次，都增加一条记录
 */
@Service("guardPayHisBiz")
public class GuardPayHisBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_GUARD_SERVICE);

	@Resource
	private GuardPayHisMapper guardPayHisMapper;
	
	@Resource
	private GuardConfMapper guardConfMapper;
	
	@Resource
	private GuardWorkConfMapper guardWorkConfMapper;
	
	@Resource
	private GuardWorkMapper guardWorkMapper;
	
	@Resource
	private IGuardWorkService guardWorkService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserCarPortService userCarPortService;

	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	public IUserAnchorService userAnchorService;
	
	@Resource
	private IPayGiftOutService payGiftOutService;
	
	private static final int ROOM_GUARD_NUM = 12; // 房间内守护个数
	
	private static final String BUY_REMARK = "首次购买";
	private static final String REL_REMARK = "续期";
	
	@Transactional(rollbackFor=Exception.class)
	public ServiceResult<Boolean> payForGuard(String userId, String anchorId, String roomId,int workId, int guardType, int priceType) throws Exception {
		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(roomId)) {
			Exception e = new GuardBizException(ErrorCode.ERROR_12001);
			throw e;
		}
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		handlePayForGuard(userId, anchorId, roomId, workId, guardType,priceType);
		return srt;
	}
	
	/**
	 * 处理购买守护业务，放在内层，以免同步与事务同事存在时，造成错误
	 * @param userId
	 * @param anchorId
	 * @param roomId
	 * @param workId
	 * @param guardType
	 * @param priceType
	 * @throws Exception
	 */
	private void handlePayForGuard(String userId, String anchorId,
			String roomId, int workId, int guardType, int priceType)
			throws Exception {
		log.info(String.format("###begin-payForGuard,userId:%s,anchorId:%s,roomId:%s,workId:%s,guardType:%s,priceType:%s", userId,anchorId,roomId,workId,guardType,priceType));
		//synchronized(UserAccount.class) {
			int price = 0;
			int diamond = 0;
			int guardId = 0;
			int validate = 0;
			int userPoint = 0;
			int anchorPoint = 0;
			int carId = 0;
			int isPeriod = 1; 
			int decorateId = 0;
			boolean periodFlag = true;
			boolean isContinue = false;
			String guardName = "";
			if(guardType == GuardTableEnum.GuardCarType.baiyin.getValue()) {
				carId = GuardTableEnum.CarId.baiyin.getValue();
				decorateId = DecorateTableEnum.Id.ShouhuOneDec.getValue();
			} else if(guardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
				carId = GuardTableEnum.CarId.huangjin.getValue();
				decorateId = DecorateTableEnum.Id.ShouhuTwoDec.getValue();
			}
			
			// 查询守护配置信息
			GuardConf guardConf = guardConfMapper.getGuardConfData(guardType,priceType);
			if(guardConf != null) {
				price = guardConf.getPrice();
				diamond = guardConf.getDiamond();
				guardId = guardConf.getId();
				userPoint = guardConf.getUserpoint();
				anchorPoint = guardConf.getAnchorpoint();
				validate = guardConf.getValidate();
				isPeriod = guardConf.getIsPeriod();
				guardName = guardConf.getName();
				if(isPeriod == 0) {
					periodFlag = false;
				}
			} else {
				// 友情提示，网咯不好啊什么的。
				Exception e = new GuardBizException(ErrorCode.ERROR_12000);
				throw e;
			}
			
			// 用户账户信息
			// 1、判断是否足够金币
			UserAccount userAccount = userAccountService.getObjectByUserId(userId);
			if(userAccount != null && userAccount.getGold() >= price) { // 金额满足购买
				// 判断是否续期
				GuardWork gkHis = guardWorkService.getObjectById(workId);
				if(gkHis != null) {
					isContinue = true;
				}
				
				// 2、判断是否还有空位可以购买
				List<GuardWork> all = guardWorkMapper.getGuardWorkList(roomId);
				GuardWorkConf guardWorkConf = guardWorkConfMapper.getGuardWorkConfData(roomId);
				log.info("### payForGuard,all.size="+all.size());
				if(!isContinue && all != null) {
					if(guardWorkConf != null) {
						if( all.size() >= guardWorkConf.getMaxSize()) {
							Exception e = new GuardBizException(ErrorCode.ERROR_12002);
							throw e;
						}
					} else {
						if(all.size() >= ROOM_GUARD_NUM) { // 设置一个默认的值，如果没有配置，最少返回长度为12，保证没有问题
							Exception e = new GuardBizException(ErrorCode.ERROR_12002);
							throw e;
						}
					}
				}
			
				String strLevel = userAccount.getUserLevel();//购买守护前等级
				//主播账户信息
				UserAccount anchorUserAccount = userAccountService.getObjectByUserId(anchorId);
				String anchorStrLevel = anchorUserAccount.getAnchorLevel();
				Date now = new Date();
				int newWorkId = 0;
				//1 增加work记录
				GuardWork gk = guardWorkService.addOrUpdateWorkHis(userId, roomId, workId, guardId, isPeriod, validate,isContinue);
				if(gk != null) {
					newWorkId = gk.getId();
					
					// begin- 若房间内金守护数量＜=4人，则开通金守护自动上四圣兽坐席
					if(guardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
						// 金守护
						List<GuardWork> roomGuardWorkList = this.guardWorkService.findRoomGuardWorkList(roomId, guardType);
						int specialSeatNum = 4; //4个席位
						int goldGuardLength = 0 ;
						if(roomGuardWorkList!=null){
							goldGuardLength = roomGuardWorkList.size();
						}
						if(goldGuardLength < specialSeatNum ){ // 购买时,少于4个金守护,直接给
							List haveUseSortIndex = new ArrayList();// 已经使用的四圣位
							for(int checkIndex=0;checkIndex<roomGuardWorkList.size();checkIndex++){
								GuardWork checkGuardWork = roomGuardWorkList.get(checkIndex);
								if(checkGuardWork!=null && checkGuardWork.getSortIndex() != 0){
									haveUseSortIndex.add(checkGuardWork.getSortIndex()) ; // 标识该四圣位序号已经被占用
								}
							}
							log.info(String.format("###有人在房间%s开通金守护,目前已被占用的四圣兽席位:%s", roomId,JsonUtil.arrayToJsonString(haveUseSortIndex)));
							for(int i=1;i<=specialSeatNum;i++){
								if(!haveUseSortIndex.contains(i)){ // 不包含,说明是空缺的四圣位
									gk.setSortIndex(i);
									this.guardWorkService.update(gk);
									log.info(String.format("###用户%s在房间%s购买金守护,直接上四圣兽,位置:%s,目前已开金守护数量:%s", userId,roomId,i,goldGuardLength));
									break;
								}
							}
						}else{
							log.info(String.format("###用户%s在房间%s购买金守护,不能直接上四圣兽,目前已开金守护数量:%s", userId,roomId,goldGuardLength));
						}
					}
					// end- 若房间内金守护数量＜=4人，则开通金守护自动上四圣兽坐席
				}
				
				UserAnchor userAnchor = userAnchorService.getUserAnchorByRoomId(roomId);
				
				String anchorUserId = userAnchor.getUserId();
				
				//2 增加购买记录
				String buyRemark = "";
				if(isContinue) {
					buyRemark = REL_REMARK;
				}else {
					buyRemark = BUY_REMARK;
				}
				GuardPayHis gph = addPayHis(userId, roomId, newWorkId, guardId, validate, now,price,diamond,anchorUserId,buyRemark);
				
				//3 赠送座驾
				GuardWork gdk = guardWorkService.getGuardEndTimeByUser(userId,null, guardType);// 查询用户所有的守护类型最长时间，因为座驾不区分房间
				if(gdk == null) {
					log.info("####addcar2user error");
					throw new GuardBizException(ErrorCode.ERROR_12000);
				}
				Date gkEndTime = gdk.getEndtime();
				userCarPortService.addCar2User(userId, carId, guardType, priceType,isContinue, validate, gkEndTime);
				
				//4 扣金币
				// 账户明细
				UserAccountBook userAccountBook = new UserAccountBook();
				userAccountBook.setUserId(userId);
				userAccountBook.setChangeGolds(-price);
				userAccountBook.setSourceId(gph.getId()+"");
				userAccountBook.setSourceDesc("sourceId为守护消费记录t_guard_pay_his的id");
				userAccountBook.setContent("购买守护，扣金币");
				userAccountBook.setRecordTime(now);
				
				userAccountService.subtractGolds(userId, price,userAccountBook);
				
				//5 增加用户经验、等级
				userAccountService.addUserPoint(userId, userPoint);
				
				//6 增加主播钻石、经验、等级
				userAccountService.addAnchorPoint(anchorId, anchorPoint);
				userAccountService.addDiamond(anchorId, diamond);
				
				//7 赠送勋章
				decoratePackageService.addPackage(userId, roomId, decorateId, periodFlag, 1, validate, guardType);
				
				String orderId = "t_guard_pay_his"+gph.getId();
				int num = 1;
				Date nowDateTime = new Date();
				String remark = "购买守护,守护id:"+guardId;
				int giftSourceType = PayGiftOutEnum.SourceType.guard.getValue();
				PayGiftOut out = new PayGiftOut();
				out.setOrderId(orderId);
				out.setUserId(userId);
				out.setToUserId(anchorUserId);
				out.setGiftId(guardId);
				out.setNumber(num);
				out.setResultTime(nowDateTime);
				out.setPrice(price);
				out.setDiamond(diamond);
				out.setRemark(remark);
				out.setSourceType(giftSourceType);
				payGiftOutService.insert(out);
				
				log.info("### payForGuard,userId="+userId + ",roomId="+roomId + ",guardType="+guardType
						+ ",priceType=" +priceType + ",workId=" + workId + ",guardId=" + guardId
						+ ",isPeriod=" + isPeriod + ",validate=" + validate + ",isContinue=" + isContinue);
				//8 插入打卡相关信息业务表
				// 打卡业务增守护暂时不要了
				// 20160-12-01
//				int payHisId = 0;
//				if(gph != null) {
//					payHisId = gph.getId();
//				}
//				try {
//					log.info("####payForGuard,购买守护,payHisId="+payHisId + ",newWorkId="+newWorkId+",priceType="+priceType+",validate="+validate);
//					guardPunchGiveService.save(payHisId,newWorkId,priceType,userId,roomId,validate);
//				} catch (Exception e) {
//					log.error(e.getMessage(),e);
//				}
//				
				//暂时不送宝箱了
				
				//清除缓存
				try {
					this.clearCache(userId,roomId);
				} catch (Exception e) {
					log.error("#### payForGuard,守护:清除守护缓存失败");
				}
				
				// 发送一条特殊消息，客户端用来刷新守护信息
				try {
					JSONObject imData = new JSONObject();
					JSONObject content = new JSONObject();
					// 与IM之间约定的全站通知时所用特殊房间号 
//					String systemUserIdOfIM  = Constants.SYSTEM_USERID_OF_IM;
					String allRoom = BaseConstants.WHOLE_SITE_NOTICE_ROOMID;
					UserInfoVo  user =  this.userCacheInfoService.getInfoFromCache(userId, roomId);
					UserInfoVo anchor = this.userCacheInfoService.getInfoFromCache(anchorId, roomId);
					//String msg = "开通" + anchor.getNickname() + "的" + guardName + "守护";
					//String msg = "恭喜" + user.getNickname() + "开通"+guardName+"守护，立下誓言守护" + anchor.getNickname();
					//String msg = "已开通"+guardName+"守护，立下誓言守护" + anchor.getNickname()+"，快去直播间内守护宫殿上留下你的守护宣言。";
					String msg = "已开通"+guardName+"守护，立下誓言守护" + anchor.getNickname();
					
					content.put("msg", msg);
					content.put("guardType", guardType);
					content.put("roomId", roomId);
					
					imData.put("msgtype", 2);
					imData.put("targetid", allRoom);
					imData.put("type", ImTypeEnum.IM_11001_specialForSH.getValue());
					imData.put("content", content);
//					imData.put("user", user);
					//imData.put("user", JsonUtil.beanToJsonString(fromUser));
					int funID = 11001;//21007.系统通知(没有字数限制)
					int seqID = 1;//一般默认为1
					try {
						IMutils.sendMsg2IM(funID, seqID, imData,userId);
					} catch(Exception e) {
						log.error(e.getMessage(),e);
					}
				} catch (Exception e) {
					log.error("####payForGuard- 守护:清除守护缓存失败");
				}				
				
				// 加catch，避免这里错误回滚
				// 2016-12-01
				try {
					//用户等级提升IM消息
					userAccount = userAccountService.getObjectByUserId(userId);
					log.info("###购买守护后userAccount:"+userAccount);
					if (userAccount!=null) {
						//购买守护后等级
						String endLevel = userAccount.getUserLevel();
						log.info(String.format("###购买守护前等级%s,购买后等级%s",strLevel,endLevel));
						//用户升级、升级消息推送
						userAccountService.userUpgradeSendMsg(userId, strLevel, endLevel, roomId);
					}
					//主播等级提升IM消息
					userAccount = userAccountService.getObjectByUserId(anchorId);
					log.info("###主播守护后userAccount:"+userAccount);
					if (userAccount!=null) {
						//主播守护后等级
						String anchorEndLevel = userAccount.getAnchorLevel();
						log.info(String.format("###主播守护前等级%s,购买后等级%s",anchorStrLevel,anchorEndLevel));
						//主播升级、升级消息推送
						userAccountService.anchorUpgradeSendMsg(anchorId, anchorStrLevel, anchorEndLevel, roomId);
					}
				} catch(Exception e) {
					log.error(e.getMessage(),e);
				}
			} else { // 不满足，来个友情提示
				Exception e = new GuardBizException(ErrorCode.ERROR_12000);
				throw e;
			}
			
		//}
		log.info(String.format("###end-payForGuard,userId:%s,anchorId:%s,roomId:%s,workId:%s,guardType:%s,priceType:%s", userId,anchorId,roomId,workId,guardType,priceType));
	}

	
	private GuardPayHis addPayHis(String userId, String roomId, int workId, int guardId,
			int validate, Date time,int price,int diamond,String toUserId,String remark) throws Exception {
		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(roomId) || validate < 0) {
			Exception e = new GuardBizException(ErrorCode.ERROR_12001);
			throw e;
		}
		Date now = new Date();
		GuardPayHis his = new GuardPayHis();
		his.setWorkId(workId);
		his.setGuardid(guardId);
		his.setUserid(userId);
		his.setRoomid(roomId);
		his.setBegintime(now);
		his.setValidate(validate);
		his.setPrice(price);
		his.setDiamond(diamond);
		his.setToUserId(toUserId);
		his.setRemark(remark);
		guardPayHisMapper.insert(his);
		return his;
	}
	
	/**
	 * 清除非守护或游客、所有守护的缓存
	 * @throws Exception
	 */
	public void clearCache(String userId, String roomId) throws Exception {
		//1, 清除游客和非守护缓存
		String youkeKey = MCPrefix.USER_WORK_INFO_DB_CACHE_FOR_ROOM +roomId;
		MemcachedUtil.delete(youkeKey);
		//2，清除房间内所有守护缓存
		List<GuardWork> listWork = guardWorkMapper.getGuardWorkList(roomId);
		if(listWork != null && listWork.size() >0) {
			for(GuardWork vo : listWork) {
				String userId2 = vo.getUserid();
				String userKey = MCPrefix.GUARD_WORK_INFO_DB_CACHE_FOR_ROOM + roomId + userId2;
				MemcachedUtil.delete(userKey);
			}
		}
		//3  清楚用户所有房间的缓存
		String userAllRoomKey = MCPrefix.USER_GUARD_INFO_IN_ALL_ROOM_CACHE + userId;
		MemcachedUtil.delete(userAllRoomKey);
		
		//4 清除用户当前房间的缓存
		String selfKey = MCPrefix.USER_GUARD_INFO_IN_ROOM_CACHE + roomId + userId;
		MemcachedUtil.delete(selfKey);
		
		String userKey = MCPrefix.GUARD_WORK_INFO_DB_CACHE_FOR_ROOM + roomId + userId;
		MemcachedUtil.delete(userKey);
		
		// 骑士军团守护榜单
		String cacheKey = MCPrefix.GUARD_RANK_CACHE;
		MemcachedUtil.delete(cacheKey);
		log.info("### mydebug,清除缓存，youkeKey=" + youkeKey + ",userAllRoomKey=" + userAllRoomKey + ",userKey=" +userKey + ",selfKey=" +selfKey);
	
		// 微章守护用户缓存
		String medalKey = MCPrefix.MEDAL_GUARD_USER+roomId;
		MemcachedUtil.delete(medalKey);
	}

}
