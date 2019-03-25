//package com.jiujun.shows.user.service.impl;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.math.RandomUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
//import com.jiujun.shows.app.dao.AppInstallChannelDao;
//import com.jiujun.shows.app.service.AppInstallChannelService;
//import com.jiujun.shows.app.service.IAppInfoService;
//import com.jiujun.shows.attention.domain.UserAttentionDo;
//import com.jiujun.shows.attention.service.IUserAttentionService;
//import com.jiujun.shows.car.domain.SysCarDo;
//import com.jiujun.shows.car.service.IUserCarPortService;
//import com.jiujun.shows.common.constant.Constants;
//import com.jiujun.shows.common.constant.ErrorCode;
//import com.jiujun.shows.common.constant.MCPrefix;
//import com.jiujun.shows.common.constant.MCTimeoutConstants;
//import com.jiujun.shows.common.enums.business.IMBusinessEnum;
//import com.jiujun.shows.common.enums.business.LogicBizEnum;
//import com.jiujun.shows.common.exception.SystemDefinitionException;
//import com.jiujun.shows.common.service.impl.CommonServiceImpl;
//import com.jiujun.shows.common.utils.DateUntil;
//import com.jiujun.shows.common.utils.Email163Util;
//import com.jiujun.shows.common.utils.FileTypeUtils;
//import com.jiujun.shows.common.utils.Helper;
//import com.jiujun.shows.common.utils.HttpUtils;
//import com.jiujun.shows.common.utils.IMutils;
//import com.jiujun.shows.common.utils.IpUtils;
//import com.jiujun.shows.common.utils.JsonUtil;
//import com.jiujun.shows.common.utils.LogUtil;
//import com.jiujun.shows.common.utils.MD5Util;
//import com.jiujun.shows.common.utils.MemcachedUtil;
//import com.jiujun.shows.common.utils.ObjCloneUtil;
//import com.jiujun.shows.common.utils.PageUtils;
//import com.jiujun.shows.common.utils.SQLUtil;
//import com.jiujun.shows.common.utils.SendSMSUtils;
//import com.jiujun.shows.common.utils.SensitiveWordUtil;
//import com.jiujun.shows.common.utils.SpringContextListener;
//import com.jiujun.shows.common.utils.StrUtil;
//import com.jiujun.shows.common.utils.SystemUtil;
//import com.jiujun.shows.common.vo.AnchorInfo;
//import com.jiujun.shows.common.vo.Code;
//import com.jiujun.shows.common.vo.DataRequest;
//import com.jiujun.shows.common.vo.GuardVo;
//import com.jiujun.shows.common.vo.Page;
//import com.jiujun.shows.common.vo.Result;
//import com.jiujun.shows.common.vo.Session;
//import com.jiujun.shows.common.vo.UserBaseInfo;
//import com.jiujun.shows.device.service.DevicePropertiesService;
//import com.jiujun.shows.dynamic.home.domain.DiaryInfo;
//import com.jiujun.shows.dynamic.home.service.IDiaryInfoService;
//import com.jiujun.shows.gift.service.IFreeGiftRecordService;
//import com.jiujun.shows.guard.service.IGuardWorkService;
//import com.jiujun.shows.peach.service.IPeachAnchorService;
//import com.jiujun.shows.room.domain.Decorate;
//import com.jiujun.shows.room.service.IDecorateService;
//import com.jiujun.shows.sys.dao.IServiceLogDao;
//import com.jiujun.shows.sys.domain.ServiceLog;
//import com.jiujun.shows.user.dao.IUserInfoDao;
//import com.jiujun.shows.user.domain.UserAnchor;
//import com.jiujun.shows.user.domain.UserInfo;
//import com.jiujun.shows.user.service.IMedalPackageService;
//import com.jiujun.shows.user.service.IUserAccountBookOutService;
//import com.jiujun.shows.user.service.IUserAccountBookinService;
//import com.jiujun.shows.user.service.IUserAccountService;
//import com.jiujun.shows.user.service.IUserAnchorService;
//import com.jiujun.shows.user.service.IUserCacheInfoService;
//import com.jiujun.shows.user.service.IUserInfoService;
//import com.jiujun.shows.vo.UserCacheInfo;
//import com.jiujun.shows.vo.UserInfoVo;
//
///**
// * @serviceimpl
// * @table t_user_info
// * @date 2015-12-02 10:22:37
// * @author user
// */
//@Service("UserInfoServiceImpl")
//public class UserInfoServiceImpl extends CommonServiceImpl<IUserInfoDao,UserInfo>{
//
//	public static int max_gift_value = 100000; // 每次最大赠送礼物价值1千元RMB(单位:分)
//	public static int min_gift_value = 10; // 最小赠送礼物价值0.01万元RMB
//	
//	@Resource(name="userInfoDao")
//	public void setDao(IUserInfoDao dao){
//		this.dao = dao;
//	}
//	
//	@Resource
//	private IUserCacheInfoService userCacheInfoService;
//	
//	@Resource
//	private IUserInfoService iUserInfoService;
//	
//	@Resource
//	public IFreeGiftRecordService freeGiftRecordService;
//	
//	@Resource
//	private DevicePropertiesService devicePropertiesService;
//	
//	@Resource
//	private IUserAccountService userAccountService;
//	
//	@Resource
//	private AppInstallChannelDao appInstallChannelDao;
//	
//	@Resource
//	private IPeachAnchorService peachAnchorService;
//	
//	@Resource
//	private IUserAccountBookOutService userAccountBookOutService;
//	
//	@Resource
//	private IUserAccountBookinService userAccountBookinService;
//	
//	@Resource
//	private IServiceLogDao serviceLogDao ;
//	
//	@Resource
//	private IUserAnchorService userAnchorService ;
//	
//	@Resource
//	private IDecorateService decorateService;
//	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
//	@Resource
//	private IGuardWorkService guardWorkService;
//	
//	@Resource
//	public IUserAttentionService userAttentionService;
//	
//	@Resource
//	public IAppInfoService appInfoService;
//	
//	@Resource
//	private IDiaryInfoService diaryInfoService;
//	
//	@Resource
//	private IMedalPackageService medalPackageService;
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
//	/** 用户头像地址前缀 */
//	private static final String ICON_PATH = Constants.cdnPath;
//	/**
//	 * U1
//	 * 登录验证
//	 * @param data
//	 * @return
//	 */
//	public  JSONObject verifyLogin(DataRequest data){
//		JSONObject json = new JSONObject();
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		try {
//				if( null == data.getUserBaseInfo() ){
//					LogUtil.log.info("###/U1/data-login-logData-is-null ");
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_2000);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//				
//				//log打印请求数据(不打印密码得把密码remove掉,所以要深度复制)
//				UserBaseInfo logData = ObjCloneUtil.deepClone(data.getUserBaseInfo());
//				if(logData!=null){
//					logData.setPwd(null);//把密码设置为null,为了不显示用户密码在log中
//					LogUtil.log.info("###/U1/data(except:pwd)-login:"+JsonUtil.beanToJsonString(logData));
//				}
//				
//				String reqBindEmail = data.getUserBaseInfo().getBindEmail();
//				String reqBindMobile = data.getUserBaseInfo().getBindMobile();
//				String reqUserId = data.getUserBaseInfo().getUserId();
//				String reqPwd = data.getUserBaseInfo().getPwd();
//				String loginUserNickName = null;
//				//www调用时会传过来,因为此时service端只能取到www的ip,不能取到web用户的真实ip
//				String clientIp = data.getUserBaseInfo().getIp();
//				if(StringUtils.isEmpty(clientIp)){
//					clientIp = IpUtils.getClientIp(data.getRequest());
//				}
//				/*String loginFromIp = HttpUtils.getIpAddress(data.getRequest());
//				String errUserKeyStr = null;
//				if(!StringUtils.isEmpty(reqBindEmail)){
//					errUserKeyStr = reqBindEmail;
//				}else if(!StringUtils.isEmpty(reqBindMobile)){
//					errUserKeyStr = reqBindMobile;
//				}else if(!StringUtils.isEmpty(reqUserId)){
//					errUserKeyStr = reqUserId;
//				}
//				
//				if(!this.iUserInfoService.validateLoginErrorNum(errUserKeyStr)){
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_2076);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}*/
//				
//				//判断密码、用户名是否都不为空
//				if(StringUtils.isEmpty(reqPwd)||(StringUtils.isEmpty(reqBindEmail)&&StringUtils.isEmpty(reqBindMobile)&&StringUtils.isEmpty(reqUserId))){
//					LogUtil.log.error(String.format("###reqPwd:%s,reqBindEmail:%s,reqBindMobile:%s,reqUserId:%s", reqPwd,reqBindEmail,reqBindMobile,reqUserId)); 
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//				Session session = new Session();
//				UserBaseInfo retUserinfo = new UserBaseInfo();
//				UserInfo  dbUserInfo = null;
//				//邮箱登录
//				if( StringUtils.isNotEmpty(reqBindEmail) ){
//					if(matchEmail(reqBindEmail)){
//						dbUserInfo = this.dao.getUserByEmail(reqBindEmail);
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_2003);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//					}
//				//手机登录
//				}else if(StringUtils.isNotEmpty(reqBindMobile)){
//					if(matchMobile(reqBindMobile)){
//						dbUserInfo = this.dao.getUserByMobile(reqBindMobile);
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_2004);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//					}
//				//用户id登录	
//				}else if(StringUtils.isNotEmpty(reqUserId)){
//					Pattern pattern = Pattern.compile("^\\d+$");
//					Matcher match = pattern.matcher(data.getUserBaseInfo().getUserId());
//					if(match.matches()){
//						dbUserInfo = this.dao.getUserByUserId(reqUserId);
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_2031);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//					}
//				}
//			//数据库查不到用户时,提示:用户名或密码错误,不能提示用户不存在
//			if(dbUserInfo==null){
//				/*//记录登录失败的信息,以用于预防暴力破解
//				this.iUserInfoService.recordLoginErrorMsg(loginFromIp,errUserKeyStr);*/
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_2012);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			loginUserNickName = dbUserInfo.getNickName();
//			// 用户状态
//			int userStatus = dbUserInfo.getUserStatus();
//			// 判断用户状态是否正常,用户状态，1-正常; 0-停用; 2-待审核
//			if(userStatus==1){
//				String userId = dbUserInfo.getUserId();
//				String pwd = dbUserInfo.getPwd();
//				//String actDate = dbUserInfo.getActTime();
//				Date addTime = dbUserInfo.getAddTime();
//				String signPwd = getPwd(addTime, userId, reqPwd);
//				//校验密码是否正确
//				if(pwd.equals(signPwd)){
//					long time = setMemcacheToSessionId(userId);
//					retUserinfo.setUserId(userId);
//					retUserinfo.setNickName(dbUserInfo.getNickName());
//					retUserinfo.setIcon(dbUserInfo.getIcon());
//					String lastTimeStr = dbUserInfo.getLastTime();
//					if(StringUtils.isEmpty(lastTimeStr)){ // lastTime字段值为空,说明是第一次登陆
//						retUserinfo.setIsFirttimeLogin(LogicBizEnum.LogicStr.True.getValue());
//					}else{
//						Date lastTime = DateUntil.parse2DefaultFormat(lastTimeStr);
//						String addTimeStr =dbUserInfo.getActTime();
//						String lastIp = dbUserInfo.getLastIp();
//						Date nowDate = new Date();
//						if(StringUtils.isEmpty(lastIp)){
//							retUserinfo.setIsFirttimeLogin(LogicBizEnum.LogicStr.True.getValue());
//						}else{
//							retUserinfo.setIsFirttimeLogin(LogicBizEnum.LogicStr.False.getValue());
//						}
//					}
//					
//					session.setTime(time);
//					session.setFrom(1);
//					session.setSessionid(MD5Util.serverEncode(userId+time));
//					
//					//登录成功后，更新最后登录时间
//					UserInfo updateInfo = new UserInfo();
//					updateInfo.setUserId(dbUserInfo.getUserId());
//					updateInfo.setLastIp(clientIp);
//					this.iUserInfoService.loginUpdateInfo(updateInfo);
//					
//					//每次登录成功,都记录历史
//					Date nowDate = new Date();
//					String serviceLogInfo = IUserInfoServiceImpl.USER_LOGIN_SUCCESS_MSG;
//					ServiceLog serviceLog = new ServiceLog();
//					serviceLog.setUserId(dbUserInfo.getUserId());
//					serviceLog.setInfo(serviceLogInfo);
//					serviceLog.setIp(clientIp);
//					serviceLog.setActTime(nowDate);
//					
//					String clientType = HttpUtils.getClientTypeStr(data.getRequest());
//					serviceLog.setUserName(dbUserInfo.getNickName());
//					serviceLog.setDeviceproperties(JsonUtil.beanToJsonString(data.getDeviceProperties()));
//					serviceLog.setClientType(clientType);
//					
//					serviceLogDao.insert(serviceLog);
//						
//					//登录成功后做的一些额外业务,由于登录入口有注册用户、微信、微博、qq,所以再封装一个共同的业务方法
//					this.iUserInfoService.doLoginSuccessCommonBusiness(dbUserInfo.getUserId());
//					
//				}else{
//					/*//登录失败,此时记录错误统一用userId
//					errUserKeyStr = dbUserInfo.getUserId();
//					//记录登录失败的信息,以用于预防暴力破解
//					this.iUserInfoService.recordLoginErrorMsg(loginFromIp,errUserKeyStr);
//					*/
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_2012);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//			}else{
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_2030);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			
//			String logInfo = String.format("#####用户登录成功,userId:%s,nickName:%s,ip:%s", dbUserInfo.getUserId(),loginUserNickName,clientIp);
//			LogUtil.log.info(logInfo);
//			
//			//记录设备环境参数
//			//devicePropertiesService.recordDeviceProperties(data.getDeviceProperties(),"U1",dbUserInfo.getUserId());
//			json.put(session.getShortName(), session.buildJson() );
//			json.put(retUserinfo.getShortName(), retUserinfo.buildJson());
//			
//		} catch (SystemDefinitionException e) {
//			LogUtil.log.info("###用户登录-用户名或密码错误:"+JsonUtil.beanToJsonString(data.getUserBaseInfo()));
//			LogUtil.log.error(e.getMessage() ,e);
//			result = new Result(e.getErrorCode(),e.getMessage());
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			result = new Result(ErrorCode.ERROR_2008);
//		}
//		
//		json.put(result.getShortName(), result.buildJson() );
//		return json;
//	}
//
//	/**验证Email*/
//	private boolean matchEmail(String email) {
//		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+(\\.?[a-zA-Z0-9_-])+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
//		if( null != email ){
//			Matcher match = pattern.matcher(email);
//			return match.matches();
//		}
//		return false;
//	}
//
//	/**验证手机号码*/
//	private boolean matchMobile(String mobile) {
//		Pattern pattern = Pattern.compile("^[1][34578][0-9]{9}$");
//		if( null != mobile ){
//			Matcher match = pattern.matcher(mobile);
//			return match.matches();
//		}
//		return false;
//	}
//	
//	/**memcache缓存sessionId*/
//	private long setMemcacheToSessionId(String userId) {
//		long time = System.currentTimeMillis(); 
//		MemcachedUtil.set(MCPrefix.MC_TOKEN_PREFIX+userId, MD5Util.serverEncode(userId+time), 
//				Integer.parseInt(SpringContextListener.getContextProValue("mc.data.token.exptime", "24*3600")));//秒
//		LogUtil.log.info(MemcachedUtil.get(MCPrefix.MC_TOKEN_PREFIX+userId));
//		return time;
//	}
//	
//	/**
//	 * U2
//	 * 注册用户
//	 */
//	//public synchronized JSONObject register(DataRequest data){
//	public JSONObject register(DataRequest data){
//		
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		UserBaseInfo userBaseInfo = new UserBaseInfo();
//		UserInfo  dbUserInfo = null;
//		try {
//			if( null != data.getUserBaseInfo()){
//				String reqBindEmail = data.getUserBaseInfo().getBindEmail();
//				String reqBindMobile = data.getUserBaseInfo().getBindMobile();
//				if(StringUtils.isNotEmpty(reqBindEmail)&&StringUtils.isNotEmpty(data.getUserBaseInfo().getPwd())){
//					if(matchEmail(reqBindEmail)){
//						if( data.getCode() != null) {
//							String securityCode = data.getCode().getCode();
//							// 先屏蔽邮箱注册(避免恶意注册)
////						setResult(result, ErrorCode.ERROR_3060);
//							// 1、验证码是否正确
//							String codeKey = Constants.EMAIL_SECURITY_CODE_KEY + reqBindEmail;
//							Object code = MemcachedUtil.get(codeKey);
//							LogUtil.log.error("#### reqBindEmail-securityCode=" + securityCode + ",cacheCode=" + code.toString());
//							if(code != null && code.toString().equals(securityCode)) {
//								// 2、校验邮箱是否已经注册，每个邮箱只能注册一次
//								String emailKey = Constants.EMAIL_SECURITY_KEY + reqBindEmail;
//								Object emailCache = MemcachedUtil.get(emailKey);
//								if(emailCache != null) {
//									// 已经注册过了
//									setResult(result, ErrorCode.ERROR_2038);
//									LogUtil.log.error("#### reqBindEmail-该邮箱已注册，emailCache=" + emailCache.toString());
//								} else {
//									
//									dbUserInfo = dao.getUserByEmail(reqBindEmail);
//									if( null == dbUserInfo){
//										UserInfo userInfo = getUserInfo(data);
//										//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
//										if(data.getDeviceProperties()!=null){
//											String channelId=null;
//											if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36))
//												channelId=appInstallChannelDao.getChannelIdByImei(data.getDeviceProperties().getImei());
//											if(channelId==null)
//												channelId=data.getDeviceProperties().getChannelId();
//											if(channelId!=null)
//												userInfo.setChannelId(channelId);
//											
//											String appPackage = data.getDeviceProperties().getPackageName();
//											userInfo.setAppPackage(appPackage);
//										}
//										this.dao.insert(userInfo);
//										userBaseInfo.setUserId(userInfo.getUserId());
//										userBaseInfo.setBindEmail(reqBindEmail);
//										dbUserInfo = userInfo;
//										// 注册成功，更新缓存
//										// 删除验证码缓存
//										MemcachedUtil.delete(codeKey);
//										// 保存邮箱缓存
//										MemcachedUtil.set(emailKey, reqBindEmail);
//									}else{
//										setResult(result, ErrorCode.ERROR_2006);
//									}
//								}
//							} else {
//								// 验证码已失效
//								setResult(result, ErrorCode.ERROR_2041);
//							}
//						} else {
//							setResult(result, ErrorCode.ERROR_3017);
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_2003);
//					}
//				}else if(StringUtils.isNotEmpty(reqBindMobile)&&StringUtils.isNotEmpty(data.getUserBaseInfo().getPwd())){
//					String clientAppVersionStr = ""; // 默认老版本，web传一个大于当前版本的版本号
//					if(data.getDeviceProperties() != null && data.getDeviceProperties().getAppVersion() != null) {
//						clientAppVersionStr = data.getDeviceProperties().getAppVersion();
//					}
//					
//					int serverReqAppVersionLimitInt = SystemUtil.parseAppVersion("3.1.6");//服务端(注册接口)要求客户端,不低于3.1.6版本
//					int clientAppVersionInt = 0;
//					if(!StringUtils.isEmpty(clientAppVersionStr)){// 请求参数版本号不为空
//						clientAppVersionInt = SystemUtil.parseAppVersion("3.1.6");
//					}
//					
//					// 老版本没有传code，提示升级，不允许注册
//					if(data.getCode() == null || StringUtils.isEmpty(data.getCode().getCode())) { 
//						LogUtil.log.info(String.format("###从版本:[%s]开始,用户注册接口最后一步要求传参数:短信验证码。客户端目前版本[%s],此次请求没有传短信验证码参数,提示用户更新",serverReqAppVersionLimitInt,clientAppVersionInt ));
//						setResult(result, ErrorCode.ERROR_2040); // 则提示用户更新
//						/*if(HttpUtils.iosClient.equals(HttpUtils.getClientTypeDetail(data.getRequest()))){
//							LogUtil.log.info(String.format("##ios端(version:[%s])用户注册最后一步没有传手机验证码,只判断有没触发过发送过短信验证码,后面做优化" ,clientAppVersionStr));
//							String codeCacheKey = Constants.SET_EMAIL_MOBILE_CODE+reqBindMobile;
//							Object codeOfMobile = MemcachedUtil.get(codeCacheKey);
//							if(codeOfMobile==null){
//								setResult(result, ErrorCode.ERROR_2039);
//							}
//						}else{	
//							LogUtil.log.info(String.format("###从版本:[%s]开始,用户注册接口最后一步要求传参数:短信验证码。客户端目前版本[%s],此次请求没有传短信验证码参数,提示用户更新",serverReqAppVersionLimitInt,clientAppVersionInt ));
//							setResult(result, ErrorCode.ERROR_2040); // 则提示用户更新
//						}*/
//					} else {
//						Pattern pattern = Pattern.compile("^[1][34578][0-9]{9}$");
//						Matcher match = pattern.matcher(reqBindMobile);
//						if(match.matches()){
//							// 手机注册,检测缓存是否有值(触发过发送手机验证码,实质上还没彻底屏蔽批量注册)
//							// 手机验证码缓存，在校验图片验证码后生成的、并发送到手机的验证码
//							// 3.1.7版本之后，需要校验验证码
//							String securityCode = data.getCode().getCode();
//							String codeCacheKey = Constants.SET_EMAIL_MOBILE_CODE+reqBindMobile;
//							Object codeOfMobile = MemcachedUtil.get(codeCacheKey);
//							if(codeOfMobile == null || !securityCode.equals(codeOfMobile.toString())){
//								setResult(result, ErrorCode.ERROR_2039);
//							}else{
//								// 校验验证码
//								dbUserInfo = dao.getUserByMobile(reqBindMobile);
//								if( null == dbUserInfo){
//									UserInfo userInfo = getUserInfo(data);
//									//记录渠道id、来源包
//									if(data.getDeviceProperties()!=null){
//										String channelId=null;
//										
//										//32位以上的UUID才用之前记录的渠道号，否则用刚上传上来的渠道号
//										if(!StringUtils.isEmpty(data.getDeviceProperties().getImei()) && (data.getDeviceProperties().getImei().length()==32 || data.getDeviceProperties().getImei().length()==36))
//											channelId = appInstallChannelDao.getChannelIdByImei(data.getDeviceProperties().getImei());
//										if(channelId==null)
//											channelId=data.getDeviceProperties().getChannelId();
//										if(channelId!=null)
//											userInfo.setChannelId(channelId);
//										//userInfo.setChannelId(appInstallChannelDao.getChannelIdByImei(data.getDeviceProperties().getImei()));
//										String appPackage = data.getDeviceProperties().getPackageName();
//										userInfo.setAppPackage(appPackage);
//									}
//									this.dao.insert(userInfo);
//									userBaseInfo.setUserId(userInfo.getUserId());
//									userBaseInfo.setBindMobile(reqBindMobile);
//									dbUserInfo = userInfo;
//									// 注册成功后，删除验证码缓存
//									MemcachedUtil.delete(codeCacheKey);
//								}else{
//									setResult(result, ErrorCode.ERROR_2006);
//								}
//							}
//						}else{
//							setResult(result, ErrorCode.ERROR_2004);
//						}
//					}
//				}else{
//					setResult(result, ErrorCode.ERROR_2005);
//				}
//				
//			//	LogUtil.log.info("###regist data.getRequest():"+data.getRequest());
//			}else{
//				setResult(result, ErrorCode.ERROR_2000);
//			}
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//		}
//		
//
//		//注册成功
//		if(result.getResultCode()==0){
//			/*
//			//送免费礼物
//			try {
//				if(data.getRequest()!=null&&userBaseInfo!=null&&StringUtils.isNotEmpty(userBaseInfo.getUserId())){
//					Boolean isAppClient = HttpUtils.testIfAppClientType(data.getRequest());
//					LogUtil.log.info("###isAppClient:"+isAppClient);
//					if(isAppClient){
//						freeGiftRecordService.addFreeGift(userBaseInfo.getUserId(), 2);
//					}else{
//						freeGiftRecordService.addFreeGift(userBaseInfo.getUserId(), 1);
//					}
//				}else{//预防app调用时拿到的request is null
//					LogUtil.log.error("###userBaseInfo.getUserId is null , donot addFreeGift");
//				}
//			} catch (Exception e) {
//				LogUtil.log.error(e.getMessage() ,e);
//			}*/
//			
//			String clientType = HttpUtils.webClient;
//			this.iUserInfoService.doRegistCommonBusiness(dbUserInfo.getUserId(), clientType);
//			
//			//注册成功,记录设备环境参数
//			devicePropertiesService.recordDeviceProperties(data.getDeviceProperties(),"U2",userBaseInfo.getUserId());
//		}
//		
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(),result.buildJson());
//		json.put(userBaseInfo.getShortName(),userBaseInfo.buildJson());
//		
//		//打印注册数据
//		UserBaseInfo logData = data.getUserBaseInfo();
//		if(logData!=null){
//			logData.setPwd(null);//把密码设置为null,为了不显示在log中
//			LogUtil.log.info("###/U2/data-regist:"+JsonUtil.beanToJsonString(logData));
//		}else{
//			LogUtil.log.info("###/U2/data-regist-data is null");
//		}
//		return json;
//	}
//	
//	/**设置userInfo对象
//	 * @throws Exception */
//	private UserInfo getUserInfo(DataRequest data) throws Exception {
//		if( null != data ){
//			Date addTime = new Date();
//			UserInfo userInfo = new UserInfo();
//			//userInfo.setUserId(getUserId());
//			userInfo.setUserId(iUserInfoService.getUserId());
//			userInfo.setBindMobile(data.getUserBaseInfo().getBindMobile());
//			userInfo.setBindEmail(data.getUserBaseInfo().getBindEmail());
//			userInfo.setPwd(getPwd(addTime, userInfo.getUserId(), data.getUserBaseInfo().getPwd()));
//			userInfo.setAddTime(addTime);
//			userInfo.setNickName(userInfo.getUserId());
//			userInfo.setUserStatus(1);
//			userInfo.setSex("未知");
//			userInfo.setIcon(Constants.USER_DEFAULT_ICON);
//			return userInfo;
//		}
//		return null;
//	}
//	
//	/**
//	 * U3
//	 * 注销 退出
//	 * @param data
//	 * @return
//	 */
//	public JSONObject exit(DataRequest data){
//		Result result = new Result();
//		if( null != data.getUserBaseInfo() && null != data.getSession()){
//			if(data.getUserBaseInfo().getUserId().length() > 0 ){
//				String sessionId = (String) MemcachedUtil.get(MCPrefix.MC_TOKEN_PREFIX+data.getUserBaseInfo().getUserId());
//				if(  null != sessionId && data.getSession().getSessionid().equals(sessionId)){
//					MemcachedUtil.delete(MCPrefix.MC_TOKEN_PREFIX+data.getUserBaseInfo().getUserId());//清除session
//					setResult(result, ErrorCode.SUCCESS_0);
//				}else{
//					setResult(result, ErrorCode.ERROR_2007);
//				}
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U4
//	 * 发送邮箱验证码
//	 * @param data
//	 * @return
//	 */
//	public JSONObject getCodebyEmail(DataRequest data){
//		Result result = new Result();
//		if( null != data.getUserBaseInfo() && 1 == data.getCode().getFrom()){
//			if(matchEmail(data.getUserBaseInfo().getBindEmail()) && 1 == data.getCode().getFrom()){
////				Object code = MemcachedUtil.get(Constants.SECURITY_CODE_KEY+data.getUserBaseInfo().getBindEmail());
////				if( null != code  && code.toString().equals(data.getCode().getCode())){
//					StringBuffer scode = new StringBuffer();
//					for(int i = 0 ; i < 6 ; i++ ){
//						scode.append(RandomUtils.nextInt(10));
//					}
//					String content = "验证码："+scode.toString()+"，感谢您注册帐号，请在10分钟内完成注册。工作人员不会向您索取验证码，请勿泄露。消息来自：美女直播";
//					try {
////						EMailUtils.sendEmail("蜜桃直播", content,data.getUserBaseInfo().getBindEmail());
//						Email163Util.send(data.getUserBaseInfo().getBindEmail(), "美女直播", content);
//						// 缓存验证码，2分钟
//						MemcachedUtil.set(Constants.EMAIL_SECURITY_CODE_KEY+data.getUserBaseInfo().getBindEmail(), scode.toString(),60*30);
//					} catch (Exception e) {
//						setResult(result, ErrorCode.ERROR_2008);
//						LogUtil.log.info("发送验证码邮件失败", e);
//					}
////				}else{
////					setResult(result, ErrorCode.ERROR_3004);
////				}
//			}else{
//				setResult(result, ErrorCode.ERROR_2003);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U5
//	 * 发送手机验证码
//	 * @param data
//	 * @return
//	 */
//	public JSONObject getCodebyMobile(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		if( null != data.getUserBaseInfo() && null != data.getCode() ){
//			if(matchMobile(data.getUserBaseInfo().getBindMobile()) && 2 == data.getCode().getFrom()){
//				Object num  = MemcachedUtil.get(Constants.MOBILE_NUM_KEY+data.getUserBaseInfo().getBindMobile());
//				int count = Integer.parseInt(SpringContextListener.getContextProValue("sentCount", "10"));
//				int ipcount = Integer.parseInt(SpringContextListener.getContextProValue("getIpCount", "100"));
//				boolean isMobileTrue = false;
//				boolean isIpTrue = false;
//				//手机号对应的发送次数
//				if( null != num && Integer.parseInt(num.toString()) >= count ){
//					setResult(result, ErrorCode.ERROR_3014);
//				}else if( null == num ){
//					MemcachedUtil.set(Constants.MOBILE_NUM_KEY+data.getUserBaseInfo().getBindMobile(),0,6*60*60);
//					isMobileTrue = true;
//				}else{
//					MemcachedUtil.set(Constants.MOBILE_NUM_KEY+data.getUserBaseInfo().getBindMobile(),Integer.parseInt(num.toString())+1,6*60*60);
//					isMobileTrue = true;
//				}
//				//ip对应的发送次数
//				Object ipnum = MemcachedUtil.get(Constants.IP_NUM_KEY+data.getRequest().getRemoteAddr());
//				if( null != ipnum && Integer.parseInt(ipnum.toString()) >= ipcount ){
//					setResult(result, ErrorCode.ERROR_3014);
//				}else if( null == ipnum ){
//					MemcachedUtil.set(Constants.IP_NUM_KEY+data.getRequest().getRemoteAddr(),0,6*60*60);
//					isIpTrue = true;
//				}else{
//					MemcachedUtil.set(Constants.IP_NUM_KEY+data.getRequest().getRemoteAddr(),Integer.parseInt(num.toString())+1,6*60*60);
//					isIpTrue = true;
//				}
//				
//				if( isMobileTrue && isIpTrue ){
//					//验证码的验证
//					Object code = MemcachedUtil.get(Constants.SECURITY_CODE_KEY+data.getUserBaseInfo().getBindMobile());
//					if( null != code  && code.toString().equals(data.getCode().getCode())){
//						StringBuffer scode = new StringBuffer();
//						for(int i = 0 ; i < 6 ; i++ ){
//							scode.append(RandomUtils.nextInt(10));
//						}
//						String content = "验证码："+scode.toString()+"，感谢您注册帐号，请在30分钟内完成注册。工作人员不会向您索取验证码。";
//						boolean isSend = SendSMSUtils.sendSMS(data.getUserBaseInfo().getBindMobile(), content);
//						MemcachedUtil.set(Constants.SET_EMAIL_MOBILE_CODE+data.getUserBaseInfo().getBindMobile(), scode.toString(),30*60);
//						if(!isSend){
//							isSend = SendSMSUtils.sendSMSTwo(data.getUserBaseInfo().getBindMobile(), content);
//							if(!isSend) {
//								setResult(result, ErrorCode.ERROR_2009);
//							}
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_3004);
//					}
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_2004);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U6
//	 * 伪登录
//	 * @return
//	 */
//	public JSONObject pseudoLogin(DataRequest data){
//		Result result = new Result();
//		JSONObject json = new JSONObject();
//		try {
//			Session session = new Session();
//			UserBaseInfo userinfo = new UserBaseInfo();
//			if( null != data.getUserBaseInfo() ){
//				if( data.getUserBaseInfo().getMd5Code().equals(SpringContextListener.getContextProValue("md5Code", "3600"))){
//					long time = System.currentTimeMillis(); 
//					String sessionId =  Constants.PSEUDO_LOGIN_SESSION_KEY+ MD5Util.serverEncode(data.getRequest().getSession().getId() + time);
//					
//					String uid = Constants.PSEUDO_LOGIN_SESSION_KEY+MD5Util.md5(data.getRequest().getSession().getId()+time);
//					
//					MemcachedUtil.set(MCPrefix.MC_TOKEN_PREFIX+uid, sessionId, 
//							Integer.parseInt(SpringContextListener.getContextProValue("mc.data.token.exptime", "3600")));//秒
//					userinfo.setUserId(uid);
//					session.setTime(time);
//					session.setFrom(1);
//					session.setSessionid(sessionId);
//					setResult(result, ErrorCode.SUCCESS_0);
//					//客户端ip
//					String ip = data.getUserBaseInfo().getIp();
//					if (StrUtil.isNullOrEmpty(ip)) {//ip为空时为app请求
//						ip = IpUtils.getClientIp(data.getRequest());
//					}
//					LogUtil.log.info(String.format("###pseudoLogin U6 伪登陆客户端ip=%s",ip));
//					String pesudoUserName = userCacheInfoService.getAndSetPesudoUserName(uid,ip);
//					LogUtil.log.info(String.format("###pseudoLogin U6 伪登陆客户端nickName=%s",pesudoUserName));
//					//返回游客名称
//					userinfo.setNickName(pesudoUserName);
//					
//				}else{
//					setResult(result, ErrorCode.ERROR_2010);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_2000);
//			}
//			
//			json.put(session.getShortName(), session.buildJson());
//			
//			json.put(userinfo.getShortName(), userinfo.buildJson());
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage(),e);
//			setResult(result, ErrorCode.ERROR_2008);
//		}
//		
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	private String getUserId(){
//		String code = "";
//		synchronized(this){
//			String sql = "select code from t_code_random ORDER BY RAND() limit 1";
//			Map<String,Object> map = this.dao.selectOne(sql);
//			code = (String) map.get("code");
//			String usql = "update t_code_random set isUserUse = 1 where code='"+code+"'";
//			this.dao.updateBySql(usql);
//		}
//		return code;
//	}
//	
//	public String getPwd(Object obj,String userId,String pwd){
//		String realPwds="";
//		if(obj instanceof String){
//			Date addTime = DateUntil.getDateByFormat("yyyy-MM-dd HH:mm:ss.SS", (String)obj);
//			String time = DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", addTime);
//			realPwds = MD5Util.md5(pwd+userId+time);
//		}else if(obj instanceof Date){
//			String time =DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", (Date)obj);
//			realPwds = MD5Util.md5(pwd+userId+time);
//		}
//		return realPwds;
//	}
//	
//	/**
//	 * U7获取你关注的 主播或用户列表
//	 */
//	public JSONObject getAttentionAnchorInfo(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONArray retArray = new JSONArray();
//		JSONObject json = new JSONObject();
//		if( null != data.getUserBaseInfo() || data.getPage() == null){
//			if( null != data.getUserBaseInfo().getUserId() ){
//				// 2016-11-30
//				// 优化里，增加缓存，当用户有新关注或者取消关注时，（U8接口）再删除缓存
//				Page page = data.getPage();
//				String userId = data.getUserBaseInfo().getUserId();
//				LogUtil.log.error("### getAttentionAnchorInfo-获取用户关注列表，begin....userId="+userId);
//				String key = MCPrefix.USER_ATTENTION_ALL_CACHE + userId;
//				Object obj = MemcachedUtil.get(key);
//				Map<String, JSONArray> m = null;
//				if(obj != null) {
//					m = (Map) obj;
//					LogUtil.log.error("### getAttentionAnchorInfo-获取用户关注列表，从缓存中获取数据。。。userId="+userId);
//				} else {
//					m = new HashMap<String, JSONArray>();
//					JSONArray jsonArray = new JSONArray();
//					StringBuffer sql = new StringBuffer();
//					List<Map<String,Object>> list = null;
//					/*sql.append("select distinct " +
//						"a.userId,c.nickName,a.roomId,c.icon,ifNull(d.status,0) status,c.remark " +
//						"from t_user_anchor a " +
//						"inner join t_user_attention b on (a.userId=b.toUserId) " +
//						"inner join t_user_info c on (a.userId = c.userId) " +
//						"left join t_live_record d on (a.userId = d.userId and d.status =1 ) " +*/
//					sql.append("select distinct ")
//					//	.append(" a.userId,c.nickName,a.roomId,c.icon,ifNull(d.status,0) status,c.remark,e.userLevel,e.anchorLevel, IFNULL(f.cnt,0) attentionCount  ")
//					.append(" a.userId,c.nickName,a.roomId,c.icon,CASE WHEN ISNULL(d.roomId) THEN 2 ELSE 1 END status,c.remark,e.userLevel,e.anchorLevel ")
//					// 2016-11-30
//					// 这里的关注先去掉，太耗性能
////								", IFNULL(f.cnt,0) attentionCount  ")
//					.append(" from t_user_anchor a ")
//					.append(" inner join t_user_attention b on (a.userId=b.toUserId) ")
//					.append(" inner join t_user_info c on (a.userId = c.userId) ")
//					//.append(" left join t_live_record d on (a.userId = d.userId and d.status =1 ) ")
//					.append(" left join t_live_record_cdn d on (a.userId=d.userId and d.endTime is null) " )
//					.append(" left join t_user_account e on (a.userId = e.userId) ")
//					// 2016-11-30
//					// 这里的关注先去掉，太耗性能
////						.append(" LEFT JOIN (SELECT toUserId,COUNT(*) cnt FROM t_user_attention GROUP BY toUserId) f ON (a.userId=f.toUserId) ")
//					.append(" where b.userId='")
//					.append(data.getUserBaseInfo().getUserId())
//					.append("'");
//					
////						LogUtil.log.debug("U7获取你关注的 主播: " + sql);
//					LogUtil.log.error("### getAttentionAnchorInfo-获取用户关注列表，关注的主播，从db中拿数据。。。sql="+sql);
////						PageUtils.setPage(data.getPage());
////						int count = getCountBySQL(sql.toString());
////						data.getPage().setCount(count+"");
////						int sum = PageUtils.getNum(count,Integer.parseInt(data.getPage().getPagelimit()));
////						if( Integer.parseInt(data.getPage().getPageNum()) <= sum){
////							list = this.dao.selectList(SQLUtil.getLimitSqlByPage(data.getPage(), sql.toString()));
////							for( Map<String,Object> map: list){
////								com.jiujun.shows.common.vo.AnchorInfo anchorInfo = Helper.map2Object(map, com.jiujun.shows.common.vo.AnchorInfo.class);
////								anchorInfo.setIsAnchor("1");
////								jsonArray.add(anchorInfo.buildJson());
////							}
////						}
//					
//					list = this.dao.selectList(sql.toString());
//					if(list != null && list.size() >0) {
//						for( Map<String,Object> map: list){
//							com.jiujun.shows.common.vo.AnchorInfo anchorInfo = Helper.map2Object(map, com.jiujun.shows.common.vo.AnchorInfo.class);
//							anchorInfo.setIsAnchor("1");
//							jsonArray.add(anchorInfo.buildJson());
//						}
//					}
//						
//					try{
//						List<UserAttentionDo> attentions = this.userAttentionService.findAttentionUser(userId);
//						LogUtil.log.error("### getAttentionAnchorInfo-获取用户关注列表-关注的用户，从db中拿数据。。。attentions=" + attentions.size());
//						if(attentions != null && attentions.size() >0) {
//							boolean isattentionUser = false;
//							// 获取用户关注的所有主播
//							List<String> anchors = this.userAttentionService.findAttentionAnchor(userId);
//							for(UserAttentionDo user : attentions) {
//								String toUserId = user.getToUserId();
//								if(anchors != null && anchors.size() >0) {
//									if(anchors.contains(toUserId)) {
////										LogUtil.log.error("### getAttentionAnchorInfo-获取关注用户列表，关注的是主播，忽略，toUserId="+toUserId);
//										continue;
//									}
//									isattentionUser = true;
//								} else {
//									isattentionUser = true;
//								}
//								
//								// 获取用户详细信息
//								if(isattentionUser) {
//									UserCacheInfo vo = this.userCacheInfoService.getOrUpdateUserInfoFromCache(toUserId);
//									if(vo == null) {
//										LogUtil.log.error("### getAttentionAnchorInfo-获取关注用户列表,获取关注用户详细信息失败,toUserId="+toUserId);
//										continue;
//									}
//									AnchorInfo info = new AnchorInfo();
//									info.setUserId(toUserId);
//									info.setNickName(vo.getNickname());
//									info.setUserLevel(vo.getUserLevel());
//									info.setIsAnchor("0");
//									info.setIcon(vo.getAvatar());
//									jsonArray.add(info.buildJson());
//								}
//							}
//						}
//					} catch(Exception e) {
//						LogUtil.log.error(e.getMessage(),e);
//					}
//					m.put("data", jsonArray);
//					MemcachedUtil.set(key, m, MCTimeoutConstants.USER_ATTENTION_ANCHOR_CACHE);
//				}
//				
//				int pageNum = Integer.parseInt(page.getPageNum()); // 页码
//				int pageSize = Integer.parseInt(page.getPagelimit()); // 单页容量
//				
//				// 关注不做分页处理，全部返回
//				// 封装列表数据
//				if(m != null) {
//					if(m.containsKey("data") && m.get("data") != null) {
//						retArray = (JSONArray) m.get("data");
////						if(arrayData != null && arrayData.size() >0) {
//							int all = retArray.size();
//							page.setCount(all + "");
//							
//							// 从哪里开始
////							int index = pageNum >1 ? (pageNum - 1) * pageSize : 0;
////							LogUtil.log.error("###getAttentionAnchorInfo-获取关注分页数据，总数:" + all + ",从第:"+index + "条开始");
////							for(int i=0;i<all;i++) {
////								if(all <= index){
////									LogUtil.log.error("###getAttentionAnchorInfo-获取关注分页数据，all=" + all + ",index="+index);
////									break; //防止越界
////								}
////								retArray.add(arrayData.get(i));
////								index++;
////							}
////						}
//					}
//				}
//				json.put(data.getPage().getShortName(), page.buildJson());
//				LogUtil.log.error("### getAttentionAnchorInfo-获取用户关注列表，end!userId="+userId);
//			}else{
//				setResult(result, ErrorCode.ERROR_3000);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		json.put(result.getShortName(), result.buildJson());
//		json.put(Constants.DATA_BODY, retArray);
//		return json;
//	}
//	
//	/**
//	 * U8 关注|取消关注 接口
//	 */
//	public JSONObject attentionUserId(DataRequest data){
////		synchronized (UserAttentionDo.class) {
//			Result result = new Result(ErrorCode.SUCCESS_0);
//			if( null != data.getUserBaseInfo() && null != data.getAnchorInfo()){
//				if( null != data.getUserBaseInfo().getUserId() && null != data.getAnchorInfo().getUserId()){
//					// 区分主播和用户
//					String toUserId = data.getAnchorInfo().getUserId();
//					String isAnchor = "1";
//					try {
//						UserAnchor vo = this.userAnchorService.getAnchorFromCacheByUserId(toUserId);;
//						if(vo == null) {
//							isAnchor = "0";
//						}
//					} catch (Exception e2) {
//						LogUtil.log.error(e2.getMessage(),e2);
//					}
//					boolean isAnchorFlag = true; // 是否主播，老版本只有主播，默认为主播，不影响老版本
//					if(!"1".equals(isAnchor)) {
//						isAnchorFlag = false;
//					}
//					
//					// 2016-12-28
//					// 优化这段代码
////					String selectSql = "select * from t_user_attention where userId='"+data.getUserBaseInfo().getUserId() +"' and toUserId='"+data.getAnchorInfo().getUserId()+"'";
////					Map<String, Object> map = this.selectOneBysql(selectSql);
//					String userId = data.getUserBaseInfo().getUserId();
//					if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId)) {
//						setResult(result, ErrorCode.ERROR_3017);
//					} else {
//						Date now = new Date();
//						UserAttentionDo attention = userAttentionService.findAttentions(userId, toUserId);
//						if(data.getAnchorInfo().getAttention() == 1 ){
////						if( null == map || map.size() <= 0){
////							String sql = "insert t_user_attention (userId,toUserId,addTime) value ("+data.getUserBaseInfo().getUserId()+","+data.getAnchorInfo().getUserId()+",'"+DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date())+"')";
////							int n = this.dao.insertBySql(sql);
////							if( n <= 0 ){
////								setResult(result, ErrorCode.ERROR_3004);
////							}
//							
//							if(attention == null) {
//								UserAttentionDo vo = new UserAttentionDo();
//								vo.setUserId(userId);
//								vo.setToUserId(toUserId);
//								vo.setAddTime(now);
//								userAttentionService.insert(vo);
//								
//								// 2016-11-30
//								// 操作成功，则删除缓存
//								String key = MCPrefix.USER_ATTENTION_ALL_CACHE + userId;
//								MemcachedUtil.delete(key);
//								// 删除粉丝列表缓存
////								String fansKey = MCPrefix.USER_FANS_ALL_CACHE + userId;
////								MemcachedUtil.delete(fansKey);
//								
//								// 删除被关注粉丝列表缓存
//								String fansKey = MCPrefix.USER_FANS_ALL_CACHE + toUserId;
//								MemcachedUtil.delete(fansKey);
//								
//								if(isAnchorFlag) {
//									try {
//										this.userAnchorService.modifyAnchorFansCount(toUserId, 1);
//									} catch (Exception e) {
//										LogUtil.log.info(e.getMessage(),e);
//									}
//									// 2016-12-28
//									// 新需求，关注成功后，为主播推送一条消息
//									UserInfoVo user = null;
//									StringBuffer msg = new StringBuffer();
//									String userName = "";
//									UserAnchor anchor = userAnchorService.getAnchorFromCacheByUserId(toUserId);
//									String roomId = anchor.getRoomId();
//									try {
//										user = userCacheInfoService.getInfoFromCache(userId, null);
//									} catch (Exception e1) {
//										LogUtil.log.error("###attentionUserId - 关注主播，发送消息，获取用户信息失败");
//									}
//									if(user != null) {
//										userName = user.getNickname();
//									}
//									msg.append(userName).append("已关注了美丽可爱的你，加油呦~~~");
//									String senderUserId = Constants.SYSTEM_USERID_OF_IM;
//									int imType = IMBusinessEnum.ImTypeEnum.IM_11001_Attention.getValue();
//									JSONObject content = new JSONObject();
//									content.put("msg", msg.toString());
//									
//									JSONObject imData = new JSONObject();
//									imData.put("msgtype", 2); 
//									imData.put("targetid", roomId);
//									imData.put("type", imType);
//									imData.put("content", content);
//									
//									int funID = IMBusinessEnum.FunID.FUN_11001.getValue();
//									int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
//									try {
//										LogUtil.log.info("####attentionUserId-发送通知：msg=" + msg);
//										IMutils.sendMsg2IM(funID, seqID, imData,senderUserId);
//									} catch (Exception e) {
//										LogUtil.log.error(e.getMessage(), e);
//									}
//								}
//							}else{
//								setResult(result, ErrorCode.ERROR_3012);
//							}
//						}else if(data.getAnchorInfo().getAttention() == 0 ){
////						if(null != map && map.size() > 0 ){
////							String sql = "delete from t_user_attention  where userId='"+data.getUserBaseInfo().getUserId()+"' and toUserId='"+data.getAnchorInfo().getUserId()+"'";
////							int n = this.dao.deleteBySql(sql);
////							if( n <= 0 ){
////								setResult(result, ErrorCode.ERROR_3004);
////							}
////							
//							
//							if(attention != null) {
//								int id = attention.getId();
//								userAttentionService.removeById(id);
//								// 2016-11-30
//								// 操作成功，则删除缓存
//								String key = MCPrefix.USER_ATTENTION_ALL_CACHE + userId;
//								MemcachedUtil.delete(key);
//								// 删除被关注用户粉丝列表缓存
//								String fansKey = MCPrefix.USER_FANS_ALL_CACHE + toUserId;
//								MemcachedUtil.delete(fansKey);
//								
//								if(isAnchorFlag) {
//									try {
//										this.userAnchorService.modifyAnchorFansCount(toUserId, -1);
//									} catch (Exception e) {
//										LogUtil.log.info(e.getMessage(),e);
//									}
//								}
//								
//							}else{
//								setResult(result, ErrorCode.ERROR_3013);
//							}
//						}else{
//							setResult(result, ErrorCode.ERROR_3003);
//						}
//					}
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_2000);
//			}
//			JSONObject json = new JSONObject();
//			json.put(result.getShortName(),result.buildJson());
//			return json;
////		}
//	}
//	
//	
//	
//	@SuppressWarnings("unchecked")
//	private Map<String,Object> getGiftInfo(int giftId){
//		Object data = MemcachedUtil.get(MCPrefix.GIFT_INFO_KEY+giftId);
//		Map<String, Object> map = null;
//		if( null == data){
//			String sql = "select * from t_gift where id="+giftId;
//			LogUtil.log.info(sql);
//			map = this.dao.selectOne(sql);
//			if( null != map){
//				MemcachedUtil.set(MCPrefix.GIFT_INFO_KEY+giftId, map,MCTimeoutConstants.GIFT_INFO_TIMEOUTSECOND);
//			}else{
//				return null;
//			}
//		}else{
//			map = (Map<String, Object>) data;
//		}
//		return map;	
//	}
//
//	/**
//	 * 找回密码
//	 * C10 获取用户基本信息根据 userId,emial，mobile
//	 */
//	public JSONObject getUserBaseInfo(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		UserBaseInfo userBaseInfo = new UserBaseInfo();
//		if( null != data.getUserBaseInfo()){
//			if( null != data.getUserBaseInfo().getUserId()){
//				Pattern pattern = Pattern.compile("^\\d+$");
//				Matcher match = pattern.matcher(data.getUserBaseInfo().getUserId());
//				boolean isTrue = false;
//				Map<String,Object> map = null;
//				if(match.matches()){
//					String sql = "select a.userId,a.bindEmail,a.bindMobile,a.addTime from t_user_info a where a.userId = '"+data.getUserBaseInfo().getUserId()+"'";
//					map = this.dao.selectOne(sql);
//					if( null != map && map.size() >0 ){
//						isTrue = true;
//					}
//				}
//				if(matchEmail(data.getUserBaseInfo().getUserId())){
//					String sql = "select a.userId,a.bindEmail,a.bindMobile,a.addTime from t_user_info a where a.bindEmail = '"+data.getUserBaseInfo().getUserId()+"'";
//					map = this.dao.selectOne(sql);
//					if( null != map && map.size() >0 ){
//						isTrue = true;
//					}
//				}
//				if(matchMobile(data.getUserBaseInfo().getUserId())){
//					String sql = "select a.userId,a.bindEmail,a.bindMobile,a.addTime from t_user_info a where a.bindMobile = '"+data.getUserBaseInfo().getUserId()+"'";
//					map = this.dao.selectOne(sql);
//					if( null != map && map.size() >0 ){
//						isTrue = true;
//					}
//				}
//				if(isTrue){
//					MemcachedUtil.set(Constants.USERBASE_INFO_KEY+data.getUserBaseInfo().getUserId(), map);
//					userBaseInfo.setUserId(map.get("userId").toString());
//					userBaseInfo.setBindEmail(map.get("bindEmail") == null ?"":map.get("bindEmail").toString());
//					userBaseInfo.setBindMobile(map.get("bindMobile") == null ?"":map.get("bindMobile").toString());
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_3003);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		json.put(userBaseInfo.getShortName(), userBaseInfo.buildJson());
//		return json;
//	}
//	
//	/**
//	 * C11 验证 校验码
//	 */
//	public JSONObject verifyChecksum(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		if( null != data.getCode() && null != data.getUserBaseInfo() ){
//			if( null != data.getCode().getCode()){
//				if(null != data.getUserBaseInfo().getBindEmail() && data.getUserBaseInfo().getBindEmail().length() > 0 && 1 == data.getCode().getFrom()){
//					if(matchEmail(data.getUserBaseInfo().getBindEmail())){
//						Object code = MemcachedUtil.get(Constants.EMAIL_SECURITY_CODE_KEY+data.getUserBaseInfo().getBindEmail());
//						if( null == code || !code.toString().equals(data.getCode().getCode())){
//							setResult(result, ErrorCode.ERROR_3007);
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_2003);
//					}
//				}else if(null != data.getUserBaseInfo().getBindMobile() && data.getUserBaseInfo().getBindMobile().length() > 0 && 2 == data.getCode().getFrom()){
//					if(matchMobile(data.getUserBaseInfo().getBindMobile())){
//						Object code = MemcachedUtil.get(Constants.SET_EMAIL_MOBILE_CODE+data.getUserBaseInfo().getBindMobile());
//						if( null == code || !code.toString().equals(data.getCode().getCode())){
//							setResult(result, ErrorCode.ERROR_3007);
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_2004);
//					}
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_3003);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * C12 重置密码
//	 */
//	public JSONObject resetPassword(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		if(null != data.getUserBaseInfo() && null != data.getCode() ){
//			if( null != data.getCode().getCode()){
//				if( 1 == data.getCode().getFrom() && null!=data.getUserBaseInfo().getBindEmail() && data.getUserBaseInfo().getBindEmail().length() > 0){
//					Object code = MemcachedUtil.get(Constants.EMAIL_SECURITY_CODE_KEY+data.getUserBaseInfo().getBindEmail());
//					if( null != code && code.toString().equals(data.getCode().getCode())){
//						String addTime = null;
//						String userId = null;
//						boolean exists = false;
//						Object userBaseInfo = MemcachedUtil.get(Constants.USERBASE_INFO_KEY+data.getUserBaseInfo().getBindEmail());
//						if( null != userBaseInfo ){
//							@SuppressWarnings("unchecked")
//							Map<String,Object> map = (Map<String, Object>) userBaseInfo;
//							addTime = map.get("addTime").toString();
//							userId = map.get("userId").toString();
//							exists = true;
//						}else{
//							String sql = "select a.userId,a.bindEmail,a.bindMobile,a.addTime from t_user_info a where a.bindEmail = '"+data.getUserBaseInfo().getBindEmail()+"'";
//							Map<String,Object> map = this.dao.selectOne(sql);
//							if( null != map && map.size() >0){
//								addTime = map.get("addTime").toString();
//								userId = map.get("userId").toString();
//								MemcachedUtil.set(Constants.USERBASE_INFO_KEY+data.getUserBaseInfo().getBindEmail(),map);
//								MemcachedUtil.set(Constants.USERBASE_INFO_KEY+map.get("userId"),map);
//								exists = true;
//							}else{
//								setResult(result, ErrorCode.ERROR_2001);
//							}
//						}
//						if(exists){
//							String updateSql = " update t_user_info a set a.pwd='"+getPwd(addTime, userId, data.getUserBaseInfo().getPwd())+"' where a.bindEmail='"+data.getUserBaseInfo().getBindEmail()+"'";
//							int n = this.dao.updateBySql(updateSql);
//							if( n <= 0){
//								setResult(result, ErrorCode.ERROR_3004);
//							}
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_3003);	
//					}
//				}else if( 2 == data.getCode().getFrom() && null != data.getUserBaseInfo().getBindMobile() && data.getUserBaseInfo().getBindMobile().length() > 0 ){
//					Object code = MemcachedUtil.get(Constants.SET_EMAIL_MOBILE_CODE+data.getUserBaseInfo().getBindMobile());
//					if( null != code && code.toString().equals(data.getCode().getCode())){
//						String addTime = null;
//						String userId = null;
//						boolean exists = false;
//						Object userBaseInfo = MemcachedUtil.get(Constants.USERBASE_INFO_KEY+data.getUserBaseInfo().getBindMobile());
//						if( null != userBaseInfo ){
//							@SuppressWarnings("unchecked")
//							Map<String,Object> map = (Map<String, Object>) userBaseInfo;
//							addTime = map.get("addTime").toString();
//							userId = map.get("userId").toString();
//							exists = true;
//						}else{
//							String sql = "select a.userId,a.bindEmail,a.bindMobile,a.addTime from t_user_info a where a.bindMobile = '"+data.getUserBaseInfo().getBindMobile()+"'";
//							Map<String,Object> map = this.dao.selectOne(sql);
//							if( null != map && map.size() >0){
//								addTime = map.get("addTime").toString();
//								userId = map.get("userId").toString();
//								MemcachedUtil.set(Constants.USERBASE_INFO_KEY+data.getUserBaseInfo().getBindMobile(),map);
//								MemcachedUtil.set(Constants.USERBASE_INFO_KEY+map.get("userId"),map);
//								exists = true;
//							}else{
//								setResult(result, ErrorCode.ERROR_2001);
//							}
//						}
//						if(exists){
//							String updateSql = " update t_user_info a set a.pwd='"+getPwd(addTime, userId, data.getUserBaseInfo().getPwd())+"' where a.bindMobile='"+data.getUserBaseInfo().getBindMobile()+"'";
//							int n = this.dao.updateBySql(updateSql);
//							if( n <= 0){
//								setResult(result, ErrorCode.ERROR_3004);
//							}
//						}
//					}else{
//						setResult(result, ErrorCode.ERROR_3003);	
//					}
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);	
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_3003);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U10 用户详细信息
//	 */
//	public JSONObject getUserDetailInfo(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		com.jiujun.shows.common.vo.AnchorInfo anchorInfo = new com.jiujun.shows.common.vo.AnchorInfo();
//		if( null != data.getUserBaseInfo() ){
//			if( null != data.getUserBaseInfo().getUserId()){
//				Pattern pattern = Pattern.compile("^\\d+$");
//				Matcher match = pattern.matcher(data.getUserBaseInfo().getUserId());
//				if(match.matches()){
//					/*String sql = " select " +
//							" a.userId,a.nickName,a.remark,a.sex,a.brithday,a.address,a.icon,b.gold,(count(c.userId)) as attentionCount,if(null=d.roomId,b.userLevel,b.anchorLevel) anchorLevel " +
//							" from t_user_info  a " +
//							" inner join t_user_account b on (a.userId = b.userId) " +
//							" left join t_user_attention c on (c.toUserId=a.userId) " +
//							" left join t_user_anchor d on (a.userId=d.userId) " +
//							" where a.userId = " + data.getUserBaseInfo().getUserId() +
//							" group by a.userId ";*/
//					String userId = data.getUserBaseInfo().getUserId();
//					StringBuffer sqlBuf = new StringBuffer(" select  " )
//					//.append(" a.userId,a.nickName,a.remark,a.sex,a.brithday,a.address,a.icon,b.gold,(count(c.userId)) as attentionCount,b.anchorLevel  AS anchorLevel,b.userLevel userLevel,IFNULL(a.sf, \" \") as sf,IFNULL(a.cs, \" \") as cs ,IFNULL(a.qy, \" \") as qy,(COUNT(e.userId)) AS toAttentionCount  " )\
////					.append(" a.userId,a.nickName,a.remark,a.sex,a.brithday,a.address,a.icon,b.gold,c.cnt AS attentionCount,b.anchorLevel  AS anchorLevel,b.userLevel userLevel,IFNULL(a.sf, \" \") as sf,IFNULL(a.cs, \" \") as cs ,IFNULL(a.qy, \" \") as qy,e.cnt AS toAttentionCount,   " )
//					.append(" a.userId,a.nickName,a.remark,a.sex,a.brithday,a.address,a.icon,b.gold,b.anchorLevel  AS anchorLevel,b.userLevel userLevel,b.renqLevel AS renqLevel,IFNULL(a.sf, \" \") as sf,IFNULL(a.cs, \" \") as cs ,IFNULL(a.qy, \" \") as qy,   " )
//					.append(" b.userPoint as userPoint,b.anchorPoint as anchorPoint ,b.renqPoint as renqPoint,nextRenqLevel.point as nextRenqPoint, " )
//					.append(" nextUserLevel.point as nextLevelUserPoint,nextAnchorLevel.point as nextLevelAnchorPoint ,CASE WHEN ISNULL(d.userId) THEN 0 ELSE 1 END isAnchor " )
//					.append(" from t_user_info  a  " )
//					.append(" inner join t_user_account b on (a.userId = b.userId)  " )
////					.append(" LEFT JOIN (SELECT toUserId,COUNT(*) cnt FROM t_user_attention where toUserId='").append(userId).append("') c on (c.toUserId=a.userId)  " )
//					.append(" left join t_user_anchor d on (a.userId=d.userId)  " )
////					.append(" LEFT JOIN (SELECT userId,COUNT(*) cnt FROM t_user_attention where userId='").append(userId).append("') e  ON (e.userId = a.userId)   " )
//					.append(" LEFT JOIN t_level nextUserLevel on nextUserLevel.levelType = 1 and nextUserLevel.point > b.userPoint   " )
//					.append(" LEFT JOIN t_level nextAnchorLevel on nextAnchorLevel.levelType = 2 and nextAnchorLevel.point > b.anchorPoint   " )
//					.append(" LEFT JOIN t_level nextRenqLevel on nextRenqLevel.levelType = 3 and nextRenqLevel.point > b.renqPoint   " )
//					.append(" where a.userId =  '" ) 
//					.append(userId)
//					.append("' group by a.userId limit 1");
//					String sql = sqlBuf.toString();
////					LogUtil.log.info("###getUserDetailInfo:sql:"+sql);
//					Map<String,Object> map = this.dao.selectOne(sql);
//					if( null != map && map.size() >0){
//						anchorInfo = Helper.map2Object(map, com.jiujun.shows.common.vo.AnchorInfo.class);
//						
//						try {
//							// 查用户勋章
//							 List<Decorate> userDecorateList = decorateService.findListOfCommonUser(userId);
//							 anchorInfo.setDecorateList(userDecorateList);
//						} catch (Exception e) {
//							LogUtil.log.error(e.getMessage(),e) ;
//						}
//						 
//						try {
//							// 查用户座驾
//							 List<SysCarDo> userCarList= userCarPortService.findUserCars(userId);
//							 anchorInfo.setCarList(userCarList);
//						} catch (Exception e) {
//							LogUtil.log.error(e.getMessage(),e) ;
//						}
//						
//						try {
//							List<GuardVo> guardList = guardWorkService.getUserGuardAllData(userId);
//							anchorInfo.setGuardList(guardList);
//						} catch (Exception e) {
//							LogUtil.log.error(e.getMessage(),e);
//						}
//						
//						try {
//							// 用户动态数量
//							List<DiaryInfo> diaryList = diaryInfoService.getUserDiaryInfosFromCache(userId);
//							int diaryCount = 0;
//							if(diaryList != null) {
//								diaryCount = diaryList.size();
//							}
//							anchorInfo.setDiaryCount(diaryCount);
//							
//							List<UserAttentionDo> fans = this.userAttentionService.finUserFans(userId);
//							int fansCount = 0;
//							if(fans != null) {
//								fansCount = fans.size();
//							}
//							anchorInfo.setAttentionCount(fansCount + "");
//							
//							int attentionCount = this.userAttentionService.find2AttentionCounts(userId);
//							anchorInfo.setToAttentionCount(attentionCount + "");
//						} catch (Exception e) {
//							LogUtil.log.error(e.getMessage(),e);
//						}
//						
//						//用户徽章个数
//						try {
//							Map<String, Object> paramMap = new HashMap<String, Object>();
//							paramMap.put("userId", userId);
//							int badgeCount = medalPackageService.getCount(paramMap);
//							anchorInfo.setBadgeCount(badgeCount);
//						}catch (Exception e) {
//							LogUtil.log.error(e.getMessage(),e);
//						}
//						
//					}else{
//						setResult(result, ErrorCode.ERROR_2001);
//					}
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_3003);
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(),result.buildJson() );
//		json.put(anchorInfo.getShortName(), anchorInfo.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U11 个人资料编辑
//	 * @throws Exception 
//	 */
//	public void editUserInfo(DataRequest data) throws Exception{
//		if( null != data|| data.getAnchorInfo() == null || data.getUserBaseInfo() == null){
//			AnchorInfo updateAnchorInfo = data.getAnchorInfo() ;
//			String loginUserId = data.getUserBaseInfo().getUserId();
//				if( StringUtils.isNotEmpty(loginUserId) ){
//					Pattern pattern = Pattern.compile("^\\d+$");
//					Matcher match = pattern.matcher(loginUserId);
//					if(match.matches()){
//						UserInfo dbUserInfo = this.dao.getUserByUserId(loginUserId);
//						String oldNickname = dbUserInfo.getNickName();
//						String newNickName = updateAnchorInfo.getNickName();
//						newNickName = StrUtil.trimStr(newNickName);//昵称去掉空格
//						if(!StringUtils.isEmpty(newNickName)){
//							if(newNickName.length()>=9){//昵称限制18位
//								newNickName = newNickName.substring(0,9);
//							}
//							
//							//1.官方2个字禁止用户注册;2. 检测敏感词
//							if(newNickName.contains("官方")||SensitiveWordUtil.isContaintSensitiveWord(newNickName)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2032);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}
//							
//							if(!newNickName.equals(dbUserInfo.getNickName())){ // 改变昵称,则先判断是否有其他人使用此昵称
//								List list = this.dao.getUserByUserNameExtractMe(loginUserId,newNickName);
//								if(list!=null&&list.size()>0){//  判断用户昵称是否唯一(不为空,说明其他人已使用此昵称)
//									Exception e = new SystemDefinitionException(ErrorCode.ERROR_2020);
//									LogUtil.log.error(e.getMessage() ,e);
//									throw e;
//								}else{
//									dbUserInfo.setNickName(newNickName);
//								}
//							}else{
//								dbUserInfo.setNickName(newNickName);
//							}
//						}
//						
//						
//						String newRemark = updateAnchorInfo.getRemark();
//						if(StringUtils.isNotEmpty(newRemark)){
//							// 检测敏感词
//							if(SensitiveWordUtil.isContaintSensitiveWord(newRemark)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2037);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}else{
//								dbUserInfo.setRemark(newRemark);
//							}
//						}
//						
//						if(null != updateAnchorInfo.getSex() && updateAnchorInfo.getSex().length() > 0){
//							dbUserInfo.setSex(updateAnchorInfo.getSex()) ;
//						}
//						
//						if(null != updateAnchorInfo.getBrithday() && updateAnchorInfo.getBrithday().length() > 0){
//							String dateStr = updateAnchorInfo.getBrithday();
//							String dateFormate = "yyyy-MM-dd";
//							Date date = DateUntil.parse(dateStr,dateFormate);
//							dbUserInfo.setBrithday(date) ;
//						}
//						
//						String newAddr = updateAnchorInfo.getAddress();
//						if(StringUtils.isNotEmpty(newAddr)){
//							// 检测敏感词
//							if(SensitiveWordUtil.isContaintSensitiveWord(newAddr)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2037);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}else{
//								dbUserInfo.setAddress(newAddr);
//							}
//						}
//						
//						String newSf = updateAnchorInfo.getSf();
//						if(StringUtils.isNotEmpty(newSf)){
//							// 检测敏感词
//							if(SensitiveWordUtil.isContaintSensitiveWord(newSf)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2037);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}else{
//								dbUserInfo.setSf(newSf);
//							}
//							
//						}
//						
//						String newCs = updateAnchorInfo.getCs();
//						if(StringUtils.isNotEmpty(newCs)){
//							// 检测敏感词
//							if(SensitiveWordUtil.isContaintSensitiveWord(newCs)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2037);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}else{
//								dbUserInfo.setCs(newCs);
//							}
//							
//						}
//						
//						String newQy = updateAnchorInfo.getQy();
//						if(StringUtils.isNotEmpty(newQy)){
//							// 检测敏感词
//							if(SensitiveWordUtil.isContaintSensitiveWord(newQy)){
//								Exception e = new SystemDefinitionException(ErrorCode.ERROR_2037);
//								LogUtil.log.error(e.getMessage() ,e);
//								throw e;
//							}else{
//								dbUserInfo.setQy(newQy);
//							}
//							
//						}
//						String newIcon = updateAnchorInfo.getIcon();
//						if(StringUtils.isNotEmpty(newIcon) && newIcon.indexOf(ICON_PATH) == -1){
//							dbUserInfo.setIcon(newIcon);
//						}
//						
//						this.dao.update(dbUserInfo);
//						if(!oldNickname.equals(newNickName)){
//							// 昵称改变,需做的一些业务
//							this.iUserInfoService.doUpdateUserNicknameCommonBiz(loginUserId);
//						}
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3003);
//						LogUtil.log.error(e.getMessage() ,e);
//						throw e;
//					}
//				}else{
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3003);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//		}else{
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_2000);
//			LogUtil.log.error(e.getMessage() ,e);
//			throw e;
//		}
//		/*json.put(result.getShortName(), result.buildJson());
//		return json;*/
//	}
//	
//	/**
//	 * 从cache中获取消息发送者的信息 U12
//	 * @param data
//	 * @return
//	 */
//	public JSONObject getInfoFromCache(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		UserInfoVo vo = new UserInfoVo();
//		try {
//			//参数校验
//			if(data==null||data.getUserBaseInfo()==null
//					||StringUtils.isEmpty(data.getUserBaseInfo().getUserId())){
//				setResult(result, ErrorCode.ERROR_2000);
//			}else{
//				String roomId = null;
//				if(data.getAnchorInfo()!=null){
//					roomId = data.getAnchorInfo().getRoomId();
//				}
//				String uid = data.getUserBaseInfo().getUserId();
//				vo = userCacheInfoService.getInfoFromCache(uid, roomId);
//			//	LogUtil.log.info("vo=" + JsonUtil.beanToJsonString(vo));
//			}
//		} catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//			
//		}
//		json.put("resultCode",result.getResultCode());
//		json.put("resultDescr",result.getResultDescr());
//		json.put("user", vo);
//		return json; 
//	}
//	
//	/**
//	 * F1 上传文件
//	 * @param data
//	 * @return
//	 */
//	public JSONObject upload(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		if( null != data.getUserBaseInfo() ){
//			String filepath = SpringContextListener.getContextProValue("cdnUpload", "/data/apps/xxwan_cdn/advert/")+File.separator+Constants.ICON_IMG_FILE_URI+File.separator;
//	        File files = new File(filepath);
//	        if(!files.exists()){
//	        	files.mkdirs();
//	        }
//	        HttpServletRequest request = data.getRequest();
//	        //创建一个通用的多部分解析器  
//			CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());
//			if( cmr.isMultipart(request)){
//				 //转换成多部分request    
//	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
//	            //获取request中的所有文件名  
//	            Iterator<String> it = multiRequest.getFileNames();
//	            StringBuffer sb = new StringBuffer();
//	            sb.append("update t_user_info a set ");
//	            while(it.hasNext()){
//	            	//取得上传文件  
//	                MultipartFile file = multiRequest.getFile(it.next());
//	                //获取文件类型
//	            	String type = FileTypeUtils.getFileByFile(file);
//	                //生成文件名
//	                String groupId = FileTypeUtils.getRandomFileGroupId();
//	                String fName = groupId+"."+type;
//	                
//	                Date nowDate = new Date();
//					String iconDateStr = DateUntil.getFormatDate("yyyyMMdd", nowDate);
//					
//					// 按天创建目录
//					File iconDateDir = new File(filepath+iconDateStr);
//			        if(!iconDateDir.exists()){
//			        	iconDateDir.mkdirs();
//			        }
//					// 头像地址按目录分类
//					fName = iconDateStr+File.separator+fName;
//	                
//	                if(type != null && (type.equals("jpg") || type.equals("png") || type.equals("gif") || type.equals("JPEG"))){
//	                	File fileP = new File(filepath+fName);
//	                	 sb.append("a.icon='"+fName+"' where a.userId='"+data.getUserBaseInfo().getUserId()+"'");
//	                	try {
//							file.transferTo(fileP);
//							int n = this.dao.updateBySql(sb.toString());
//							if( n <= 0){
//								setResult(result, ErrorCode.ERROR_3016);
//							}
//						} catch (IllegalStateException e) {
//							LogUtil.log.error("响应异常", e);
//							LogUtil.log.error(e.getMessage(), e);
//							setResult(result, ErrorCode.ERROR_3009);
//						} catch (IOException e) {
//							LogUtil.log.error("响应异常", e);
//							LogUtil.log.error(e.getMessage(), e);
//							setResult(result, ErrorCode.ERROR_3003);
//						}
//	                }else{
//	                	setResult(result, ErrorCode.ERROR_3008);
//	                } 
//	            }
//			}
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		JSONObject json = new JSONObject();
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	
//	/**
//	 * 发送email,已便让用户修改密码 U21
//	 * @param data
//	 * @return
//	 */
//	public JSONObject sendEmailForUpdatePwd(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		try {
//			//参数校验
//			if(data==null||data.getUserBaseInfo()==null
//					||StringUtils.isEmpty(data.getUserBaseInfo().getBindEmail())){
//				setResult(result, ErrorCode.ERROR_2000);
//			}	
//			String emailContent = this.iUserInfoService.sendEmailForUpdatePwd(data.getUserBaseInfo().getBindEmail());
//			//json.put("content", emailContent);
//		}catch (SystemDefinitionException e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, e.getErrorCode());
//			
//		}catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//			
//		}
//		json.put(result.getShortName(), result.buildJson());
//		return json; 
//	}
//	
//	/**
//	 * 手机/邮箱找回密码 U22
//	 * @param data
//	 * @return
//	 */
//	public JSONObject updatePassword(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		try {
//			Code code = data.getCode();
//			UserBaseInfo userBaseInfo = data.getUserBaseInfo();
//			if(userBaseInfo==null||code==null){
//				throw new SystemDefinitionException(ErrorCode.ERROR_3017);
//			}
//			if(1==code.getFrom()){//from 1:email
//				UserInfo userInfo = new UserInfo();
//				userInfo.setPwd(userBaseInfo.getPwd());
//				this.iUserInfoService.updateUserByEmail(userInfo, code.getCode());
//			}else if(2==code.getFrom()){//from 2:mobile
//				UserInfo userInfo = new UserInfo();
//				userInfo.setPwd(userBaseInfo.getPwd());
//				userInfo.setBindMobile(userBaseInfo.getBindMobile());
//				this.iUserInfoService.updateUserByPhonenumber(userInfo, code.getCode());
//			}else{
//				throw new SystemDefinitionException(ErrorCode.ERROR_3017);
//			}
//		}catch (SystemDefinitionException e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, e.getErrorCode());
//			
//		}catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//			
//		}
//		json.put(result.getShortName(), result.buildJson());
//		return json; 
//	}
//	
//	/**
//	 * 邮箱修改密码来到修改页面时,校验打开页面时的code，再生成并返回新的code U23
//	 * @param data
//	 * @return
//	 */
//	public JSONObject validateCodeInEmailRetrievePage(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		try {
//			Code code = data.getCode();
//			if(code==null||StringUtils.isEmpty(code.getCode())){
//				throw new SystemDefinitionException(ErrorCode.ERROR_3017);
//			}
//			String newCodeStr = this.iUserInfoService.validateCodeInEmailRetrievePage(code.getCode());
//			json.put("content", newCodeStr);
//		}catch (SystemDefinitionException e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, e.getErrorCode());
//			
//		}catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//			
//		}
//		json.put(result.getShortName(), result.buildJson());
//		return json; 
//	}
//	
//	/**
//	 * 根据userId查询当前房间用户资料 U60
//	 * @param data
//	 * @return achorInfo , result
//	 * */
//	public JSONObject getRoomMemberInfo(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		com.jiujun.shows.common.vo.AnchorInfo anchorInfo = new com.jiujun.shows.common.vo.AnchorInfo();
//		try{
//			if( null != data.getUserBaseInfo() ){
//				if( null != data.getUserBaseInfo().getUserId()){
//					
//					String roomId = null;
//					Pattern pattern = Pattern.compile("^\\d+$");
//					Matcher match = pattern.matcher(data.getUserBaseInfo().getUserId());
//					//根据userId判断用户属性：纯数字为普通用户， 非纯数字为机器人
//					if(match.matches()){ //普通用户
//						
//						
//						if( null != data.getAnchorInfo()){
//							roomId = data.getAnchorInfo().getRoomId();
//						}
//						
//						/*
//						sql :  SELECT a.userId AS userId ,a.icon AS icon , a.cs AS cs , a.brithday AS brithday , a.nickName AS nickName ,a.sex AS sex ,   b.userLevel AS userLevel , c.image AS levelImage, c.title AS levelTitle, e.image AS carImage FROM
//								(SELECT * FROM t_user_info WHERE userId = '104384' ) a
//								JOIN t_user_account b ON (b.userId = a.userId)
//								JOIN t_level c ON(c.level = b.userLevel)
//								LEFT JOIN t_user_carport  d ON (d.userId = a.userId)
//								 JOIN t_sys_car e ON (e.id = d.carId AND d.inUse=1)
//								GROUP BY  a.userId 
//						*/
//						String userId = data.getUserBaseInfo().getUserId();
//						StringBuffer sqlBuf = new StringBuffer(" SELECT ")
//						.append(" a.userId AS userId ,a.icon AS icon , a.cs AS cs ,a.sf AS sf , a.qy AS qy, a.brithday AS brithday , a.nickName AS nickName ,a.sex AS sex , a.remark AS remark,  b.userLevel AS userLevel ,b.anchorLevel AS anchorLevel,b.renqLevel AS renqLevel, ")
//						//.append("  c.image AS levelImage, c.title AS levelTitle, d.image AS carImage ,e.roomId AS roomId ,f.cnt AS attentionCount , g.cnt AS toAttentionCount,  ")
//						.append("  c.image AS levelImage, c.title AS levelTitle, d.image AS carImage ,IFNULL(e.roomId,\"\") AS roomId , CASE WHEN ISNULL(e.userId) THEN 0 ELSE 1 END isAnchor, ")
//						.append(" b.userPoint as userPoint,b.anchorPoint as anchorPoint ,b.renqPoint as renqPoint,nextRenqLevel.point as nextRenqPoint, " )
//						.append(" nextUserLevel.point as nextLevelUserPoint,nextAnchorLevel.point as nextLevelAnchorPoint  " )
//						.append("	FROM t_user_info a ")
//						.append("	JOIN t_user_account b ON (b.userId = a.userId) ")
//						.append("	JOIN t_level c ON(c.level = b.userLevel) ")
//						.append("	LEFT JOIN (SELECT userId , image , carId FROM t_user_carport t1  JOIN t_sys_car t2 ON (t1.carId = t2.id AND t1.inUse=1) ) d ON( d.userId = a.userId ) ") 
//						.append(" LEFT JOIN t_user_anchor e ON(e.userId = a.userId AND (e.anchorStatus = 0 OR e.anchorStatus IS NULL)) ")
//					 //   .append("  LEFT JOIN (SELECT toUserId,COUNT(*) cnt FROM t_user_attention WHERE toUserId='"+userId+"')  f ON ( f.toUserId = a.userId)  ")
//					//    .append("  LEFT JOIN (SELECT userId,COUNT(*) cnt FROM t_user_attention WHERE userId='"+userId+"') g ON ( g.userId = a.userId)  ")
//					    .append(" LEFT JOIN t_level nextUserLevel on nextUserLevel.levelType = 1 and nextUserLevel.point > b.userPoint   " )
//					    .append(" LEFT JOIN t_level nextAnchorLevel on nextAnchorLevel.levelType = 2 and nextAnchorLevel.point > b.anchorPoint   " )
//					    .append(" LEFT JOIN t_level nextRenqLevel on nextRenqLevel.levelType = 3 and nextRenqLevel.point > b.renqPoint   " )
//					    .append(" WHERE a.userId = '").append(userId).append("'")
//						.append("	GROUP BY  a.userId ");
//						String sql = sqlBuf.toString();
//						LogUtil.log.info("### getRoomMemberInfo:sql:"+sql);
//						Map<String,Object> map = this.dao.selectOne(sql);
//						if( null != map && map.size() >0){
//							anchorInfo = Helper.map2Object(map, com.jiujun.shows.common.vo.AnchorInfo.class);
//							
//							  int fansCount = this.userAttentionService.findAttentionCounts(userId);
//							  anchorInfo.setAttentionCount(fansCount+"");
//								
//							  int toAttenionCount = this.userAttentionService.find2AttentionCounts(userId);
//							  anchorInfo.setToAttentionCount(toAttenionCount+"");
//							
//							if(StringUtils.isNotEmpty(roomId)){
//								UserInfoVo userInfoVo = this.userCacheInfoService.getInfoFromCache(data.getUserBaseInfo().getUserId(), roomId);
//								if(userInfoVo!=null && userInfoVo.isForbidSpeak()){
//									anchorInfo.setIsForbidSpeak("y");
//								}else{
//									anchorInfo.setIsForbidSpeak("n");
//								}
//							}
//							// 查用户勋章
//							 List<Decorate> userDecorateList = decorateService.findListOfCommonUser(userId);
//							 anchorInfo.setDecorateList(userDecorateList);
//							 
//							// 查用户座驾
//							 List<SysCarDo> userCarList= userCarPortService.findUserCars(userId);
//							 anchorInfo.setCarList(userCarList);
//							 
//							 try {
//									List<GuardVo> guardList = guardWorkService.getUserGuardAllData(userId);
//									anchorInfo.setGuardList(guardList);
//								} catch (Exception e) {
//									LogUtil.log.error(e.getMessage(),e);
//								}
//							 
//							 try {
//									// 用户动态数量
//									List<DiaryInfo> diaryList = diaryInfoService.getUserDiaryInfosFromCache(userId);
//									int diaryCount = 0;
//									if(diaryList != null) {
//										diaryCount = diaryList.size();
//									}
//									anchorInfo.setDiaryCount(diaryCount);
//								} catch (Exception e) {
//									LogUtil.log.error(e.getMessage(),e);
//								}
//							json.put(anchorInfo.getShortName(), anchorInfo.buildJson());
//						}else{
//							setResult(result, ErrorCode.ERROR_2001);
//						}
//						
//					}else{//机器人
//						if(this.iUserInfoService.validateIfRobot(data.getUserBaseInfo().getUserId())){
//							/* 
//							 * sql :  SELECT a.userId AS userId ,a.icon AS icon , a.cs AS cs , a.brithday AS brithday , a.nickName AS nickName ,a.sex AS sex ,   b.userLevel AS userLevel , c.image AS levelImage, c.title AS levelTitle, e.image AS carImage FROM
//									(SELECT * FROM t_user_info_robot WHERE userId = 'robot100004' ) a
//									JOIN t_user_account_robot b ON (b.userId = a.userId)
//									JOIN t_level c ON(c.level = b.userLevel)
//									LEFT JOIN t_user_carport  d ON (d.userId = a.userId)
//									LEFT JOIN t_sys_car e ON (e.id = d.carId AND d.inUse=1)
//									GROUP BY  a.userId 
//							*/
//							StringBuffer sqlBuf = new StringBuffer(" SELECT ")
//							.append(" a.userId AS userId ,a.icon AS icon , a.cs AS cs , a.brithday AS brithday , a.nickName AS nickName ,a.sex AS sex , b.userLevel AS userLevel , c.image AS levelImage, c.title AS levelTitle,d.carId AS carId , e.image AS carImage FROM ")
//							.append("	(SELECT * FROM t_user_info_robot WHERE userId = '")
//							.append(data.getUserBaseInfo().getUserId())
//							.append("' ) a ")
//							.append("	JOIN t_user_account_robot b ON (b.userId = a.userId) ")
//							.append("	JOIN t_level c ON(c.level = b.userLevel) ")
//							.append("	LEFT JOIN t_user_carport  d ON (d.userId = a.userId) ") 
//							.append("   LEFT JOIN t_sys_car e ON (e.id = d.carId AND d.inUse=1) ")
//							.append("	GROUP BY  a.userId ");
//							String sql = sqlBuf.toString();
//							LogUtil.log.info("sql:"+sql);
//							Map<String,Object> map = this.dao.selectOne(sql);
//							if( null != map && map.size() >0){
//								anchorInfo = Helper.map2Object(map, com.jiujun.shows.common.vo.AnchorInfo.class);
//								if(StringUtils.isNotEmpty(roomId)){
//									anchorInfo.setIsForbidSpeak("n");//机器人,设置成n
//								}
//								json.put(anchorInfo.getShortName(), anchorInfo.buildJson());
//							}else{
//								setResult(result, ErrorCode.ERROR_2001);
//							}
//						}else{
//							setResult(result, ErrorCode.ERROR_3003);
//						}
//						
//					}
//					
//				}else{
//					setResult(result, ErrorCode.ERROR_3003);
//				}
//			}else{
//				setResult(result, ErrorCode.ERROR_2000);
//			}
//		}catch(Exception e){
//			LogUtil.log.error(e.getMessage() , e);
//			setResult(result , ErrorCode.ERROR_2008);
//		}
//		
//		json.put(result.getShortName(), result.buildJson());
//		return json;
//	}
//	
//	/**
//	 * U-91
//	 * 修改密码，输入原密码，重新设置新密码
//	 * @param data
//	 * @return
//	 */
//	public JSONObject modifyPWD(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		try {
//			if(data == null || data.getUserBaseInfo() == null 
//					|| data.getUserBaseInfo().getUserId() == null
//					|| data.getUserBaseInfo().getPwd() == null
//					|| data.getUserBaseInfo().getNewPwd() == null){
//				throw new SystemDefinitionException(ErrorCode.ERROR_3017);
//			}
//			String reqUserId = data.getUserBaseInfo().getUserId();
//			String reqPwd = data.getUserBaseInfo().getPwd();
//			String newPwd = data.getUserBaseInfo().getNewPwd();
//			// 参数校验
//			UserInfo dbUserInfo = null;
//			// 新密码校验
//			if(StringUtils.isEmpty(newPwd) || newPwd.length() > 20) {
//				throw new SystemDefinitionException(ErrorCode.ERROR_3183);
//			}
//			Pattern pattern = Pattern.compile("^\\d+$");
//			Matcher match = pattern.matcher(data.getUserBaseInfo().getUserId());
//			if(match.matches()) {
//				dbUserInfo = this.dao.getUserByUserId(reqUserId);
//			}else{
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_2031);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			// 判断用户是否存在
//			if(dbUserInfo==null) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_2012);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			
//			// 校验是否绑定过邮箱或者手机
//			String mobile = dbUserInfo.getBindMobile();
//			String email = dbUserInfo.getBindEmail();
//			if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)){
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3184);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//			// 用户状态
//			int userStatus = dbUserInfo.getUserStatus();
//			// 判断用户状态是否正常,用户状态，1-正常; 0-停用; 2-待审核
//			if(userStatus==1){
//				String userId = dbUserInfo.getUserId();
//				String pwd = dbUserInfo.getPwd();
//				Date addTime = dbUserInfo.getAddTime();
//				String signPwd = getPwd(addTime, userId, reqPwd);
//				//校验密码是否正确
//				if(pwd.equals(signPwd)){
//					// 设置新密码
//					String resetPwd = getPwd(dbUserInfo.getAddTime(), dbUserInfo.getUserId(), newPwd);
//					dbUserInfo.setPwd(resetPwd);
//					this.dao.update(dbUserInfo);
//				}else{
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_2002);
//					LogUtil.log.error(e.getMessage() ,e);
//					throw e;
//				}
//			}else{
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_2030);
//				LogUtil.log.error(e.getMessage() ,e);
//				throw e;
//			}
//		}catch (SystemDefinitionException e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, e.getErrorCode());
//		}catch (Exception e) {
//			LogUtil.log.error(e.getMessage() ,e);
//			setResult(result, ErrorCode.ERROR_2008);
//		}
//		json.put(result.getShortName(), result.buildJson());
//		return json; 
//	}
//	
//	/**
//	 * U92
//	 * 获取用户粉丝
//	 * @param data
//	 * @return
//	 */
//	public JSONObject getUserFans(DataRequest data){
//		Result result = new Result(ErrorCode.SUCCESS_0);
//		JSONObject json = new JSONObject();
//		JSONArray retArray = new JSONArray();
//		if( null != data.getUserBaseInfo() || null != data.getUserBaseInfo().getUserId()
//				 || data.getPage() != null){
//			Page page = data.getPage();
//			String toUserId = data.getUserBaseInfo().getUserId();
//			LogUtil.log.error("### getUserFans-获取用户粉丝列表，begin....toUserId="+toUserId);
//			String key = MCPrefix.USER_FANS_ALL_CACHE + toUserId;
//			Object obj = MemcachedUtil.get(key);
//			Map<String, JSONArray> m = null;
//			if(obj != null) {
//				m = (Map) obj;
//				LogUtil.log.error("### getUserFans-获取用户粉丝列表，从缓存中获取数据。。。userId="+toUserId);
//			} else {
//				try{
//					JSONArray jsonArray = new JSONArray();
//					m = new HashMap<String, JSONArray>();
//					List<UserAttentionDo> fans = this.userAttentionService.finUserFans(toUserId);
//					LogUtil.log.error("### getUserFans-获取用户粉丝列表，从db中拿数据。。。fans=" + fans.size());
//					if(fans != null && fans.size() >0) {
//						// 获取用户关注的所有主播
//						for(UserAttentionDo user : fans) {
//							String userId = user.getUserId();
//							// 获取用户详细信息
//							UserCacheInfo vo = this.userCacheInfoService.getOrUpdateUserInfoFromCache(userId);
//							if(vo == null) {
//								LogUtil.log.error("### getUserFans-获取关注粉丝列表,获取粉丝用户详细信息失败,userId="+userId);
//								continue;
//							}
//							AnchorInfo info = new AnchorInfo();
//							String isAnchor = "0";
//							String roomId = "";
//							UserAnchor userAnchor = this.userAnchorService.getAnchorFromCacheByUserId(userId);
//							if(userAnchor != null) {
//								isAnchor = "1";
//								roomId = userAnchor.getRoomId();
//							}
//							info.setIsAnchor(isAnchor);
//							info.setRoomId(roomId);
//							info.setUserId(userId);
//							info.setNickName(vo.getNickname());
//							info.setUserLevel(vo.getUserLevel());
//							info.setIsAnchor("0");
//							info.setIcon(vo.getAvatar());
//							jsonArray.add(info.buildJson());
//						}
//						m.put("data", jsonArray);
//						MemcachedUtil.set(key, m, MCTimeoutConstants.USER_ATTENTION_ANCHOR_CACHE);
//					}
//				} catch(Exception e) {
//					LogUtil.log.error(e.getMessage(),e);
//				}
//			}
//			int pageNum = Integer.parseInt(page.getPageNum()); // 页码
//			int pageSize = Integer.parseInt(page.getPagelimit()); // 单页容量
//			
//			// 不用做分页处理
//			// 封装分页数据
//			if(m != null) {
//				if(m.containsKey("data") && m.get("data") != null) {
//					retArray = (JSONArray) m.get("data");
////					if(arrayData != null && arrayData.size() >0) {
//						int all = retArray.size();
//						page.setCount(all + "");
//						
//						// 从哪里开始
////						int index = pageNum >1 ? (pageNum - 1) * pageSize : 0;
////						LogUtil.log.error("###getUserFans-获取粉丝分页数据，总数:" + all + ",从第:"+index + "条开始");
////						for(int i=0;i<all;i++) {
////							if(all <= index){
////								LogUtil.log.error("###getUserFans-获取粉丝分页数据，all=" + all + ",index="+index);
////								break; //防止越界
////							}
////							retArray.add(arrayData.get(i));
////							index++;
////						}
////					}
//				}
//			}
//			json.put(data.getPage().getShortName(), page.buildJson());
//			LogUtil.log.error("### getUserFans-获取用户关注列表，end!toUserId="+toUserId);
//		}else{
//			setResult(result, ErrorCode.ERROR_2000);
//		}
//		json.put(result.getShortName(), result.buildJson());
//		json.put(Constants.DATA_BODY, retArray);
//		return json;
//	}
//}
