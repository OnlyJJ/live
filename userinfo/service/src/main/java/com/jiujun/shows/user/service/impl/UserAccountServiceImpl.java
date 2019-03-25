//package com.jiujun.shows.user.service.impl;
//
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.jiujun.shows.activities.pay.service.IActivitiesPayConfService;
//import com.jiujun.shows.activity.festival.domain.ActivityFestivalConf;
//import com.jiujun.shows.activity.festival.service.FestivalActivityService;
//import com.jiujun.shows.activity.festival.service.IActivityFestivalConfService;
//import com.jiujun.shows.activity.luckygift.service.IActivitiesLuckyGiftBizService;
//import com.jiujun.shows.activity.luckygift.service.impl.ActivitiesLuckyGiftBizServiceImpl;
//import com.jiujun.shows.activity.temporary.service.ITemporaryService;
//import com.jiujun.shows.car.dao.ICarGiveHisDao;
//import com.jiujun.shows.car.dao.ISysCarDao;
//import com.jiujun.shows.car.domain.SysCarDo;
//import com.jiujun.shows.car.service.IUserCarPortService;
//import com.jiujun.shows.charge.dao.IPayChargeGiftConfDao;
//import com.jiujun.shows.charge.dao.IPayGiftInDao;
//import com.jiujun.shows.charge.dao.IPayGiftOutDao;
//import com.jiujun.shows.charge.domain.PayChargeGiftConf;
//import com.jiujun.shows.charge.domain.PayGiftIn;
//import com.jiujun.shows.charge.domain.PayGiftOut;
//import com.jiujun.shows.common.component.im.ImSendComponent;
//import com.jiujun.shows.common.constant.Constants;
//import com.jiujun.shows.common.constant.ErrorCode;
//import com.jiujun.shows.common.constant.MCPrefix;
//import com.jiujun.shows.common.constant.MCTimeoutConstants;
//import com.jiujun.shows.common.enums.business.GiftTypeBusinessEnum;
//import com.jiujun.shows.common.enums.business.IMBusinessEnum.ImTypeEnum;
//import com.jiujun.shows.common.enums.business.IMBusinessEnum.MsgTypeEnum;
//import com.jiujun.shows.common.enums.business.IMBusinessEnum.SeqID;
//import com.jiujun.shows.common.enums.table.ActivitiesConfEnum;
//import com.jiujun.shows.common.enums.table.DecorateTableEnum;
//import com.jiujun.shows.common.enums.table.GiftTableEnum;
//import com.jiujun.shows.common.enums.table.GiftTableEnum.GiftIdEnum;
//import com.jiujun.shows.common.enums.table.GuardTableEnum;
//import com.jiujun.shows.common.enums.table.PayGiftOutEnum;
//import com.jiujun.shows.common.enums.table.SysCarTableEnum;
//import com.jiujun.shows.common.enums.table.SysConfTableEnum;
//import com.jiujun.shows.common.enums.table.TableCommonEnum;
//import com.jiujun.shows.common.enums.table.TaskTableEnum;
//import com.jiujun.shows.common.enums.table.ToolHisTableEnum;
//import com.jiujun.shows.common.enums.table.ToolTableEnum;
//import com.jiujun.shows.common.exception.SystemDefinitionException;
//import com.jiujun.shows.common.service.impl.CommonServiceImpl;
//import com.jiujun.shows.common.utils.DateUntil;
//import com.jiujun.shows.common.utils.IMutils;
//import com.jiujun.shows.common.utils.JsonUtil;
//import com.jiujun.shows.common.utils.LogUtil;
//import com.jiujun.shows.common.utils.MemcachedUtil;
//import com.jiujun.shows.common.utils.StrUtil;
//import com.jiujun.shows.common.utils.UserInfoUtil;
//import com.jiujun.shows.enums.table.CarGiveHisEnum;
//import com.jiujun.shows.enums.table.ImMsgFailHisEnum;
//import com.jiujun.shows.enums.table.PayChargeGiftConfEnum;
//import com.jiujun.shows.gift.domain.Gift;
//import com.jiujun.shows.gift.service.IGiftService;
//import com.jiujun.shows.guard.service.IGuardWorkService;
//import com.jiujun.shows.im.domain.ImMsgFailHis;
//import com.jiujun.shows.im.service.IImMsgFailHisService;
//import com.jiujun.shows.level.his.domain.LevelHistAnchor;
//import com.jiujun.shows.level.his.domain.LevelHistUser;
//import com.jiujun.shows.level.his.service.ILevelHistAnchorService;
//import com.jiujun.shows.level.his.service.ILevelHistUserService;
//import com.jiujun.shows.pay.dao.IPayChargeOrderDao;
//import com.jiujun.shows.peach.dao.IPeachAnchorDao;
//import com.jiujun.shows.peach.domain.PeachAnchor;
//import com.jiujun.shows.peach.service.IPeachAnchorService;
//import com.jiujun.shows.robot.domain.RobotUserDo;
//import com.jiujun.shows.robot.service.IRobotAccountService;
//import com.jiujun.shows.robot.service.RobotService;
//import com.jiujun.shows.room.domain.Decorate;
//import com.jiujun.shows.room.domain.DecorateHis;
//import com.jiujun.shows.room.domain.DecoratePackage;
//import com.jiujun.shows.room.service.IDecorateHisService;
//import com.jiujun.shows.room.service.IDecorateImNotifyHisService;
//import com.jiujun.shows.room.service.IDecoratePackageService;
//import com.jiujun.shows.room.service.IDecorateService;
//import com.jiujun.shows.room.service.RoomCacheInfoService;
//import com.jiujun.shows.room.service.RoomService;
//import com.jiujun.shows.sys.domain.SysConf;
//import com.jiujun.shows.sys.service.ISysConfService;
//import com.jiujun.shows.sys.service.impl.SysConfServiceImpl;
//import com.jiujun.shows.tool.service.IUserToolPackageService;
//import com.jiujun.shows.user.dao.ILevelDao;
//import com.jiujun.shows.user.dao.IUserAccountDao;
//import com.jiujun.shows.user.dao.IUserPackageDao;
//import com.jiujun.shows.user.domain.Level;
//import com.jiujun.shows.user.domain.UserAccount;
//import com.jiujun.shows.user.domain.UserAccountBook;
//import com.jiujun.shows.user.domain.UserAnchor;
//import com.jiujun.shows.user.service.ISysTaskPrizesService;
//import com.jiujun.shows.user.service.IUserAccountBookOutService;
//import com.jiujun.shows.user.service.IUserAccountBookService;
//import com.jiujun.shows.user.service.IUserAccountBookinService;
//import com.jiujun.shows.user.service.IUserAccountService;
//import com.jiujun.shows.user.service.IUserAnchorService;
//import com.jiujun.shows.user.service.IUserCacheInfoService;
//import com.jiujun.shows.user.service.IUserPackageInHisService;
//import com.jiujun.shows.user.service.IUserPackageOutHisService;
//import com.jiujun.shows.user.service.IUserPackageService;
//import com.jiujun.shows.vo.UserCacheInfo;
//import com.jiujun.shows.vo.UserInfoVo;
//@Service("userAccountService")
//public class UserAccountServiceImpl extends CommonServiceImpl<IUserAccountDao, UserAccount> implements
//		IUserAccountService {
//
//	@Resource(name="userAccountDao")
//	public void setDao(IUserAccountDao dao){
//		this.dao = dao;
//	}
//	
//	@Resource
//	private IPayChargeOrderDao payChargeOrderDao;
//
//	@Resource
//	private ILevelDao levelDao;
//	
//	public UserAccount getObjectByUserId(String userId){
//		return this.dao.getObjectByUserId(userId);
//	}
//	
//	@Resource
//	private IPeachAnchorService peachAnchorService;
//	
//	@Resource
//	private IPayGiftOutDao payGiftOutDao;
//	
//	@Resource
//	private IPayGiftInDao payGiftInDao;
//	
//	@Resource
//	private IPayChargeGiftConfDao payChargeGiftConfDao;
//	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
//	@Resource
//	private ICarGiveHisDao carGiveHisDao;
//	
//	@Resource
//	private IUserAccountBookOutService userAccountBookOutService;
//	
//	@Resource
//	private IUserAccountBookinService userAccountBookinService;
//	
//	@Resource
//	private IUserPackageDao userPackageDao;
//	
//	@Resource
//	IUserPackageOutHisService userPackageOutHisService ;
//	
//	@Resource
//	private ISysTaskPrizesService sysTaskPrizesService;
//	
//	@Resource
//	private ISysCarDao sysCarDao;
//	
//	@Resource
//	private IUserToolPackageService userToolPackageService;
//	
//	@Resource
//	private IUserCacheInfoService userCacheInfoService;
//	
//	@Resource
//	private IUserAnchorService userAnchorService;
//	
//	@Resource
//	public IImMsgFailHisService imMsgFailHisService;
//	
//	@Resource
//	public IPeachAnchorDao peachAnchorDao;
//	
//	@Resource
//	private IDecoratePackageService decoratePackageService;
//	
//	@Resource
//	private ISysConfService sysConfService;
//	
//	@Resource
//	private IDecorateHisService decorateHisService;
//	
//	@Resource
//	private IDecorateImNotifyHisService decorateImNotifyHisService;
//	
//	@Resource
//	private IActivitiesPayConfService activitiesPayConfService;
//	
//	@Resource
//	private IUserPackageService userPackageService;
//	
//	@Resource
//	private IGiftService giftService;
//	
//	@Resource
//	private IGuardWorkService guardWorkService;
//	
//	@Resource
//	private RoomService roomService;
//	
//	@Resource
//	private IUserPackageInHisService userPackageInHisService;
//	
//	@Resource
//	private IUserAccountBookService userAccountBookService;
//	
//	@Resource
//	private IActivitiesLuckyGiftBizService activitiesLuckyGiftBizService;
//	
//	@Resource
//	private FestivalActivityService festivalActivityService;
//	
//	@Resource
//	private IDecorateService decorateService;
//	
//	@Resource
//	private ITemporaryService temporaryService;
//	
//	@Resource
//	private IActivityFestivalConfService activityFestivalConfService;
//	
//	@Resource
//	private RoomCacheInfoService roomCacheInfoService;
//	
//	@Resource
//	private ImSendComponent imSendComponent;
//	
//	@Resource
//	private ILevelHistUserService levelHistUserService;
//	
//	@Resource
//	private ILevelHistAnchorService levelHistAnchorService;
//	
//	@Resource
//	private RobotService robotService;
//	
//	@Resource
//	private IRobotAccountService robotAccountService;
//	
//	/** 用户赠送主播礼物 */
//	private static final String SOURCE_USER = "用户送礼";
//	/** 主播接受礼物 */
//	private static final String SOURCE_ANCHOR = "主播收礼";
//	/** 用户送礼记录 */
//	private static final String CONTENT_USER = "用户送礼记录，sourceId关联t_pay_gift_in表的orderId";
//	/** 主播接受礼物，增加钻石记录 */
//	private static final String CONTENT_ANCHOR = "主播增加钻石记录，sourceId关联t_pay_gift_out表的orderId";
//	
//	// 每次最大赠送礼物价值十万元RMB(单位:分)
//	private static final int max_give_totalPrice_oneTime = 10000000;
//	
//	// 每次最小赠送礼物价值0.1元RMB(单位:分)
//	private static final int min_give_totalPrice_oneTime = 10; 
//	
//	private static final String USER_SOURCE_KEY = "2016AoyunActivity";
//	
//	/** 守护特权礼物大于52RMB，则发大喇叭 */
//	private static final int GUARD_GIFT_NUM = 52000;
//	
//	private static final String USER_ACCOUNT_BOOK_SOURCEDEC = "sourceId为本次送礼的订单号";
//	/** 上礼物跑道的金币条件 */
//	private static final int GIFT_RUNWAY_GOLD_CONTION = 52000; 
//	
//	private static final String GIFT_SENDER_CONMENT = "用户间送礼，扣除赠送用户金币";
//	private static final String GIFT_RECIVEDER_CONMENT = "用户间送礼，增加收礼用户金币";
//	
//	@Override
//	public void addGolds(String userId, int golds, UserAccountBook userAccountBook) throws Exception{
//		if(golds<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3035);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		 synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			long preRemainGolds = userAccount.getGold(); // 更改账户前的金币
//			userAccount.setGold(userAccount.getGold()+golds);
//			//this.dao.update(userAccount);
//			this.dao.updateByUserId(userAccount);
//			
//			// 插入账户明细
//			if(userAccountBook != null) {
//				LogUtil.log.info("#### userAccountBook-addGolds,加金币前账户余额："+ preRemainGolds + ",加完后金币："+userAccount.getGold());
//				userAccountBook.setPreRemainGolds(preRemainGolds);
//				userAccountBook.setSufRemainGolds(userAccount.getGold());
//				this.userAccountBookService.insert(userAccountBook);
//			}
//		}
//		 
//		
//	}
//
//
//	@Override
//	public void subtractGolds(String userId, int golds,UserAccountBook userAccountBook) throws Exception{
//		
//		if(golds<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3035);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		
//		synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			if(userAccount.getGold()<golds){//若金币不足,则抛出异常
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3006);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}else{
//				long preRemainGolds = userAccount.getGold(); // 更改账户前的金币
//				userAccount.setGold(userAccount.getGold()-golds);
//				//this.dao.update(userAccount);
//				this.dao.updateByUserId(userAccount);
//
//				// 插入账户明细
//				if(userAccountBook != null) {
//					LogUtil.log.info("#### userAccountBook-addGolds,扣金币前账户余额："+ preRemainGolds + ",扣完后金币："+userAccount.getGold());
//					userAccountBook.setPreRemainGolds(preRemainGolds);
//					userAccountBook.setSufRemainGolds(userAccount.getGold());
//					this.userAccountBookService.insert(userAccountBook);
//				}
//			}
//		}
//		
//	}
//	
//	@Override
//	public void addDiamond(String userId, int diamond) throws Exception{
//		if(diamond<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3035);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		 synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			userAccount.setDiamond(userAccount.getDiamond()+diamond);
//			//this.dao.update(userAccount);
//			this.dao.updateByUserId(userAccount);
//		}
//		
//	}
//	
//	
//	@Override
//	public void subtractDiamond(String userId, int diamond) throws Exception {
//		if(diamond<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			if(userAccount.getDiamond() < diamond){//若钻石不足,则抛出异常
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3056);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}else{
//				userAccount.setDiamond(userAccount.getDiamond()-diamond);
//				//this.dao.update(userAccount);
//				this.dao.updateByUserId(userAccount);
//			}
//			
//		}
//		
//	}
//
//
//	@Override
//	public void addAnchorPoint(String userId, int anchorPoint) throws Exception {
//		if(anchorPoint<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		 synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			long totalAnchorPoint = userAccount.getAnchorPoint()+anchorPoint ;
//			Level level = levelDao.getObjectByAnchorPoint(Long.parseLong(totalAnchorPoint+""));
//			String newAnchorLevel = level.getLevel();
//			userAccount.setAnchorLevel(newAnchorLevel);
//			userAccount.setAnchorPoint(totalAnchorPoint);
//			this.dao.update(userAccount);
//		}
//		
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public com.jiujun.shows.common.vo.Gift doSendGiftBusiness(String senderUserId,String anchorUserId,int sendGiftId, int sendGiftNum,String anchorRoomId,boolean isOnlySubpackage,boolean isOnGiftRunwayChoice,double extraRate,boolean isExtraAnchor) throws Exception {
//		
//		LogUtil.log.info(String.format("###sendGift,senderUserId:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s,sendGiftId:%s,sendGiftNum:%s",senderUserId,anchorRoomId,anchorUserId,sendGiftId,sendGiftNum));
//		
//		//校验请求参数
//		if(StringUtils.isEmpty(senderUserId) ||StringUtils.isEmpty(anchorUserId)
//				||sendGiftId<=0||sendGiftNum<=0
//				||StringUtils.isEmpty(anchorRoomId)
//				){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		
//		
//		
//		// 从缓存中查询gift
//		Gift gift = getGiftInfoFromCache(sendGiftId);
//		int sumGolds = gift.getPrice() * sendGiftNum;
//		
//		// 不可购买的礼物
//		if(gift.getBuyable() == GiftTableEnum.Buyable.NO.getValue()){
//			// 用户包裹数量
//			int packageRemainSum = this.userPackageDao.countUserPackage(senderUserId, sendGiftId);
//			if(gift != null) {
//				if(  sendGiftNum > packageRemainSum) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3185);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//			}
//		}
//		
//		//赠送用户的送礼前的账号信息
//		UserAccount senderUserAcc = this.getObjectByUserId(senderUserId);	
//		
//		LogUtil.log.info("###before sendGiftBiz,senderUserAcc:"+JsonUtil.beanToJsonString(senderUserAcc));
//		String senderOldUserLevel = senderUserAcc.getUserLevel();	
//		//主播原来的账号信息
//		UserAccount anchorUserAcc = this.getObjectByUserId(anchorUserId);
//		//主播的原来主播等级
//		String anchorOldAnchorLevel = anchorUserAcc.getAnchorLevel();
//		
//		// 执行加减账户相关操作,1:扣用户金币、加用户经验; 2:给主播加砖石、加主播经验; 3:加金币支出流水记录
//		String sendGiftBusinessId = subUserAssetBySendGift(senderUserId, anchorUserId, sendGiftId, sendGiftNum, anchorRoomId,senderUserAcc.getGold(),isOnlySubpackage,extraRate,isExtraAnchor);
//		/*
//		// begin-201702-情人节活动另外加经验
//		if(this.temporaryService.checkIf2017ActivityQingrenjieGift(sendGiftId)){
//			int qingrenjie201702ActivityNo = ActivitiesConfEnum.ActivityNo.Qingrenjie201702.getValue();
//			boolean flagInActiveTime = this.activityFestivalConfService.checkIfAtActivityTime(qingrenjie201702ActivityNo);
//			if(flagInActiveTime){
//				int totalAddUserPoint = 0;
//				// 一次性送999个或9999个,得1.5倍经验
//				if(sendGiftNum == 999 || sendGiftNum == 99999){
//					Gift sendGift = this.giftService.getGiftInfoFromCache(sendGiftId);
//					// 送礼逻辑中已加了1倍经验,所以这里只要加0.5倍 
//					int  eachGiftUserPoint = sendGift.getUserPoint()/2;
//					totalAddUserPoint = eachGiftUserPoint*sendGiftNum; 
//					this.addUserPoint(senderUserId, totalAddUserPoint);
//					LogUtil.log.info(String.format("###情人节礼物,加额外0.5倍经验,userId:%s,sendGiftId:%s,sendGiftNum:%s,totalAddUserPoint:%s", senderUserId,sendGiftId,sendGiftNum,totalAddUserPoint));
//				}
//			}
//		}
//		// end-01702-情人节活动另外加经验
//		*/
//		
//		// 守护礼物，校验权限
//		int giftType = gift.getGiftType();
//		int giftEachSpendGolds = gift.getPrice();  //每个礼物的金币数
//		//幸运礼物可抽奖,但不加蜜桃经验
//		int giftTotalGolds = giftEachSpendGolds*sendGiftNum; 
//		//幸运礼物待发的im消息
//		List<JSONObject> listImMsgOfLuckGift2BeSend = null;
//		LogUtil.log.info(String.format("### doSendGiftBusiness-已走完扣金币流程(接着送蜜桃成长经验或者奖金池抽奖)-giftType:%s",giftType));
//		// 幸运礼物
//		if(giftType == GiftTableEnum.GiftType.LuckyGift.getType()){
//			// giftTotalGolds(送礼总金币) 转成BigDecimal
//			BigDecimal giftTotalGoldsBigDecimal = new BigDecimal(giftTotalGolds);
//			// 放入奖金池的比例
//			BigDecimal rateInPool = ActivitiesLuckyGiftBizServiceImpl.luckygiftInPoolRate;
//			// 放入奖金池的金币=送礼金币* 放入奖金池的比例
//			int goldInpool = giftTotalGoldsBigDecimal.multiply(rateInPool).intValue();
//			LogUtil.log.info(String.format("### doSendGiftBusiness-gold2pool幸运礼物-部分金币放入奖金池,giftId:%s,sendGiftNum:%s,giftTotalGolds:%s,rateInPool:%s,goldInpool:%s",sendGiftId,sendGiftNum,giftTotalGolds,rateInPool,goldInpool));
//			//加奖金池
//			activitiesLuckyGiftBizService.addGold2Pool(goldInpool);
//			try {
//				LogUtil.log.info(String.format("### doSendGiftBusiness-begin幸运礼物-抽奖,senderUserId:%s,anchorUserId:%s,giftId:%s,sendGiftNum:%s,giftTotalGolds:%s",senderUserId,anchorUserId,sendGiftId,sendGiftNum,giftTotalGolds));
//				// 送幸运礼物-抽奖,返回待发的im消息()
//				listImMsgOfLuckGift2BeSend = activitiesLuckyGiftBizService.lotteryLuckygiftPrize(senderUserId, anchorUserId, sendGiftId, sendGiftNum, sendGiftBusinessId, giftTotalGolds) ;
//				LogUtil.log.info(String.format("### doSendGiftBusiness-end-1幸运礼物-抽奖,senderUserId:%s,anchorUserId:%s,giftId:%s,sendGiftNum:%s,giftTotalGolds:%s",senderUserId,anchorUserId,sendGiftId,sendGiftNum,giftTotalGolds));
//			} catch (Exception e) {
//				LogUtil.log.error(e.getMessage(), e);
//				//幸运礼物-抽奖发生异常时，不往外抛异常(就当是不中奖,以保证送礼是正常的)
//				//throw e;
//			}
//			
//			LogUtil.log.info(String.format("### doSendGiftBusiness-end-2幸运礼物-抽奖,senderUserId:%s,anchorUserId:%s,giftId:%s,sendGiftNum:%s,giftTotalGolds:%s,listImMsgOfLuckGift2BeSend:%s",senderUserId,anchorUserId,sendGiftId,sendGiftNum,giftTotalGolds,JsonUtil.arrayToJsonString(listImMsgOfLuckGift2BeSend)));
//		}else{
//			LogUtil.log.info(String.format("### doSendGiftBusiness-送礼加蜜桃经验-begin,anchorUserId:%s,giftId:%s,giftTotalGolds:%s",anchorUserId,sendGiftId,giftTotalGolds));
//			//加蜜桃成长经验
//			peachAnchorService.peachAnchorGrowUp(anchorUserId, sendGiftId, sendGiftNum);
//			LogUtil.log.info(String.format("### doSendGiftBusiness-送礼加蜜桃经验-end,anchorUserId:%s,giftId:%s,giftTotalGolds:%s",anchorUserId,sendGiftId,giftTotalGolds));
//		}
//		
//		String senderNewUserLevel = levelDao.getUserLevel(senderUserId);
//		// ------------ begin 用户、主播升级相关------------------------
//		{
//			try {
//				// ---------begin 用户、主播升级检测, 注意: 必须放到本业务的后面,不然发送升级通知im消息会有bug----------------------------------
//				//更新赠送用户的用户等级 
//				LogUtil.log.info(String.format("###after sendGift,senderUserId:%s,senderNewUserLevel:%s,senderOldUserLevel:%s", senderUserId,senderNewUserLevel,senderOldUserLevel));
//				//升级则从缓存中删除用户信息
//				if(!senderOldUserLevel.equals(senderNewUserLevel)){
//					this.updateUserLevel(senderUserId, senderNewUserLevel);
//					String senderUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + senderUserId;
//					LogUtil.log.info("###clearUserCacheInfo"+senderUserCacheKey);
//					MemcachedUtil.delete(senderUserCacheKey);
//					// 及时刷新缓存(预防在其他的事务中,由于事务隔离级别,不能及时拿到最新的db数据,如:用户等级)
//					this.userCacheInfoService.getInfoFromCache(senderUserId, anchorRoomId);
//				}		
//				
//				//更新主播的主播等级
//				String anchorNewAnchorLevel = levelDao.getAnchorLevel(anchorUserId);
//				LogUtil.log.info(String.format("###anchorUserId:%s,roomId:%s,anchorOldAnchorLevel:%s,anchorNewAnchorLevel:%s,", anchorUserId,anchorRoomId,anchorOldAnchorLevel,anchorNewAnchorLevel));
//				//升级则从缓存中删除用户信息
//				if(!anchorOldAnchorLevel.equals(anchorNewAnchorLevel)){
//					this.updateAnchorLevel(anchorUserId, anchorNewAnchorLevel);
//					String anchorUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + anchorUserId;
//					MemcachedUtil.delete(anchorUserCacheKey);
//					// 及时刷新缓存(预防在其他的事务中,由于事务隔离级别,不能及时拿到最新的db数据,如:主播等级)
//					this.userCacheInfoService.getInfoFromCache(anchorUserId, anchorRoomId);
//				}
//				// ---------end 用户、主播升级检测----------------------------------		
//
//				//用户等级提升消息推送,并记录升级历史
//				userUpgradeSendMsg(senderUserId,senderOldUserLevel, senderNewUserLevel, anchorRoomId);
//				//主播等级提升消息推送,并记录升级历史
//				anchorUpgradeSendMsg(anchorUserId,anchorOldAnchorLevel,anchorNewAnchorLevel,anchorRoomId);
//			} catch (Exception e) {
//				LogUtil.log.error(String.format("###送礼完成后,升级相关的方法发生异常,userId:%s,anchorUserId:%s,giftId:%s,num:%s",senderUserId,anchorUserId,sendGiftId,sendGiftNum));
//				LogUtil.log.error(e.getMessage(),e);
//			}
//		}
//		// ------------ end 用户、主播升级相关------------------------
//		
//		//节日活动奖励消息
////		List<JSONObject> imMsgJsonListOfPrizeActivityFestival = null;
////		// 20161111万人迷活动,表白巧克力礼物
////		if(sendGiftId == GiftTableEnum.GiftIdEnum.Biaobaiqiaokeli.getValue()){
////			 imMsgJsonListOfPrizeActivityFestival= festivalActivityService.prizeActivityFestivalWithSendGift(senderUserId, anchorUserId, sendGiftId, sendGiftNum, imNotifyRoomId);
////		}
//		
//		// === begin 上礼物跑道的判断======================================
//		Boolean flagOnGiftRunwayCondition = Boolean.FALSE; 
//		JSONObject onGiftRunwayImData = null;  //上礼物跑道的im内容
//				{
//					/*//每个礼物花费的金币
//					int eachGiftSpendGold = gift.getPrice();
//					// 送礼总的金币价值
//					int sendGiftTotalGoldValue = eachGiftSpendGold * sendGiftNum;*/
//					// 上礼物跑道需一次赠送n个金币以上
////					int onGiftRunwayNeedGold = 131400 ;
//					if(isOnGiftRunwayChoice){ //如果用户选择了上礼物跑道
//						/*if(giftTotalGolds >= onGiftRunwayNeedGold &&
//								(GiftTableEnum.GiftType.ClassicGift.getType() ==giftType  // 经典礼物类型
//								|| GiftTableEnum.GiftType.GD.getType() ==giftType //守护礼物类型
//								||GiftTableEnum.GiftType.LuckyGift.getType() ==giftType) // 幸运礼物类型
//						){ //且满足上礼物跑道的条件(1.送礼金币价值n个以上;2.满足配置的礼物类型)
//							// 达到上跑道的条件,滚屏全站通知
//							flagOnGiftRunway = Boolean.TRUE;
//						}*/
//						boolean isSpecialGift = false;
//						if(gift.getSpecialFlag() == 1) {
//							isSpecialGift = true;
//						}
//						int onGiftRunwayNeedGold = 52000;
////						if(gift.getGiftType() == GiftTableEnum.GiftType.GD.getType()) {
////							onGiftRunwayNeedGold = 52000;
////						}
//						if(isSpecialGift || giftTotalGolds >= onGiftRunwayNeedGold){ //满足上礼物跑道的条件(1.送礼金币价值n个以上)
//							// 达到上跑道的条件,滚屏全站通知
//							flagOnGiftRunwayCondition = Boolean.TRUE;
//						}
//						
//					}
//					
//					if(flagOnGiftRunwayCondition){ //达到上礼物跑道的条件
//						StringBuffer msg = new StringBuffer();
//						String headMsg = null;
//						String msgColor = "#000000";// 非守护礼物默认黑色()
//						UserInfoVo vo = userCacheInfoService.getInfoFromCache(senderUserId, anchorRoomId);
//						UserCacheInfo anchorUserInfo = userCacheInfoService.getOrUpdateUserInfoFromCache(anchorUserId);
//						if(anchorUserInfo != null && vo != null) {
//							if(vo.getNickname() != null) {
//								msg.append(vo.getNickname()).append("在");
//							} 
//							if(anchorUserInfo.getNickname() != null) {
//								msg.append(anchorUserInfo.getNickname()).append("房间");
//							}
//							msg.append("送出");
//							headMsg = msg.toString();//设置头信息
//							msg.append(gift.getName()).append("x").append(sendGiftNum);
//						} else {
//							Exception e = new SystemDefinitionException(ErrorCode.ERROR_3049);
//							throw e;
//						}
//						if(GiftTableEnum.GiftType.GD.getType() ==giftType){ //守护礼物要根据守护类型设置颜色
//							List<Map> gkList = guardWorkService.getUserGuardInfoByRoomCache(senderUserId, anchorRoomId);
//							if(gkList == null || gkList.size() <=0) {
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_3113);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}
//							int senderUserGuardType = Integer.parseInt(gkList.get(0).get("guardType").toString());
//							if(senderUserGuardType == GuardTableEnum.GuardCarType.baiyin.getValue()) {
//								msgColor ="#18d4f0";
//							} else if(senderUserGuardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
//								msgColor ="#a43efb";
//							} else {
//								msgColor = "#383838";
//							}
//						}else{
//							msgColor = "#000000";// 非守护礼物默认黑色()
//						}
//						
//						onGiftRunwayImData = new JSONObject();
//						JSONObject shouhuContent = new JSONObject();
//						// 与IM之间约定的全站通知时所用特殊房间号 
//						String wholeSiteNoticeRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
//						
//						shouhuContent.put("msg", msg.toString());
//						shouhuContent.put("msgColor", msgColor);
//						shouhuContent.put("headMsg", headMsg);
//						shouhuContent.put("giftId", sendGiftId);
//						shouhuContent.put("giftNum", sendGiftNum);
//						shouhuContent.put("roomId", anchorRoomId);
//						shouhuContent.put("giftImg", Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage());
//						shouhuContent.put("golds", giftTotalGolds);
//												
//						onGiftRunwayImData.put("msgtype", 2);
//						onGiftRunwayImData.put("targetid", wholeSiteNoticeRoomId);
//						onGiftRunwayImData.put("type", ImTypeEnum.IM_11001_hanhuasmg.getValue());
//						onGiftRunwayImData.put("content", shouhuContent);
//					}else{
//						onGiftRunwayImData = null;
//					}
//					
//				}
//		// === end 上礼物跑道的判断======================================
//				
//		
//		// 礼物消息体
//		JSONObject giftMsgAllDataBodyJson = new JSONObject();
//		giftMsgAllDataBodyJson.put("funID", 11001);
//		giftMsgAllDataBodyJson.put("seqID", 1);
//		JSONObject giftImMsgJsonData = new JSONObject() ;
//		giftImMsgJsonData.put("msgtype", 2);
//		giftImMsgJsonData.put("targetid", anchorRoomId);
//		giftImMsgJsonData.put("type", ImTypeEnum.IM_11001_liwu.getValue());
//		JSONObject content = new JSONObject() ;
//		content.put("id", sendGiftId);
//		content.put("num", sendGiftNum);
//		content.put("type", 1);
//		content.put("sumGolds", sumGolds);
//		content.put("flashId", gift.getShowType());//flashId
//		content.put("giftImg", Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage());//礼物图片地址
//		content.put("giftType", gift.getGiftType());
//		giftImMsgJsonData.put("content", content);
//		giftMsgAllDataBodyJson.put("data", giftImMsgJsonData);
//		//发IM消息
//		String giftMsgAllDataBodyJsonStr = JsonUtil.beanToJsonString(giftMsgAllDataBodyJson) ;
//		
//		
//		// === begin 守护礼物额外的im消息====================================
//		/*
//		JSONObject shouhuImData = null;
//		//守护礼物
//		// special-Debug
//		// 如果不是守护礼物，但是仍然要放在守护礼物栏内，则需要在这里加条件，因为勋章特权与守护特权礼物没有区分，以后区分了再去掉这里
//		if(giftType == GiftTableEnum.GiftType.GD.getType() && sendGiftId != GiftTableEnum.GiftIdEnum.JinHulu.getValue()) {
//			// 送出的礼物是否满足发大喇叭条件
//			if(giftTotalGolds >= GUARD_GIFT_NUM ) {
//				StringBuffer msg = new StringBuffer();
//				String msgColor = "#383838";
//				UserInfoVo vo = userCacheInfoService.getInfoFromCache(senderUserId, imNotifyRoomId);
//				UserCacheInfo userInfo = userCacheInfoService.getOrUpdateUserInfoFromCache(anchorUserId);
//				if(userInfo != null && vo != null) {
//					if(vo.getNickname() != null) {
//						msg.append(vo.getNickname()).append("在");
//					} 
//					if(userInfo.getNickname() != null) {
//						msg.append(userInfo.getNickname()).append("房间");
//					}
//					msg.append("送出").append(gift.getName()).append("x").append(sendGiftNum);
//				} else {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3049);
//					throw e;
//				}
//				List<Map> gkList = guardWorkService.getUserGuardInfoByRoomCache(senderUserId, imNotifyRoomId);
//				if(gkList == null || gkList.size() <=0) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3113);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//				int senderUserGuardType = Integer.parseInt(gkList.get(0).get("guardType").toString());
//				if(senderUserGuardType == GuardTableEnum.GuardCarType.baiyin.getValue()) {
//					msgColor ="#18d4f0";
//				} else if(senderUserGuardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
//					msgColor ="#a43efb";
//				} else {
//					msgColor = "#383838";
//				}
//				shouhuImData = new JSONObject();
//				JSONObject shouhuContent = new JSONObject();
//				// 与IM之间约定的全站通知时所用特殊房间号 
//				String wholeSiteNoticeRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
//				
//				shouhuContent.put("msg", msg.toString());
//				shouhuContent.put("msgColor", msgColor);
//				
//				shouhuImData.put("msgtype", 2);
//				shouhuImData.put("targetid", wholeSiteNoticeRoomId);
//				shouhuImData.put("type", ImTypeEnum.IM_11001_hanhuasmg.getValue());
//				shouhuImData.put("content", shouhuContent);
//				//imData.put("user", JsonUtil.beanToJsonString(fromUser));
//			}
//		}	
//		*/
//		// === end 守护礼物额外的im消息====================================
//		
//		// ====================推送Im消息统一放到最后,1.首先推送礼消息; 2.再推其他消息……==========================================
//		
//		
//		//推送礼物im消息
//		try {
//			LogUtil.log.info(String.format("###begin,sendGift_sendMsg2IM_begin,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",senderUserId,sendGiftNum,sendGiftId,anchorRoomId,anchorUserId));
//			IMutils.sendMsg2IM(giftMsgAllDataBodyJson,senderUserId);
//			LogUtil.log.info(String.format("###end,sendGift_sendMsg2IM_end,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",senderUserId,sendGiftId,sendGiftNum,anchorRoomId,anchorUserId));
//		} catch (SystemDefinitionException e1) {
//			//保存失败消息
//			//saveImMsgFailHis(senderUserId, sendGiftBusinessId,imAllDataBodyJsonStr);
//			LogUtil.log.error(String.format("###sendGift_sendMsg2IM_SystemDefinitionException,senderUserId:%s,imAllDataBodyJson:%s",senderUserId,JsonUtil.beanToJsonString(giftMsgAllDataBodyJson)));
//			throw e1;
//		} catch (Exception e2) {
//			LogUtil.log.error(String.format("###sendGift_sendMsg2IM_Exception,senderUserId:%s,imAllDataBodyJson:%s",senderUserId,JsonUtil.beanToJsonString(giftMsgAllDataBodyJson)));
//			//保存失败消息
//			//saveImMsgFailHis(senderUserId, sendGiftBusinessId,imAllDataBodyJsonStr);
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3098);
//			throw e;
//		}
//		
//		/*
//		// 推送守护的im消息
//		try {
//			int funID = 11001;//21007.系统通知(没有字数限制)
//			int seqID = 1;//一般默认为1
//			if(onGiftRunwayImData != null){
//				LogUtil.log.info(String.format("####　begin土豪开始送守护礼物了:发消息begin..,shouhuImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
//				IMutils.sendMsg2IM(funID, seqID, onGiftRunwayImData,senderUserId);
//				LogUtil.log.info(String.format("####　end土豪开始送守护礼物了:发消息begin..,shouhuImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
//			}
//		} catch(Exception e) {
//			LogUtil.log.info(String.format("####　守护礼物发送失败,senderUserId:%s,shouhuImData:%s",senderUserId,JsonUtil.beanToJsonString(onGiftRunwayImData)));
//			LogUtil.log.error(e.getMessage(),e);
//			//推送守护礼物消息异常时，不往外抛异常(只要礼物消息发出去就ok了,少了这条也没太大影响)
//			//throw e;
//		}
//		*/
//		
//		
//		// 推送礼物跑道的im消息
//		try {
//			int funID = 11001;//21007.系统通知(没有字数限制)
//			int seqID = 1;//一般默认为1
//			if(onGiftRunwayImData != null){
//				LogUtil.log.info(String.format("####　begin推送礼物跑道的im消息:发消息begin..,onGiftRunwayImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
//				IMutils.sendMsg2IM(funID, seqID, onGiftRunwayImData,senderUserId);
//				LogUtil.log.info(String.format("####　end推送礼物跑道的im消息:发消息end..,onGiftRunwayImData:%s",JsonUtil.beanToJsonString(onGiftRunwayImData)));
//			}
//		} catch(Exception e) {
//			LogUtil.log.info(String.format("####　礼物跑道的im消息,senderUserId:%s,shouhuImData:%s",senderUserId,JsonUtil.beanToJsonString(onGiftRunwayImData)));
//			LogUtil.log.error(e.getMessage(),e);
//			//推送礼物跑道的im消息异常时，不往外抛异常(只要礼物消息发出去就ok了,少了这条也没太大影响)
//			//throw e;
//		}
//		
//		
//		
//		
//		//推送幸运礼物im消息
//		if(listImMsgOfLuckGift2BeSend!=null && listImMsgOfLuckGift2BeSend.size() >0){
//			LogUtil.log.info(String.format("###begin,sendGift_ImMsgOfLuckGift,listImMsgOfLuckGift2BeSend:%s",JsonUtil.arrayToJsonString(listImMsgOfLuckGift2BeSend)));
//			for(JSONObject json:listImMsgOfLuckGift2BeSend){
//				try {
//					IMutils.sendMsg2IM(json,Constants.SYSTEM_USERID_OF_IM);
//				} catch (Exception e) {
//					LogUtil.log.error(String.format("###sendGift_ImMsgOfLuckGift_error,senderUserId:%s,json:%s",senderUserId,JsonUtil.beanToJsonString(json)));
//					LogUtil.log.error(e.getMessage(), e);
//					//推送幸运礼物消息异常时，不往外抛异常(不影响送礼业务)
//					//throw e;
//				}
//			
//			}
//			LogUtil.log.info(String.format("###end,sendGift_ImMsgOfLuckGift,listImMsgOfLuckGift2BeSend:%s",JsonUtil.arrayToJsonString(listImMsgOfLuckGift2BeSend)));
//		}
//		
//		//推im消息节日活动奖励消息
////		if(imMsgJsonListOfPrizeActivityFestival!=null && imMsgJsonListOfPrizeActivityFestival.size() >0){
////			LogUtil.log.info(String.format("###begin,sendGift_ImMsgOfPrizeActivityFestival,imMsgJsonListOfPrizeActivityFestival:%s",JsonUtil.arrayToJsonString(imMsgJsonListOfPrizeActivityFestival)));
////			for(JSONObject json:imMsgJsonListOfPrizeActivityFestival){
////				try {
////					if(json!=null){
////						IMutils.sendMsg2IM(json,Constants.SYSTEM_USERID_OF_IM);
////					}
////				
////				} catch (Exception e) {
////					LogUtil.log.error(String.format("###sendGift_ImMsgOfPrizeActivityFestival,senderUserId:%s,json:%s",senderUserId,JsonUtil.beanToJsonString(json)));
////					LogUtil.log.error(e.getMessage(), e);
////					//推送幸运礼物消息异常时，不往外抛异常(不影响送礼业务)
////					//throw e;
////				}
////			
////			}
////			LogUtil.log.info(String.format("###end,sendGift_ImMsgOfPrizeActivityFestival,imMsgJsonListOfPrizeActivityFestival:%s",JsonUtil.arrayToJsonString(imMsgJsonListOfPrizeActivityFestival)));
////		}
//		
//		
//		// 送礼成功,删除缓存：用户(真实用户的)包裹数
//		String cacheKeyRoomUserGiftPackageCache = MCPrefix.ROOM_USER_GIFT_PACKAGE_CACHE+senderUserId;
//		MemcachedUtil.delete(cacheKeyRoomUserGiftPackageCache);
//		
//		cacheKeyRoomUserGiftPackageCache = MCPrefix.ROOM_USER_GIFT_PACKAGE_CACHE+senderUserId;
//		// 缓存再加后缀,以区别于C9的缓存
//		cacheKeyRoomUserGiftPackageCache +="-packNew";
//		MemcachedUtil.delete(cacheKeyRoomUserGiftPackageCache);
//		
//		LogUtil.log.info(String.format("###end-doSendGiftBusiness,sendGiftBusinessId:%s,senderUserId:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s,sendGiftId:%s,sendGiftNum:%s",sendGiftBusinessId, senderUserId,anchorRoomId,anchorUserId,sendGiftId,sendGiftNum));
//		return null;
//	}
//
//
//	private void saveImMsgFailHis(String senderUserId,
//			String sendGiftBusinessId, String imAllDataBodyJsonStr) {
//		ImMsgFailHis  his = new ImMsgFailHis();
//		his.setUserId(senderUserId);
//		his.setImAllDataBodyJson(imAllDataBodyJsonStr);
//		his.setBusiNessId(sendGiftBusinessId);
//		his.setType(ImMsgFailHisEnum.TypeEnum.Gift.getType());
//		imMsgFailHisService.insert(his);
//	}
//	
//	/***
//	 * 用户升级、升级消息推送 和保存升级纪录
//	 * @param userId
//	 * @param strLevel 送礼前等级
//	 * @param endLevel 送礼后等级
//	 * @param roomId
//	 */
//	@Override
//	public void userUpgradeSendMsg(String userId,String strLevel,String endLevel,String roomId) {
//		try {
//			LogUtil.log.info(String.format("###begin-推送用户升级IM消息,用户userId:%s,送礼前等级:%s,送礼后等级:%s",userId,strLevel,endLevel));
//			if (!strLevel.equals(endLevel)) {//开始和结束等级不一致
//				LogUtil.log.info(String.format("###用户送礼前后等级不一样-推送用户升级IM消息,用户userId:%s,送礼前等级:%s,送礼后等级:%s",userId,strLevel,endLevel));
//				int levelOldInt =  UserInfoUtil.converUserLevelStr2Int(strLevel);
//				int levelNewInt = UserInfoUtil.converUserLevelStr2Int(endLevel);
//				
//				int maxLevelReachorder = 0;
//				// 每一级都要加记录
//				for(int i=levelOldInt+1;i<=levelNewInt;i++){
//					String userLevelStr = Constants.LEVEL_PREFIX_USER+i;
//					// 保存用户升级历史纪录
//					Date nowDate = new Date();
//					LevelHistUser levelHistUser = new LevelHistUser();
//					levelHistUser.setUserId(userId);
//					levelHistUser.setUserLevel(i);
//					levelHistUser.setResultTime(nowDate);
//					levelHistUser.setUserLevelStr(userLevelStr);
//					int reachOrder = this.dao.getUserLevelReachOrder(userLevelStr); 
//					//取当前最高等级的排名ß
//					if(i == levelNewInt){
//						maxLevelReachorder = reachOrder;
//					}
//					LogUtil.log.info(String.format("###用户送礼前后等级不一样-reachOrder:%s,用户userId:%s,等级:%s",reachOrder,userId,i));
//					levelHistUser.setReachOrder(reachOrder);
//					this.levelHistUserService.insert(levelHistUser);
//				}
//				
//				UserInfoVo toUser = userCacheInfoService.getInfoFromCache(userId, roomId);
//				if (toUser==null) return;
//				String msg = null;
//				boolean msgType = true;
//				if (levelNewInt<=10) {//草民-10富
//					msg = String.format("恭喜%s荣升%s级，可喜可贺！", toUser.getNickname(),endLevel);
//					msgType = false;
//				}else if(11<=levelNewInt&&levelNewInt<=16){//勋爵-公爵
//					msg = String.format("恭喜%s荣升%s级，大家速来膜拜！", toUser.getNickname(),endLevel);
//				}else if (17<=levelNewInt&&levelNewInt<=24) {//郡公-国王
//					msg = String.format("恭喜%s荣升%s级，普天同庆！", toUser.getNickname(),endLevel);
//				}else if (25<=levelNewInt) {//皇帝-创世神
//					msg = String.format("恭喜%s荣升%s级，天神下凡,万民敬仰！！", toUser.getNickname(),endLevel);
//				}
//				
//				//推送im消息
//				JSONObject content = new JSONObject();
//				content.put("msg", msg);
//				content.put("oldLevel", levelOldInt);//旧等级
//				content.put("nowlevel", levelNewInt);//现在等级
//				content.put("rank", maxLevelReachorder);
//				content.put("isAnchorUpgrade", false);//是否为主播升级
//				content.put("isAllRoomNotify", msgType);//是否全站通知
//				
//				JSONObject imData = new JSONObject();
//				imData.put("msgtype", 2);
//				imData.put("targetid", roomId);
//				imData.put("type", ImTypeEnum.IM_11001_Upgrade.getValue());
//				imData.put("content", content); 
//				imData.put("to", toUser.getUid());
//				
//				int funID = 11001;
//				int seqID = 1;
//				
//				if (msgType) {
//					//发送全站通知
//					imData.put("targetid", Constants.WHOLE_SITE_NOTICE_ROOMID);
//				}
//				LogUtil.log.info("###升级消息通知data:"+JsonUtil.beanToJsonString(imData));
//				//发送IM消息
//				IMutils.sendMsg2IM(funID, seqID, imData,Constants.SYSTEM_USERID_OF_IM);
//				
//				// 及时更新房间成员列表,体现效果
//				roomCacheInfoService.addOrRefreshRoomOnlineMembers(roomId, userId);
//				
//				String targetid = roomId;
//				JSONObject imContent = new JSONObject();
//				imContent.put("msg", String.format("用户%s升级,刷新房间成员列表", userId));
//				// 发送聊天消息
//				imSendComponent.sendFun11001Msg2Im(SeqID.SEQ_1, MsgTypeEnum.GroupChat,  targetid,ImTypeEnum.IM_11001_RefreshRoomOnlineMembers, imContent);
//				LogUtil.log.info(String.format("###end-推送用户升级IM消息,用户userId:%s,送礼前等级:%s,送礼后等级:%s,升级后排名:%s",userId,strLevel,endLevel,maxLevelReachorder));
//			}else{
//				LogUtil.log.info(String.format("##不必推送用户升级IM消息(没有升级),用户userId:%s,送礼前等级:%s,送礼后等级:%s",userId,strLevel,endLevel));
//			}
//		} catch (Exception e) {
//			LogUtil.log.error("###升级发送im消息失败,roomId="+roomId);
//			LogUtil.log.error(e.getMessage(),e);
//		}
//	}
//	
//	/***
//	 * 主播升级、升级消息推送和保存升级纪录
//	 * @param userId
//	 * @param strLevel 送礼前等级
//	 * @param endLevel 送礼后等级
//	 * @param roomId
//	 */
//	@Override
//	public void anchorUpgradeSendMsg(String userId,String strLevel,String endLevel,String roomId){
//		try {
//			LogUtil.log.info(String.format("###begin-推送主播升级IM消息,主播userId:%s,房间:%s,送礼前等级:%s,送礼后等级:%s",userId,roomId,strLevel,endLevel));
//			if (!strLevel.equals(endLevel)) {//开始和结束等级不一致
//				LogUtil.log.info(String.format("###主播收礼前后等级不一样-推送主播升级IM消息,主播userId:%s,房间:%s,送礼前等级:%s,送礼后等级:%s",userId,roomId,strLevel,endLevel));
//				int levelOldInt =  UserInfoUtil.converUserLevelStr2Int(strLevel);
//				int levelNewInt = UserInfoUtil.converUserLevelStr2Int(endLevel);
//				
//				// 每一级都要加记录
//				for(int i=levelOldInt+1;i<=levelNewInt;i++){
//					String anchorLevelStr =   Constants.LEVEL_PREFIX_ANCHOR+i;
//					// 保存主播升级历史纪录
//					Date nowDate = new Date();
//					LevelHistAnchor levelHistAnchor = new LevelHistAnchor();
//					levelHistAnchor.setUserId(userId);
//					levelHistAnchor.setAnchorLevel(i);
//					levelHistAnchor.setAnchorLevelStr(anchorLevelStr) ;
//					int reachOrder = this.dao.getAnchorLevelReachOrder(anchorLevelStr);
//					LogUtil.log.info(String.format("###主播收礼前后等级不一样-reachOrder:%s,主播userId:%s,等级:%s",reachOrder,userId,i));
//					levelHistAnchor.setReachOrder(reachOrder);
//					levelHistAnchor.setResultTime(nowDate);
//					this.levelHistAnchorService.insert(levelHistAnchor);
//				}
//				
//				UserInfoVo toUser = userCacheInfoService.getInfoFromCache(userId, roomId);
//				if (toUser==null) return;
//				String msg = null;
//				boolean msgType = true;
//				if (levelNewInt<=10) {
//					msg = String.format("恭喜%s荣升%s级！艺坛新星正冉冉升起，好好加油，再接再厉！", toUser.getNickname(),endLevel);
//					msgType = false;
//				}else if(11<=levelNewInt&&levelNewInt<=20){
//					msg = String.format("恭喜%s荣升%s级！新一代乐坛红人已经诞生，愿你所有的努力都不被白费,从此暗中有光，锦绣前程！", toUser.getNickname(),endLevel);
//				}else if (21<=levelNewInt) {
//					msg = String.format("恭喜%s荣升%s级！成为超级明星 ，集万千宠爱于一身、聚百般羡艳为一体！", toUser.getNickname(),endLevel);
//				}
//				
//				//推送im消息
//				JSONObject content = new JSONObject();
//				content.put("msg", msg);
//				content.put("nowlevel", levelNewInt);//现在等级
//				content.put("isAnchorUpgrade", true);//是否为主播升级
//				content.put("isAllRoomNotify", msgType);//是否全站通知
//				
//				JSONObject imData = new JSONObject();
//				imData.put("msgtype", 2);
//				imData.put("targetid", roomId);
//				imData.put("type", ImTypeEnum.IM_11001_Upgrade.getValue());
//				imData.put("content", content); 
//				imData.put("to", toUser.getUid());
//				
//				int funID = 11001;
//				int seqID = 1;
//				
//				if (msgType) {
//					//发送全站通知
//					imData.put("targetid", Constants.WHOLE_SITE_NOTICE_ROOMID);
//				}
//				LogUtil.log.info("###主播升级消息通知data:"+JsonUtil.beanToJsonString(imData));
//				//发送IM消息
//				IMutils.sendMsg2IM(funID, seqID, imData,Constants.SYSTEM_USERID_OF_IM);
//				
//				// 及时更新房间成员列表,体现效果
//				roomCacheInfoService.addOrRefreshRoomOnlineMembers(roomId, userId);
//				
//				String targetid = roomId;
//				JSONObject imContent = new JSONObject();
//				imContent.put("msg", String.format("主播%s升级,刷新房间成员列表", userId));
//				// 发送聊天消息
//				imSendComponent.sendFun11001Msg2Im(SeqID.SEQ_1, MsgTypeEnum.GroupChat,  targetid,ImTypeEnum.IM_11001_RefreshRoomOnlineMembers, imContent);
//				LogUtil.log.info(String.format("###end-推送主播升级IM消息,主播userId:%s,房间:%s,送礼前等级:%s,送礼后等级:%s",userId,roomId,strLevel,endLevel));
//			}else{
//				LogUtil.log.info("###收礼前后主播等级一样(没有升级)，不进行推送IM消息,主播userId:"+userId);
//			}
//		} catch (Exception e) {
//			LogUtil.log.error("###主播升级发送im消息失败,主播userId:"+userId);
//			LogUtil.log.error(e.getMessage(),e);
//		}
//	}
//	
//	/**
//	 * 减用户资产(扣金币、礼物包裹)
//	 *  1:扣用户金币、加用户经验; <br /> 
//	 *  2:给主播加砖石、加主播经验;  <br />
//	 *  3:加金币支出流水记录...
//	 * @param senderUserId
//	 * @param anchorUserId
//	 * @param sendGiftId
//	 * @param sendGiftNum
//	 * @param imNotifyRoomId
//	 * @param gold 账户明细,送礼前用户账户实际余额
//	 * @param isOnlySubGiftpackage 是否只扣背包(即不扣金币)
//	 * @return 返回此次送礼的订单号
//	 * @throws Exception
//	 */
//	private String subUserAssetBySendGift(String senderUserId, String anchorUserId,int sendGiftId, int sendGiftNum,String imNotifyRoomId,Long gold,boolean isOnlySubGiftpackage,double extraRate,boolean isExtraAnchor) throws Exception {
//		//把用户账户类UserAccount当做锁头
//	//	synchronized (UserAccount.class) {
//			
//			String sendGiftBusinessId = StrUtil.getOrderId();//此次订单号 
//			
//			// 账户明细
//			UserAccountBook userAccountBook = new UserAccountBook();
//			userAccountBook.setGiftId(sendGiftId); // 账户明细，送礼礼物id
//			userAccountBook.setSendGiftNum(sendGiftNum); // 账户明细，用户送出礼物数量
//			userAccountBook.setUserId(senderUserId); // 账户明细，用户id
//			userAccountBook.setSourceId(sendGiftBusinessId); // 账户明细，送礼业务唯一订单号，sendGiftBusinessId
//			userAccountBook.setSourceDesc(USER_ACCOUNT_BOOK_SOURCEDEC);
//			int changeGolds = 0; // 账户明细，需要增、减的金币数
//			int changeGiftNum  = 0; // 账户明细，需要增、减的包裹数
//			String content = ""; // 账户明细,备注说明
//			boolean isEnoughPackage = false; // 是否包裹足够支付，默认false
//			
//			int senderCountPackagetPeriodInDb = 0;//送礼用户用户背包中受限包裹总数
//			int senderCountPackagetNotPeriodInDb = 0;//送礼用户用户背包中活动包裹总数	
//			//送礼用户用户背包中已有的包裹总数
//			int senderCountPackageAllInDb = 0; 
//			//此次从送礼用户背包中所减的包裹数
//			int subtractPackageNumFromSender  = 0;
//			//从缓存中取礼物
//			com.jiujun.shows.gift.domain.Gift gift = getGiftInfoFromCache(sendGiftId);
//			if(gift==null){
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3048);
//				throw e;
//			}
//			int giftEachSpendGolds = gift.getPrice();  //每个礼物的金币数
//			int giftEachDiamond =gift.getDiamond() ;  //每个礼物的钻石数
//			Integer totalGold2PayFromUserAccount = 0; //需要从用户账户扣除的金币总数
//			Date nowDateTime = new Date();
//			String nowDateTimeStr =  DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", nowDateTime); //时间字符串
//			float giftEachPriceRMB = StrUtil._Float(gift.getPriceRMB()); //每个礼物RMB价值，单位：分
//			float giftTotalPriceRMB = giftEachPriceRMB * sendGiftNum; //此次赠送的礼物RMB价值，单位：分
//			int giftEachAnchorPoint = gift.getAnchorPoint();
//			int giftEachUserPoint = gift.getUserPoint();
//			// 主播获得礼物分成
//			double anchorGetDiamondRate = gift.getRate();
//			LogUtil.log.info(String.format("###begin-doSendGiftBusiness,sendGiftBusinessId:%s,senderUserId:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s,sendGiftId:%s,sendGiftNum:%s,giftEachSpendGolds:%s",sendGiftBusinessId, senderUserId,imNotifyRoomId,anchorUserId,sendGiftId,sendGiftNum,giftEachSpendGolds));
//			LogUtil.log.info("giftTotalPriceRMB:" + giftTotalPriceRMB + ",max_give_totalPrice_oneTime:" + max_give_totalPrice_oneTime + ",min_give_totalPrice_oneTime= " + min_give_totalPrice_oneTime);
//			if(giftTotalPriceRMB > max_give_totalPrice_oneTime || giftTotalPriceRMB < min_give_totalPrice_oneTime ){
//				String errorMsg = String.format("此次送礼(分):%s,系统设置单次送礼最多(分):%s,最少(分):%s", giftTotalPriceRMB,max_give_totalPrice_oneTime,min_give_totalPrice_oneTime);
//				// 礼物总价值不在限制范围内错
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3055,errorMsg);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			} 
//			//List<String> listToBeExecuteSql = new ArrayList<String>();//待执行的sql集合
//			List<String> listToBeExecuteUpdateSql = new ArrayList<String>();//待执行的sql集合
//			List<String> listToBeExecuteInsertSql = new ArrayList<String>();//待执行的sql集合
//			List<String> listToBeExecuteDeleteSql = new ArrayList<String>();//待执行的sql集合
//			
//			StringBuffer toBeExecuteSqlSbf = new StringBuffer();//待执行的sql StringBuffer
//		
//			boolean flagEnoughPay = false; //剩余金币是否足够支付的标志,默认为false
//			
//			int subtractPacketNum = 0; //此次送礼减去的背包个数
//			StringBuffer sumNotPeriodTimeSqlSbf = new StringBuffer(); 
//			
//			// 清空一下StringBuffer
//			sumNotPeriodTimeSqlSbf.append(" select sum(number) sum from t_user_package ")
//			.append(" where isValid = 1 and isPeriod = 0 ").append(" and userId = '")
//			.append(senderUserId).append("' and giftId = ").append(sendGiftId);
//			//查没有日期限制的对应礼物背包数
//			String sumNotPeriodTimeSql  = sumNotPeriodTimeSqlSbf.toString();
//			
//			//查有日期限制的对应礼物背包数
//			StringBuffer sumPeriodTimeSqlSbf = new StringBuffer();
//			/*String sumPeriodTimeSql = "select sum(number) sum  from t_user_package " +
//					" where isValid = 1 and  isPeriod = 1 " +
//					" and userId = " + senderUserId +
//					" and giftId = " + sendGiftId +
//					" and endTime >= '"+ DateUntil.getFormatDate("yyyy-MM-dd", nowDateTime) +"'";*/
//			sumPeriodTimeSqlSbf.append(" select sum(number) sum  from t_user_package where isValid = 1 and  isPeriod = 1  and userId = '")
//			.append(senderUserId).append("' and giftId = ").append(sendGiftId)
//			.append(" and endTime >='").append(nowDateTimeStr).append("'");
//			String sumPeriodTimeSql = sumPeriodTimeSqlSbf.toString();
//			
//			LogUtil.log.info("###sumPeriodTimeSql:"+sumPeriodTimeSql);
//			LogUtil.log.info("###sumNotPeriodTimeSql:"+sumNotPeriodTimeSql);
//			Map<String,Object> mapSumPeriodTimeLimit = this.dao.selectOne(sumPeriodTimeSql);
//			Map<String,Object> mapSumNotPeriodTimeLimit = this.dao.selectOne(sumNotPeriodTimeSql);
//			if( null == mapSumPeriodTimeLimit || mapSumPeriodTimeLimit.size() <= 0 ) {
//				senderCountPackagetPeriodInDb = 0;
//			}else{
//				senderCountPackagetPeriodInDb = Integer.parseInt(mapSumPeriodTimeLimit.get("sum").toString());
//			}
//			
//			if( null == mapSumNotPeriodTimeLimit || mapSumNotPeriodTimeLimit.size() <= 0){
//				senderCountPackagetNotPeriodInDb = 0;
//			}else{
//				senderCountPackagetNotPeriodInDb =Integer.parseInt(mapSumNotPeriodTimeLimit.get("sum").toString());
//			}
//			
//			//送礼用户用户背包中已有的包裹总数 = 受限 + 非受限
//			senderCountPackageAllInDb = senderCountPackagetNotPeriodInDb + senderCountPackagetPeriodInDb;
//			
//			userAccountBook.setGiftPackagePreRemainNum(senderCountPackageAllInDb); // 账户明细，送礼前包裹总数
//			userAccountBook.setRecordTime(nowDateTime);
//			
//			LogUtil.log.info(String.format("###doSendGiftBusiness,countPackagetPeriod:%s,countPackagetNotPeriod:%s,sendNum:%s",senderCountPackagetPeriodInDb,senderCountPackagetNotPeriodInDb,sendGiftNum));
//			if( senderCountPackagetPeriodInDb + senderCountPackagetNotPeriodInDb < sendGiftNum ){ //用户剩余对应礼物背包总数  小于 送出礼物数
//				
//				if(isOnlySubGiftpackage){ //若余量不足且送礼时指定只扣背包(不扣金币),则抛出业务异常
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3151);
//					throw e;
//				}
//				
//				totalGold2PayFromUserAccount = giftEachSpendGolds * (sendGiftNum-senderCountPackagetNotPeriodInDb-senderCountPackagetPeriodInDb); //需要从账户扣除的金币价值
//				String selectUserAccountSql = "select * from t_user_account a where a.gold >= "+totalGold2PayFromUserAccount+" and userId= '"+senderUserId+"'";
//				Map<String, Object> mapUserAccount = this.dao.selectOne(selectUserAccountSql);
//
//				//判断用户剩余金币是否足够
//				if( null == mapUserAccount || mapUserAccount.size() <=0){  //用户剩余对应礼物背包数+用户余额  还不足以支付
//					LogUtil.log.error("######送礼,余额不足以支付,userId:"+senderUserId);
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3006,senderUserId);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}else{
//					subtractPacketNum = senderCountPackagetPeriodInDb + senderCountPackagetNotPeriodInDb;
//					changeGiftNum = subtractPacketNum; // 账户明细，扣除的包裹数
//					changeGolds = totalGold2PayFromUserAccount; // 账户明细，扣除包裹后，需要扣除的金币
//					flagEnoughPay = true; //设置标示用户余额足够支付
//					//用户剩余对应礼物背包数+用户余额 的情况下，1:减所有背包;2:减部分金币;3:加送出记录
//					
//					//减受限背包
//					if(senderCountPackagetPeriodInDb > 0){
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//						toBeExecuteSqlSbf.append(" delete  from t_user_package where isValid = 1 and isPeriod = 1 and userId = '").append(senderUserId)
//						.append("' and giftId =  ").append(sendGiftId)
//						.append(" and endTime >= '").append(nowDateTimeStr).append("'");
//						listToBeExecuteDeleteSql.add(toBeExecuteSqlSbf.toString());
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//					}
//					
//					//减非受限背包
//					if(senderCountPackagetNotPeriodInDb > 0){
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//						toBeExecuteSqlSbf.append(" delete  from t_user_package ")
//						.append(" where isValid = 1 and isPeriod = 0 and userId =  '").append(senderUserId)
//						.append("' and giftId =  ").append(sendGiftId);
//						listToBeExecuteDeleteSql.add(toBeExecuteSqlSbf.toString());
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//					}
//					
//					
//					//扣金币,蜜桃平台此次有了金币收入
//					// 清空一下StringBuffer
//					toBeExecuteSqlSbf.setLength(0);
//					toBeExecuteSqlSbf.append(" insert into t_pay_gift_in(orderId,userId,giftId,price,number,gold,resultTime) ")
//					.append(" value('")
//					.append(sendGiftBusinessId).append("','")
//					.append(senderUserId).append("','")
//					.append(sendGiftId).append("','")
//					.append(giftEachSpendGolds).append("','")
//					.append((sendGiftNum-senderCountPackagetNotPeriodInDb-senderCountPackagetPeriodInDb)).append("','")
//					.append(totalGold2PayFromUserAccount).append("','")
//					.append(nowDateTimeStr).append("')");
//					listToBeExecuteInsertSql.add(toBeExecuteSqlSbf.toString());
//					// 清空一下StringBuffer
//					toBeExecuteSqlSbf.setLength(0);
//								
//					/*PayGiftIn payGiftIn = new PayGiftIn();
//					payGiftIn.setOrderId(orderId);
//					payGiftIn.setUserId(senderUserId);
//					payGiftIn.setGiftId(sendGiftId);
//					payGiftIn.setPrice(giftEachPrice);
//					payGiftIn.setNumber(sendNum-countPackagetNotPeriod-countPackagetPeriod);
//					payGiftIn.setGold(totalGold2PayFromUserAccount);
//					payGiftIn.setResultTime(nowDateTime);
//					this.payGiftInDao.insert(payGiftIn);*/
//				}
//				
//			}else{  //用户剩余背包价值大于等于送出礼物总价值
//				flagEnoughPay = true; //设置标示用户余额足够支付
//				isEnoughPackage = true;
//				subtractPacketNum = sendGiftNum; 
//				changeGiftNum = sendGiftNum; // 账户明细，扣除的包裹数
//				//执行如下步骤 ,1:扣期限型背包;2:若期限型背包不足，在扣长期背包
//				
//				if( senderCountPackagetPeriodInDb < sendGiftNum ){ //受限背包不足,先delete受限背包
//					
//					if(senderCountPackagetPeriodInDb > 0){
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//						toBeExecuteSqlSbf.append(" delete  from t_user_package where isValid = 1 and isPeriod = 1  and userId =  '").append(senderUserId)
//						.append("' and giftId =  ").append(sendGiftId)
//						.append(" and endTime >= '").append(nowDateTimeStr).append("';");
//						listToBeExecuteDeleteSql.add(toBeExecuteSqlSbf.toString());
//						// 清空一下StringBuffer
//						toBeExecuteSqlSbf.setLength(0);
//					}
//					
//					
//					int reaminCountPackagetNotPeriod = senderCountPackagetNotPeriodInDb - (sendGiftNum - senderCountPackagetPeriodInDb );
//					// 清空一下StringBuffer
//					toBeExecuteSqlSbf.setLength(0);
//					//检测背包是否反而变多了
//					if(reaminCountPackagetNotPeriod>senderCountPackagetNotPeriodInDb){
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_2008);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//					}
//					
//					String sqlSelectNotPeriodPackage = "select *  from t_user_package " +
//							" where isValid = 1 and isPeriod = 0 and userId =  '" +senderUserId+
//							"' and giftId = " + sendGiftId ;
//					List<Map<String,Object>> listNotPeriodPackage = this.selectListBySql(sqlSelectNotPeriodPackage);
//					int toBeSubStractFromNotPeriodCountNum = (sendGiftNum - senderCountPackagetPeriodInDb );
//					for(int n=0 ; n<listNotPeriodPackage.size() ; n++ ){
//						Integer ncount = Integer.parseInt(listNotPeriodPackage.get(n).get("number").toString());
//						toBeSubStractFromNotPeriodCountNum -= ncount;
//						if( toBeSubStractFromNotPeriodCountNum > 0 ){
//							listToBeExecuteDeleteSql.add(" delete  from t_user_package where isValid = 1 and isPeriod = 0 and  id="+listNotPeriodPackage.get(n).get("id"));
//						}else if( toBeSubStractFromNotPeriodCountNum == 0 ){
//							listToBeExecuteDeleteSql.add(" delete  from t_user_package where isValid = 1 and isPeriod = 0 and id="+listNotPeriodPackage.get(n).get("id"));
//							break; 
//						}else{
//							int reaminCountNotPeriod = 0-toBeSubStractFromNotPeriodCountNum;
//							if(reaminCountNotPeriod>senderCountPackagetNotPeriodInDb){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_3000);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}
//							listToBeExecuteUpdateSql.add(" update t_user_package set number="+reaminCountNotPeriod+" where isValid = 1 and isPeriod = 0  and id="+listNotPeriodPackage.get(n).get("id"));
//							break;
//						}
//						
//					}
//					
//					
//					/*toBeExecuteSqlSbf.append(" update t_user_package set number= ").append(reaminCountPackagetNotPeriod)
//					.append(" where isValid = 1 and isPeriod = 0 and userId =  ").append(senderUserId)
//					.append(" and giftId =  ").append(sendGiftId).append(";");
//					listToBeExecuteSql.add(toBeExecuteSqlSbf.toString());*/
//					// 清空一下StringBuffer
//					toBeExecuteSqlSbf.setLength(0);
//					
//				}else{ //受限背包足够,则只扣受限背包
//					String sqlSelectPeriodPackage = "select *  from t_user_package " +
//							" where isValid = 1 and isPeriod = 1 " +
//							" and userId = '" + senderUserId +
//							"' and giftId = " + sendGiftId +
//							" and endTime >= '"+ nowDateTimeStr +"'";
//					List<Map<String,Object>> listPeriodPackage = this.selectListBySql(sqlSelectPeriodPackage);
//					int countNum = sendGiftNum;
//					for(int n=0 ; n<listPeriodPackage.size() ; n++ ){
//						Integer ncount = Integer.parseInt(listPeriodPackage.get(n).get("number").toString());
//						countNum -= ncount;
//						if( countNum > 0 ){
//							listToBeExecuteDeleteSql.add(" delete  from t_user_package where isValid = 1 and  id="+listPeriodPackage.get(n).get("id"));
//						}else if( countNum == 0 ){
//							listToBeExecuteDeleteSql.add(" delete  from t_user_package where isValid = 1 and  id="+listPeriodPackage.get(n).get("id"));
//							break; 
//						}else{
//							int reaminCountPeriod = 0-countNum;
//							if(reaminCountPeriod>senderCountPackagetPeriodInDb){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_3000);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}
//							listToBeExecuteUpdateSql.add(" update t_user_package set number="+reaminCountPeriod+" where isValid = 1 and id="+listPeriodPackage.get(n).get("id"));
//							break;
//						}
//					}
//				}
//			}
//			
//			if(!flagEnoughPay){//如果以上判断用户不够支付,则抛出异常
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3006);
//				//LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}else{
//				
//				//赠送用户的送礼前的账号信息
//				//UserAccount senderUserAcc = this.dao.getObjectByUserId(senderUserId);
//				
//				userAccountBook.setPreRemainGolds(gold); // 账户明细,送礼前用户账户实际余额
//				
//				//赠送用户的原来用户等级
//				//String senderOldUserLevel = senderUserAcc.getUserLevel();
//				
//				//主播原来的账号信息
//				//UserAccount anchorUserAcc = this.dao.getObjectByUserId(anchorUserId);
//				//主播的原来主播等级
//				//String anchorOldAnchorLevel = anchorUserAcc.getAnchorLevel();
//				
//				LogUtil.log.info("###SendGift-subtractPacketNum:"+subtractPacketNum);
//				LogUtil.log.info("###SendGift-扣除完包裹后，需要扣除的金币，totalGold2PayFromUserAccount:"+totalGold2PayFromUserAccount); 
//				
//				//判断是否扣金币(因为有可能包裹够扣了,就无需扣账户的金币)
//				if(totalGold2PayFromUserAccount>0){ //totalGold2PayFromUserAccount>0,说明包裹不足,则扣账户金币
//					
//					//扣金币的业务方法已加锁
//					this.subtractGolds(senderUserId,totalGold2PayFromUserAccount, null); // 这里执行原来的逻辑，账户明细放在最后加
//					
//					// 金币流水账
////					userAccountBookOutService.subGoldsRecord(senderUserId, totalGold2PayFromUserAccount, sendGiftBusinessId, SOURCE_USER, CONTENT_USER);
//				}
//				
//				//若有扣包裹,则加包裹支出流水账
//				if((senderCountPackageAllInDb) > 0){
//					String refDesc = "refId与表:t_pay_gift_out的orderId字段对应";
//					String comment = "送出礼物消费";
//					
//					if(senderCountPackageAllInDb > sendGiftNum){
//						subtractPackageNumFromSender = sendGiftNum;
//					}else{
//						subtractPackageNumFromSender =  senderCountPackageAllInDb;
//					}
//					userPackageOutHisService.addRecord(senderUserId, sendGiftId, sendGiftNum, sendGiftBusinessId, refDesc, comment);
//					
//				}
//				
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				int giftSourceType = PayGiftOutEnum.SourceType.gift.getValue();
//				toBeExecuteSqlSbf.append("insert into t_pay_gift_out(orderId,userId,toUserId,giftId,number,resultTime,price,diamond,sourceType) value ('")
//				.append(sendGiftBusinessId).append("','")
//				.append(senderUserId).append("','")
//				.append(anchorUserId).append("',")
//				.append(sendGiftId).append(",'")
//				.append(sendGiftNum).append("','")
//				.append(nowDateTimeStr).append("',")
//				.append(giftEachSpendGolds*sendGiftNum).append(",")
//				.append(giftEachDiamond*sendGiftNum).append(",")
//				.append(giftSourceType).append(");");
//				listToBeExecuteInsertSql.add(toBeExecuteSqlSbf.toString());
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				
//				
//				
//				/*PayGiftOut payGiftOut = new PayGiftOut();
//				payGiftOut.setOrderBy(orderId);
//				payGiftOut.setUserId(senderUserId);
//				payGiftOut.setToUserId(anchorUserId);
//				payGiftOut.setGiftId(sendGiftId);
//				payGiftOut.setNumber(sendNum);
//				payGiftOut.setResultTime(nowDateTime);
//				payGiftOut.setPrice(giftEachPrice*sendNum);
//				payGiftOut.setDiamond(giftEachDiamond*sendNum);
//				this.payGiftOutDao.insert(payGiftOut);*/
//				
//				// 给收礼主播加的主播经验
////				int addPoint2AnchorExp = giftEachAnchorPoint*sendGiftNum;
//				int addPoint2AnchorExp = giftEachAnchorPoint*sendGiftNum;
//				if(isExtraAnchor) {
//					addPoint2AnchorExp = (int) (addPoint2AnchorExp*extraRate);
//				}
//				// 主播获得钻石
//				int anchorAddDiamond = giftEachDiamond*sendGiftNum;
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				//更新主播剩余钻石、主播的主播经验
//				toBeExecuteSqlSbf.append(" update t_user_account set diamond = diamond+").append(anchorAddDiamond)
//				.append(" ,anchorPoint=anchorPoint+").append(addPoint2AnchorExp)
//				.append(" where userId= '").append(anchorUserId).append("';");
//				listToBeExecuteUpdateSql.add(toBeExecuteSqlSbf.toString());
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				
//				// 增加主播增加钻石记录
////				userAccountBookinService.addGoldsRecord(anchorUserId, 0,giftEachDiamond*sendGiftNum, sendGiftBusinessId, SOURCE_ANCHOR, CONTENT_ANCHOR);
//				
//				// 给赠送用户加的用户经验
////				int addPoint2UserExp = giftEachUserPoint*sendGiftNum;
//				int addPoint2UserExp = (int) (giftEachUserPoint*sendGiftNum * extraRate);
//				
//				//更新赠送用户的用户经验
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				toBeExecuteSqlSbf.append(" update t_user_account set userPoint=userPoint+ ").append(addPoint2UserExp)
//				.append("  where userId= '").append(senderUserId).append("';");
//				listToBeExecuteUpdateSql.add(toBeExecuteSqlSbf.toString());
//				// 清空一下StringBuffer
//				toBeExecuteSqlSbf.setLength(0);
//				
//				LogUtil.log.info("#####SendGiftSqlList-listToBeExecuteUpdateSql:" + JsonUtil.arrayToJsonString(listToBeExecuteUpdateSql));
//				LogUtil.log.info("#####SendGiftSqlList-listToBeExecuteInsertSql:" + JsonUtil.arrayToJsonString(listToBeExecuteInsertSql));
//				LogUtil.log.info("#####SendGiftSqlList-listToBeExecuteDeleteSql:" + JsonUtil.arrayToJsonString(listToBeExecuteDeleteSql));
//				
//				//批量执行sql
//				//执行update语句,(必须用ibatis执行,因为前面的操作是通过ibatais的,不然db事务控制无效)
//				for (String sql : listToBeExecuteUpdateSql) {
//					this.dao.updateBySql(sql);
//					LogUtil.log.info(">>>sendGift-sql:"+sql);
//				}
//				
//				//执行insert语句(必须用ibatis执行,因为前面的操作是通过ibatais的,不然db事务控制无效)
//				for (String sql : listToBeExecuteInsertSql) {
//					this.dao.insertBySql(sql);
//					LogUtil.log.info(">>>sendGift-sql:"+sql);
//				}
//				
//				//执行delete语句(必须用ibatis执行,因为前面的操作是通过ibatais的,不然db事务控制无效)
//				for (String sql : listToBeExecuteDeleteSql) {
//					this.dao.deleteBySql(sql);
//					LogUtil.log.info(">>>sendGift-sql:"+sql);
//				}
//				
//				
//				
//				//更新赠送用户的用户等级 
//				/*String senderNewUserLevel = levelDao.getUserLevel(senderUserId);
//				LogUtil.log.info(String.format("###senderUserId:%s,senderNewUserLevel:%s,senderOldUserLevel:%s", anchorUserId,senderNewUserLevel,senderOldUserLevel));
//				this.dao.updateUserLevel(senderUserId, senderNewUserLevel);
//				//升级则从缓存中删除用户信息
//				if(!senderOldUserLevel.equals(senderNewUserLevel)){
//					String senderUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + senderUserId;
//					LogUtil.log.info("###clearUserCacheInfo"+senderUserCacheKey);
//					MemcachedUtil.delete(senderUserCacheKey);
//					// 及时刷新缓存(预防在其他的事务中,由于事务隔离级别,不能及时拿到最新的db数据,如:用户等级)
//					this.userCacheInfoService.getInfoFromCache(senderUserId, imNotifyRoomId);
//				}
//				
//				//更新主播的主播等级
//				String anchorNewAnchorLevel = levelDao.getAnchorLevel(anchorUserId);
//				LogUtil.log.info(String.format("###anchorUserId:%s,anchorNewAnchorLevel:%s,", anchorUserId,anchorNewAnchorLevel));
//				this.dao.updateAnchorLevel(anchorUserId, anchorNewAnchorLevel);
//				//升级则从缓存中删除用户信息
//				if(!anchorOldAnchorLevel.equals(anchorNewAnchorLevel)){
//					String anchorUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + anchorUserId;
//					MemcachedUtil.delete(anchorUserCacheKey);
//					// 及时刷新缓存(预防在其他的事务中,由于事务隔离级别,不能及时拿到最新的db数据,如:主播等级)
//					this.userCacheInfoService.getInfoFromCache(anchorUserId, imNotifyRoomId);
//				}*/
//				
//				LogUtil.log.info(String.format("###doSendGiftBusiness,senderUserId:%s,giftTotalPriceRMB(分):%s,sendGiftBusinessId:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s,sendGiftId:%s,sendGiftNum:%s",senderUserId,giftTotalPriceRMB, sendGiftBusinessId ,imNotifyRoomId,anchorUserId,sendGiftId,sendGiftNum));
//				
//				userAccountBook.setChangeGiftNum(-changeGiftNum); // 账户明细，实际支付的包裹数（包裹足够，则changeGiftNum=sendGiftNum,即包裹足额支付）
//				userAccountBook.setChangeGolds(-changeGolds); // 账户明细，实际需要扣除的金币数(先扣包裹， 后扣金币，为0说明包裹足够支付)
//				if(isEnoughPackage) { // 用户包裹足够支付
//					content = "NO.1，用户送礼，包裹足够，只减包裹，不扣金币";
//				} else {
//					content = "NO.2，用户送礼，包裹不足够，先扣包裹:" + changeGiftNum + "个，再扣金币:" +  changeGolds;
//				}
//				userAccountBook.setContent(content); // 账户明细，备注说明
//				// 账户明细,送礼后，剩余包裹数
//				int afterSum = this.userPackageService.countUserPackage(senderUserId, sendGiftId);
//				userAccountBook.setGiftPackageSufRemainNum(afterSum); 
//				// 账户明细,送礼后，剩余金币
//				UserAccount afterUserAccount = this.dao.getObjectByUserId(senderUserId);
//				userAccountBook.setSufRemainGolds(afterUserAccount.getGold());
//				
//				// 插入账户明细
//				this.userAccountBookService.insert(userAccountBook);
//				LogUtil.log.info(String.format("###送礼log,用户%s送给主播%s礼物%s(礼物单价:%s(分),主播获得分成%s),%s个(总价值%s分),用户获得经验%s,主播获得经验%s,主播获得钻石%s",senderUserId,anchorUserId,sendGiftId,giftEachPriceRMB,anchorGetDiamondRate,sendGiftNum,giftTotalPriceRMB,addPoint2UserExp,addPoint2AnchorExp ,anchorAddDiamond));
//			}
//			return sendGiftBusinessId;
//		//}
//	}
//
//	
//	
//	@SuppressWarnings("unchecked")
//	private com.jiujun.shows.gift.domain.Gift getGiftInfoFromCache(int giftId){
//		Gift gift = this.giftService.getGiftInfoFromCache(giftId);
//		return gift;	
//	}
//
//
//	@Override
//	public void sendGiftWithCharge(String userId,int onceChargeMoney) throws Exception {
//		if(StringUtils.isEmpty(userId)||onceChargeMoney<0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		//为了不影响正常的加金币,所以每一小块都加try ... catch
//		
//		/*try {
//			//充值送礼(长期有效)
//			sendGiftWithChargeAllTheTime(userId);
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}*/
//		
//		/*try {
//			//20160520节日活动
//			sendGiftWithChargeAt20160520Activity(userId,onceChargeMoney);
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}*/
//		
//		/*try {
//			//20160815奥运充值送勋章活动
//			LogUtil.log.info("###############奥运充值送勋章活动，begin...");
//			sendDecorateByChargeOfTime(userId);
//			LogUtil.log.info("###############奥运充值送勋章活动，end...");
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}*/
//		
//		try {
//			// 20160926,首充大特惠
//			sendGiftWithFirstCharge(userId, onceChargeMoney);
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}
//		
//		try {
//			// 充值送礼活动(后台模板)
//			activitiesPayConfService.payAndGivePrizes(userId, onceChargeMoney);
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}
//		
//		try {
//			// 2017新年充值送礼活动
//			festivalActivityService.do2017NewYearPayChargeActivity(userId);
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//		}
//		
//	}
//
//
//	/**
//	 * 首充送礼,20161212更改规则,详情问嘉裕哥
//	 * @param userId
//	 * @param onceChargeMoney 充值金额(单位:分)
//	 * @throws Exception 
//	 */
//	private void sendGiftWithFirstCharge(String userId, int onceChargeMoney) throws Exception {
//		
//		LogUtil.log.info(String.format("###begin-sendGiftWithFirstCharge,userId:%s,onceChargeMoney:%s",userId,onceChargeMoney));
//		int successOrderTimes =  this.payChargeOrderDao.findChargSuccessTimes(userId);
//		if(successOrderTimes >=2){ //非首次充值
//			LogUtil.log.info(String.format("###sendGiftWithFirstCharge,userId:%s,successOrderTimes:%s is not first charge ,don't send gift...",userId,successOrderTimes));
//		}else{
//			// 100元(单位：分，10000)
//			int chargeMoneyForGift98 = 10000;
//			// 30元(3000分)充值对应礼物
//			int chargeMoneyForGift50 = 3000;
//			// 5元(500分)充值对应礼物
//			int chargeMoneyForGift5 = 500;
//			
//			String comment = "系统赠送:首次充值礼物";
//			
//			int carId = 0; // 赠送座驾id
//			int toolId = ToolTableEnum.Id.Gekonghanhua.getValue(); // 赠送工具id
//			int toolNum = 1; // 工具数量，默认1个
//			int type = ToolHisTableEnum.TYPE.ADD.getValue();
//			
//			//送蜜桃
//			int giftId = GiftIdEnum.YangGuang.getValue();
//			int giftNum = 10 ;
//			boolean isPeriod = false;
//			Date endTime = null;
//			String refId = null;
//			String refDesc = null;
//			boolean isGivePeach = false;
//			
//			//送铜勋章
//			int decorateId = 0; // 默认铜
//			int number = 1;
//			int days = 7; // 给个默认值
//			int isAccumulation = 0;
//			String sourceKey = "firstPayFor5"; // 默认铜
//			String desc = "首次充值大于5，赠送新人铜勋章"; // 默认铜
//			
//			// 账户明细
//			UserAccountBook userAccountBook = new UserAccountBook();
//			userAccountBook.setUserId(userId);
//			userAccountBook.setGiftId(giftId);
//			userAccountBook.setRecordTime(new Date());
//			
//			// 是否给奖励
//			boolean flagGivePrize = false;
//			// 是否直接升到四级 
//			boolean falgUpto4userLevel = false; 
//			
//			// 98元
//			if(onceChargeMoney >= chargeMoneyForGift98) {
//				LogUtil.log.info(String.format("###begin-sendGiftWithFirstCharge,userId:%s,onceChargeMoney:%s,sendGift100元-begin",userId,onceChargeMoney));
//				
//				flagGivePrize = true;
//				isGivePeach = true;
//				falgUpto4userLevel = true;
//				
//				// 阳光
//				giftNum = 200;
//				userAccountBook.setChangeGiftNum(giftNum);
//				userAccountBook.setContent("活动，首次充值大于100元，赠送阳光200个");
//				
//				//送金勋章
//				decorateId = DecorateTableEnum.Id.xinrenJin.getValue();
//				sourceKey = "firstPayFor100";
//				desc = "首次充值大于100，赠送新人金勋章";
//				
//				//送隔空喊话
//				toolNum = 5;
//				
//				//送座驾(玛莎拉蒂)
//				carId = SysCarTableEnum.ID.masaladi.getValue();
//				
//				
//			//50元
//			} else if(onceChargeMoney >= chargeMoneyForGift50){
//				LogUtil.log.info(String.format("###begin-sendGiftWithFirstCharge,userId:%s,onceChargeMoney:%s,sendGift50元-begin",userId,onceChargeMoney));
//				
//				flagGivePrize = true;
//				isGivePeach = true;
//				
//				//送阳光
//				giftNum = 100;
//				userAccountBook.setChangeGiftNum(giftNum);
//				userAccountBook.setContent("活动，首次充值大于50元，赠送阳光100个");
//				
//				//送勋章
//				decorateId = DecorateTableEnum.Id.xinrenYin.getValue();
//				sourceKey = "firstPayFor50";
//				desc = "首次充值大于50，赠送新人银勋章";
//				
//				//送隔空喊话
//				toolNum = 3;
//				
//				//送座驾(奔驰)
//				carId = SysCarTableEnum.ID.benchi.getValue();
//			}else if(onceChargeMoney >= chargeMoneyForGift5){ //5元
//				LogUtil.log.info(String.format("###begin-sendGiftWithFirstCharge,userId:%s,onceChargeMoney:%s,sendGift5元-begin",userId,onceChargeMoney));
//				
//				flagGivePrize = true;
//				
//				// 送勋章
//				decorateId = DecorateTableEnum.Id.xinrenTong.getValue();
//				sourceKey = "firstPayFor5";
//				desc = "首次充值大于5，赠送新人铜勋章";
//				
//				//送隔空喊话
//				toolNum = 1;
//				
//				//送座驾(宝马)
//				carId = SysCarTableEnum.ID.loversBike.getValue();
//			}else{
//				LogUtil.log.info(String.format("###sendGiftWithFirstCharge,userId:%s chargeMoney:%s doesn't enough for send gift...",userId,onceChargeMoney));
//			}
//			
//			if(	flagGivePrize ){
//				LogUtil.log.info("####sendGiftWithFirstCharge-首次充值,给奖励,金额："+onceChargeMoney + ",发放奖励：是否奖励蜜桃-"+isGivePeach 
//						+ ",giftId：" + giftId +",giftNum=" + giftNum + ",勋章：" + decorateId + ",工具toolId：" + toolId + ",toolNum=" + toolNum + ",座驾：" + carId);
//				// 加座驾
//				this.userCarPortService.sysActiveGiveCar(userId, carId, 5, null, comment, false);
//				
//				// 送勋章
//				Decorate decorate = decorateService.getObjectById(decorateId);
//				if(decorate != null) {
//					days = decorate.getLightenDay();
//				}
//				decoratePackageService.addPackage(userId, null, decorateId, true, number, days, sourceKey, desc, isAccumulation);
//				
//				//送隔空喊话
//				this.userToolPackageService.addToolPackage(userId, toolId, toolNum,type, null, null, comment);
//				
//				
//				// 送蜜桃a
//				if(isGivePeach) {
//					this.userPackageService.addPackage(userId, giftId, giftNum, isPeriod, endTime, refId, refDesc, comment, userAccountBook);
//				}
//				
//				if(falgUpto4userLevel){
//					UserAccount chargeUserAccount = this.dao.getObjectByUserId(userId) ;
//					long userCurrentPoint = chargeUserAccount.getUserPoint();
//					int userLevel4 = 4;
//					Level level = this.levelDao.getUserLevelByLevel(userLevel4);
//					long levelPoint = Long.parseLong(level.getPoint());
//					if(levelPoint > userCurrentPoint ){
//						LogUtil.log.info(String.format("###levelPoint <= userCurrentPoint,to update userPoint,userId:%s,userCurrentPoint:%s,levelPoint:%s,level:%s ",userId,userCurrentPoint,levelPoint,level));
//						LogUtil.log.info(String.format("###begin -chargeUserAccount:%s",JsonUtil.beanToJsonString(chargeUserAccount)));
//						chargeUserAccount.setUserPoint(levelPoint);
//						chargeUserAccount.setUserLevel(level.getLevel());
//						this.dao.update(chargeUserAccount) ;
//						LogUtil.log.info(String.format("###end -chargeUserAccount:%s",JsonUtil.beanToJsonString(chargeUserAccount)));
//					}else{
//						LogUtil.log.info(String.format("###levelPoint <= userCurrentPoint,not update userPoint,userId:%s,userCurrentPoint:%s,levelPoint:%s,level:%s ",userId,userCurrentPoint,levelPoint,userLevel4));
//					}
//					
//				}
//				
//			}else{
//				LogUtil.log.info(String.format("###不满足首充送礼条件,userId:%s,onceChargeMoney:%s",userId,onceChargeMoney)) ;
//			}
//			
//			
//		}
//		LogUtil.log.info(String.format("###end-sendGiftWithFirstCharge,userId:%s,onceChargeMoney:%s",userId,onceChargeMoney));
//	}
//
//
//	/**
//	 * 20160520节日活动
//	 * @param userId
//	 * @param onceChargeMoney
//	 * @throws Exception 
//	 */
//	private void sendGiftWithChargeAt20160520Activity(String userId,int onceChargeMoney) throws Exception {
//		//活动1
////		festivalActivityService.do20160520Activity1WithTimeTotalCharge(userId);
//		//活动2
//		festivalActivityService.do20160520Activity2WithOnceCharge(userId, onceChargeMoney);
//	}
//
//	/**
//	 * 充值送礼(长期有效)
//	 * @param userId
//	 * @throws Exception
//	 */
//	private void sendGiftWithChargeAllTheTime(String userId) throws Exception {
//		LogUtil.log.info(String.format("###begin-sendGiftWithChargeAllTheTime,userId:%s",userId));
//		int totalChargeMoney = this.payChargeOrderDao.getTotalChargeMoneyByUserId(userId);
//		PayChargeGiftConf payChargeGiftConf = payChargeGiftConfDao.getObjectByMoney(totalChargeMoney);
//		LogUtil.log.info("###sendGiftWithCharge-totalChargeMoney:"+totalChargeMoney+",userId:"+userId);
////		LogUtil.log.info("###sendGiftWithCharge-payChargeGiftConf:"+JsonUtil.beanToJsonString(payChargeGiftConf+",userId:"+userId));
//		if(payChargeGiftConf!=null){
//			int giftType = payChargeGiftConf.getGiftType();//从db配置中获取金额对应的充值礼物类型
//			int carType = PayChargeGiftConfEnum.GifeTypeEnum.car.getGiftType();
//			int giftId = payChargeGiftConf.getGiftId();//从db配置中获取金额对应的充值礼物id
//			if(giftType==carType){ //如果是座驾类型,则查询座驾赠送历史,看是否已经赠送过相应的礼物,避免重复赠送
//				List list = carGiveHisDao.findHis(userId,CarGiveHisEnum.TypeEnum.systemGive_Charge.getType(),giftId);
//				if(list==null||list.size()<1){//系统还没赠送过
//					String comment = "累计充值送礼";
//					SysCarDo  sysCar  = sysCarDao.getObjectById(giftId);
//					if(sysCar != null) {
//						sysTaskPrizesService.insertSysTaskPrizes(userId, TaskTableEnum.TaskIdEnum.pay, GiftTypeBusinessEnum.Car, giftId, 1,sysCar.getCarName(), comment);
//					}
//					boolean flag2taskList = false;
//					userCarPortService.sysActiveGiveCar(userId, giftId,CarGiveHisEnum.TypeEnum.systemGive_Charge.getType(), null,comment,flag2taskList);
//					// 又不需要这样搞了。。。
////					userCarPortService.allotCar2UserTaskList(TaskIdEnum.pay, userId, giftId, CarGiveHisEnum.TypeEnum.systemGive_Charge.getType(), null, comment);
//				}else{
//					LogUtil.log.info("###充值送礼已发放,不重复赠送,目前累计充值(分)"+totalChargeMoney+",userId:"+userId);
//				}
//				
//			}else{
//				LogUtil.log.error("###充值送礼,gifType:"+giftType+"的逻辑未实现");
//			}
//			
//		}else{
//			LogUtil.log.info("###累计充值额度不够,系统无需送礼.目前累计充值(分)"+totalChargeMoney+",userId:"+userId);
//		}
//		LogUtil.log.info(String.format("###end-sendGiftWithChargeAllTheTime,userId:%s",userId));
//	}
//
//
//	@Override
//	public int getAnchorLevel(String roomId) throws Exception {
//		UserAccount userAccount = dao.getByRoomId(roomId);
//		if(userAccount==null||StringUtils.isEmpty(userAccount.getAnchorLevel())||userAccount.getAnchorLevel().length()<2){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3049);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		}else{
//			String anchorLevelStr = userAccount.getAnchorLevel();
//			//正常情况,DB中主播等级的数据如:S1,S2……
//			anchorLevelStr = anchorLevelStr.substring(1,anchorLevelStr.length());
//			int anchorLevel = Integer.parseInt(anchorLevelStr);
//			return anchorLevel;
//		}
//	}
//
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public void subtractGoldsAndAddAccountBookOutHis(String userId, int golds,String businessId, String businessModel, String content,UserAccountBook userAccountBook)
//			throws Exception {
//		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(businessId)){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		this.subtractGolds(userId, golds,userAccountBook);
////		userAccountBookOutService.subGoldsRecord(userId, golds, businessId, businessModel, content);
//	}
//
//	/**
//	 * 奥运充值送勋章活动-20160815-20160831
//	 * @param userId
//	 * @throws Exception
//	 */
//	private void sendDecorateByChargeOfTime(String userId) throws Exception {
//		if (StringUtils.isEmpty(userId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		}
//		String code = SysConfTableEnum.Code.AOYUN2016XUNZHANG.getValue();
//		SysConf sysConf = sysConfService.getByCode(code);// 获取活动配置
//		String confValue = sysConf.getConfValue();
//		Date nowDate = new Date();
//		Date beginTime = null;
//		Date endTime = null;
//		JSONObject jsonConf = JsonUtil.strToJsonObject(confValue);
//		// 活动赠送勋章配置
//		JSONArray jsonTotalChargeMoney = jsonConf.getJSONArray(SysConfServiceImpl.aoyun2016chargexunzhang_totalChargeMoney);
//		if (jsonTotalChargeMoney == null || jsonTotalChargeMoney.size() <= 0) {
//			LogUtil.log.error("#####2016-sendDecorateByChargeOfTime-奥运充值送勋章活动配置不正确");
//			return;
//		}
//		// 活动有效期
//		JSONObject jsonConfFree = jsonConf.getJSONObject(SysConfServiceImpl.aoyun2016chargexunzhang_validDate);
//		String beginTimeStr = jsonConfFree.getString("beginTime");
//		String endTimeStr = jsonConfFree.getString("endTime");
//		beginTime = DateUntil.parse2DefaultFormat(beginTimeStr);
//		endTime = DateUntil.parse2DefaultFormat(endTimeStr);
//		if (nowDate.after(beginTime) && nowDate.before(endTime)) { // 是否在活动期内
//			// 加个同步，防止并发时产生脏数据
//			synchronized (DecoratePackage.class) {
//				String userSourceKey = UserAccountServiceImpl.USER_SOURCE_KEY+userId;
//				String imMsg = "获得 奥运";
//				String desc ="奥运充值送勋章活动";
//				boolean isSendMsg = false;
//				String sourceKey = null;
//				int decorateId = 0;
//				int validDay = 30;// 有效期
//				int number = 1; // 个数
//				List<DecorateHis> decorateHisList = null;
//				DecoratePackage decoratePackage = null;
//				DecorateHis decorateHis = null;
//				for (int i = 0; i < jsonTotalChargeMoney.size(); i++) { // 充值区间升序，第一个不满足就break
//					JSONObject chargeMoneyRangeJson = jsonTotalChargeMoney.getJSONObject(i);
//					JSONArray chargeMoneyRangeArrItem = chargeMoneyRangeJson.getJSONArray("totalChargeMoneyRange");
//					sourceKey = chargeMoneyRangeJson.getString("prizeKey");
//					decorateId = chargeMoneyRangeJson.getInt("decorateIdKey");
//					// 活动期内充值总金额
//					int totalChargeMoney = payChargeOrderDao.sumUserChargeTotal(userId, beginTimeStr, endTimeStr);
//					// 这里单位为分(单位为分)
//					int minMoney = chargeMoneyRangeArrItem.getInt(0);
//					int maxMoney = chargeMoneyRangeArrItem.getInt(1);
//					LogUtil.log.info("累计充值金额totleMoney="+totalChargeMoney +",充值区间,min="+ minMoney + ",max=" +maxMoney);
//					// 是否满足充值送勋章
//					if (totalChargeMoney >= minMoney && totalChargeMoney < maxMoney) {
//						// 根据奥运奖牌同一个spurceKey查看是否获得过哪个勋章
//						decorateHisList = decorateHisService.findByUserAndSourceKeyOrder(userId, sourceKey);
//						if (decorateHisList != null && decorateHisList.size() > 0) {
//							decorateHis = decorateHisList.get(0); // 按降序排序，拿第一个
//							int decorateHisId = decorateHis.getDecorateId();
//							if (decorateId > decorateHisId) { // 如果配置的奖牌勋章id大于已获得的，说明充值获得奖牌勋章要高于之前的，则重新分配
//								decoratePackage = decoratePackageService.getDecoratePackageByUserIdAndDercoateId(userId, decorateHisId);
//								if (decoratePackage != null) {
//									// 先结束之前获得的
//									decoratePackage.setEndtime(nowDate);
//									this.decoratePackageService.update(decoratePackage);
//									LogUtil.log.info("#####2016-sendDecorateByChargeOfTime-奥运充值送勋章活动,更新现有的勋章的结束时间为当前时间,"
//											+ "累计金额=" + totalChargeMoney + ",userId="+ userId + ",现有勋章decorateId=" + decorateHisId);
//								}
//							} else { 
//								// 否则，不用再分配
//								LogUtil.log.info("#####2016-sendDecorateByChargeOfTime-奥运充值送勋章活动,该勋章已发放，勋章id=" + decorateId + ",userId="+ userId);
//								continue;
//							}
//						}
//						// 新增一条
//						switch(decorateId) {
//						case 17:
//							imMsg += "铜牌勋章 X1";
//							break;
//						case 18:
//							imMsg += "银牌勋章 X1";
//							break;
//						case 19:
//							imMsg += "金牌勋章 X1";
//							break;
//						default:
//							Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//							LogUtil.log.error(e.getMessage(), e);
//							throw e;
//						}
//						userSourceKey += decorateId;
//						LogUtil.log.info("#####2016-sendDecorateByChargeOfTime-奥运充值送勋章活动，发放勋章,"
//								+ "累计金额=" + totalChargeMoney + ",userId="+ userId + ",发放勋章decorateId=" + decorateId
//								+ ",userSourceKey="+userSourceKey);
//						this.addPackageAndHis(userId, null, decorateId,true, number, validDay, sourceKey, desc);
//						isSendMsg = true;
//					} else {
//						LogUtil.log.info("#####2016-sendDecorateByChargeOfTime-奥运充值送勋章活动,累计充值金额不在区间内,不送勋章该勋章,勋章id="+decorateId
//								+ "累计金额=" + totalChargeMoney + ",userId="+ userId);
//					}
//				}
//				LogUtil.log.info("####2016奥运充值送勋章活动,userId="+userId + ",isSendMsg="+isSendMsg);
//				// 是否需要发送通知
//				if(isSendMsg) {
//					LogUtil.log.info("####2016奥运充值送勋章活动,发送通知，begin，userId="+userId + ",isSendMsg="+isSendMsg);
//					// 发送全站消息
//					String wholeSiteNoticeRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
//					int funID = 11001;
//					int seqID = 1;
//					int msgtype = 2;
//					int imType = ImTypeEnum.IM_11001_2016qixi.getValue();
//					UserInfoVo  user =  this.userCacheInfoService.getInfoFromCache(userId, null);
//					String systemUserIdOfIM  = Constants.SYSTEM_USERID_OF_IM;
//					
//					JSONObject imDataUser = new JSONObject();
//					imDataUser.put("msgtype", msgtype);
//					imDataUser.put("type", imType);
//					
//					JSONObject contentUser = new JSONObject();
//					contentUser.put("msg", imMsg);
//					contentUser.put("user", user);
//					imDataUser.put("content", contentUser);
//					
//					// 发送邮件消息
//					int funID2 = 21007;
//					int seqID2 = 1;
//					int msgtype2 = 1;
//					int imType2 = 0;
//					String msg = "恭喜您" +imMsg;
//					JSONObject imDataUser2 = new JSONObject();
//					imDataUser2.put("msgtype", msgtype2);
//					imDataUser2.put("type", imType2);
//					imDataUser2.put("content", msg);
//					try {
//						//全站通知
//						imDataUser.put("targetid", wholeSiteNoticeRoomId);
//						IMutils.sendMsg2IM(funID, seqID, imDataUser,systemUserIdOfIM);
//						LogUtil.log.info("####2016奥运充值送勋章活动,发送通知，END，userId="+userId + ",isSendMsg="+isSendMsg);
//						// 推送系统消息到用户邮件箱
//						imDataUser2.put("targetid", userId);
//						IMutils.sendMsg2IM(funID2, seqID2, imDataUser2, systemUserIdOfIM);
//						LogUtil.log.info("####2016奥运充值送勋章活动,发送邮件消息end");
//					} catch (Exception e) {
//						LogUtil.log.error("###2016充值送勋章章通知，im消息通知失败");
//						LogUtil.log.error(e.getMessage(),e);
//					}
//				}
//			}
//		} else {
//			LogUtil.log.info("#####奥运充值送勋章活动，不在有效期...");
//		}
//	}
//
//	public void addPackageAndHis(String userId, String roomId, int decorateId,
//			boolean isPeriod, int number, int days, String sourceKey,
//			String desc) throws Exception {
//		if (StringUtils.isEmpty(userId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		synchronized (DecoratePackage.class) {
//			Date nowDate = new Date();
//			DecoratePackage newDecoratePackage = new DecoratePackage();
//			newDecoratePackage.setUserid(userId);
//			newDecoratePackage.setRoomId(roomId);
//			newDecoratePackage.setDecorateid(decorateId);
//			newDecoratePackage.setNumber(number);
//			newDecoratePackage.setBegintime(nowDate); // 开始时间为当前时间
//			Date endTime = DateUntil.addDatyDatetime(nowDate, days);
//			if (isPeriod) {// 有时间限制
//				newDecoratePackage.setIsperiod(TableCommonEnum.LogicIntVal.YES.getValue());
//				newDecoratePackage.setEndtime(endTime); // 结束时间为当前时间 + 点亮有效期
//			} else {
//				newDecoratePackage.setIsperiod(TableCommonEnum.LogicIntVal.NO.getValue());
//				// 无结束时间
//			}
//			decoratePackageService.insert(newDecoratePackage);
//
//			// 保存获取历史记录
//			DecorateHis his = new DecorateHis();
//			his.setUserId(userId);
//			his.setDecorateId(decorateId);
//			his.setNumber(number);
//			his.setAddTime(nowDate);
//			his.setSourceKey(sourceKey);
//			his.setDescs(desc);
//			decorateHisService.insert(his);
//		}
//	}
//	
//	/**
//	 * 校验勋章礼物权限
//	 * @param userId
//	 * @param anchorUserId1
//	 * @return
//	 * @throws Exception
//	 */
//	private boolean checkDecorateGiftSendStatus(String userId,String anchorUserId1,List<Integer> list) throws Exception{
//		boolean sendGiftAccess = false;
//		if(StringUtils.isEmpty(userId)) {
//			return sendGiftAccess;
//		}
//		
//		DecoratePackage decoratePackage = null;
//		for(Integer decorateId : list) {
//			decoratePackage = decoratePackageService.findValidDecoratePackage(userId, decorateId);
//			if(decoratePackage != null) {
//				sendGiftAccess = true;
//				break;
//			}
//		}
//		return sendGiftAccess;
//	}
//
//
//	@Override
//	public void addUserPoint(String userId, int userPoint) throws Exception {
//		if(userPoint<=0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		 synchronized (UserAccount.class) {
//			UserAccount userAccount = this.dao.getObjectByUserId(userId);
//			long totalUserPoint = userAccount.getUserPoint()+userPoint;
//			Level level = levelDao.getObjectByUserPoint(totalUserPoint);
//			String newUserLevel = level.getLevel();
//			userAccount.setUserLevel(newUserLevel);
//			userAccount.setUserPoint(totalUserPoint);
//			this.dao.update(userAccount);
//		}		
//	}
//
//
//	@Override
//	public void checkSendGiftAccess(String senderUserId, String anchorUserId,String reciveUserId, int sendGiftId, int sendGiftNum, String imNotifyRoomId) throws Exception {
//		// 2017-07-14
//		// 新增用户与用户的送礼，因此，这里判断需要修改
//		// 从缓存中查询gift
//		Gift gift = getGiftInfoFromCache(sendGiftId);
//		if(gift == null) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3048);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		int giftType = gift.getGiftType(); // 礼物类型，7-用户礼物
//		boolean isSendUserGift = (giftType == 7 ? true : false);
//		// 如果礼物为用户礼物，则只需判断此处逻辑
//		if(isSendUserGift) {
//			// 判断是否收礼为用户
//			if(StringUtils.isEmpty(reciveUserId)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3208);
//				throw e;
//			}
//			if(reciveUserId.equals(anchorUserId)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3208);
//				throw e;
//			}
//			// 收礼用户是否是主播
//			UserAnchor anchor =  userAnchorService.getNormalAnchor(reciveUserId);
//			if(anchor != null) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3208);
//				throw e;
//			}
//			// 判断用户是否存在，机器人也可以接收礼物
//			if(reciveUserId.indexOf(Constants.ROBOT_USER_PRE_USERID_KEY) == -1) {
//				UserInfoVo user = userCacheInfoService.getInfoFromCache(reciveUserId, null);
//				if(user == null) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3156);
//					throw e;
//				}
//			}
//			return;
//		}
//		//送礼频繁,所以从缓存中取主播信息
//		UserAnchor userAnchorDbCache  = userAnchorService.getAnchorFromCacheByUserId(anchorUserId);
//		//判断主播是否存在
//		if(userAnchorDbCache == null){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3049,anchorUserId);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		//判断传过来的roomId 与db中的roomId是否一致
//		String roomIdDbCache = userAnchorDbCache.getRoomId();
//		if(!imNotifyRoomId.equals(roomIdDbCache)){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3097);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		
//		if(sendGiftId == GiftTableEnum.GiftIdEnum.ZNCake.getValue()) {
//			if(!senderUserId.equals(anchorUserId)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3193);
//				throw e;
//			}
//		}
//		
//		String roomId = userAnchorDbCache.getRoomId();
//		
//		
//		// 已停用
//		if(gift.getIsUse() == GiftTableEnum.IsUse.NO.getValue()){
//			if(sendGiftId == GiftTableEnum.GiftIdEnum.Yiqikanliuxingyu.getValue()){ //一起去看流星雨稍后开放
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3186);
//				throw e;
//			}else{
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3162);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			
//		}
//		
//		// 是否校验所送恋爱勋章专属礼物可送
//		if(sendGiftId == GiftTableEnum.GiftIdEnum.BIDONG.getValue()) {
//			String cacheKey =MCPrefix.USER_ATROOM_CAN_SEND_DECOREATE_GIFT_CACHE+roomId+anchorUserId;
//			int cacheTime = MCTimeoutConstants.USER_ATROOM_CAN_SEND_DECOREATE_GIFT_CACHE_TIME;
//			Object cacheObj = MemcachedUtil.get(cacheKey);
//			if(cacheObj == null){
//				List<Integer> list = new ArrayList<Integer>();
//				list.add(DecorateTableEnum.Id.UserLoveSeptemberDer.getValue());
//				list.add(DecorateTableEnum.Id.AnchorLoveSeptemberDer.getValue());
//				list.add(DecorateTableEnum.Id.UserQixiNan.getValue());
//				list.add(DecorateTableEnum.Id.AnchorQixiNv.getValue());
//				boolean checkAccess = checkDecorateGiftSendStatus(senderUserId,anchorUserId,list);
//				if(!checkAccess) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3104);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}else{
//					MemcachedUtil.set(cacheKey,checkAccess ,cacheTime);
//				}
//			}
//			return;
//		}
//		
//		// 校验特权勋章
//		if(sendGiftId == GiftTableEnum.GiftIdEnum.JinHulu.getValue()) {
//			List<Integer> list = new ArrayList<Integer>();
//			list.add(DecorateTableEnum.Id.YunqiWang1.getValue());
//			list.add(DecorateTableEnum.Id.YunqiWang2.getValue());
//			list.add(DecorateTableEnum.Id.YunqiWang3.getValue());
//			boolean checkAccess = checkDecorateGiftSendStatus(senderUserId,anchorUserId,list);
//			if(!checkAccess) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3104);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			return;
//		}
//				
//		// 守护礼物，校验权限
//		int totalGolds = gift.getPrice() * sendGiftNum;
//		if(giftType == GiftTableEnum.GiftType.GD.getType()) {
//			LogUtil.log.info("####　土豪开始送守护礼物了...");
//			List<Map> gkList = guardWorkService.getUserGuardInfoByRoomCache(senderUserId, imNotifyRoomId);
//			if(gkList == null || gkList.size() <=0) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3113);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			int senderUserGuardType =Integer.parseInt(gkList.get(0).get("guardType").toString());
//			int guardLevel = gift.getGuardLevel();
//			if(senderUserGuardType < guardLevel) {
//				String errMsg = "您要先开通守护"+guardLevel+"级才可以赠送该礼物哟";
//				LogUtil.log.warn(errMsg);
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3163);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//		}
//		
//		// 判断蜜桃是否成熟,成熟才可以送高跟鞋礼物	
//		
//		if(GiftTableEnum.GiftIdEnum.Gaogenxie.getValue() == sendGiftId){//若前5阶段尚未成熟，则不给送高跟鞋(因为这时没涨人气和魅力)
//			Date nowDate = new Date();
//			String nowDateStr = DateUntil.getFormatDate("yyyy-MM-dd", nowDate);
//			String cacheKey =MCPrefix.ROOM_PEACH_DAILY_RIPE_CACHE+nowDateStr+roomId;
//			int cacheTime = MCTimeoutConstants.ROOM_PEACH_DAILY_RIPE_CACHE_TIME;
//			Object cacheObj = MemcachedUtil.get(cacheKey);
//			if(cacheObj == null){
//				PeachAnchor  peachAnchor  = this.peachAnchorDao.getDailyData(nowDateStr, roomId);
//				String isRipe = null;
//				if(peachAnchor != null){
//					isRipe = peachAnchor.getIsRipe();
//				}
//				if(StringUtils.isEmpty(isRipe) || "n".equals(isRipe.toLowerCase())){//5阶段尚未成熟
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3101);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//				}else{
//					MemcachedUtil.set(cacheKey, "y",cacheTime);
//				}
//			}else{
//				LogUtil.log.info(String.format("###date:%s,roomId:%s peach is ripe", nowDateStr,roomId)) ;
//			}
//		
//		}
//			
//	}
//
//
//	@Override
//	public void doCommonBizAfterConsumeGold(String senderUserId, String anchorUserId, boolean isUserGift, String roomId, int consumeGold) throws Exception {
//		LogUtil.log.info(String.format("###doCommonBizAfterConsumeGold-begin-消费金币后做的一些共同业务,senderUserId:%s,anchorUserId:%s,consumeGold:%s", senderUserId,anchorUserId,consumeGold));
//		if (isUserGift) {
//			if (StringUtils.isEmpty(senderUserId)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//				throw e;
//			}
//		} else {
//			if (StringUtils.isEmpty(senderUserId) || StringUtils.isEmpty(anchorUserId)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//				throw e;
//			}
//		}
//
//		if(consumeGold <= 0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		
//		try {
//			// LogUtil.log.info(String.format("###doCommonBizAfterConsumeGold-begin-通用消费活动入口,senderUserId:%s,anchorUserId:%s,sendGold:%s", senderUserId, anchorUserId, consumeGold)) ;
//			// 处理奇幻迟到之旅活动业务
//			// this.temporaryService.handle201703QihuanchidaozhilvBiz(senderUserId, anchorUserId, consumeGold);
//			// LogUtil.log.info(String.format("###doCommonBizAfterConsumeGold-begin-通用消费活动入口,senderUserId:%s,anchorUserId:%s,sendGold:%s", senderUserId, anchorUserId, consumeGold)) ;
//			
//			// 周年庆活动
//			// this.temporaryService.handleAnniversaryActivity(senderUserId, anchorUserId, consumeGold);
//			
//			// 粉红女郎获动
//			// this.temporaryService.handlePinkActivity(senderUserId, anchorUserId, roomId, consumeGold);
//			// 灰姑娘变身记活动
//			// temporaryService.handleGreygirlActivity(senderUserId, anchorUserId, roomId, consumeGold);
//			
//			// 仲夏之夜活动
//			// temporaryService.handleMidsummerNight(senderUserId, anchorUserId, isUserGift, roomId, consumeGold);
//			
//			// 七夕活动
//			// temporaryService.handleMagpieFestival(senderUserId, anchorUserId, isUserGift, roomId, sendGiftId, consumeGold);
//			
//			// 0905活动
//			temporaryService.handleFestival0905(senderUserId, anchorUserId, isUserGift, roomId, consumeGold);
//		} catch (Exception e) {
//			LogUtil.log.error(String.format("###doCommonBizAfterConsumeGold-通用消费活动入口，发生异常,,userId:%s,anchorUserId:%s,sendGold:%s",senderUserId,anchorUserId,consumeGold));
//			LogUtil.log.error(e.getMessage(),e);
//		}
//		
//		LogUtil.log.info(String.format("###doCommonBizAfterConsumeGold-end-消费金币后做的一些共同业务,senderUserId:%s,anchorUserId:%s,consumeGold:%s", senderUserId,anchorUserId,consumeGold));
//	}
//
//
//	@Override
//	public void doCommonConsumeGift(String senderUserId, String anchorUserId,
//			String roomId, int sendGiftId, int consumeNum) throws Exception {
//		LogUtil.log.error(String.format("###doCommonConsumeGift-通用消费活动入口，begin,,userId:%s,anchorUserId:%s,consumeNum:%s",senderUserId,anchorUserId,consumeNum));
//		if(StringUtils.isEmpty(senderUserId)){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		if(consumeNum <= 0){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		try {
//			// 2017-04-18 拯救主播活动
//			// temporaryService.handle20170418Activity(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 2017-05-20 
//			// temporaryService.handle2017520Activity(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 2017-05-23 端午活动
//			// temporaryService.handleDWActivity(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 2017-05-31—— 06-02
//			// temporaryService.handleChildenDay(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 2017-05-31—— 06-02
//			// temporaryService.handleChildenDay(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 泳池派对活动
//			// temporaryService.handleSwimmingParty(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//			
//			// 处理活动送特定礼物直播间开奖
//			// temporaryService.handleActivityLottery(senderUserId, anchorUserId, roomId, sendGiftId, consumeNum);
//		} catch (Exception e) {
//			LogUtil.log.error(String.format("###doCommonConsumeGift-通用消费活动入口，发生异常,,userId:%s,anchorUserId:%s,consumeNum:%s",senderUserId,anchorUserId,consumeNum));
//			LogUtil.log.error(e.getMessage(),e);
//		}
//		LogUtil.log.error(String.format("###doCommonConsumeGift-通用消费活动入口，end,,userId:%s,anchorUserId:%s,consumeNum:%s",senderUserId,anchorUserId,consumeNum));
//	}
//
//	@Override
//	public void updateUserLevel(String senderUserId, String senderNewUserLevel) {
//		this.dao.updateUserLevel(senderUserId, senderNewUserLevel);
//	}
//
//
//	@Override
//	public void updateAnchorLevel(String anchorUserId,
//			String anchorNewAnchorLevel) {
//		this.dao.updateAnchorLevel(anchorUserId, anchorNewAnchorLevel);
//	}
//
//	@Override
//	public JSONObject doAddExtraExperienceByGift(String userId, String anchorId,
//			String roomId, int giftId, int giftNum) throws Exception {
//		JSONObject ret = null;
//		String retKey = MCPrefix.EXTRA_POINT_CACHE;
//		Object obj = MemcachedUtil.get(retKey);
//		if(obj != null) {
//			ret = (JSONObject) obj;
//		} else {
//			ret = new JSONObject();
//			double extraRate = 1.0; // 默认不加额外经验
//			String isExtraAnchor = "false"; // 默认不加额外经验
//			ActivityFestivalConf afc = activityFestivalConfService.getActivityFestivalConf(ActivitiesConfEnum.ActivityNo.ExtraPointGift.getValue());
//			if(afc == null) {
//				LogUtil.log.error("###doAddExtraExperience-活动未配置");
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
//				throw e;
//			}
//			
//			Date nowDate = new Date();
//			Date beginTime = afc.getBeginTime();
//			Date endTime = afc.getEndTime();
//			
//			String activityConfJsonStr = afc.getActivityConf();
//			if(StringUtils.isEmpty(activityConfJsonStr)){
//				LogUtil.log.info(String.format("#####doAddExtraExperience,activityConfJsonStr is null, return"));
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
//				throw e;
//			}
//			
//			// 配置json
//			JSONObject activityConfJson = JsonUtil.strToJsonObject(activityConfJsonStr);
//			//配置中的测试用户
//			String confTestUserId = activityConfJson.getString("testUserId");
//			String activityGiftsListStr = activityConfJson.getString("activityGifts");
//			if(StringUtils.isEmpty(activityGiftsListStr)){
//				LogUtil.log.info(String.format("#####doAddExtraExperience,activityGiftsListStr is null, return"));
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
//				throw e;
//			}
//			
//			JSONObject activityGifts = JsonUtil.strToJsonObject(activityGiftsListStr);
//			if(activityGifts ==null ||activityGifts.size() <= 0){
//				LogUtil.log.info(String.format("#####doAddExtraExperience,activityGiftsListJSONArray size <=0, return"));
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
//				throw e;
//			}
//			
//			if(userId.equals(confTestUserId)||(nowDate.after(beginTime) && nowDate.before(endTime))){
//				JSONArray gifts = JsonUtil.strToJSONArray(activityGifts.getString("gifts"));
//				LogUtil.log.error("###doAddExtraExperience-gifts=" + gifts.toString());
//				String isAllGift = activityGifts.getString("isAllGift"); 
//				if("true".equals(isAllGift) || gifts.contains(giftId)) {
//					extraRate = activityGifts.getDouble("extraRate");
//					isExtraAnchor = activityGifts.getString("isExtraAnchor");
//				}
//			}
//			ret.put("extraRate", extraRate);
//			ret.put("isExtraAnchor", isExtraAnchor);
//			// 保存配置，5分钟缓存
//			MemcachedUtil.set(retKey, ret, 60*5);
//		}
//		return ret;
//	}
//
//
//	@Override
//	public void doAddExtraExperiencsByGold(String userId, String anchorId,
//			int consumGolds) throws Exception {
//		ActivityFestivalConf afc = activityFestivalConfService.getActivityFestivalConf(ActivitiesConfEnum.ActivityNo.ExtraPointGold.getValue());
//		if(afc == null) {
//			LogUtil.log.error("###doAddExtraExperiencsByGold-活动未配置");
//			return;
//		}
//		Date nowDate = new Date();
//		Date beginTime = afc.getBeginTime();
//		Date endTime = afc.getEndTime();
//		
//		String activityConfJsonStr = afc.getActivityConf();
//		if(StringUtils.isEmpty(activityConfJsonStr)){
//			LogUtil.log.info(String.format("#####doAddExtraExperiencsByGold,activityConfJsonStr is null, return"));
//			return;
//		}
//		
//		// 配置json
//		JSONObject activityConfJson = JsonUtil.strToJsonObject(activityConfJsonStr);
//		//配置中的测试用户
//		String confTestUserId = activityConfJson.getString("testUserId");
//		
//		int extraPoint = 0;
//		if(userId.equals(confTestUserId)||(nowDate.after(beginTime) && nowDate.before(endTime))){
//			double extraRate = activityConfJson.getDouble("extraRate");
//			extraPoint = (int) (consumGolds * extraRate);
//			LogUtil.log.error("doAddExtraExperiencsByGold-extraPoint=" + extraPoint);
//			// 加经验
//			this.dao.updateUserPoint(userId, extraPoint);
//			// 加主播经验
//			boolean isExtraAnchor = activityConfJson.getBoolean("isExtraAnchor");
//			if(isExtraAnchor) {
//				this.dao.updateAnchorPoint(anchorId, extraPoint);
//			}
//		}	
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public void doSendUserGiftBiz(String senderUserId, String reciveUserId,
//			String anchorUserId, int sendGiftId, int sendGiftNum, String anchorRoomId,
//			boolean isOnGiftRunway,double extraRate) throws Exception {
//		LogUtil.log.error("###doSendUserGiftBiz-senderUserId=" + senderUserId + ",sendGiftId=" + sendGiftId 
//				+ ",sendGiftNum=" + sendGiftNum + ",reciveUserId=" + reciveUserId + ",begin....");
//				
//		// 从缓存中查询gift
//		Gift gift = getGiftInfoFromCache(sendGiftId);
//		int sumGolds = gift.getPrice() * sendGiftNum;
//		
//		// 不可购买的礼物
//		if(gift.getBuyable() == GiftTableEnum.Buyable.NO.getValue()){
//			// 用户包裹数量
//			int packageRemainSum = this.userPackageDao.countUserPackage(senderUserId, sendGiftId);
//			if(gift != null) {
//				if(sendGiftNum > packageRemainSum) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3185);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//			}
//		}
//		// 收礼用户是否为机器人
//		boolean isRobt = reciveUserId.indexOf(Constants.ROBOT_USER_PRE_USERID_KEY) == -1 ? false : true;
//		//赠送用户的送礼前的账号信息
//		UserAccount senderUserAcc = this.getObjectByUserId(senderUserId);	
//		String senderOldUserLevel = senderUserAcc.getUserLevel();	
//		String oldRenqLevel = "R0"; // 默认0级
//		
//		UserAccount reciveder = null;
//		if(!isRobt) {
//			reciveder = this.getObjectByUserId(reciveUserId);
//			if(reciveder.getRenqLevel() != null) {
//				oldRenqLevel = reciveder.getRenqLevel();
//			}
//		}
//		// 送礼逻辑
//		this.handleSendUserGiftBiz(senderUserAcc, reciveder, isRobt,reciveUserId, sendGiftId, sendGiftNum, extraRate);
//		
//		// 送礼之后
//		// 送礼用户等级
//		String senderNewUserLevel = levelDao.getUserLevel(senderUserId);
//		
//		try {
//			//更新赠送用户的用户等级 
//			LogUtil.log.info(String.format("###doSendUserGiftBiz-after,senderUserId:%s,senderNewUserLevel:%s,senderOldUserLevel:%s", senderUserId,senderNewUserLevel,senderOldUserLevel));
//			//升级则从缓存中删除用户信息
//			if(!senderOldUserLevel.equals(senderNewUserLevel)){
//				this.updateUserLevel(senderUserId, senderNewUserLevel);
//				String senderUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + senderUserId;
//				LogUtil.log.info("###clearUserCacheInfo"+senderUserCacheKey);
//				MemcachedUtil.delete(senderUserCacheKey);
//				// 及时刷新缓存(预防在其他的事务中,由于事务隔离级别,不能及时拿到最新的db数据,如:用户等级)
//				this.userCacheInfoService.getInfoFromCache(senderUserId, anchorRoomId);
//			}		
//			if(!isRobt) {
//				// 收礼后的人气等级
//				String newRenqLevel = levelDao.getRenqLevel(reciveUserId);
//				if(!oldRenqLevel.equals(newRenqLevel)) {
//					this.dao.updateRenqLevel(reciveUserId, newRenqLevel); // 更新人气等级
//					String reciveUserCacheKey = MCPrefix.USERCACHEINFO_PREKEY + reciveUserId;
//					LogUtil.log.info("###clearUserCacheInfo"+reciveUserCacheKey);
//					MemcachedUtil.delete(reciveUserCacheKey);
//					this.userCacheInfoService.getInfoFromCache(reciveUserId, null); // 更新缓存
//				} 
//			}
//			//用户等级提升消息推送,并记录升级历史
//			userUpgradeSendMsg(senderUserId,senderOldUserLevel, senderNewUserLevel, anchorRoomId);
//		} catch (Exception e) {
//			LogUtil.log.error(String.format("###送礼完成后,升级相关的方法发生异常,userId:%s,anchorUserId:%s,giftId:%s,num:%s",senderUserId,anchorUserId,sendGiftId,sendGiftNum));
//			LogUtil.log.error(e.getMessage(),e);
//		}
//		
//		// 封装礼物消息
//		JSONObject content = new JSONObject();
//		content.put("roomId", anchorRoomId);
//		String notifyRoomId = anchorRoomId; // 默认本房间
//		Boolean flagOnGiftRunwayCondition = Boolean.FALSE; 
//		if(isOnGiftRunway){ //如果用户选择了上礼物跑道
//			int onGiftRunwayNeedGold = 52000;
//			if(sumGolds >= onGiftRunwayNeedGold){ //满足上礼物跑道的条件(1.送礼金币价值n个以上)
//				// 达到上跑道的条件,滚屏全站通知
//				flagOnGiftRunwayCondition = Boolean.TRUE;
//				notifyRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID; // 全站通知
//			}
//		}
//		content.put("isRunway", flagOnGiftRunwayCondition); // 是否上礼物跑道
//		if(!isRobt) {
//			UserInfoVo reciverVo = userCacheInfoService.getInfoFromCache(reciveUserId, null);
//			if(reciverVo != null) {
//				content.put("userName", reciverVo.getNickname());
//				content.put("goodCodeUrl", reciverVo.getGoodCodeLevelUrl());
//				content.put("userLevel", reciverVo.getUserLevel());
//				content.put("renqLevel", reciverVo.getRenqLevel());
//				content.put("userId", reciverVo.getUid());
//			}
//		} else {
//			RobotUserDo robot = robotService.getByUserId(reciveUserId);
//			if(robot != null) {
//				content.put("userName", robot.getNickName());
//				content.put("goodCodeUrl", "");
//				content.put("userLevel", "V0");
//				content.put("renqLevel", "R0");
//				content.put("userId", robot.getUserId());
//			}
//		}
//		content.put("giftId", sendGiftId);
//		content.put("num", sendGiftNum);
//		content.put("sumGolds", sumGolds);
//		content.put("flashId", gift.getShowType());//flashId
//		content.put("giftImg", Constants.cdnPath + Constants.GIFT_IMG_FILE_URI + "/" + gift.getImage());//礼物图片地址
//		content.put("giftType", gift.getGiftType());
//		//推送礼物im消息
//		try {
//			LogUtil.log.info(String.format("###begin,send_user_gift,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",senderUserId,sendGiftNum,sendGiftId,anchorRoomId,anchorUserId));
//			this.imSendComponent.sendUserGiftMsg(senderUserId,notifyRoomId, content);
//			LogUtil.log.info(String.format("###end,send_user_gift,senderUserId:%s,sendGiftId:%s,sendGiftNum:%s,imNotifyRoomId:%s,receiveAnchorUserId:%s",senderUserId,sendGiftId,sendGiftNum,anchorRoomId,anchorUserId));
//		} catch (SystemDefinitionException e1) {
//			LogUtil.log.error("###send_user_gift-filed,senduserid=" + senderUserId + ",reciveuserId=" + reciveUserId);
//			throw e1;
//		} catch (Exception e2) {
//			LogUtil.log.error("###send_user_gift-filed,senduserid=" + senderUserId + ",reciveuserId=" + reciveUserId);
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3098);
//			throw e;
//		}
//		
//		// 送礼成功,删除缓存：用户(真实用户的)包裹数
//		String cacheKeyRoomUserGiftPackageCache = MCPrefix.ROOM_USER_GIFT_PACKAGE_CACHE+senderUserId;
//		MemcachedUtil.delete(cacheKeyRoomUserGiftPackageCache);
//		
//		cacheKeyRoomUserGiftPackageCache = MCPrefix.ROOM_USER_GIFT_PACKAGE_CACHE+senderUserId;
//		// 缓存再加后缀,以区别于C9的缓存
//		cacheKeyRoomUserGiftPackageCache +="-packNew";
//		MemcachedUtil.delete(cacheKeyRoomUserGiftPackageCache);
//		
//		LogUtil.log.error("###doSendUserGiftBiz-senderUserId=" + senderUserId + ",sendGiftId=" + sendGiftId 
//				+ ",sendGiftNum=" + sendGiftNum + ",reciveUserId=" + reciveUserId + ",end!");
//	}
//	
//	/**
//	 * 处理用户对用户送礼
//	 * @param sender 送礼用户
//	 * @param reciveder 收礼用户
//	 * @param sendGiftId 礼物id
//	 * @param sendGiftNum 礼物个数
//	 * @param imNotifyRoomId 
//	 * @param isOnlySubGiftpackage
//	 * @param extraRate
//	 * @param isExtraAnchor
//	 * @throws Exception
//	 */
//	private void handleSendUserGiftBiz(UserAccount sender, UserAccount reciveder,boolean isRobt, String reciveUserId,int sendGiftId, int sendGiftNum,double extraRate) throws Exception {
//		String orderId = StrUtil.getOrderId();
//		Date now = new Date();
//		// 账户明细
//		String senderUserId = sender.getUserId();
//		long gold = sender.getGold();
//		String sendGiftBusinessId = StrUtil.getOrderId();//此次订单号 
//		UserAccountBook userAccountBook = new UserAccountBook();
//		userAccountBook.setGiftId(sendGiftId); // 账户明细，送礼礼物id
//		userAccountBook.setSendGiftNum(sendGiftNum); // 账户明细，用户送出礼物数量
//		userAccountBook.setUserId(senderUserId); // 账户明细，用户id
//		userAccountBook.setSourceId(sendGiftBusinessId); // 账户明细，送礼业务唯一订单号，sendGiftBusinessId
//		userAccountBook.setSourceDesc(USER_ACCOUNT_BOOK_SOURCEDEC);
//		userAccountBook.setRecordTime(new Date());
//		userAccountBook.setContent(GIFT_SENDER_CONMENT); // 账户明细，备注说明
//		int changeGolds = 0; // 账户明细，需要增、减的金币数
//		com.jiujun.shows.gift.domain.Gift gift = getGiftInfoFromCache(sendGiftId);
//		if(gift==null){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3048);
//			throw e;
//		}
//		changeGolds = sendGiftNum * gift.getPrice();
//		userAccountBook.setChangeGolds(-changeGolds);
//		
//		// 加用户经验
//		int giftEachUserPoint = gift.getUserPoint();
//		int addPoint2UserExp = (int) (giftEachUserPoint * sendGiftNum * extraRate);
//		this.addUserPoint(senderUserId, addPoint2UserExp);
//		
//		// 扣送礼用户金币
//		this.subtractGolds(senderUserId, changeGolds, null);
//		
//		// 账户明细,送礼后，剩余金币
//		userAccountBook.setPreRemainGolds(gold); // 账户明细,送礼前用户账户实际余额
//		UserAccount afterUserAccount = this.dao.getObjectByUserId(senderUserId);
//		userAccountBook.setSufRemainGolds(afterUserAccount.getGold());
//		
//		// 插入账户明细
//		this.userAccountBookService.insert(userAccountBook);
//		
//		// 送礼记录
//		PayGiftIn giftin = new PayGiftIn();
//		giftin.setOrderId(orderId);
//		giftin.setUserId(senderUserId);
//		giftin.setGiftId(sendGiftId);
//		giftin.setPrice(gift.getPrice());
//		giftin.setGold(changeGolds);
//		giftin.setNumber(sendGiftNum);
//		giftin.setResultTime(now);
//		giftin.setIsOnSale(0);
//		giftin.setReturnGold(0);
//		giftin.setRealGold(0);
//		this.payGiftInDao.insert(giftin);
//		
//		// 礼物送出记录
//		PayGiftOut out = new PayGiftOut();
//		out.setOrderId(orderId);
//		out.setUserId(senderUserId);
//		out.setToUserId(reciveUserId);
//		out.setGiftId(sendGiftId);
//		out.setNumber(sendGiftNum);
//		out.setResultTime(now);
//		out.setPrice(changeGolds);
//		out.setDiamond(0);
//		this.payGiftOutDao.insert(out);
//		
//		String reciveUser = "";
//		// 加收礼用户金币及记录
//		UserAccountBook reciverBook = new UserAccountBook();
//		int reciveGold = (int) (changeGolds * gift.getRate()); 
//		long preRemainGolds = 0;
//		long afterReciveGold = 0;
//		if(!isRobt) {
//			reciveUser = reciveder.getUserId();
//			this.addGolds(reciveUser, reciveGold, null);
//			// 加收礼用户人气
//			this.dao.updateRenqPoint(reciveUser, addPoint2UserExp);
//			
//			preRemainGolds = reciveder.getGold();
//			UserAccount afterReciveAccount = this.dao.getObjectByUserId(reciveUser);
//			afterReciveGold = afterReciveAccount.getGold();
//		} else {
//			reciveUser = reciveUserId;
//			this.robotAccountService.addGolds(reciveUser,reciveGold);
//		}
//		
//		// 收礼用户明细
//		reciverBook.setUserId(reciveUser); // 账户明细，用户id
//		reciverBook.setSourceId(sendGiftBusinessId); // 账户明细，送礼业务唯一订单号，sendGiftBusinessId
//		reciverBook.setSourceDesc(USER_ACCOUNT_BOOK_SOURCEDEC);
//		reciverBook.setRecordTime(new Date());
//		reciverBook.setContent(GIFT_RECIVEDER_CONMENT); // 账户明细，备注说明
//		reciverBook.setChangeGolds(reciveGold);
//		reciverBook.setPreRemainGolds(preRemainGolds);
//		
//		reciverBook.setSufRemainGolds(afterReciveGold);
//		this.userAccountBookService.insert(reciverBook);
//		
//		LogUtil.log.error("###handleSendUserGiftBiz-sendUserId=" + senderUserId + ",sendGold=" + changeGolds + ",reciverId=" + reciveUserId
//				+ ",reciveGold=" + reciveGold);
//	}
//}
