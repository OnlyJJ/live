//package com.jiujun.shows.user.service.impl;
//
//import java.util.Date;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.jiujun.shows.car.service.IUserCarPortService;
//import com.jiujun.shows.common.constant.ErrorCode;
//import com.jiujun.shows.common.exception.SystemDefinitionException;
//import com.jiujun.shows.common.service.impl.CommonServiceImpl;
//import com.jiujun.shows.common.utils.LogUtil;
//import com.jiujun.shows.room.service.IDecoratePackageService;
//import com.jiujun.shows.tool.service.IUserToolPackageService;
//import com.jiujun.shows.user.dao.IUserAccountBookDao;
//import com.jiujun.shows.user.domain.UserAccountBook;
//import com.jiujun.shows.user.service.IUserAccountBookService;
//import com.jiujun.shows.user.service.IUserAccountService;
//import com.jiujun.shows.user.service.IUserPackageService;
//
//
///**
// * Service -用户账户(金币、钻石)加减流水记录汇总 
// */
//@Service("userAccountBookServiceImpl")
//public class UserAccountBookServiceImpl extends CommonServiceImpl<IUserAccountBookDao, UserAccountBook> implements IUserAccountBookService{
//
//	@Resource
//	public void setDao(IUserAccountBookDao dao) {
//		this.dao = dao;
//	}
//	
//	@Resource
//	private IUserPackageService userPackageService;
//	
//	@Resource
//	private IDecoratePackageService decoratePackageService;
//	
//	@Resource
//	private IUserToolPackageService userToolPackageService;
//	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
//	@Resource
//	private IUserAccountService userAccountService;
//	
//	@Override
//	public void givePrizeAndRecordBook(String userId, int prizeType, int prizeId,boolean isPeriod,int num,int golds,
//			int validate,int isAccumulation,String content,String sourceId, String sourceDesc,String sourceKey,String desc) throws Exception {
//		if(StringUtils.isEmpty(userId)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			LogUtil.log.error(e.getMessage(),e);
//			throw e;
//		}
//		Date now = new Date();
//		// 账户明细
//		UserAccountBook userAccountBook = new UserAccountBook();
//		userAccountBook.setUserId(userId);
//		userAccountBook.setGiftId(prizeId);
//		userAccountBook.setRecordTime(now);
//		if(StringUtils.isNotEmpty(content)) {
//			userAccountBook.setContent(content);
//		}
//		if(StringUtils.isNotEmpty(content)) {
//			userAccountBook.setSourceId(sourceId);
//		}
//		if(StringUtils.isNotEmpty(content)) {
//			userAccountBook.setSourceDesc(sourceDesc);
//		}
//		
//		switch(prizeType) {
//		case 0: // 金币
//			LogUtil.log.info("### givePrizeForPay-发放物品:金币,userId="+userId+ ",golds=" + golds);
//			userAccountBook.setChangeGolds(golds);
//			userAccountService.addGolds(userId, golds, userAccountBook);
//		case 1: // 礼物
//			LogUtil.log.info("### givePrizeForPay-发放物品:礼物,userId="+userId+ ",giftId=" + prizeId + ",num="+num);
//			userAccountBook.setChangeGiftNum(num);
//			userPackageService.addPackage(userId, prizeId, num, isPeriod, now,sourceId, sourceDesc ,content ,userAccountBook);
//			break;
//		case 2: // 座驾
//			LogUtil.log.info("### givePrizeForPay-发放物品:座驾,userId="+userId+ ",carId=" + prizeId);
//			userCarPortService.addCar2User(userId, prizeId);
//			break;
//		case 3:  // 勋章
//			LogUtil.log.info("### givePrizeForPay-发放物品:勋章,userId="+userId+ ",decorateId=" + prizeId + ",validate="+validate);
//			decoratePackageService.addPackage(userId, null, prizeId, isPeriod, num, validate, sourceKey, desc, isAccumulation);
//			break;
//		case 4: // 工具
//			LogUtil.log.info("### givePrizeForPay-发放物品:宝箱,userId="+userId+ ",toolId=" + prizeId + ",num=" + num);
//			userToolPackageService.addToolPackage(userId, prizeId, num);
//			break;
//		default :
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3106);
//			throw e;
//		}
//		
//	}
//}
