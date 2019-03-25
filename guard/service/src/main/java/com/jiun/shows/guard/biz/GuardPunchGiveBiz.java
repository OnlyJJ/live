package com.jiun.shows.guard.biz;


import org.springframework.stereotype.Service;



/**
 * 守护打卡业务
 * （暂时不用了，需要时再来实现这里的逻辑）
 * @author shao.xiang
 * @date 2017-06-26
 */
@Service("guardPunchGiveServiceImpl")
@Deprecated
public class GuardPunchGiveBiz {
//
//	@Resource
//	private GuardPunchGiveMapper guardPunchGiveMapper;
//	@Resource
//	private IGuardPunchGiveHisService guardPunchGiveHisService;
//	
//	@Resource
//	private IGuardPunchConfService guardPunchConfService;
//	
//	@Resource
//	private IGuardPayHisService guardPayHisService;
//	
//	@Resource
//	private IUserCarPortService userCarPortService;
//
//	@Resource
//	private IDecoratePackageService decoratePackageService;
//	
//	@Resource
//	private IGuardWorkService guardWorkService;
//	
//	@Resource
//	private IGuardConfService guardConfService;
//	
//	@Override
//	public void handleGuardRelateInfo(String userId, String roomId)
//			throws Exception {
//		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isEmptyOrWhitespaceOnly(roomId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		LogUtil.log.info("##### handleGuardRelateInfo-处理打卡相关业务，begin...userId="+userId + ",roomId=" + roomId);
////		synchronized(GuardPunchGive.class) {
//			//1 根据用户和房间，查询该房间内的所有守护信息
//			List<GuardPunchGive> list = this.getGuardPunchGiveData(userId, roomId);
//			if(list != null && list.size() >0) {
//				Date now = new Date();
//				// 1 从give表中拿出某一条记录
//				for(GuardPunchGive gp : list) {
//					// 2  拿到这条记录的守护日期类型（月度，季度，年度）
//					int type = gp.getType();
//					// 3 通过类型，获取该类型下的配置信息
//					LogUtil.log.error("####handleGuardRelateInfo type=(月度，季度，年度):"+type);
//					GuardPunchConf guardPunchConf = guardPunchConfService.getConfData(type);
//					if(guardPunchConf == null) {
//						LogUtil.log.error("####handleGuardRelateInfo 打卡奖励守护时间，punchConf没有配置");
//						continue;
//					}
//					// 4 配置是否启用
//					int status = guardPunchConf.getStatus();
//					if(status == 0) {
//						LogUtil.log.error("####handleGuardRelateInfo 打卡奖励守护时间，punchConf未启用");
//						continue;
//					}
//					// 5 获取配置的数据
//					int punchDay = guardPunchConf.getPunchday();
//					int prizeNum = guardPunchConf.getNumber();
//					// 6 查询奖品记录情况，判断是否再奖励
//					int giveId = gp.getId();
//					boolean isRecord = false;
//					LogUtil.log.error("####handleGuardRelateInfo,giveId="+giveId);
//					GuardPunchGiveHis gh = guardPunchGiveHisService.getGiveHisData(giveId);
//					if(gh != null) { 
//						int number = gh.getNumber();
//						LogUtil.log.error("####handleGuardRelateInfo,number >= prizeNum表示已经赠送，不再赠送，number="+number + ",prizeNum="+prizeNum);
//						if(number >= prizeNum) { // 已经送了，不重复送
//							LogUtil.log.error("###handleGuardRelateInfo 打卡送守护时长，已经赠送，不重复赠送");
//							continue;
//						}
//						isRecord = true;
//					}
//					// 7 当前时间，到截止时间为止，是否满足打卡次数
//					Date beginTime = null;
//					if(isRecord) {
//						beginTime = gh.getAddtime();
//					} else {
//						beginTime = gp.getBegintime();
//					}
//					Date endTime = gp.getEndtime(); 
//					int num = userPunchRecordService.getNumRecordData(userId, roomId, beginTime, endTime);
//					LogUtil.log.error("####handleGuardRelateInfo,userId="+userId + ",roomId=" + roomId 
//							+ "punchDay="+punchDay + ",prizeNum="+prizeNum +",num="+num 
//							+ ",beginTime=" + DateUntil.format2Str(beginTime, "yyyy-MM-dd HH:ss:mm") 
//							+ ",endTime=" + DateUntil.format2Str(endTime, "yyyy-MM-dd HH:ss:mm"));
//					if(num < punchDay) {
//						LogUtil.log.error("###handleGuardRelateInfo 打卡送守护时长，打卡次数未到，不予赠送");
//						continue;
//					}
//					// 8 通过购买记录id，获取守护基本信息
//					int payHisId = gp.getPayhisid();
//					GuardPayHis gph = guardPayHisService.getObjectById(payHisId);
//					if(gph == null) {
//						LogUtil.log.error("###handleGuardRelateInfo 打卡送守护时长，守护购买记录为空，数据错误");
//						continue;
//					}
//					int guardId = gph.getGuardid();
//					GuardConf gcf = guardConfService.getObjectById(guardId);
//					if(gcf == null) {
//						LogUtil.log.error("###handleGuardRelateInfo 打卡送守护时长，守护配置数据错误");
//						continue;
//					}
//					int guardType = gcf.getGuardType();
//					int priceType = gcf.getPriceType();
//					int carId = 0;
//					int decorateId = 0;
//					int isPeriod = 0;
//					boolean periodFlag = false;
//					isPeriod = gcf.getIsPeriod();
//					if(isPeriod == 0) {
//						periodFlag = false;
//					}
//					if(guardType == GuardTableEnum.GuardCarType.baiyin.getValue()) {
//						carId = GuardTableEnum.CarId.baiyin.getValue();
//						decorateId = DecorateTableEnum.Id.ShouhuOneDec.getValue();
//					} else if(guardType == GuardTableEnum.GuardCarType.huangjin.getValue()) {
//						carId = GuardTableEnum.CarId.huangjin.getValue();
//						decorateId = DecorateTableEnum.Id.ShouhuTwoDec.getValue();
//					}
//					int prizesDays = guardPunchConf.getPrizesday();
//					int workId = gp.getWorkId();
//					boolean isContinue = true;
//					// 加时间
//					guardWorkService.addOrUpdateWorkHis(userId, roomId, workId, guardId, isPeriod, prizesDays,isContinue);
//					// 赠送座驾
//					userCarPortService.addCar2User(userId, carId, guardType, priceType,isContinue);
//					// 赠送勋章
//					decoratePackageService.addPackage(userId, roomId, decorateId, periodFlag, 1, prizesDays, guardType);
//					
//					if(isRecord) {
//						// 更新赠送历史记录
//						gh.setAddtime(now);
//						gh.setNumber(gh.getNumber() + 1);
//						guardPunchGiveHisService.update(gh);
//					} else {
//						// 插入赠送历史新记录
//						GuardPunchGiveHis gpgh = new GuardPunchGiveHis();
//						gpgh.setPunchgiveid(giveId);
//						gpgh.setNumber(1);
//						gpgh.setAddtime(now);
//						guardPunchGiveHisService.insert(gpgh);
//					}
//				}
//				
//				// 清理下缓存
//				try {
//					guardPayHisService.clearCache(userId, roomId);
//				} catch(Exception e) {
//					LogUtil.log.error("#### 清理缓存失败");
//				}
//				LogUtil.log.info("##### handleGuardRelateInfo-处理打卡相关业务，end...");
//			} else {
//				LogUtil.log.error("### handleGuardRelateInfo, 打卡送守护时长，用户不是守护，不处理相关业务");
//			}
////		}
//	}
//
//	@Override
//	public void save(int payHisId, int workId, int priceType, String userId, String roomId,
//			int validate) throws Exception {
//		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isEmptyOrWhitespaceOnly(roomId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		synchronized(GuardPunchGive.class) {
//			Date now = new Date();
//			GuardPunchGive vo = new GuardPunchGive();
//			vo.setPayhisid(payHisId);
//			vo.setWorkId(workId);
//			vo.setUserid(userId);
//			vo.setRoomid(roomId);
//			vo.setBegintime(now);
//			vo.setType(priceType);// priceType 购买守护时，价格类型，1:月、2:季、3:年
//			vo.setEndtime(DateUntil.addDatyDatetime(now, validate));
//			this.dao.insert(vo);
//		}
//		
//	}
//
//	@Override
//	public List<GuardPunchGive> getGuardPunchGiveData(String userId,
//			String roomId) throws Exception {
//		if(StringUtils.isNullOrEmpty(userId) || StringUtils.isEmptyOrWhitespaceOnly(roomId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		return this.dao.getGuardPunchGiveData(userId, roomId);
//	}
}
