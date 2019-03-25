package com.jiujun.shows.user.biz;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.jiujun.shows.account.domain.UserAccount;
import com.jiujun.shows.account.service.IUserAccountService;
//import com.jiujun.shows.car.domain.SysCarDo;
//import com.jiujun.shows.car.domain.UserCarPortDo;
//import com.jiujun.shows.car.service.ISysCarService;
//import com.jiujun.shows.car.service.IUserCarPortService;
//import com.jiujun.shows.charge.service.IPayGiftOutService;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.constant.MCTimeoutConstants;
//import com.jiujun.shows.common.enums.table.DecorateTableEnum;
//import com.jiujun.shows.common.enums.table.MessageFontColorEnum;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.common.utils.SpringContextListener;
import com.jiujun.shows.user.constant.Constants;
//import com.jiujun.shows.guard.service.IGuardConfService;
//import com.jiujun.shows.guard.service.IGuardWorkService;
//import com.jiujun.shows.pay.service.IPayChargeOrderService;
//import com.jiujun.shows.province.service.IProvinceService;
//import com.jiujun.shows.robot.dao.RobotUserAccountDao;
//import com.jiujun.shows.robot.dao.RobotUserDao;
//import com.jiujun.shows.robot.domain.RobotUserAccountDo;
//import com.jiujun.shows.robot.domain.RobotUserDo;
//import com.jiujun.shows.room.domain.Decorate;
//import com.jiujun.shows.room.service.IDecorateService;
//import com.jiujun.shows.room.service.RoomBannedOperationService;
//import com.jiujun.shows.user.domain.Medal;
import com.jiujun.shows.user.domain.UserAnchor;
import com.jiujun.shows.user.domain.UserInfo;
//import com.jiujun.shows.user.domain.UserPunchIntimacy;
//import com.jiujun.shows.user.domain.UserRoomMembers;
import com.jiujun.shows.user.enums.UserInfoVoEnum;
//import com.jiujun.shows.user.service.IMedalService;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.service.IUserInfoService;
//import com.jiujun.shows.user.service.IUserPunchIntimacyService;
//import com.jiujun.shows.user.service.IUserRoomMembersService;
import com.jiujun.shows.user.vo.RoomRoleRelCacheInfo;
import com.jiujun.shows.user.vo.UserCacheInfo;
import com.jiujun.shows.user.vo.UserInfoVo;

@Service
public class UserCacheInfoServiceBiz implements IUserCacheInfoService {
	//靓号图片路径
	private static final String GOOD_CODE_ICON = SpringContextListener.getContextProValue("cdnPath", "") +"/" + BaseConstants.GOOD_CODE_ICON_URL + "/";
	//真爱图片路径
	private static final String LOVE_LEVEL_ICON = SpringContextListener.getContextProValue("cdnPath", "") +"/" + BaseConstants.LOVE_LEVEL_ICON_URL + "/";
	
	

	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private IUserAnchorService userAnchorService;
	
//	@Autowired
//	private IUserRoomMembersService userRoomMembersService;
//	
//	@Autowired
//	private RoomBannedOperationService roomBannedOperationService;
//	
//	@Resource
//	private RobotUserDao robotUserDao ;
//	
//	@Resource
//	private RobotUserAccountDao robotUserAccountDao;
//	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
//	@Resource
//	private IDecorateService decorateService;
//	
//	@Resource
//	private IGuardWorkService guardWorkService;
//	
//	@Resource
//	private IGuardConfService guardConfService;
//	
//	@Resource
//	private IPayChargeOrderService payChargeOrderService;
//	
//	@Resource
//	private IProvinceService iProvinceService;
//	
//	@Resource
//	private IPayGiftOutService payGiftOutService;
//	
//	@Resource
//	private ISysCarService sysCarService;
//	
//	@Resource
//	private IUserPunchIntimacyService userPunchIntimacyService;
//	
//	@Resource
//	private IMedalService medalService;
	
	/** 座驾图片地址 */
	private static final String CAR_CDN_URL = SpringContextListener.getContextProValue("cdnPath", "") + BaseConstants.CAR_IMG_FILE_URI + File.separator;
	/**
	 * 跟新聊天的用户信息到memcache
	 * @param vo
	 * @param timeoutSecond 秒
	 * @throws Exception
	 */
	private  UserCacheInfo updateUserInfoInCache(String uid) throws Exception{
		if(StringUtils.isEmpty(uid)){
			throw new Exception("###parameter error: uid can't be empty.");
		} 
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + uid;
		
		//先删除cache
		removeFromCache(userCacheKey);
		
		UserCacheInfo userCacheInfo = new UserCacheInfo();
		UserInfo  userInfo = null;
		UserAccount userAccount = null;
		String robotUserIdPre = SpringContextListener.getContextProValue("user.robot.userid.pre", "robot");
		LogUtil.log.info("###uid:"+uid+",robotUserIdPre:"+robotUserIdPre);
		
		//游客用户userId前缀
		String visitorUserIdPreStr = Constants.PSEUDO_LOGIN_SESSION_KEY;
		
		if(uid.indexOf(visitorUserIdPreStr) == -1 ){//非游客
			if(uid.indexOf(robotUserIdPre) == -1 ){//userId中没包含有机器人的userId前缀，说明是注册用户
				  
				  //禁用主播等级图标显示处理
				  userCacheInfo = IsLevelIconShow(uid, userCacheInfo);
				
				  userInfo  = userInfoService.getUserByUserId(uid);
				  userAccount  = userAccountService.getObjectByUserId(uid);
				  //获取用户勋章
				  List userDecorateJsonArr = new ArrayList();
				 // List<Decorate> userDecorateList = decorateService.findListOfCommonUser(uid);
				//获取用户已佩戴勋章
				// my-todo
//				  List<Decorate> userDecorateList = decorateService.findHasAdornListOfCommonUser(uid);
//				  if(userDecorateList != null && userDecorateList.size() > 0){
//					  for(Decorate d:userDecorateList){
//						  // 是否有聊天消息颜色特权
//						  if(d.getId() == DecorateTableEnum.Id.YunqiWang1.getValue()
//								  || d.getId() == DecorateTableEnum.Id.YunqiWang2.getValue()
//								  || d.getId() == DecorateTableEnum.Id.YunqiWang3.getValue()) {
//							  // 设置特殊颜色
//							  userCacheInfo.setFontColor(MessageFontColorEnum.Color.Red.getColor());
//						  }
//						  JSONObject jo = new JSONObject();
//						  jo.put("id", d.getId());
//						  jo.put("imgUrl", SpringContextListener.getContextProValue("cdnPath", "") + Constants.DECORATE_IMG_FILE_URL + "/" + d.getLightenimg());
//						  userDecorateJsonArr.add(jo);
//					  }
//				  }
//				  List anchorDecorateJsonArr = new ArrayList();
//				  List<Decorate> anchorDecorateList = decorateService.findListOfAnchor(uid);
//				  if(anchorDecorateList != null && anchorDecorateList.size() > 0){
//					  for(Decorate d:anchorDecorateList){
//						  if(d.getResource() ==1 || d.getLightenimg() == null) { // 过滤主播的勋章，1. resource =1，摘蜜桃获得，2.没有配置图片的勋章
//							  continue;
//						  }
//						  JSONObject jo = new JSONObject();
//						  jo.put("id", d.getId());
//						  jo.put("imgUrl", SpringContextListener.getContextProValue("cdnPath", "") + Constants.DECORATE_IMG_FILE_URL + "/" + d.getLightenimg());
//						  anchorDecorateJsonArr.add(jo);
//					  }
//				  }
//				  userCacheInfo.setAnchorDecorateList(anchorDecorateJsonArr);
//				  userCacheInfo.setUserDecorateList(userDecorateJsonArr);
//				  
//				  // 充值记录
//				  int successTimes = payChargeOrderService.getPayCountByUser(uid);
//				  userCacheInfo.setPayCount(successTimes);
//				  
				  // 真是身份
				  int identity = 0; // 默认为普通用户
				  UserAnchor userAnchor  = userAnchorService.getNormalAnchor(uid);
				  if(userAnchor != null) {
					  identity = 1;
				  }
				  userCacheInfo.setIdentity(identity);
				// my-todo
				  //设置用户徽章
//				  List<JSONObject> medalList = medalService.setUserMedal(uid);
//				  userCacheInfo.setUserBadgeList(medalList);
				  
			}else{  // 机器人
				// my-todo
				userInfo = new UserInfo();
//				RobotUserDo robotDo  = robotUserDao.getByUserId(uid);
			//	LogUtil.log.info("###robotDo:"+JsonUtil.beanToJsonString(robotDo));
//				BeanUtils.copyProperties(userInfo, robotDo);
				
				userAccount = new UserAccount();
//				RobotUserAccountDo robotUserAccountDo  = this.robotUserAccountDao.getByUserId(uid);
//				BeanUtils.copyProperties(userAccount, robotUserAccountDo);
			}
		}
		userCacheInfo.setUid(uid);
		
		if(userInfo!=null){
			userCacheInfo.setChannelId(userInfo.getChannelId());
			userCacheInfo.setNickname(userInfo.getNickName());
			//拼接头像的完整url,再存到cache
			StringBuffer avatarSbf = new StringBuffer();
			avatarSbf.append(SpringContextListener.getContextProValue("cdnPath", ""))
			.append("/")
			.append(BaseConstants.ICON_IMG_FILE_URI)
			.append("/")
			.append(userInfo.getIcon());
			userCacheInfo.setAvatar(avatarSbf.toString());
			
			//设置靓号等级
			// 设置靓号等级图片url
			if(userInfo.getGoodCodeLevel() > 0){
				//靓号升级处理
				int curCoodCodeLevel = updateGoodCode(userInfo.getUserId());
				userCacheInfo.setGoodCodeLevel(userInfo.getGoodCodeLevel());
				//显示红色风格靓号
				userCacheInfo.setGoodCodeLevelUrl(GOOD_CODE_ICON+"red_"+curCoodCodeLevel+".png");
				//设置当前靓号类型的等级
				userCacheInfo.setCurGoodCodeLevel(curCoodCodeLevel);
			}
			
		}
		
		if(userAccount!=null){
			userCacheInfo.setAnchorLevel(userAccount.getAnchorLevel());
			userCacheInfo.setUserLevel(userAccount.getUserLevel());
			userCacheInfo.setRenqLevel(userAccount.getRenqLevel());
		}
		// my-todo
		//设置正在使用的座驾的carId
//		UserCarPortDo  d  =  userCarPortService.getInUseUserCarPort(uid);
//		if(d!=null){
//			userCacheInfo.setCarId(d.getCarId());
//			SysCarDo car = this.sysCarService.getObjectById(d.getCarId());
//			userCacheInfo.setCarName(car.getCarName());
//			userCacheInfo.setCarImg(CAR_CDN_URL + car.getImage());
//		}
		
		
		//重新保存到cache
		MemcachedUtil.set(userCacheKey, userCacheInfo, MCTimeoutConstants.USERCACHEINFO_TIMEOUTSECOND);
		return userCacheInfo;
	}
	

	/**
	 * 从memcache获取聊天的用户基本信息(缓存没有,则查询db后放到缓存)
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	@Override
	public  UserCacheInfo getOrUpdateUserInfoFromCache(String uid) throws Exception{
		if(StringUtils.isEmpty(uid)){
			throw new Exception("###parameter error: uid can't be empty.");
		}
		LogUtil.log.info("###uid:"+uid);
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + uid;
		UserCacheInfo vo = null;
		Object object = MemcachedUtil.get(userCacheKey);
//		LogUtil.log.info("###uid:"+uid+",obj:"+object); 
		if(object != null){
			vo = (UserCacheInfo)object;
		}else{
			//如果memcache查到为null，则先取出来，放到memcache中   
			
			vo =  updateUserInfoInCache(uid);
		}
		return vo;
	}
	
	/**
	 * memcache中移除
	 * @param id
	 */
	private  void removeFromCache(String id) throws Exception{
		MemcachedUtil.delete(id);
	}
	
	/**
	 * @param uid
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserInfoVo getInfoFromCache(String uid,String roomId) throws Exception{
		if(StringUtils.isEmpty(uid)){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
		
		UserInfoVo userInfoVo = new UserInfoVo();
		//先给类型默认值为普通用户
		userInfoVo.setType(UserInfoVoEnum.Type.CommonUser.getValue());
		//游客用户userId前缀
		String visitorUserIdPreStr = Constants.PSEUDO_LOGIN_SESSION_KEY;
		
		if(uid.indexOf(visitorUserIdPreStr) == -1 ){//非游客
			//发送系统通知的用户信息
			String systemUserId = SpringContextListener.getContextProValue("system.user.userId", "100000");
			if(systemUserId.equals(uid)){
				String systemUserNickName = "系统公告";
				userInfoVo.setType(UserInfoVoEnum.Type.OfficialUser.getValue()); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客 5官方人员（权限最高）
				userInfoVo.setLevel("V20");
				userInfoVo.setForbidSpeak(false);
				userInfoVo.setForceOut(false);
				userInfoVo.setUid(uid);
				userInfoVo.setNickname(systemUserNickName);
				userInfoVo.setUserLevel("V30"); //设置默认的V20,避免im端对等级低的截取长度
				userInfoVo.setAnchorLevel("S0"); //设置默认的S20
				userInfoVo.setIfOfficialUser(true);
			}else{
				UserCacheInfo userCacheInfo = getOrUpdateUserInfoFromCache(uid);
				
				//设置禁用主播是否显示主播等级图标
				if(userCacheInfo != null){
					userInfoVo.setAnchorLevelIcon(userCacheInfo.getIsLevelIcon());
				}
				
				// 获取用户所有房间的守护信息，缓存12h，如果有用户购买守护时，则删除该缓存
				List guardList = getGuardListAllCache(uid);
				if(guardList != null && guardList.size() > 0) {
					userInfoVo.setRoomGuard(true);
				} else {
					userInfoVo.setRoomGuard(false);
				}
				userInfoVo.setGuardList(guardList);
				
				// 用户充值次数
				userInfoVo.setPayCount(userCacheInfo.getPayCount());
				
				boolean isAnchor = false;
				//LogUtil.log.info("#####userCacheInfo:"+JsonUtil.beanToJsonString(userCacheInfo));
				//roomId不为空(即表示在房间内)
				if(StringUtils.isNotEmpty(roomId)){
					RoomRoleRelCacheInfo roomRoleRelCacheInfo = getRoomRelMsgVoFromCache(roomId);
					if(uid.equals(roomRoleRelCacheInfo.getAnchorUserId())){//主播
						userInfoVo.setType(UserInfoVoEnum.Type.Anchor.getValue());
						//vo.setLevel(userCacheInfo.getAnchorLevel());
						userInfoVo.setLevel(userCacheInfo.getUserLevel());
						isAnchor = true;
					}else if(roomRoleRelCacheInfo.getRoomAdminUserIds().contains(uid)){  //房管
						
						userInfoVo.setType(UserInfoVoEnum.Type.RoomMgr.getValue());
						userInfoVo.setLevel(userCacheInfo.getUserLevel());
						// my-todo
//						boolean isForbidSpeak = roomBannedOperationService.getIfBannedOperation(uid, roomId, 0);
//						boolean isForceOut = roomBannedOperationService.getIfBannedOperation(uid, roomId, 1);
//						userInfoVo.setForbidSpeak(isForbidSpeak);
//						userInfoVo.setForceOut(isForceOut);
					}else{//普通用户
						userInfoVo.setType(UserInfoVoEnum.Type.CommonUser.getValue());
						userInfoVo.setLevel(userCacheInfo.getUserLevel());
						// my-todo
//						boolean isForbidSpeak = roomBannedOperationService.getIfBannedOperation(uid, roomId, 0);
//						boolean isForceOut = roomBannedOperationService.getIfBannedOperation(uid, roomId, 1);
//						userInfoVo.setForbidSpeak(isForbidSpeak);
//						userInfoVo.setForceOut(isForceOut);
					}
				}else{//私聊(无需设置type的值)
					userInfoVo.setLevel(userCacheInfo.getUserLevel());
				}
				userInfoVo.setUid(uid);
				userInfoVo.setNickname(userCacheInfo.getNickname());
				userInfoVo.setAvatar(userCacheInfo.getAvatar());
				if(StringUtils.isEmpty(userInfoVo.getLevel())){
					userInfoVo.setLevel("0");
				}
				userInfoVo.setCarId(userCacheInfo.getCarId());
				userInfoVo.setCarName(userCacheInfo.getCarName());
				userInfoVo.setCarImg(userCacheInfo.getCarImg());
				userInfoVo.setChannelId(userCacheInfo.getChannelId());
				userInfoVo.setUserLevel(userCacheInfo.getUserLevel());
				userInfoVo.setAnchorLevel(userCacheInfo.getAnchorLevel());
				if(isAnchor) {
					userInfoVo.setUserDecorateList(userCacheInfo.getAnchorDecorateList());
				} else {
					userInfoVo.setUserDecorateList(userCacheInfo.getUserDecorateList());
				}				
				//设置是否官方人员的标志 
				String officialUsers = SpringContextListener.getContextProValue("official.users", "");
				if(StringUtils.isNotEmpty(officialUsers)&&officialUsers.indexOf(uid)!=-1){
					userInfoVo.setIfOfficialUser(true);
					userInfoVo.setType(UserInfoVoEnum.Type.OfficialUser.getValue()); // 官方人员标识
				}
				
				// 设置消息字体颜色
				if(userCacheInfo.getFontColor() != null) {
					userInfoVo.setFontColor(userCacheInfo.getFontColor());
				}
				
				// 设置靓号等级
				userInfoVo.setGoodCodeLevel(userCacheInfo.getGoodCodeLevel());
				// 设置靓号等级图片url
				userInfoVo.setGoodCodeLevelUrl(userCacheInfo.getGoodCodeLevelUrl());
				// 设置当前靓号类型等级
				userInfoVo.setCurGoodCodeLevel(userCacheInfo.getCurGoodCodeLevel());
				
				//设置真爱等级图标
				String loveiconUrl = handleFansIcon(uid,roomId);
				userInfoVo.setFansLevelIcon(loveiconUrl);

				// 设置用户的真是身份
				userInfoVo.setIdentity(userCacheInfo.getIdentity());
				
				// 人气等级
				userInfoVo.setRenqLevel(userCacheInfo.getRenqLevel());
				
				//设置用户佩戴的徽章
				userInfoVo.setUserBadgeList(userCacheInfo.getUserBadgeList());
				
				
			}
		}else{//游客
			String pesudoUserName = this.getAndSetPesudoUserName(uid,null);
			//拼接头像的完整url,再存到cache
			StringBuffer avatarSbf = new StringBuffer();
			avatarSbf.append(SpringContextListener.getContextProValue("cdnPath", ""))
			.append("/")
			.append(BaseConstants.ICON_IMG_FILE_URI)
			.append("/")
			.append(BaseConstants.USER_DEFAULT_ICON);
			userInfoVo.setType(UserInfoVoEnum.Type.Visitor.getValue()); // 发送者类型  1:主播，2:普通用户，3:房管  4:游客
			userInfoVo.setLevel("V0");
			userInfoVo.setForbidSpeak(false);
			userInfoVo.setForceOut(false);
			userInfoVo.setUid(uid);
			userInfoVo.setNickname(pesudoUserName);
			userInfoVo.setAvatar(avatarSbf.toString());
			userInfoVo.setUserLevel("V0"); //游客设置默认的V0
			userInfoVo.setAnchorLevel("S0"); //游客设置默认的S0
		}
		
		
		
		// LogUtil.log.info("#####userRoomInfo:"+JsonUtil.beanToJsonString(userInfoVo));
		return userInfoVo;
	}

	


	/**
	 * 从memcache获取房间信息
	 * @param roomId
	 * @return
	 * @throws Exception 
	 */
	private  RoomRoleRelCacheInfo getRoomRelMsgVoFromCache(String roomId) throws Exception {
		if(StringUtils.isEmpty(roomId)){
			throw new Exception("###parameter error: roomId can't be empty.");
		}
		String roomCacheKey = MCPrefix.ROOMCHEINFO_PREKEY + roomId;
		RoomRoleRelCacheInfo roomRoleRelCacheInfo = null;
		Object object = MemcachedUtil.get(roomCacheKey);
		if(object != null ){
			roomRoleRelCacheInfo = (RoomRoleRelCacheInfo)object;
		}else{
			//如果memcache查到为null，则先取出来，放到memcache中
			roomRoleRelCacheInfo =   updateRoomRelMsgInCache(roomId);
		}
//		LogUtil.log.info("###RoomRoleRelCacheInfo:"+JsonUtil.beanToJsonString(roomRoleRelCacheInfo));
		return roomRoleRelCacheInfo;
	}

	/**
	 * 跟新房间-角色信息到cache
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	private  RoomRoleRelCacheInfo updateRoomRelMsgInCache(String roomId) throws Exception {
		if(StringUtils.isEmpty(roomId)){
			throw new Exception("###parameter error: roomId can't be empty.");
		}
		String roomCacheKey = MCPrefix.ROOMCHEINFO_PREKEY + roomId;
		removeFromCache(roomCacheKey);
		
		//根据房间id,获取相应的主播信息(userId)
		UserAnchor userAnchor = userAnchorService.getUserAnchorByRoomId(roomId);
		
		RoomRoleRelCacheInfo roomRoleRelCacheInfo = new RoomRoleRelCacheInfo();
		if(userAnchor!=null){
			roomRoleRelCacheInfo.setAnchorUserId(userAnchor.getUserId());
			roomRoleRelCacheInfo.setRoomId(userAnchor.getRoomId());
		}
		// my-todo
		//获取房管列表
//		List<UserRoomMembers> userRoomMembersList = this.userRoomMembersService.findRoomAdmin(roomId); 
		
		List roomAdminUserIdList = new ArrayList();
		// my-todo
//		
//		if(userRoomMembersList!=null){
//			for(UserRoomMembers userRoomMembers:userRoomMembersList){
//				String userId = userRoomMembers.getUserId();
//				roomAdminUserIdList.add(userId);
//			}
//			
//		}
		
		roomRoleRelCacheInfo.setRoomAdminUserIds(roomAdminUserIdList);
		
		MemcachedUtil.set(roomCacheKey, roomRoleRelCacheInfo, MCTimeoutConstants.USERCACHEINFO_TIMEOUTSECOND);
		return roomRoleRelCacheInfo;
	}

	@Override
	public void removeUserCacheInfo(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
		String userCacheKey = MCPrefix.USERCACHEINFO_PREKEY + userId;
		MemcachedUtil.delete(userCacheKey);
	}

	@Override
	public String getAndSetPesudoUserName(String userId,String ip)throws Exception {
		if(StringUtils.isEmpty(userId)){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
		String pesudoUserName = null;
		String key = MCPrefix.PESUDOUSERNAME+userId;
		Object userObj = MemcachedUtil.get(key);
		if(userObj == null){
			Random rnd = new Random();
			long time = System.currentTimeMillis(); 
			String timeStr= time+"";
			int rndInt = rnd.nextInt(100);
			String seq = null;
			if(rndInt>=10){
				seq= timeStr.substring(4,9)+rndInt;
			}else{
				seq= timeStr.substring(4,9)+"0"+rndInt;
			}
			
			if(seq.startsWith("0")){
				seq = 1+seq.substring(1, seq.length());
			}
			
			String region = "火星";//ip归属地(如：广东)
			// my-todo
//			region =iProvinceService.getProviceBy(ip);
			/*if (!StrUtil.isNullOrEmpty(ip)) {
				String[] ip_split = ip.split("\\.");
				String shortIp = ip_split[0] + "." + ip_split[1] + "." + ip_split[2];//取前三段ip缓存
				String ipKey = MCPrefix.USER_IP_REGION_CACHE+shortIp;
				Object regionObj = MemcachedUtil.get(ipKey);
				if (regionObj == null) {
					LogUtil.log.info("###getAndSetPesudoUserName select");
					//开条线程根据ip获取归属地省id并缓存
					RegionThread thread = new RegionThread(ip);
					thread.start();
				}else {
					//根据province_code_+code获取省市信息
					String code = regionObj.toString();
					LogUtil.log.info(String.format("###getAndSetPesudoUserName cache cacheKey=%s,code=%s",ipKey,code));
					code = code.substring(0, 2)+"0000";//获取省级
					String codeKey = MCPrefix.PROVINCE_CODE_CACHE+code;
					regionObj = MemcachedUtil.get(codeKey);
					LogUtil.log.info(String.format("###getAndSetPesudoUserName cache cacheCodeKey=%s,regionObj=%s",codeKey,regionObj));
					if (regionObj!=null) {
						JSONObject json = null;
						try {
							json = JSONObject.fromObject(regionObj);
							region = json.getString("region");
							if (!StrUtil.isNullOrEmpty(region)) {
								//省份过滤（省 市  特别行政区   壮族自治区   回族自治区 维吾尔自治区   自治区几个关键字）
								region = region.replaceAll("(?:省|市|特别行政区|壮族自治区|回族自治区|维吾尔自治区|自治区)", "");
							}
						} catch (Exception e) {
							LogUtil.log.error("###getAndSetPesudoUserName 解析json error,json="+json);
						}
					}else {
						//重新设置省市缓存
						iProvinceService.getProvinceSetCache();
						LogUtil.log.info(String.format("###getAndSetPesudoUserName 重新设置省市缓存,ip=%s,code=%s",ip,code));
					}
					LogUtil.log.info(String.format("###getAndSetPesudoUserName cache region=%s,cacheKey=%s",region,ipKey));
				}
			}*/
			//pesudoUserName = "游客"+seq+"号";
			pesudoUserName = String.format("来自%s帅哥%s号", region,seq);
			MemcachedUtil.set(key, pesudoUserName,MCTimeoutConstants.PESUDOUSERNAME_TIMEOUTSECOND);
			LogUtil.log.info(String.format("###getAndSetPesudoUserName select nickName=%s,cacheKey=%s",pesudoUserName,key));
		}else{
			pesudoUserName = userObj.toString();
			LogUtil.log.info(String.format("###getAndSetPesudoUserName cache nickName=%s,cacheKey=%s",pesudoUserName,key));
		}
		return pesudoUserName;
	}
	
	/**
	 * 用户所有房间守护信息缓存，一个房间只取最高等级的守护一条。。。
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private List getGuardListAllCache(String userId) throws Exception {
		// my-todo
		if(StringUtils.isEmpty(userId)){
			throw new Exception("###parameter error: userId can't be empty.");
		}
		List guardList = new ArrayList();
//		List<Map> list = guardWorkService.getUserGuardInfoAllRoomCache(userId);
//		if(list != null && list.size() >0) {
//			String roomId = "";
//			String image = "";
//			int level = 0;
//			
//			String roomIdKey = "roomId";
//			String imageKey = "image";
//			String levelKey = "level";
//			for(Map m : list) {
//				JSONObject json = new JSONObject();
//				if(m.containsKey(roomIdKey) && m.get(roomIdKey) != null) {
//					roomId = m.get(roomIdKey).toString();
//				}
//				if(m.containsKey(imageKey) && m.get(imageKey) != null) {
//					image = m.get(imageKey).toString();
//				}
//				if(m.containsKey(levelKey) && m.get(levelKey) != null) {
//					level = Integer.parseInt(m.get(levelKey).toString());
//				}
//				
//				json.put("roomId", roomId);
//				json.put("image", SpringContextListener.getContextProValue("cdnPath", "")+ "/" +Constants.GUARD_IMG_FILE_URL + "/" +image);
//				json.put("level", level);
//				guardList.add(json);
//			}
//		}
		return guardList;
	}
	
	
	/**
	 * 禁用主播等级图标显示处理
	 */
	private UserCacheInfo IsLevelIconShow(String uid,UserCacheInfo userCacheInfo){
		//查询是否是禁用主播，是不显示主播等级图标
		 //UserAnchor userAnchor  = userAnchorService.getUserAnchorByUserId(uid);
		UserAnchor userAnchor  = userAnchorService.getAnchorFromCacheByUserId(uid);
		// my-todo
//		 //主播
//		 if(userAnchor != null){
//			 //已被禁播
//			 if(userAnchor.getAnchorStatus()!= null && userAnchor.getAnchorStatus() == UserAnchorTableEnum.IsForbidden.forbidden.getValue()){
//				 if(userAnchor.getIsLevelIcon() == UserAnchorTableEnum.IsLevelIcon.yes.getValue()){
//					 userCacheInfo.setIsLevelIcon(UserAnchorTableEnum.IsLevelIcon.yes.getValue());
//				 }else{
//					 userCacheInfo.setIsLevelIcon(UserAnchorTableEnum.IsLevelIcon.no.getValue());
//				 }
//			 }else{//未被禁播
//				 userCacheInfo.setIsLevelIcon(UserAnchorTableEnum.IsLevelIcon.yes.getValue());
//			 }
//		 }
		return userCacheInfo;
	}
	
	/**
	 * 靓号升级处理
	 * @param userId
	 */
	private int updateGoodCode(String userId) {
		String key = MCPrefix.CUR_GOOD_CODE_LEVEL;
		Object object = MemcachedUtil.get(key);
		int curgoodCodeLevel = 0;
		Map<String,Object> map = null;
		if(object == null){
				synchronized(this){
					object = MemcachedUtil.get(key);
					if(object == null){
						map = new HashMap<String, Object>();
						//获取靓号等级
						curgoodCodeLevel = getGoodCodeLevel(userId,map,key);
					}else{
						//从缓存中取
						curgoodCodeLevel = getFromGoodCodeCache(object,userId,key);
					}
				}
		}else{
			//从缓存中取
			curgoodCodeLevel = getFromGoodCodeCache(object,userId,key);
		}
		return curgoodCodeLevel;
	}
	
	
	private int judgeLevel(long sumPrice){
		if(sumPrice < 200000){
			//靓号0级
			return 0;
		}else if(sumPrice >= 200000 && sumPrice < 500000){
			//靓号1级
			return 1;
		}else if(sumPrice >= 500000 && sumPrice < 1000000){
			//靓号2级
			return 2;
		}else if(sumPrice >= 1000000 && sumPrice < 2000000){
			//靓号3级
			return 3;
		}else if(sumPrice >= 2000000){
			//靓号顶级
			return 4;
		}else{
			return 0;
		}
	}
	
	//获取靓号等级、设置缓存
	private int getGoodCodeLevel(String userId,Map<String,Object> map,String key){
		//查数据库
		// my-todo
//		Date date = new Date();
//		String startTime = DateUntil.getStartTime(date);
//		String endTime = DateUntil.getEndTime(date);
//		long sumPrice = payGiftOutService.getSumpriceInWeekByUserid(userId,startTime,endTime);
//		//判断当前靓号等级
//		int curgoodCodeLevel = judgeLevel(sumPrice);
//		map.put(userId, curgoodCodeLevel);
//		//设置缓存5分钟
//		//MemcachedUtil.set(key, map,MCTimeoutConstants.CUR_GOOD_CODE_LEVEL_TIMEOUTSECOND);
//		//正式服缓存时间
//		int timeoutSecond = getCacheTime();
//		MemcachedUtil.set(key, map,timeoutSecond);
//		return curgoodCodeLevel;
		return 0;
	}
	
	private int getFromGoodCodeCache(Object object,String userId,String key){
		Map<String,Object> map = (Map<String,Object>)(object);
		int curgoodCodeLevel = 0;
		if(map.get(userId) != null){
			curgoodCodeLevel = Integer.parseInt(map.get(userId).toString());
		}else{
			//获取靓号等级
			curgoodCodeLevel = getGoodCodeLevel(userId,map,key);
		}
		return curgoodCodeLevel;
	}
	
	/**获取靓号缓存时间(秒)*/
	private static int getCacheTime(){
		Date date = new Date();
		//获取下周一 0点时间
		Date nextDate = DateUntil.getFirstDayOfNextWeek(date);
		//获取到下周一 0点时间差
		int num = DateUntil.getTimeIntervalSecond(date,nextDate);
		LogUtil.log.info("靓号缓存有效时间为:"+num+"秒");
		return num;
	}
	
	
	/**
	 * 设置真爱等级图标
	 * @return
	 */
	private String setFansLevelIcon(int sumDay){
		//continueDay++;//数据库是从0开始记录的
		String iconurl = "";
		if(sumDay >= 180){
			iconurl = "love9.png";
		}else if(sumDay >= 150 && sumDay<180){
			iconurl = "love8.png";
		}else if(sumDay >= 120 && sumDay<150){
			iconurl = "love7.png";
		}else if(sumDay >= 90 && sumDay<120){
			iconurl = "love6.png";
		}else if(sumDay >= 70 && sumDay<90){
			iconurl = "love5.png";
		}else if(sumDay >= 50 && sumDay<70){
			iconurl = "love4.png";
		}else if(sumDay >= 30 && sumDay<50){
			iconurl = "love3.png";
		}else if(sumDay >= 15 && sumDay<30){
			iconurl = "love2.png";
		}else if(sumDay >= 5 && sumDay<15){
			iconurl = "love1.png";
		}
		return iconurl;
	}
	
	/**
	 * 真爱等级图标业务处理
	 * @return
	 * @throws Exception 
	 */
	private String handleFansIcon(String uid,String roomId) throws Exception{
		// my-todo
//		String loveiconUrl = "";
//		if(StringUtils.isEmpty(roomId)){
//			return loveiconUrl;
//		}
//		String key = MCPrefix.LOVE_FANS+uid+"_"+roomId;
//		Object cache = MemcachedUtil.get(key);
//		if(cache != null){
//			loveiconUrl = (String) cache;
//			return loveiconUrl;
//		}
//		//UserPunchRecord userPunchRecord = userPunchRecordService.getUserPunchRecordHasFansIcon(uid,roomId);
//		UserPunchIntimacy userPunchIntimacy = userPunchIntimacyService.getUserPunchIntimacyByRoom(uid, roomId);
//
//		if(userPunchIntimacy != null && userPunchIntimacy.getSumDay() >= 5){
//			//Date iconDate = userPunchRecord.getPunchday();
//			//查询获得图标后的30天内在直播间没有打卡记录
//			//String startDate = DateUntil.getFormatDate("yyyyMMdd",iconDate);
//			//String endDate = DateUntil.getFormatDate("yyyyMMdd",DateUntil.addDatyDatetime(iconDate, 30));
//			//UserPunchRecord upunchRecord = userPunchRecordService.getUserPunchRecordByTime(uid,roomId,startDate,endDate);
//			//if(upunchRecord != null){
//			//LOVE_LEVEL_ICON+			
//			loveiconUrl =  this.setFansLevelIcon(userPunchIntimacy.getSumDay());
//			if(StringUtils.isNotEmpty(loveiconUrl)){
//				loveiconUrl = LOVE_LEVEL_ICON+loveiconUrl;
//			}
//			//}
//		}
//		MemcachedUtil.set(key,loveiconUrl,MCTimeoutConstants.LOVE_FANS);
//		return loveiconUrl;
		// my-todo
		return "";
	}
	
	
	
	

}
