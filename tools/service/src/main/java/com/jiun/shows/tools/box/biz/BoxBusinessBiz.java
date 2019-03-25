package com.jiun.shows.tools.box.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.base.constant.SysConfTableEnum;
import com.jiujun.shows.base.domain.SysConf;
import com.jiujun.shows.base.service.ISysConfService;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.service.ISysCarService;
import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.enums.IMBusinessEnum.ImTypeEnum;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.IMutils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.LotteryUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.tools.box.domain.BoxGiftConf;
import com.jiujun.shows.tools.box.domain.BoxGiftGetRecord;
import com.jiujun.shows.tools.enums.GiftTypeBusinessEnum;
import com.jiujun.shows.tools.gift.domain.Gift;
import com.jiujun.shows.tools.pack.domain.UserPackageInHis;
import com.jiujun.shows.tools.pack.service.IUserPackageService;
import com.jiujun.shows.tools.tool.domain.Tool;
import com.jiujun.shows.tools.tool.service.IUserToolPackageService;
import com.jiujun.shows.tools.vo.ToolVo;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;
import com.jiun.shows.tools.box.dao.BoxGiftConfMapper;
import com.jiun.shows.tools.box.dao.BoxGiftGetRecordMapper;
import com.jiun.shows.tools.gift.dao.GiftMapper;
import com.jiun.shows.tools.pack.dao.UserPackageInHisMapper;
import com.jiun.shows.tools.tool.dao.ToolMapper;

/**
 * 宝箱服务实现
 * @author shao.xiang
 * @date 2017-08-20
 *
 */
@Service("boxBusinessBiz")
public class BoxBusinessBiz {

	@Resource
	private ToolMapper toolMapper;
	
	@Resource
	private GiftMapper giftMapper;
	
	@Resource
	private UserPackageInHisMapper userPackageInHisMapper;
	
	@Resource
	private IUserCarPortService userCarPortService;
	
	@Resource
	private BoxGiftGetRecordMapper boxGiftGetRecordMapper;
	
	@Resource
	private BoxGiftConfMapper boxGiftConfMapper;
	
	@Resource
	private IUserPackageService userPackageService;
	
	@Resource
	private IUserToolPackageService userToolPackageService;
	
	@Resource
	private ISysCarService sysCarService;
	
	@Resource
	private IUserInfoService userInfoService;

	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IUserAccountService userAccountService;

	@Resource
	private ISysConfService sysConfService;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	
	@Transactional(rollbackFor=Exception.class)
	public void openLevelBox(String userId, String roomId,String imToken,ToolVo toolsVo) throws Exception {
		int toolsType = GiftTypeBusinessEnum.key_5.getValue();//默认铜宝箱
		if (toolsVo!=null) {
			toolsType = toolsVo.getType();
		}
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(roomId) || StringUtils.isEmpty(imToken) || StringUtils.isEmpty(toolsType)) {
			Exception e = new SystemDefinitionException("openLevelBox("+userId+",("+roomId+","+imToken+"),"+toolsType+")",ErrorCode.ERROR_3017);
			LogUtil.log.error(e.getMessage(), e);
			throw e;
		}

		openLevelBoxWithSyn(userId, roomId,imToken,toolsType);

	}

	private void openLevelBoxWithSyn(String userId, String roomId ,String imToken,int toolsType)
			throws Exception {
		//synchronized (UserToolPackage.class){

			// 验证用户是否有宝箱(1.有则减宝箱;2.抛自定义异常,提示宝箱开完了)
			if (toolMapper.getUserToolNum(userId, GiftTypeBusinessEnum.valueOf("key_" + toolsType)) <= 0) {
				Exception e = new SystemDefinitionException("openLevelBoxWithSyn("+userId+",("+roomId+","+imToken+"),"+toolsType+")",ErrorCode.ERROR_3093);
				LogUtil.log.error(e.getMessage(), e);
				throw e;
			}
			
			LogUtil.log.info("宝箱id:"+GiftTypeBusinessEnum.valueOf("key_"+toolsType).getValue());
			// 减宝箱
			userToolPackageService.subPackageTool(userId,GiftTypeBusinessEnum.valueOf("key_"+toolsType), 1);
			//开宝箱是否免费(从db查询配置)
			boolean flagOpenBoxIsFree = checkOpenBoxIsFree();
			
			//如果没有配置为免费开宝箱,则减钥匙或金币
			if(!flagOpenBoxIsFree){
				// 检测用户是否有钥匙(1.有则减钥匙,2.若没有钥匙,检测用户账户的金币是否足够购买钥匙)
				if (toolMapper.getUserToolNum(userId, GiftTypeBusinessEnum.Key) <= 0) {
					userToolPackageService.useToolBySubGold(userId, GiftTypeBusinessEnum.Key);
				} else {
					// 扣除钥匙开宝箱
					userToolPackageService.subPackageTool(userId,GiftTypeBusinessEnum.Key, 1);
				}
			}
			
			//宝箱id
			int boxIdOfBronze = GiftTypeBusinessEnum.valueOf("key_" + toolsType).getValue();
			// 启动抽奖
			// 等级宝箱的奖品列表(按奖品概率升序)
			List<BoxGiftConf> listBoxGiftConfInAscSortByRate = boxGiftConfMapper.findBoxGiftConfInAscSortByRate(boxIdOfBronze);
			// 按概率升序排序
			sortAscByRate(listBoxGiftConfInAscSortByRate);
			// 调用抽奖方法,获取抽奖结果
			BoxGiftConf boxGiftOpen = boxLotteryDraw(listBoxGiftConfInAscSortByRate);
			int giftType = boxGiftOpen.getGiftType();
			int giftId = boxGiftOpen.getGiftId();
			int giftNum = boxGiftOpen.getNumber();
			String giftName = "";
			//宝箱奖品图片地址
			String prizeImageUrl = "";

			// 保存用户开启宝箱记录
			BoxGiftGetRecord bggr = new BoxGiftGetRecord();
			bggr.setAddTime(DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss",
					new Date()));
			bggr.setUserId(userId);
			bggr.setBoxGiftConfId(boxGiftOpen.getId());

			boxGiftGetRecordMapper.insert(bggr);

			String remark = "";
			// 分配奖品
			switch (giftType) {
				case 0: // 座驾
					SysCarDo sysCar = sysCarService.getObjectById(giftId);
					giftName = sysCar.getCarName();
					prizeImageUrl = SpringContextListener.getContextProValue("cdnPath", "") + BaseConstants.CAR_IMG_FILE_URI + "/" + sysCar.getImage();
					String commentForSysGiveCar = "开等级宝箱获得座驾";
					//userCarPortService.allotCar2UserTaskList(TaskTableEnum.TaskIdEnum.BOX_GIFT, userId, giftId, 5, null, commentForSysGiveCar);
					userCarPortService.addCar2User(userId, sysCar.getId());// 车入库
					break;
				case 1: // 礼物
					Gift gift = giftMapper.getObjectById(giftId);
					giftName = gift.getName();
					prizeImageUrl = SpringContextListener.getContextProValue("cdnPath", "") + BaseConstants.GIFT_IMG_FILE_URI + "/" +gift.getImage();
					boolean isPeroidPackage = false;// 非限制的背包
					Date packageEndTime = null;
					// 加包裹入账流水
					UserPackageInHis his = new UserPackageInHis();
					his.setUserId(userId);
					his.setGiftId(giftId);
					his.setInNum(giftNum);
					his.setRefId(String.valueOf(bggr.getId()));
					his.setRefDesc("表t_box_gift_getRecord");
					his.setComment("开宝箱");
					his.setRecordTime(new Date());
					userPackageInHisMapper.insert(his);
					
					// 账户明细
					UserAccountBook userAccountBook = new UserAccountBook();
					userAccountBook.setUserId(userId);
					userAccountBook.setGiftId(giftId);
					userAccountBook.setSourceId(bggr.getId() + "");
					userAccountBook.setSourceDesc("sourceId关联t_box_gift_getRecord表id");
					userAccountBook.setRecordTime(new Date());
					userAccountBook.setContent("开宝箱，获得蜜桃，加入包裹");
					userAccountBook.setChangeGiftNum(giftNum);
					
					// 加包裹
					userPackageService.addPackage(userId, giftId, giftNum,
							isPeroidPackage, packageEndTime, userAccountBook);
					break;
				case 3: // 大喇叭
					Tool toolHorn = toolMapper.getObjectById(giftId);
					giftName = toolHorn.getName();
					prizeImageUrl = SpringContextListener.getContextProValue("cdnPath", "") + BaseConstants.TOOL_IMG_FILE_URI + "/" + toolHorn.getImage();
					remark = "开宝箱，获得大喇叭";
					// 加toolpackage , 1为大喇叭
					//userToolPackageService.addSysPrizeForBox(userId, TaskTableEnum.TaskIdEnum.BOX_GIFT, GiftTypeBusinessEnum.Horn, giftId, giftNum, giftName, remark);
					// 大喇叭入工具箱
					userToolPackageService.save(userId, GiftTypeBusinessEnum.Horn, giftNum);
					break;
				case 4: // 钥匙
					Tool toolKey = toolMapper.getObjectById(giftId);
					giftName = toolKey.getName();
					prizeImageUrl = SpringContextListener.getContextProValue("cdnPath", "") + BaseConstants.TOOL_IMG_FILE_URI + "/" + toolKey.getImage();
					remark = "开宝箱，获得钥匙";
					// 加toolpackage , 2为钥匙
					//userToolPackageService.addSysPrizeForBox(userId, TaskTableEnum.TaskIdEnum.BOX_GIFT, GiftTypeBusinessEnum.Key, giftId, giftNum, giftName, remark);
					// 钥匙入工具箱
					userToolPackageService.save(userId, GiftTypeBusinessEnum.Key, giftNum);
					break;
				default:
					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3096);
					LogUtil.log.error(e.getMessage(), e);
					throw e;
			}
			//宝箱消息
			Tool tool = toolMapper.getObjectById(boxIdOfBronze);
			
			// 推送im消息
			UserInfoVo toUser = userCacheInfoService.getInfoFromCache(userId,roomId);
			
			String msg = String.format("恭喜 %s 开启了神奇%s，获得%s %s个",toUser.getNickname(),tool.getName(), giftName, giftNum);
			
			JSONObject content = new JSONObject();
			content.put("giftType", giftType);
			content.put("giftId", giftId);
			content.put("msg", msg);
			content.put("giftNum", giftNum);
			content.put("prizeImageUrl", prizeImageUrl);
			content.put("boxImageUrl", BaseConstants.cdnPath+BaseConstants.TOOL_IMG_FILE_URI+"/"+tool.getImage());//宝箱图片
			/*	content.put("preMsg", preMsg);
			content.put("behindMsg", behindMsg);*/
			
			content.put("userId", userId);
			content.put("userName", toUser.getNickname());
			content.put("userLevel", toUser.getUserLevel());
			content.put("boxType", toolsType);
			content.put("roomId", roomId);
			
			
			
			// 消息类型
			int imType = ImTypeEnum.IM_11001_baoxiang.getValue();;
			JSONObject imData = new JSONObject();
			imData.put("token", imToken);
			imData.put("msgtype", 2);
			//imData.put("targetid", roomId);
			imData.put("type", imType);
			imData.put("content", content);
			imData.put("to", toUser.getUid());

			//发送全站通知
			sendImMsg2AllRoom(imData);
			
			//删除用户-工具缓存
			String cacheKey = MCPrefix.USER_TOOL_PACKAGE_CACHE+userId;
			MemcachedUtil.delete(cacheKey);
		//}
	}

	/**
	 * 发送全站通知
	 * @param imData
	 * @throws Exception
	 */
	private void sendImMsg2AllRoom(JSONObject imData) {
		String roomId = null;
		try {
			String imToken;
			int funID = 11001;
			int seqID = 1;
			// 发送IM消息
			//发送全站通知
			String wholeSiteNoticeRoomId = BaseConstants.WHOLE_SITE_NOTICE_ROOMID;
			imData.put("targetid", wholeSiteNoticeRoomId);
			//imData.put("token", imToken);
			String systemUserIdOfIM  = BaseConstants.SYSTEM_USERID_OF_IM;
			IMutils.sendMsg2IM(funID, seqID, imData,systemUserIdOfIM);
		} catch (Exception e) {
			LogUtil.log.error("###开启宝箱，im消息通知失败,roomId="+roomId);
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	/**
	 * 按概率升序
	 * @param listBoxGiftConfInAscSortByRate
	 * @return
	 */
	private void sortAscByRate(List<BoxGiftConf> listBoxGiftConfInAscSortByRate) {
		if(listBoxGiftConfInAscSortByRate != null){
			//根据字段UserPoint排序
			Collections.sort(listBoxGiftConfInAscSortByRate, new Comparator<BoxGiftConf>() {
				
				public int compare(BoxGiftConf o1, BoxGiftConf o2) {
					return o1.getRate()-o2.getRate();
				}
			});	
		}	
	}

	/**
	 * 等级宝箱,抽奖方法
	 * 
	 * @param listBoxGiftConfInAscSortByRate
	 *            等级宝箱的奖品列表(按奖品概率升序)
	 * @return
	 * @throws Exception
	 */
	private BoxGiftConf boxLotteryDraw(
			List<BoxGiftConf> listBoxGiftConfInAscSortByRate) throws Exception {

		LogUtil.log.info("###boxGiftConfInSortList:"
				+ JsonUtil.arrayToJsonString(listBoxGiftConfInAscSortByRate));
		if (listBoxGiftConfInAscSortByRate == null
				|| listBoxGiftConfInAscSortByRate.size() <= 0) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
			LogUtil.log.error(e.getMessage(), e);
			throw e;
		}
		List rateList = new ArrayList();
		int totalRate = 0;
		for (BoxGiftConf p : listBoxGiftConfInAscSortByRate) {
			totalRate += p.getRate();
		}
		double tobeSumRate = 100; // 概率总和为100，即是100%
		if (totalRate != tobeSumRate) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3051);
			LogUtil.log.error(e.getMessage(), e);
			throw e;
		}
		for (BoxGiftConf p : listBoxGiftConfInAscSortByRate) {
			rateList.add(p.getRate() / tobeSumRate);// 转成double类型的概率
		}
		// 按设定的概率, 随机抽取蜜桃礼物
		int giftIndex = LotteryUtil.lottery(rateList);
		BoxGiftConf BoxGiftConf = listBoxGiftConfInAscSortByRate.get(giftIndex);
		return BoxGiftConf;
	}
	
	private boolean checkOpenBoxIsFree() throws Exception {
		boolean flag = false;
		String code = SysConfTableEnum.Code.OPENBOX.getValue();
		SysConf sysConf = null;
		ServiceResult<SysConf> srt = sysConfService.getByCode(code);
		if(srt.isSucceed()) {
			sysConf = srt.getData();
		}
		if(sysConf == null){
			return flag;
		}else{
			String confValue = sysConf.getConfValue();
			JSONObject jsonConf = JsonUtil.strToJsonObject(confValue);
			JSONObject jsonConfFree = jsonConf.getJSONObject("free");
			String beginTimeStr = jsonConfFree.getString("beginTime");
			String endTimeStr = jsonConfFree.getString("endTime");
			Date beginTime = DateUntil.parse2DefaultFormat(beginTimeStr);
			Date endTime = DateUntil.parse2DefaultFormat(endTimeStr);
			Date nowDate = new Date();
			if(nowDate.after(beginTime) && nowDate.before(endTime)){
				flag = true;
			}
		}
		return flag;
	}

}
