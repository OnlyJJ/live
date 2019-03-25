package com.lm.live.login.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.appclient.enums.AppType;
import com.lm.live.appclient.service.IAppInstallChannelService;
import com.lm.live.base.dao.ServiceLogMapper;
import com.lm.live.base.domain.ServiceLog;
import com.lm.live.base.domain.ThirdpartyConf;
import com.lm.live.base.enums.ThirdpartyType;
import com.lm.live.base.service.IProvinceService;
import com.lm.live.base.service.IThirdpartyConfService;
import com.lm.live.common.constant.LockKey;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.redis.RdLock;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.FileTypeUtils;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MD5Util;
import com.lm.live.common.utils.Md5CommonUtils;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Session;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.login.constant.Constants;
import com.lm.live.login.constant.MCPrefix;
import com.lm.live.login.dao.CodeRandomMapper;
import com.lm.live.login.dao.UserRegistAutoMapper;
import com.lm.live.login.dao.WechatOauth2TokenRefreshMapper;
import com.lm.live.login.dao.WechatUserMapper;
import com.lm.live.login.domain.CodeRandom;
import com.lm.live.login.domain.QQConnectUserInfoDo;
import com.lm.live.login.domain.UserRegistAuto;
import com.lm.live.login.domain.WechatOauth2TokenRefresh;
import com.lm.live.login.domain.WechatUser;
import com.lm.live.login.domain.WeiboUserInfoDo;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.enums.LockTarget;
import com.lm.live.login.enums.LoginType;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.ILoginService;
import com.lm.live.login.service.QQAccessService;
import com.lm.live.login.service.WeChatAccessService;
import com.lm.live.login.service.WeiboAccessService;
import com.lm.live.login.vo.AccessToken;
import com.lm.live.login.vo.QQConnectUserInfoVo;
import com.lm.live.login.vo.WechatUserInfo;
import com.lm.live.login.vo.WeiboUserInfo;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.service.IUserCacheInfoService;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;

/**
 * 登录服务实现类
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public class LoginServiceImpl implements ILoginService {

	@Resource
	private UserRegistAutoMapper userRegistAutoMapper;
	
	@Resource
	private CodeRandomMapper codeRandomMapper;
	
	@Resource
	private ServiceLogMapper serviceLogMapper;
	
	@Resource
	private WechatOauth2TokenRefreshMapper wechatOauth2TokenRefreshMapper;
	
	@Resource
	private WechatUserMapper wechatUserMapper;
	
	@Resource
	private IProvinceService provinceService;
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IThirdpartyConfService thirdpartyConfService;
	
	@Resource
	private WeChatAccessService weChatAccessService;
	
	@Resource
	private QQAccessService qqAccessService;
	
	@Resource
	private WeiboAccessService weiboAccessService;

	@Override
	public UserBaseInfo autoRegist(String clientType, String clientIp,
			DeviceProperties deviceProperties, String accessToken)
			throws Exception {
		if (StringUtils.isEmpty(clientType) || StringUtils.isEmpty(clientIp)
				|| StringUtils.isEmpty(accessToken) || deviceProperties == null) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		String uuid = deviceProperties.getUuid();
		// 校验参数
		if (StringUtils.isEmpty(uuid)) {
			throw new LoginBizException(ErrorCode.ERROR_102);
		}
		filterAppAutoRegist(clientIp);

		/** uuid加密字符串，格式：md5(md5(uuid)+uuid) */
		String uuidServerMd5 = Md5CommonUtils.getMD5String(Md5CommonUtils
				.getMD5String(uuid) + uuid);
		if (!accessToken.equalsIgnoreCase(uuidServerMd5)) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		UserRegistAuto userRegistAuto = userRegistAutoMapper.getByUuid(uuid);

		Date nowDate = new Date();
		// 返回的实体
		UserInfo retUserInfo = new UserInfo();
		String userId = null;
		UserInfoDo userInfo = null;
		// 是否设置过密码
		String resetPwdFlag = Constants.FLAG_NO;
		// 根据uuid查不到,说明此设备没有自动注册过
		if (userRegistAuto == null) {
			LogUtil.log.info(String.format("###自动注册,此设备首次登录,进行自动注册,devInfo；%s",
					JsonUtil.beanToJsonString(deviceProperties)));
			// 保存的实体
			userInfo = new UserInfoDo();
			// 从配置库中获取userId
			userId = getUserId();
			userInfo.setUserId(userId);
			// 密码: 先取Md5(uuid)
			String imeiMd5Str = Md5CommonUtils.getMD5String(uuid);
			// 密码加密
			String encryptDbPwd = getPwd(nowDate, userId, imeiMd5Str);

			// 根据ip获取省份
			String clientProvince = provinceService.getProviceBy(clientIp);
			// 随机获取昵称
			String[] strArr = Constants.DEFAULT_AUTOREGIST_NAME.split(Constants.SEPARATOR);
			int strLength = strArr.length;
			Random rnd = new Random();
			int getNickNameIndex = rnd.nextInt(strLength - 1);
			String subNickName = strArr[getNickNameIndex];

			// userId过长,截取一部分用于拼接nickName
			String userIdForNickName = userId;
			if (userIdForNickName.length() > 6) {
				userIdForNickName = userIdForNickName.substring(2, userIdForNickName.length());
			}

			// 设置昵称:预登录帐号命名规则：所在地+帅哥+id
			String nickName = clientProvince + subNickName + userIdForNickName;
			String appPackage = null;
			String channelId = null;
			appPackage = deviceProperties.getPackageName();
			channelId = deviceProperties.getChannelId();
			if (channelId != null) {
				if (channelId.length() > 10)
					channelId = channelId.substring(0, 10);
			}
			userInfo.setUuid(uuid);
			userInfo.setPwd(Constants.DEFAULT_AUTO_PWD);
			userInfo.setAddTime(nowDate);
			userInfo.setNickName(nickName);
			// 记录ip,不然会被当成首次登陆
			userInfo.setLastIp(clientIp);
			// 用户状态，1-正常; 0-停用; 2-待审核
			userInfo.setUserStatus(1);
			userInfo.setSex("未知");
			userInfo.setIcon(Constants.USER_DEFAULT_ICON);
			userInfo.setChannelId(channelId);
			userInfo.setAppPackage(appPackage);
			userInfo.setLoginType(LoginType.AUTO.getType());
			userInfoService.insert(userInfo);
			
			retUserInfo.setIsFirttimeLogin(Constants.STATUS_1);
			String verifyId = encryptDbPwd;

			// 保存自动注册信息
			userRegistAuto = new UserRegistAuto();
			userRegistAuto.setUserId(userId);
			userRegistAuto.setDeviceMac(deviceProperties.getMac());
			userRegistAuto.setUuId(uuid);
			userRegistAuto.setDeviceProperties(JsonUtil.beanToJsonString(deviceProperties));
			userRegistAuto.setVerifyId(verifyId);
			userRegistAuto.setRecordTime(nowDate);
			userRegistAuto.setIsChangeNickname(Constants.STATUS_0);
			userRegistAutoMapper.insert(userRegistAuto);

			String cacheKeyOfAppAutoRegistEachIp = MCPrefix.AUTO_REGIST_LIMIT_CACHE + clientIp;
			String dateStr = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD, nowDate);
			String cacheKeyOfAppAutoRegistEveryDay = MCPrefix.AUTO_REGIST_LIMIT_DAY_CACHE + dateStr;

			int eachIpAutoRegistNum = 0;
			int eachDayAutoRegistNum = 0;
			Object cacheObjOfAppAutoRegistEachIp = MemcachedUtil.get(cacheKeyOfAppAutoRegistEachIp);
			Object cacheObjOfAppAutoRegistEveryDay = MemcachedUtil.get(cacheKeyOfAppAutoRegistEveryDay);
			if (cacheObjOfAppAutoRegistEachIp != null) {
				eachIpAutoRegistNum = Integer.parseInt(cacheObjOfAppAutoRegistEachIp.toString());
			}

			if (cacheObjOfAppAutoRegistEveryDay != null) {
				eachDayAutoRegistNum = Integer.parseInt(cacheObjOfAppAutoRegistEveryDay.toString());
			}

			eachIpAutoRegistNum++;
			eachDayAutoRegistNum++;
			MemcachedUtil.set(cacheKeyOfAppAutoRegistEachIp, eachIpAutoRegistNum, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);

			MemcachedUtil.set(cacheKeyOfAppAutoRegistEveryDay, eachDayAutoRegistNum, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);

		} else {
			LogUtil.log.info(String.format("###自动注册,此设备已登录过,uuid:%s,devInfo；%s", uuid,
					JsonUtil.beanToJsonString(deviceProperties)));
			userId = userRegistAuto.getUserId();
			userInfo = userInfoService.getUserByUserId(userId);

			if (userInfo == null) {
				throw new LoginBizException(ErrorCode.ERROR_103);
			}

			// 是否被封号
			if (userInfo.getUserStatus() != 1) {
				throw new LoginBizException(ErrorCode.ERROR_103);
			}

			// 更新user_info信息
			userInfo.setLastTime(DateUntil.format2Str(nowDate, Constants.DATEFORMAT_YMDHMS));
			userInfo.setLastIp(clientIp);
			userInfoService.update(userInfo);

			String oldPwd = userInfo.getPwd();
			if (!Constants.DEFAULT_AUTO_PWD.equals(oldPwd)) {
				resetPwdFlag = Constants.FLAG_YES;
			}
		}
		retUserInfo.setUserId(userId);
		retUserInfo.setNickName(userInfo.getNickName());
		retUserInfo.setIcon(userInfo.getIcon());

		// 登录成功记录
		String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
		ServiceLog serviceLog = new ServiceLog();
		serviceLog.setUserId(userInfo.getUserId());
		serviceLog.setInfo(serviceLogInfo);
		serviceLog.setIp(clientIp);
		serviceLog.setActTime(nowDate);
		serviceLog.setUserName(userInfo.getNickName());
		serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(deviceProperties));
		serviceLog.setClientType(clientType);
		serviceLogMapper.insert(serviceLog);
		return retUserInfo;
	}

	@Override
	public JSONObject appWechatLogin(String code, String channelId,
			String clientIp, String clientType,
			DeviceProperties deviceProperties) throws Exception {
		LogUtil.log.info(String.format("###begin-appWechatLogin,clientIp:%s,clientType:%s,deviceProperties:%s",clientIp,clientType ,JsonUtil.beanToJsonString(deviceProperties))) ;
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(channelId)
				|| StringUtils.isEmpty(clientIp) || deviceProperties == null) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		JSONObject json = new JSONObject();
		UserInfo retUserinfo = new UserInfo();
		Session session = new Session();
		
		int thirdpartyType = ThirdpartyType.WEIXIN.getValue();
		String packageName = "";
		if(null != deviceProperties.getPackageName()) {
			packageName = deviceProperties.getPackageName();
		}
		int clientTypeInt = convertClientType(clientType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			LogUtil.log.info(String.format("###查询不到相关的第三方登录配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		if(thirdpartyConf.getInUseLogin() != Constants.STATUS_1){
			LogUtil.log.info(String.format("###查询到相关的第三方配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_104);
		}
		
		// appId
		String appid = thirdpartyConf.getAppId();
		// 秘钥
		String loginsecret = thirdpartyConf.getLoginKey();
		
		WechatUserInfo wechatUserInfo = null; 
		Date nowDate = new Date();
		
		Boolean flagUseLocalAccessToken = false;
		WechatOauth2TokenRefresh dbWechatOauth2TokenRefresh = wechatOauth2TokenRefreshMapper.getByCode(code);
		if(dbWechatOauth2TokenRefresh!=null){
			Date tokenResultTime =  dbWechatOauth2TokenRefresh.getResultTime();
			// access_token接口调用凭证超时时间，单位（秒）,原来是7200秒
			//int expiresinSecond = 7200;
			int expiresinSecond = dbWechatOauth2TokenRefresh.getExpiresinSecond();
			Date accessTokenEndTime = DateUntil.addSecond(tokenResultTime, expiresinSecond);
			
			if(accessTokenEndTime.before(nowDate)){ //accessToken已过期
				int wechatOauth2TokenRefreshId = dbWechatOauth2TokenRefresh.getId(); 
				LogUtil.log.info(String.format("###微信登录,accessToken已过期,删除...,dbWechatOauth2TokenRefresh:%s", JsonUtil.beanToJsonString(dbWechatOauth2TokenRefresh)));
				wechatOauth2TokenRefreshMapper.removeById(wechatOauth2TokenRefreshId);// 删除db中纪录code对应的accssToken行
				flagUseLocalAccessToken = false;
			}else{
				flagUseLocalAccessToken = true;
			}
		}else{
			 flagUseLocalAccessToken = false;
		}
		
		if(flagUseLocalAccessToken){ // 本地db中纪录的code对应的accessToken未过期
			LogUtil.log.info(String.format("###微信登录,用code在本地db取到了accessToken,code:%s",code));
			String accessTokenStr = dbWechatOauth2TokenRefresh.getAccessToken();
			String openid = dbWechatOauth2TokenRefresh.getOpenid();
			if(StringUtils.isEmpty(accessTokenStr) || StringUtils.isEmpty(openid)){
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
			//请求腾讯服务器,获取用户个人信息
			wechatUserInfo = weChatAccessService.getUserinfo(accessTokenStr, openid);
			if(!wechatUserInfo.isSuccess()){ //用户db中保存的accessToken获取用户信息失败
				LogUtil.log.info(String.format("###微信登录失败(用db中保存的caccessToken记录请求微信服务器,返回false),code:%s",code));
				LogUtil.log.info(String.format("###微信登录,accessToken已过期,删除...,dbWechatOauth2TokenRefresh:%s", JsonUtil.beanToJsonString(dbWechatOauth2TokenRefresh)));
				int wechatOauth2TokenRefreshId = dbWechatOauth2TokenRefresh.getId(); 
				wechatOauth2TokenRefreshMapper.removeById(wechatOauth2TokenRefreshId);// 删除db中纪录code对应的accssToken行
				LogUtil.log.info(String.format("###微信登录失败,code:%s,accessTokenStr:%s,wechatUserInfo:%s,用db中的accessToken获取用户信息时微信服务器返回false,",code,accessTokenStr,JsonUtil.beanToJsonString(wechatUserInfo)));
				
				// 返回错误码,提示app重新拉起
				throw new LoginBizException(ErrorCode.ERROR_1);
			}else{
				LogUtil.log.info(String.format("###微信登录成功,用db中保存的caccessToken,code:%s",code));
			}
		}else{
			LogUtil.log.info(String.format("###微信登录,用code请求微信服务器获取accessToken,code:%s",code));
			//请求微信服务器,通过code获取微信的accessToken
			AccessToken accessToken = weChatAccessService.getAccessToken(appid, loginsecret, code);
			if(!accessToken.isSuccess()){
				LogUtil.log.error("###微信登录失败,用code获取accessToken时微信服务器返回false,"+JsonUtil.beanToJsonString(accessToken));
				// 返回错误码,提示app重新拉起
				throw new LoginBizException(ErrorCode.ERROR_1);
			}
			String accessTokenStr = accessToken.getAccess_token();
			String openid = accessToken.getOpenid();
			String refreshToken = accessToken.getRefresh_token();
			String scope = accessToken.getScope();
			//请求腾讯服务器,获取用户个人信息
			wechatUserInfo = weChatAccessService.getUserinfo(accessTokenStr, openid);
			if(!wechatUserInfo.isSuccess()){
				LogUtil.log.info(String.format("######微信登录失败,code:%s,accessTokenStr:%s,wechatUserInfo:%s,用code通过微信服务器交换得到的accessToken获取用户信息时微信服务器返回false,",code,accessTokenStr,JsonUtil.beanToJsonString(wechatUserInfo)));
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
			
			dbWechatOauth2TokenRefresh = new WechatOauth2TokenRefresh();
			dbWechatOauth2TokenRefresh.setAppid(appid);
			dbWechatOauth2TokenRefresh.setOpenid(openid);
			dbWechatOauth2TokenRefresh.setCode(code);
			dbWechatOauth2TokenRefresh.setAccessToken(accessTokenStr);
			dbWechatOauth2TokenRefresh.setExpiresinSecond(accessToken.getExpires_in());
			dbWechatOauth2TokenRefresh.setRefreshToken(refreshToken);
			dbWechatOauth2TokenRefresh.setScope(scope);
			Date resultTime = new Date();
			dbWechatOauth2TokenRefresh.setResultTime(resultTime);
			wechatOauth2TokenRefreshMapper.insert(dbWechatOauth2TokenRefresh);
		}
		String time = DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date());
		String unionid = wechatUserInfo.getUnionid() ;
		// 在本地db查找用户,切记,android用了多个壳,所以要用unionid查询
		UserInfoDo dbUserInfo = userInfoService.getByWechatUnionid(unionid);
		if(dbUserInfo==null){//数据库中没有此openid,表示是第一次登录,则插入一条新纪录
			//从系统统一地方获取userId
			String userId = getUserId();
			String wechatHeadImgUrl = wechatUserInfo.getHeadimgurl();
			String imgName = null;
			//下载微信用户的头像
			if(!StringUtils.isEmpty(wechatHeadImgUrl)){
				String filepath = Constants.USER_ICON_URL +File.separator+Constants.ICON_IMG_FILE_URI+File.separator;
				
				String iconDateStr = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD_1, nowDate);
				
				//生成图片文件名 
				imgName = FileTypeUtils.getRandomFileGroupId() +  Constants.IMG_JPG;
				
				// 头像地址按目录分类
				imgName = iconDateStr+File.separator+imgName;
				
				String  imgFileLocalPath = filepath+imgName;
				try {
					HttpUtils.downLoadFile(wechatHeadImgUrl, imgFileLocalPath);
				} catch (Exception e) {
					imgName = null;
					LogUtil.log.error("###下载头像失败:微信登录");
				}
			}
			
			if(StringUtils.isEmpty(imgName)){ //为空或拿不到,则用默认头像
				//用默认头像
				imgName = Constants.USER_DEFAULT_ICON;
			}
			
			//加记录 userInfo
			dbUserInfo = new UserInfoDo();
			String nickName = wechatUserInfo.getNickname();
			if (!StrUtil.isNullOrEmpty(nickName)) {
				//昵称=微信昵称（昵称过长时，截取前18个字符）
				nickName = StrUtil.jiequStr(nickName,18);
			}
			dbUserInfo.setNickName(nickName);
			dbUserInfo.setSex(convertWechatSex(wechatUserInfo.getSex())) ;
			dbUserInfo.setSf(wechatUserInfo.getProvince()) ;
			dbUserInfo.setCs(wechatUserInfo.getCity()) ;
			dbUserInfo.setUserId(userId);
			dbUserInfo.setAddTime(new Date());
			dbUserInfo.setActTime(time);
			dbUserInfo.setLastTime(time);
			dbUserInfo.setPwd(Constants.DEFAULT_WECHAT_PWD);
			dbUserInfo.setIcon(imgName);
			dbUserInfo.setUserStatus(1); //'用户状态，1-正常; 0-停用;
			dbUserInfo.setChannelId(channelId);
			dbUserInfo.setAppPackage(packageName);
			//增加UUID
			if(!StringUtils.isEmpty(deviceProperties.getUuid())){
				dbUserInfo.setUuid(deviceProperties.getUuid());
			}
			
			dbUserInfo.setLoginType(LoginType.WECHAT.getType());
			userInfoService.insert(dbUserInfo);
			
			//插表t_wechat_userinfo
			WechatUser wechatUserDo = new WechatUser();
			wechatUserDo.setUserId(userId);
			wechatUserDo.setOpenid(wechatUserInfo.getOpenid());
			wechatUserDo.setNickname(wechatUserInfo.getNickname());
			wechatUserDo.setSex(wechatUserInfo.getSex());
			wechatUserDo.setProvince(wechatUserInfo.getProvince());
			wechatUserDo.setCity(wechatUserInfo.getCity());
			wechatUserDo.setCountry(wechatUserInfo.getCountry());
			wechatUserDo.setHeadimgurl(wechatUserInfo.getHeadimgurl());
			wechatUserDo.setPrivilege(JsonUtil.beanToJsonString(wechatUserInfo.getPrivilege()));
			wechatUserDo.setUnionid(wechatUserInfo.getUnionid());
			wechatUserMapper.insert(wechatUserDo);
			
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_1);
			
			// 注册成功，处理业务
			doRegistCommonBusiness(userId, clientType);

		}else{//数据库中已有此openid,则更新信息
			// 用户状态,被封不给登录
			int userStatus = dbUserInfo.getUserStatus();
			// 用户状态，1-正常; 0-停用; 2-待审核
			if(userStatus != 1) {
				throw new LoginBizException(ErrorCode.ERROR_103);
			}
		}
		
		dbUserInfo.setLastTime(time); //更新最后登录的时间
		userInfoService.updateByUserId(dbUserInfo);
		
		retUserinfo.setIsFirttimeLogin(Constants.STATUS_0);
		retUserinfo.setUserId(dbUserInfo.getUserId());
		retUserinfo.setNickName(dbUserInfo.getNickName());
		retUserinfo.setIcon(dbUserInfo.getIcon());
		
		//登录成功,保存session
		setMemcacheToSessionId(session, dbUserInfo.getUserId());
		session.setFrom(LoginType.WECHAT.getType());
				
		//登录成功后，更新最后登录时间
		if(dbUserInfo!=null&&!StringUtils.isEmpty(dbUserInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(dbUserInfo.getUserId());
			updateInfo.setLastIp(clientIp);
			loginUpdateInfo(updateInfo);
			
			//登录成功后做的一些额外业务，此处预留扩展
//			doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
			
			//每次登录成功,都记录历史
			String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setUserId(dbUserInfo.getUserId());
			serviceLog.setInfo(serviceLogInfo);
			serviceLog.setIp(clientIp);
			serviceLog.setActTime(nowDate);
			serviceLog.setUserName(dbUserInfo.getNickName());
			serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(deviceProperties));
			serviceLog.setClientType(clientType);
			serviceLogMapper.insert(serviceLog);
		}
		String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),dbUserInfo.getUserId(),clientIp);
		LogUtil.log.info(logInfo);
		json.put(session.getShortName(), session.buildJson() );
		json.put(retUserinfo.getShortName(), retUserinfo.buildJson() );
		return json;
		
	}

	@Override
	public JSONObject appQQConnectLogin(String accessToken,
			QQConnectUserInfoVo qqConnectUserInfoVo, String channelId,
			String clientIp, String clientType,
			DeviceProperties deviceProperties) throws Exception {
		LogUtil.log.info(String.format("###begin-appQQConnectLogin,clientIp:%s,clientType:%s,deviceProperties:%s",clientIp,clientType ,JsonUtil.beanToJsonString(deviceProperties))) ;
		JSONObject json = new JSONObject();
		UserInfo retUserinfo = new UserInfo();
		Session session = new Session();
		String openid = qqConnectUserInfoVo.getOpenid();
		if(StringUtils.isEmpty(openid)){
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		int thirdpartyType = ThirdpartyType.QQ.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 转换成typeInt
		int clientTypeInt = convertClientType(clientType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			LogUtil.log.info(String.format("###查询不到相关的第三方登录配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		if(thirdpartyConf.getInUseLogin() != Constants.STATUS_1){
			LogUtil.log.info(String.format("###查询到相关的第三方配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_104);
		}
		
		String serverConfAppid = thirdpartyConf.getAppId();
		JSONObject qqData = getQQdata(accessToken, openid, serverConfAppid);
		String clentid = qqData.getString("client_id");
		String unionid = qqData.getString("unionid");
		
		UserInfoDo  dbUserInfo = userInfoService.getByQQConnectUnionid(unionid);
		if(dbUserInfo==null){//数据库中没有此openid,表示是第一次登录,则插入一条新纪录
			dbUserInfo  = saveQQInfoAndUserInfo(qqConnectUserInfoVo,channelId,deviceProperties,clentid,unionid);
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_1);
			// 注册后处理的业务，预留扩展
			doRegistCommonBusiness(dbUserInfo.getUserId(), clientType);
		} else {//数据库中已有此openid,则更新信息
			// 用户状态,被封不给登录
			int userStatus = dbUserInfo.getUserStatus();
			// 用户状态，1-正常; 0-停用;
			if(userStatus!=1){
				throw new LoginBizException(ErrorCode.ERROR_103);
			}
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date())); 
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_0);
			userInfoService.updateByUserId(dbUserInfo);
		}
		
		retUserinfo.setUserId(dbUserInfo.getUserId());
		retUserinfo.setNickName(dbUserInfo.getNickName());
		retUserinfo.setIcon(dbUserInfo.getIcon());

		//登录成功,保存session
		setMemcacheToSessionId(session,dbUserInfo.getUserId());
		session.setFrom(LoginType.APPQQ.getType()); //app QQ互联登录
		
		//登录成功后，更新最后登录时间
		if(dbUserInfo!=null&&!StringUtils.isEmpty(dbUserInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(dbUserInfo.getUserId());
			updateInfo.setLastIp(clientIp);
			loginUpdateInfo(updateInfo);
			
			// 登录成功后，处理的业务，保留扩展
//			doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
			
			Date nowDate = new Date();
			String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setUserId(dbUserInfo.getUserId());
			serviceLog.setInfo(serviceLogInfo);
			serviceLog.setIp(clientIp);
			serviceLog.setActTime(nowDate);
			serviceLog.setUserName(dbUserInfo.getNickName());
			serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(deviceProperties));
			serviceLog.setClientType(clientType);
			serviceLogMapper.insert(serviceLog);
		}
		String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),dbUserInfo.getUserId(),clientIp);
		LogUtil.log.info(logInfo);
		json.put(session.getShortName(), session.buildJson() );
		json.put(retUserinfo.getShortName(), retUserinfo.buildJson() );
		return json;
	}

	@Override
	public JSONObject webQQConnectLogin(String accessToken,
			QQConnectUserInfoVo qqConnectUserInfoVo, String channelId,
			String clientIp) throws Exception {
		LogUtil.log.info(String.format("###begin-webQQConnectLogin,clientIp:%s,servername:%s,qqConnectUserInfoVo:%s",clientIp,JsonUtil.beanToJsonString(qqConnectUserInfoVo))) ;
		JSONObject json = new JSONObject();
		UserInfo retUserinfo = new UserInfo();
		Session session = new Session();
		String openid = qqConnectUserInfoVo.getOpenid();
		if(StringUtils.isEmpty(openid)){
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		int thirdpartyType = ThirdpartyType.QQ.getValue();
		String packageName = Constants.DEFAULT_PACKAGENAME;
		// web端qq登录
		int clientTypeInt = AppType.WEB.getValue();
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			LogUtil.log.info(String.format("###查询不到相关的第三方登录配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		if(thirdpartyConf.getInUseLogin() != Constants.STATUS_1){
			LogUtil.log.info(String.format("###查询到相关的第三方配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_104);
		}
		
		String serverConfAppid = thirdpartyConf.getAppId();
		// 登录校验
		JSONObject qqData = getQQdata(accessToken, openid, serverConfAppid);
		String clentid = qqData.getString("client_id");
		String unionid = qqData.getString("unionid");
		
		UserInfoDo dbUserInfo = userInfoService.getByQQConnectUnionid(unionid);
		//数据库中没有此openid,表示是第一次登录,则插入一条新纪录
		if(dbUserInfo == null) {
			dbUserInfo = saveQQInfoAndUserInfo(qqConnectUserInfoVo,channelId,null,clentid,unionid);
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_1);
			String clientType = HttpUtils.webClient;
			// 注册成功后，处理的业务
			doRegistCommonBusiness(dbUserInfo.getUserId(), clientType);
		} else { //数据库中已有此openid,则更新信息
			// 用户状态,被封不给登录
			int userStatus = dbUserInfo.getUserStatus();
			// 用户状态，1-正常; 0-停用; 2-待审核
			if(userStatus != Constants.STATUS_1) {
				throw new LoginBizException(ErrorCode.ERROR_103);
			}
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_0);
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date())); //更新最后登录的时间
			userInfoService.updateByUserId(dbUserInfo);
		}
		retUserinfo.setUserId(dbUserInfo.getUserId());
		retUserinfo.setNickName(dbUserInfo.getNickName());
		retUserinfo.setIcon(dbUserInfo.getIcon());

		//登录成功,保存session
		setMemcacheToSessionId(session,dbUserInfo.getUserId());
		session.setFrom(LoginType.WEBQQ.getType()); //web QQ互联登录
		
		//登录成功后，更新最后登录时间
		if(dbUserInfo!=null&&!StringUtils.isEmpty(dbUserInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(dbUserInfo.getUserId());
			updateInfo.setLastIp(clientIp);
			this.loginUpdateInfo(updateInfo);
			
			//登录成功后做的一些额外业务
//			doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
			
			//每次登录成功,都记录历史
			Date nowDate = new Date();
			String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setUserId(dbUserInfo.getUserId());
			serviceLog.setInfo(serviceLogInfo);
			serviceLog.setIp(clientIp);
			serviceLog.setActTime(nowDate);
			serviceLog.setUserName(dbUserInfo.getNickName());
			serviceLog.setClientType(HttpUtils.webClient);
			serviceLogMapper.insert(serviceLog);
		}
		String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),dbUserInfo.getUserId(),clientIp);
		LogUtil.log.info(logInfo);
		json.put(session.getShortName(), session.buildJson() );
		json.put(retUserinfo.getShortName(), retUserinfo.buildJson() );
		return json;
	}
	
	@Override
	public JSONObject appWeiboLogin(String accessToken, String uid,
			String channelId, String clientIp, String clientType,
			DeviceProperties deviceProperties) throws Exception {
		LogUtil.log.info(String.format("###begin-appWeiboLogin,clientIp:%s,clientType:%s,deviceProperties:%s",clientIp,clientType ,JsonUtil.beanToJsonString(deviceProperties))) ;
		JSONObject json = new JSONObject();
		UserInfo retUserinfo = new UserInfo();
		Session session = new Session();
		if(StringUtils.isEmpty(accessToken)||StringUtils.isEmpty(uid)){
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		
		int thirdpartyType = ThirdpartyType.WEIBO.getValue();
		String packageName = null;
		if(deviceProperties != null){
			packageName = deviceProperties.getPackageName();
		}
		// 转换成typeInt
		int clientTypeInt = convertClientType(clientType);
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			LogUtil.log.info(String.format("###查询不到相关的第三方登录配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		if(thirdpartyConf.getInUseLogin() != Constants.STATUS_1){
			LogUtil.log.info(String.format("###查询到相关的第三方配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_104);
		}
		String clientId = thirdpartyConf.getAppId();
		
		// 校验数据
		boolean flag = validateWeiboToken(accessToken,clientId);
		if(!flag){
			String errorMsg = "微博app端接入，服务器端accessToken认证不通过";
			LogUtil.log.error("###"+errorMsg);
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
		//获取用户个人信息
		WeiboUserInfo weiboUserInfo = weiboAccessService.getInfo(accessToken, uid);
		LogUtil.log.info("###weiboUserInfo:"+JsonUtil.beanToJsonString(weiboUserInfo));
		if(weiboUserInfo == null ||StringUtils.isEmpty(weiboUserInfo.getIdstr())){
			throw new LoginBizException(ErrorCode.ERROR_1); 
		}
		UserInfoDo dbUserInfo = userInfoService.getByWeiboUid(uid);
		//数据库中没有此openid,表示是第一次登录,则插入一条新纪录
		if(dbUserInfo == null) {
			//从系统统一地方获取userId
			dbUserInfo = saveWeiBoAndUserInfo(weiboUserInfo,channelId,deviceProperties);
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_1);
			// 注册成功后，处理的业务
			doRegistCommonBusiness(dbUserInfo.getUserId(), clientType);
		}else{ //数据库中已有此openid,则更新信息
			// 用户状态,被封不给登录
			int userStatus = dbUserInfo.getUserStatus();
			// 用户状态，1-正常; 0-停用; 2-待审核
			if(userStatus != Constants.STATUS_1){
				throw new LoginBizException(ErrorCode.ERROR_103); 
			}
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_0);
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date())); //更新最后登录的时间
			userInfoService.updateByUserId(dbUserInfo);
		}
		retUserinfo.setUserId(dbUserInfo.getUserId());
		retUserinfo.setNickName(dbUserInfo.getNickName());
		retUserinfo.setIcon(dbUserInfo.getIcon());
		
		//登录成功,保存session
		setMemcacheToSessionId(session,dbUserInfo.getUserId());
		session.setFrom(LoginType.APPWB.getType());
		
		//登录成功后，更新最后登录时间
		if(dbUserInfo!=null&&!StringUtils.isEmpty(dbUserInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(dbUserInfo.getUserId());
			updateInfo.setLastIp(clientIp);
			loginUpdateInfo(updateInfo);
			
			//登录成功后做的一些额外业务,由于登录入口有注册用户、微信、微博、qq,所以再封装一个共同的业务方法
			doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
			
			//每次登录成功,都记录历史
			Date nowDate = new Date();
			String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setUserId(dbUserInfo.getUserId());
			serviceLog.setInfo(serviceLogInfo);
			serviceLog.setIp(clientIp);
			serviceLog.setActTime(nowDate);
			serviceLog.setUserName(dbUserInfo.getNickName());
			serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(deviceProperties));
			serviceLog.setClientType(clientType);
			serviceLogMapper.insert(serviceLog);
		}
		String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),dbUserInfo.getUserId(),clientIp);
		LogUtil.log.info(logInfo);
		json.put(session.getShortName(), session.buildJson() );
		json.put(retUserinfo.getShortName(), retUserinfo.buildJson() );
		return json;
	}

	@Override
	public JSONObject webWeiboLogin(String accessToken, String uid,
			String channelId, String clientIp)
			throws Exception {
		LogUtil.log.info(String.format("###begin-webWeiboLogin,clientIp:%s,servername:%s",clientIp)) ;
		JSONObject json = new JSONObject();
		UserInfo retUserinfo = new UserInfo();
		Session session = new Session();
		if(StringUtils.isEmpty(accessToken)||StringUtils.isEmpty(uid)){
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		// my-todo，需要确认第三方配置，设计是否需要更改
		int thirdpartyType = ThirdpartyType.WEIBO.getValue();
		String packageName = Constants.DEFAULT_PACKAGENAME;
		// 转换成typeInt
		int clientTypeInt = AppType.WEB.getValue();
		// 转换成typeInt
		ThirdpartyConf thirdpartyConf = null;
		ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(thirdpartyType, packageName, clientTypeInt);
		if(srt.isSucceed()) {
			thirdpartyConf = srt.getData();
		}
		if(thirdpartyConf == null){
			LogUtil.log.info(String.format("###查询不到相关的第三方登录配置,thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		if(thirdpartyConf.getInUseLogin() != Constants.STATUS_1){
			LogUtil.log.info(String.format("###查询到相关的第三方配置成停用(inUseLogin!=1),thirdpartyType:%s,packageName:%s,clientTypeInt:%s", thirdpartyType,packageName,clientTypeInt));
			throw new LoginBizException(ErrorCode.ERROR_104);
		}
		
		String clientId = thirdpartyConf.getAppId();
		boolean flag = validateWeiboToken(accessToken,clientId);
		if(!flag){
			String errorMsg = "微博web端接入，服务器端accessToken认证不通过";
			LogUtil.log.error("###"+errorMsg);
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
		
		//获取用户个人信息
		WeiboUserInfo weiboUserInfo = weiboAccessService.getInfo(accessToken, uid);
		LogUtil.log.info("###weiboUserInfo:"+JsonUtil.beanToJsonString(weiboUserInfo));
		if(weiboUserInfo == null ||StringUtils.isEmpty(weiboUserInfo.getIdstr())){
			throw new LoginBizException(ErrorCode.ERROR_1); 
		}
		UserInfoDo dbUserInfo = userInfoService.getByWeiboUid(uid);
		//数据库中没有此openid,表示是第一次登录,则插入一条新纪录
		if(dbUserInfo==null) {
			dbUserInfo = saveWeiBoAndUserInfo(weiboUserInfo,channelId,null);
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_1);
			String clientType = HttpUtils.webClient;
			
			// 注册成功后，处理的业务
			doRegistCommonBusiness(dbUserInfo.getUserId(), clientType);
		} else { //数据库中已有此openid,则更新信息
			int userStatus = dbUserInfo.getUserStatus();
			// 用户状态，1-正常; 0-停用;
			if(userStatus != Constants.STATUS_1){
				throw new LoginBizException(ErrorCode.ERROR_103);
			}
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date())); 
			retUserinfo.setIsFirttimeLogin(Constants.STATUS_0);
			userInfoService.updateByUserId(dbUserInfo);
		}
		retUserinfo.setUserId(dbUserInfo.getUserId());
		retUserinfo.setNickName(dbUserInfo.getNickName());
		retUserinfo.setIcon(dbUserInfo.getIcon());
		//登录成功,保存session
		setMemcacheToSessionId(session,dbUserInfo.getUserId());
		session.setFrom(LoginType.WEBWB.getType());//web 微博互联登录
		
		//登录成功后，更新最后登录时间
		if(dbUserInfo!=null&&!StringUtils.isEmpty(dbUserInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(dbUserInfo.getUserId());
			updateInfo.setLastIp(clientIp);
			this.loginUpdateInfo(updateInfo);
			
			//登录成功后做的一些额外业务
			doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
			
			//每次登录成功,都记录历史
			Date nowDate = new Date();
			String serviceLogInfo = Constants.USER_LOGIN_SUCCESS;
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setUserId(dbUserInfo.getUserId());
			serviceLog.setInfo(serviceLogInfo);
			serviceLog.setIp(clientIp);
			serviceLog.setActTime(nowDate);
			serviceLog.setUserName(dbUserInfo.getNickName());
			serviceLog.setClientType(HttpUtils.webClient);
			serviceLogMapper.insert(serviceLog);
		}
		String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),dbUserInfo.getUserId(),clientIp);
		LogUtil.log.info(logInfo);
		json.put(session.getShortName(), session.buildJson() );
		json.put(retUserinfo.getShortName(), retUserinfo.buildJson() );
		return json;
	}

	@Override
	public JSONObject pseudoLogin(String sessionId, String uid, String ip, long time) throws Exception {
		if(StringUtil.isEmpty(sessionId) || StringUtil.isEmpty(uid)) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		JSONObject json = new JSONObject();
		Session session = new Session();
		UserBaseInfo userinfo = new UserBaseInfo();
		
		userinfo.setUserId(uid);
		session.setTime(time);
		session.setFrom(LoginType.PESO.getType());
		session.setSessionid(sessionId);
		
		String pesudoUserName = userCacheInfoService.getAndSetPesudoUserName(uid,ip);
		userinfo.setNickName(pesudoUserName);
		
		json.put(session.getShortName(), session.buildJson());
		json.put(userinfo.getShortName(), userinfo.buildJson());
		return json;
	}
	
	
	@Override
	public void setMemcacheToSessionId(Session session, String userId) {
		long time = System.currentTimeMillis();
		String sessionid = MD5Util.serverEncode(userId + time);
		MemcachedUtil.set(MCPrefix.MC_TOKEN_PREFIX + userId, sessionid,
				MCTimeoutConstants.DEFAULT_TIMEOUT_24H);
		session.setTime(time);
		session.setSessionid(sessionid);
	}

	/**
	 * 自动注册过滤
	 * 
	 * @param clientIp
	 * @throws Exception
	 */
	private void filterAppAutoRegist(String clientIp) throws Exception {
		if (StringUtils.isEmpty(clientIp)) {
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		// 每个ip可注册的总数量
		String cacheKeyOfAppAutoRegistEachIp = MCPrefix.AUTO_REGIST_LIMIT_CACHE
				+ clientIp;
		int eachIpAutoRegistNum = 0;
		Object total = MemcachedUtil.get(cacheKeyOfAppAutoRegistEachIp);
		if (total != null) {
			eachIpAutoRegistNum = Integer.parseInt(total.toString());
			eachIpAutoRegistNum = eachIpAutoRegistNum + 1;
		} else {
			eachIpAutoRegistNum = 1;
		}
		if (eachIpAutoRegistNum > Constants.AUTO_REGIST_IP_TALL_LIMIT) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}

		// 每天可自动注册的总量
		Date nowDate = new Date();
		String dateStr = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD,
				nowDate);
		String cacheKeyOfAppAutoRegistEveryDay = MCPrefix.AUTO_REGIST_LIMIT_DAY_CACHE
				+ dateStr;
		int eachDayAutoRegistNum = 0;
		Object cacheObj = MemcachedUtil.get(cacheKeyOfAppAutoRegistEveryDay);
		if (cacheObj != null) {
			eachDayAutoRegistNum = Integer.parseInt(cacheObj.toString());
			eachDayAutoRegistNum = eachDayAutoRegistNum + 1;
		} else {
			eachDayAutoRegistNum = 1;
		}
		if (eachDayAutoRegistNum > Constants.AUTO_REGIST_IP_DAY_LIMIT) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
	}

	/**
	 * 获取库中的userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	@SuppressWarnings("unchecked")
	private String getUserId() throws Exception{
		String code = "";
		String lockname = LockTarget.LOCK_LOGIN_USERCODE.getLockName();
		try {
			RdLock.lock(lockname);
			List<CodeRandom> list = null;
			String cachekey = MCPrefix.LOGIN_USERCODE_RONDOM_CACHE;
			Object obj = MemcachedUtil.get(cachekey);
			if(obj != null) {
				list = (List<CodeRandom>) obj;
				LogUtil.log.error("### getUserId-从缓存中获取code，剩余code数 = " + list.size());
			} 
			if(list == null || list.size() <= 0) {
				list = codeRandomMapper.listCodeRandom();
				LogUtil.log.error("### getUserId-从DB中获取code，总个数 = " + list.size());
			}
			if(list != null && list.size() >0) {
				CodeRandom cr = list.get(new Random().nextInt(list.size()));
				code = cr.getCode();
				codeRandomMapper.updateStatus(code);
				// 取出code后，从缓存中移除
				list.remove(cr);
				MemcachedUtil.set(cachekey, list);
			} else {
				String errorMsg = "###表t_code_random的code已用完";
				LogUtil.log.error(errorMsg);
				throw new LoginBizException(ErrorCode.ERROR_1);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			RdLock.unlock(lockname);
		}
		return code;
	}
	
	/**
	 * 生成密码
	 * @param obj
	 * @param userId
	 * @param pwd
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	private String getPwd(Object obj,String userId,String pwd){
		String realPwds="";
		if(obj instanceof String){
			Date addTime = DateUntil.getDateByFormat(Constants.DATEFORMAT_YMDHMS_1, (String)obj);
			String time = DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, addTime);
			realPwds = MD5Util.md5(pwd+userId+time);
		}else if(obj instanceof Date){
			String time = DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, (Date)obj);
			realPwds = MD5Util.md5(pwd+userId+time);
		}
		return realPwds;
	}
	
	/**
	 * 转化为客户端
	 * @param clientType
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	private int convertClientType(String clientType) {
		// 默认取通用的type
		int clientTypeInt = AppType.WEB.getValue();;
		if(HttpUtils.androidClient.equals(clientType)){
			clientTypeInt = AppType.ANDROID.getValue();
		}
		return clientTypeInt;
	}
	
	private String convertWechatSex(Integer sex) { 
		// my-todo，确认第三方登录拉取的信息，性别是否都是男女，或者英文
		//普通用户性别，1为男性，2为女性
		String sexStr = "";
		switch(sex){
			case 1:
				sexStr = "men";
				break;
			case 2:
				sexStr = "women";
				break;
			default:
				sexStr = "unkown";
				break;
		}
		return sexStr;
	}
	
	/**
	 * 注册成功后，处理的业务
	 * @param userId
	 * @param clientType
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	private void doRegistCommonBusiness(String userId, String clientType) {
		try {
//			freeGiftRecordService.addFreeGiftQieziForNewRegistUser(userId, clientType);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 更新状态
	 * @param userInfo
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	private void loginUpdateInfo(UserInfoDo userInfo) throws Exception {
		if(userInfo!=null&&!StringUtils.isEmpty(userInfo.getUserId())){
			UserInfoDo updateInfo = new UserInfoDo();
			updateInfo.setUserId(userInfo.getUserId());
			//更新"最后登录时间"
			updateInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date()));
			updateInfo.setLastIp(userInfo.getLastIp());
			userInfoService.updateByUserId(updateInfo);
		}
	}
	
	/**
	 * 登录成功后处理的业务
	 * @param userId
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	private void doLoginSuccessCommonBusiness(String userId) {
		LogUtil.log.info("###doLoginSuccessCommonBusiness,userId:" + userId + ",login service success.");
		// 对用户加锁
		String lockname = LockKey.LOCK_USER_TOOL + userId;
		try {
			RdLock.lock(lockname);
			// 预留业务扩展
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		} finally {
			RdLock.unlock(lockname);
		}
	}
	
	/**
	 * 保存qq信息和用户信息
	 * @author sx
	 * @param qqConnectUserInfoVo qq互联接入实体 ,channelId 渠道
	 * @param deviceProperties
	 * @return userInfo 返回用户信息
	 */
	private UserInfoDo saveQQInfoAndUserInfo(QQConnectUserInfoVo qqConnectUserInfoVo,
			String channelId,DeviceProperties deviceProperties,
			String clentid, String unionid) throws Exception{
		UserInfoDo dbUserInfo = null;
		//40x40像素则是一定会有
		String headImgUrl = qqConnectUserInfoVo.getFigureurl_qq_1();
		String imgName = null;
		//下载微信用户的头像
		try {
			if(!StringUtils.isEmpty(headImgUrl)){
				String filepath = Constants.USER_ICON_URL + File.separator+Constants.ICON_IMG_FILE_URI+File.separator;
				Date nowDate = new Date();
				String iconDateStr = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD_1, nowDate);
				//生成图片文件名 
				imgName = FileTypeUtils.getRandomFileGroupId() + Constants.IMG_JPG;
				// 头像地址按目录分类
				imgName = iconDateStr+File.separator+imgName;
				String imgFileLocalPath = filepath+imgName;
				try {
					HttpUtils.downLoadFile(headImgUrl, imgFileLocalPath);
				} catch (Exception e) {
					imgName = null;
					LogUtil.log.error("###下载头像失败:qq登录");
				}
			}
			
			if(StringUtils.isEmpty(imgName)){ //为空或拿不到,则用默认头像
				//用默认头像
				imgName = Constants.USER_DEFAULT_ICON;
			}
			
			String userId = getUserId();
			//从系统统一地方获取userId
			//保存 userInfo信息
			dbUserInfo = new UserInfoDo();
			String nickName = qqConnectUserInfoVo.getNickname();
			if (!StrUtil.isNullOrEmpty(nickName)) {
				//昵称=微信昵称（昵称过长时，截取前18个字符）
				nickName = StrUtil.jiequStr(nickName,18);
			}
			dbUserInfo.setNickName(nickName);
			dbUserInfo.setUserId(userId);
			dbUserInfo.setSex(qqConnectUserInfoVo.getGender());
			dbUserInfo.setAddTime(new Date());
			dbUserInfo.setActTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date()));
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date()));
			int loginType = LoginType.APPQQ.getType();
			if(StringUtils.isEmpty(channelId)){//web端请求是不带有渠道信息的
				dbUserInfo.setPwd(Constants.DEFAULT_WEBQQ_PWD);
				loginType = LoginType.WEBQQ.getType();
			}else{
				dbUserInfo.setPwd(Constants.DEFAULT_APPQQ_PWD);
			}
			dbUserInfo.setLoginType(loginType);
			dbUserInfo.setIcon(imgName);
			dbUserInfo.setUserStatus(Constants.STATUS_1);
			dbUserInfo.setSf(qqConnectUserInfoVo.getProvince());
			dbUserInfo.setCs(qqConnectUserInfoVo.getCity());
			dbUserInfo.setChannelId(channelId);
			if(deviceProperties!=null){
				String appPackage = deviceProperties.getPackageName();
				dbUserInfo.setAppPackage(appPackage);
				//增加UUID
				if(!StringUtils.isEmpty(deviceProperties.getUuid())){
					dbUserInfo.setUuid(deviceProperties.getUuid());
				}
			}
			userInfoService.insert(dbUserInfo);
			
			//保存qq用户信息
			QQConnectUserInfoDo qqConnectUserInfoDo = new QQConnectUserInfoDo();
			BeanUtils.copyProperties(qqConnectUserInfoDo, qqConnectUserInfoVo);
			qqConnectUserInfoDo.setUserId(userId);
			qqConnectUserInfoDo.setClientid(clentid);
			qqConnectUserInfoDo.setUnionid(unionid);
			qqAccessService.insert(qqConnectUserInfoDo);
     	} catch (Exception e) {
			throw new LoginBizException(ErrorCode.ERROR_105);
		}
		return dbUserInfo;
	}
	
	/**
	 * 获取qq登录请求的相关数据
	 * 此是方法先校验，成功后才返回，否则直接跑出异常提示
	 * @param accessToken 客户端上传
	 * @param openid 客户端上传
	 * @param serverConfAppid 数据库配置的appid
	 * @return
	 * @throws Exception
	 */
	private JSONObject getQQdata(String accessToken, String openid,String serverConfAppid) throws Exception {
		JSONObject oauthMeJson = qqAccessService.getAppOpenidByAccessToken(accessToken);
		String oauthMeOpenid = "";
		if(oauthMeJson.containsKey("openid")) {
			oauthMeOpenid = oauthMeJson.getString("openid");
		}
		String oauthMeAppid = "";
		if(oauthMeJson.containsKey("client_id")) {
			oauthMeAppid = oauthMeJson.getString("client_id");
		}
		String unionid = "";
		if(oauthMeJson.containsKey("unionid")) {
			unionid = oauthMeJson.getString("unionid");
		}
		if(oauthMeJson.containsKey("error")) {
			LogUtil.log.error("###qq_error:"+oauthMeJson.getString("error"));
			throw new LoginBizException(ErrorCode.ERROR_1);
		}
		LogUtil.log.info(String.format("###qq登录,校验appId,openid:%s,serverConfAppid:%s,oauthMeJson:%s",openid ,serverConfAppid,JsonUtil.beanToJsonString(oauthMeJson))) ;
		if(StringUtils.isEmpty(openid) || StringUtils.isEmpty(oauthMeAppid) 
				|| StringUtils.isEmpty(oauthMeOpenid) 
				|| !serverConfAppid.equals(oauthMeAppid)
				|| StringUtils.isEmpty(unionid)) {
			String errorMsg = "qq互联app端接入，服务器端appid,openid，unionid认证不通过";
			LogUtil.log.error("###"+errorMsg);
			throw new LoginBizException(ErrorCode.ERROR_105);
		}
		return oauthMeJson;
	}
	
	/**
	 * 校验微博token
	 * @param accessToken
	 * @param clientId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	private boolean validateWeiboToken(String accessToken, String clientId) throws Exception {
		//参考: http://open.weibo.com/wiki/Oauth2/get_token_info
		StringBuffer sbfUrl = new StringBuffer();
		sbfUrl.append(Constants.WEIBO_TOKEN_URL).append("?")
		.append("access_token=").append(accessToken);
		String url = sbfUrl.toString();
		//请求微信服务器，获取到json字符串
		String responseJsonString = HttpUtils.post(url);
		LogUtil.log.info(String.format("###微博登录,用accessToken查询对应的appKey,accessToken:%s,服务端配置clientId:%s,取到信息:%s",accessToken,clientId,responseJsonString));
		JSONObject json = JsonUtil.strToJsonObject(responseJsonString);
		String weiboServerResponseClientId = json.getString("appkey");
		if((!StringUtils.isEmpty(weiboServerResponseClientId))&&clientId.equals(weiboServerResponseClientId)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 保存微博登录用户信息
	 * @param weiboUserInfo
	 * @param channelId
	 * @param deviceProperties
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月11日
	 */
	private UserInfoDo saveWeiBoAndUserInfo(WeiboUserInfo weiboUserInfo,String channelId,DeviceProperties deviceProperties) throws Exception{
		UserInfoDo dbUserInfo = new UserInfoDo();
		String userId;
		try {
			userId = getUserId();
			String weiboHeadImgUrl = weiboUserInfo.getProfile_image_url();
			String imgName = null;
			//下载微信用户的头像
			if(!StringUtils.isEmpty(weiboHeadImgUrl)){
				String filepath = Constants.USER_ICON_URL+File.separator+Constants.ICON_IMG_FILE_URI+File.separator;
				Date nowDate = new Date();
				String iconDateStr = DateUntil.getFormatDate(Constants.DATEFORMAT_YMD_1, nowDate);
				
				//生成图片文件名 
				imgName = FileTypeUtils.getRandomFileGroupId() + Constants.IMG_JPG;
				
				// 头像地址按目录分类
				imgName = iconDateStr+File.separator+imgName;
				String imgFileLocalPath = filepath+imgName;
	             try {
					HttpUtils.downLoadFile(weiboHeadImgUrl, imgFileLocalPath);
				} catch (Exception e) {
					imgName = null;
					LogUtil.log.error("###下载头像失败:微博登录");
				}
			}
			
			if(StringUtils.isEmpty(imgName)){ //为空或拿不到,则用默认头像
				//用默认头像
				imgName = Constants.USER_DEFAULT_ICON;
			}
			
			//加记录 userInfo
			dbUserInfo = new UserInfoDo();
			String nickName = weiboUserInfo.getScreen_name();
			if (!StrUtil.isNullOrEmpty(nickName)) {
				//昵称=微信昵称（昵称过长时，截取前18个字符）
				nickName = StrUtil.jiequStr(nickName,18);
			}
			dbUserInfo.setNickName(nickName);
			// 微信返回的sex是英文字符，得转换为中文字符
			dbUserInfo.setSex(transerWeiboSex(weiboUserInfo.getGender())) ;
			String location = weiboUserInfo.getLocation(); // "location": "广东 清远",province,city字段返回的是数字
			if(!StringUtils.isEmpty(location)&&location.contains(" ")){
				String[] strArr = location.split(" "); 
				if(strArr!=null&&strArr.length>=2){
					dbUserInfo.setSf(strArr[0]) ;
					dbUserInfo.setCs(strArr[1]) ;
				}
			}
			dbUserInfo.setUserId(userId);
			dbUserInfo.setAddTime(new Date());
			dbUserInfo.setActTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date()));
			dbUserInfo.setLastTime(DateUntil.getFormatDate(Constants.DATEFORMAT_YMDHMS, new Date()));
			int loginType = LoginType.APPWB.getType();
			if(StringUtils.isEmpty(channelId)) {//web端请求是不带有渠道信息的
				dbUserInfo.setPwd(Constants.DEFAULT_WEBWB_PWD);
				loginType = LoginType.WEBWB.getType();
			}else{
				dbUserInfo.setPwd(Constants.DEFAULT_APPWB_PWD);//数据库中设置了不能为空，所以默认给一个(实际是没什么作用的)
			}
			dbUserInfo.setLoginType(loginType);
			dbUserInfo.setIcon(imgName);
			dbUserInfo.setUserStatus(1);
			dbUserInfo.setChannelId(channelId);
			if(deviceProperties!=null){
				String appPackage = deviceProperties.getPackageName();
				dbUserInfo.setAppPackage(appPackage);
				//增加UUID
				if(!StringUtils.isEmpty(deviceProperties.getImei())){
					dbUserInfo.setUuid(deviceProperties.getImei());
				}
			}
			userInfoService.insert(dbUserInfo);
			
			WeiboUserInfoDo weiboUserInfoDo = new WeiboUserInfoDo();
			BeanUtils.copyProperties(weiboUserInfoDo, weiboUserInfo);
			weiboUserInfoDo.setUserId(userId);
			weiboAccessService.insert(weiboUserInfoDo);
		} catch (Exception e) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
		return dbUserInfo;
	}
	
	/**
	 * 微博性别转换
	 * 性别，m：男、f：女、n：未知
	 * @param gender
	 * @return
	 */
	private String transerWeiboSex(String gender) {
		if("m".equals(gender)){
			return "男";
		}
		
		if("f".equals(gender)){
			return "女";
		}
		
		if("n".equals(gender)){
			return "未知";
		}
		
		return null;
	}

}
