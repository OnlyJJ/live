package com.jiun.shows.others.push.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiun.shows.others.push.constant.CacheTime;
import com.jiun.shows.others.push.constant.RedisKey;
import com.jiun.shows.others.push.dao.PushConfigMapper;
import com.jiun.shows.others.push.dao.PushContentConfMapper;
import com.jiun.shows.others.push.dao.PushDevMapper;
import com.jiun.shows.others.push.domain.PushConfig;
import com.jiun.shows.others.push.domain.PushContentConf;
import com.jiun.shows.others.push.domain.PushDev;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;


/**
 * 客户端推送消息设备信息记录表
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("pushDevBiz")
public class PushDevBiz {

	@Resource
	private PushDevMapper pushDevMapper;
	
	@Resource
	private PushConfigMapper pushConfigMapper;
	
	@Resource
	private PushContentConfMapper pushContentConfMapper;
	
	/** ios证书环境，IOSENV_DEV-开发环境，IOSENV_PROD-生产环境 */
	private static final int IOS_ENVIRONMENT = XingeApp.IOSENV_PROD;
	/** 多包分发，安卓使用，1-多包分发，0-否 */
	private static final int MULTIPKG = 1;
	private static final int DEVICE_TYPE = 0;
	/** android指定跳转到房间的app activity */
	private static final String ROOM_ACTIVITY = "com.cn.nineshows.activity.LiveActivity";
	/** android指定跳转到活动页面 activity */
	private static final String WEB_ACTIVITY = "com.cn.nineshows.activity.WebviewActivity";
	/** 定义参数Map的key */
	private static final String NICKNAME = "nickname";
	private static final String	URL = "url";
	private static final String	TYPE = "type";
	private static final String	ACCESSID = "accessId";
	private static final String	SECRETKEY = "secretKey";
	private static final String	TITLE = "title";
	private static final String	CONTENT = "content";
	private static final String	BODY = "body";
	
	private static final String RESULT = "result";
	private static final String RET_CODE = "ret_code";
	private static final String PUSH_ID = "push_id";
	
	/** 批量推送，一次最多推送账号个数 */
	private static final int MAX_PUSHACCOUNT = 900;
	
	public void savePushDev(String userId, String token, int appType, String pckName) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		// 校验是否已存，不重复存
		PushDev his = this.getPushDevFromCache(userId, token);
		if(his != null) {
			LogUtil.log.error("###savePushDev-userId=" + userId + ",pckName=" + pckName + ",已注册，不重复！");
			return;
		}
		PushDev vo = new PushDev();
		vo.setUserid(userId);
		vo.setToken(token);
		vo.setCreattime(new Date());
		vo.setApptype(appType);
		vo.setPckName(pckName);
		pushDevMapper.insert(vo);
	}

	public void pushLiveStartMsg(String anchorId, Map<String,Object> custom) {
		LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",begin。。。");
		try {
			int type = 1; // 推送到房间
			PushContentConf conf = pushContentConfMapper.getPushContentConf(type);
			if(conf == null) {
				LogUtil.log.error("###pushLiveStartMsg-推送消息内容未配置！");
				return;
			}
			int useStatus = conf.getUsestatus();
			if(0 == useStatus) {
				LogUtil.log.error("###pushLiveStartMsg-推送关注开播消息，已停用，不推送");
				return;
			}
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put(TYPE, type);
			parms.put(TITLE, conf.getTitle());
			StringBuilder content = new StringBuilder();
			if(custom.containsKey(NICKNAME) && null != custom.get(NICKNAME)) {
				content.append("【").append(custom.get(NICKNAME).toString()).append("】说：");
			}
			content.append(conf.getContent());
			parms.put(CONTENT, content.toString());
			// 处理安卓用户
			int appType = 0;
			List<PushConfig> androidConf = pushConfigMapper.listPushConfig(appType);
			if(androidConf != null && androidConf.size() >0) {
				// 获取关注主播的粉丝，安卓用户
				List<String> users = listAndroidFans(anchorId);
				LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",安卓用户数：size=" + users.size());
				if(users != null && users.size() >0) {
					for(PushConfig pc : androidConf) {
						parms.put(ACCESSID, pc.getAccessid());
						parms.put(SECRETKEY, pc.getSecretkey());
						this.pushAndroidAccountList(users, parms, custom);
					}
				}
			}
			// 处理ios用户
			appType = 3;
			List<PushConfig> iosConf = pushConfigMapper.listPushConfig(appType);
			if(iosConf != null && iosConf.size() > 0) {
				for(PushConfig pc : iosConf) {
					parms.put(ACCESSID, pc.getAccessid());
					parms.put(SECRETKEY, pc.getSecretkey());
					String pckName = pc.getPckname();
					List<String> users = listIOSFans(anchorId, pckName);
					LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",ios：包="+ pckName + ",关注用户数：size=" + users.size());
					if(users != null && users.size() > 0) {
						this.pushIOSAccountList(users, parms, custom);
					}
				}
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushLiveStartMsg-推送消息失败！");
			LogUtil.log.error(e.getMessage(),e);
		}
		LogUtil.log.error("###pushLiveStartMsg-推送开播消息，anchorId=" + anchorId + ",end!");
	}
	
	public void pushMSGByConfig(int configId) {
		try {
			LogUtil.log.error("###pushMSGByConfig-处理后台配置的手动消息推送，configId=" + configId + ",begin...");
			PushContentConf conf = pushContentConfMapper.getObjectById(configId);
			if(conf == null) {
				LogUtil.log.error("###pushMSGByConfig - 未获取到配置，不处理");
				return;
			}
			int useStatus = conf.getUsestatus();
			if(0 == useStatus) {
				LogUtil.log.error("###pushMSGByConfig-推送自定义消息，已停用，不推送");
				return;
			}
			Map<String, Object> parms = new HashMap<String, Object>();
			String title = conf.getTitle();
			String content = conf.getContent();
			int msgType = conf.getMsgType();
			
			// 创建大批量推送，安卓与IOS分别处理
			int appType = 0; // 安卓
			List<PushConfig> androidConf = pushConfigMapper.listPushConfig(appType);
			if(androidConf != null && androidConf.size() >0) {
				Message message = new Message();
				message.setTitle(title);
				message.setContent(content);
				message.setMultiPkg(MULTIPKG); // 多包发送
				message.setType(Message.TYPE_NOTIFICATION);
				message.setStyle(new Style(0,1,1,0,0));
				
				ClickAction action = new ClickAction();
				int actionType = ClickAction.TYPE_ACTIVITY;
				if(msgType == 1) { // 打开到房间
					actionType = ClickAction.TYPE_ACTIVITY;
					action.setActivity(ROOM_ACTIVITY);
				} else if(msgType == 2) { // 打开到活动页
					actionType = ClickAction.TYPE_ACTIVITY;
					action.setActivity(WEB_ACTIVITY);
					if(null != conf.getUrl()) {
						Map<String, Object> custom = new HashMap<String, Object>();
						custom.put(URL, conf.getUrl());
						message.setCustom(custom);
					}
				}
				action.setActionType(actionType);
				message.setAction(action);
				
				// 获取注册安卓用户
				List<String> users = pushDevMapper.listByApptype(appType);
				int sumUser = users.size();
				if(users != null && sumUser >0) {
					for(PushConfig pc : androidConf) {
						long accessId = pc.getAccessid();
						String secretKey = pc.getSecretkey();
						parms.put(ACCESSID, accessId);
						parms.put(SECRETKEY, secretKey);
						String pushId = this.createMultipushAndroid(parms, message);
						if(StringUtils.isEmpty(pushId)) {
							LogUtil.log.error("##pushMSGByConfig-批量推送安卓，创建pushId失败");
							continue;
						}
						// 信鸽最多一次处理1000个用户
						if(sumUser > MAX_PUSHACCOUNT) {
							while(users.size() > MAX_PUSHACCOUNT) {
								List<String> accountList = new ArrayList<String>();
								if(users.size() / MAX_PUSHACCOUNT > 0) {
									accountList.addAll(users.subList(0, MAX_PUSHACCOUNT));
									users.subList(0, MAX_PUSHACCOUNT).clear();
								} else {
									accountList.addAll(users.subList(0, sumUser));
									users.subList(0, sumUser).clear();
								}
								this.pushAccountListMultiple(Long.parseLong(pushId), accountList, accessId, secretKey);
							} 
							// 把剩余的全部一次推送
							if(users.size() >0) {
								this.pushAccountListMultiple(Long.parseLong(pushId), users, accessId, secretKey);
							}
						} else {
							this.pushAccountListMultiple(Long.parseLong(pushId), users, accessId, secretKey);
						}
					}
				}
			}
			// 处理ios用户
			appType = 3;
			List<PushConfig> iosConf = pushConfigMapper.listPushConfig(appType);
			if(iosConf != null && iosConf.size() > 0) {
				MessageIOS message = new MessageIOS();
				JSONObject alert = new JSONObject();
				alert.put(TITLE, title);
				alert.put(BODY, content);
				message.setAlert(alert);
				
				// 跳转到的房间所需参数
				Map<String, Object> custom = new HashMap<String, Object>();
				custom.put(TYPE, msgType);
				if(null != conf.getUrl()) {
					custom.put(URL, conf.getUrl());
				}
				message.setCustom(custom);
				
				// 获取注册ios用户
				List<String> users = pushDevMapper.listByApptype(appType);
				int sumUser = users.size();
				if(users != null && sumUser >0) {
					for(PushConfig pc : iosConf) {
						long accessId = pc.getAccessid();
						String secretKey = pc.getSecretkey();
						parms.put(ACCESSID, accessId);
						parms.put(SECRETKEY, secretKey);
						String pushId = this.createMultipushIOS(parms, message);
						if(StringUtils.isEmpty(pushId)) {
							LogUtil.log.error("##pushMSGByConfig-批量推送安卓，创建pushId失败");
							continue;
						}
						// 信鸽最多一次处理1000个用户
						if(sumUser > MAX_PUSHACCOUNT) {
							while(users.size() > MAX_PUSHACCOUNT) {
								List<String> accountList = new ArrayList<String>();
								if(users.size() / MAX_PUSHACCOUNT > 0) {
									accountList.addAll(users.subList(0, MAX_PUSHACCOUNT));
									users.subList(0, MAX_PUSHACCOUNT).clear();
								} else {
									accountList.addAll(users.subList(0, sumUser));
									users.subList(0, sumUser).clear();
								}
								this.pushAccountListMultiple(Long.parseLong(pushId), accountList, accessId, secretKey);
							}
							// 把剩余的全部一次推送
							if(users.size() >0) {
								this.pushAccountListMultiple(Long.parseLong(pushId), users, accessId, secretKey);
							}
						} else {
							this.pushAccountListMultiple(Long.parseLong(pushId), users, accessId, secretKey);
						}
					}
				}
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushMSGByConfig-推送消息失败！");
			LogUtil.log.error(e.getMessage(),e);
		}
		LogUtil.log.error("###pushMSGByConfig-处理后台配置的手动消息推送，configId=" + configId + ",end!");
	}
	
	public PushDev getPushDevFromCache(String userId, String token) throws Exception {
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		PushDev vo = null;
		String key = RedisKey.XINGE_PUSH_CACHE + userId;
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			vo = (PushDev) obj;
		} else {
			vo = pushDevMapper.getPushDev(userId, token);
			if(vo != null) {
				MemcachedUtil.set(key, vo, CacheTime.XINGE_PUSH_CACHE_TIME);
			}
		}
		return vo;
	}

	public void pushAndroidSingleAccount(final int deviceType, final String account, final long accessId, final String secretKey,
			final String title, final String content) {
		if(StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(account)
				|| StringUtils.isEmpty(content)) {
			LogUtil.log.error("###pushAndroidSingleAccount-推送用户列表为空，不处理!");
			return;
		}
		LogUtil.log.error("###pushAndroidSingleAccount-userId=" + account + ",begin...");
		Thread t = null;
		try {
			t = new Thread(new Runnable(){
				@Override
				public void run() {
					Message message = new Message();
					message.setTitle(title);
					message.setContent(content);
					message.setMultiPkg(MULTIPKG); // 多包发送
					message.setType(Message.TYPE_NOTIFICATION);
					message.setStyle(new Style(0,1,1,0,0));
					XingeApp push = new XingeApp(accessId, secretKey);
					JSONObject json = push.pushSingleAccount(deviceType, account, message);
					if(json != null) {
						LogUtil.log.error("###pushAndroidSingleAccount-json=" + json.toString());
					}
				}
			});
			t.start();
		} catch(Exception e) {
			LogUtil.log.error("###pushAndroidSingleAccount-userId=" + account + ",推送失败！");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushAndroidSingleAccount-userId=" + account + ",end!");
	}

	public void pushIOSSingleAccount(final int deviceType, final String account,
			final long accessId, final String secretKey, final String title, final String content) {
		if(StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(account)
				|| StringUtils.isEmpty(content)) {
			LogUtil.log.error("###pushIOSSingleAccount-推送用户列表为空，不处理!");
			return;
		}
		LogUtil.log.error("###pushIOSSingleAccount-userId=" + account + ",begin...");
		Thread t = null;
		try {
			t = new Thread(new Runnable(){
				@Override
				public void run() {
					XingeApp push = new XingeApp(accessId, secretKey);
					MessageIOS message = new MessageIOS();
					JSONObject alert = new JSONObject();
					alert.put("body", content);
					alert.put("subtitle", title);
					alert.put("title", title);
					message.setAlert(alert);
					JSONObject json = push.pushSingleAccount(deviceType, account, message, IOS_ENVIRONMENT);
					if(json != null) {
						LogUtil.log.error("###pushIOSSingleAccount-json=" + json.toString());
					}
				}
			});
			t.start();
		} catch(Exception e) {
			LogUtil.log.error("###pushIOSSingleAccount-userId=" + account + ",推送失败！");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushIOSSingleAccount-userId=" + account + "，end！");
	}

	public void pushAndroidAccountList(List<String> accountList,  Map<String, Object> parms, Map<String,Object> custom) {
		if(accountList == null || accountList.size() <= 0) {
			LogUtil.log.error("###pushAndroidAccountList-推送用户列表为空，不处理!");
			return;
		}
		if(parms == null
				|| !parms.containsKey(ACCESSID) || null == parms.get(ACCESSID)
				|| !parms.containsKey(SECRETKEY) || null == parms.get(SECRETKEY)) {
			LogUtil.log.error("###pushIOSAccountList-推送参数列表错误，不处理!");
			return;
		}
		LogUtil.log.error("###pushAndroidAccountList-size=" + accountList.size() + ",begin...");
		try {
			Message message = new Message();
			if(parms.containsKey(TITLE) && null != parms.get(TITLE)) {
				message.setTitle(parms.get(TITLE).toString());
			}
			if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
				message.setContent(parms.get(CONTENT).toString());
			}
			message.setMultiPkg(MULTIPKG); // 多包发送
			message.setType(Message.TYPE_NOTIFICATION);
			message.setStyle(new Style(0,1,1,0,0));
			
			// 定义指定跳转到房间的安卓activity
			int type = 0;
			if(parms.containsKey(TYPE) && null != parms.get(TYPE)) {
				type = Integer.parseInt(parms.get(TYPE).toString());
			}
			ClickAction action = new ClickAction();
			int actionType = ClickAction.TYPE_ACTIVITY;
			if(0 == type) { // 打开到首页
				
			} else if(1 == type) { // 打开到房间
				actionType = ClickAction.TYPE_ACTIVITY;
				action.setActivity(ROOM_ACTIVITY);
			} else if(2 == type) { // 打开到活动页
//				if(parms.containsKey(URL) && null != parms.get(URL)) {
//					action.setUrl(parms.get(URL).toString());
//				}
				actionType = ClickAction.TYPE_ACTIVITY;
				action.setActivity(WEB_ACTIVITY);
			}
			action.setActionType(actionType);
			message.setAction(action);
			
			// 跳转到的房间所需参数
			if(custom != null) {
				message.setCustom(custom);
			}
			
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			JSONObject json = push.pushAccountList(DEVICE_TYPE, accountList, message);
			if(json != null) {
				LogUtil.log.error("###pushAndroidAccountList-json=" + json.toString());
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushAndroidAccountList-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushAndroidAccountList-size=" + accountList.size() + ",end!");
	}

	public void pushIOSAccountList(List<String> accountList, Map<String, Object> parms, Map<String,Object> custom) {
		if(accountList == null || accountList.size() <= 0) {
			LogUtil.log.error("###pushIOSAccountList-推送用户列表为空，不处理!");
			return;
		}
		if(parms == null || custom == null
				|| !parms.containsKey(ACCESSID) || null == parms.get(ACCESSID)
				|| !parms.containsKey(SECRETKEY) || null == parms.get(SECRETKEY)) {
			LogUtil.log.error("###pushIOSAccountList-推送参数列表错误，不处理!");
			return;
		}
		LogUtil.log.error("###pushIOSAccountList-size=" + accountList.size() + ",begin...");
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			MessageIOS message = new MessageIOS();
			JSONObject alert = new JSONObject();
			if(parms.containsKey(CONTENT) && null != parms.get(CONTENT)) {
				alert.put(BODY, parms.get(CONTENT).toString());
			}
			if(parms.containsKey(TITLE) && null != parms.get(TITLE)) {
				alert.put(TITLE, parms.get(TITLE).toString());
			}
			message.setAlert(alert);
			
			// 跳转到的房间所需参数
			int type = 0;
			if(parms.containsKey(TYPE) && null != parms.get(TYPE)) {
				type = Integer.parseInt(parms.get(TYPE).toString());
			}
			custom.put(TYPE, type);
//			if(parms.containsKey(URL) && null != parms.get(URL)) {
//				custom.put(URL, parms.get(URL).toString());
//			}
			message.setCustom(custom);
			
			JSONObject json = push.pushAccountList(DEVICE_TYPE, accountList, message, IOS_ENVIRONMENT);
			if(json != null) {
				LogUtil.log.error("###pushIOSAccountList-json=" + json.toString());
			}
		} catch(Exception e) {
			LogUtil.log.error("###pushIOSAccountList-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushIOSAccountList-size=" + accountList.size() + ",end!");
	}

	private List<String> listAndroidFans(String anchorId) throws Exception {
		return pushDevMapper.listAndroidFans(anchorId);
	}
	
	private List<String> listIOSFans(String anchorId, String pckName) throws Exception {
		return pushDevMapper.listIOSFans(anchorId, pckName);
	}
	
	/**
	 * 创建大批量推送pushId，安卓
	 * @param message
	 * @return
	 */
	private String createMultipushAndroid(Map<String, Object> parms, Message message) {
		if(parms == null || parms.get(ACCESSID) == null
				|| parms.get(SECRETKEY) == null 
				|| message == null) {
			LogUtil.log.error("createMultipushAndroid-参数错误！");
			return null;
		}
		String pushId = "";
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			JSONObject ret = push.createMultipush (message);
			if(ret != null && ret.get(RET_CODE) != null) {
				System.err.println("ret=" + ret.toString());
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code && ret.getJSONObject(RESULT) != null) {
					JSONObject result = ret.getJSONObject(RESULT);
					pushId = result.get(PUSH_ID).toString();
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###createMultipushAndroid-创建失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		return pushId;
	}
	/**
	 * 创建大批量推送，IOS
	 * @param message
	 * @param environment
	 * @return
	 */
	private String createMultipushIOS(Map<String, Object> parms, MessageIOS message) {
		if(parms == null || message == null) {
			LogUtil.log.error("createMultipushIOS-参数错误！");
			return null;
		}
		String pushId = "";
		try {
			XingeApp push = new XingeApp(Long.parseLong(parms.get(ACCESSID).toString()), parms.get(SECRETKEY).toString());
			JSONObject ret = push.createMultipush(message, IOS_ENVIRONMENT);
			if(ret != null && ret.get(RET_CODE) != null) {
				System.err.println("retios=" + ret.toString());
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code && ret.getJSONObject(RESULT) != null) {
					JSONObject result = ret.getJSONObject(RESULT);
					pushId = result.get(PUSH_ID).toString();
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###createMultipushIOS-创建失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		return pushId;
	}
	
	/**
	 * 大批量推送
	 * @param pushId
	 * @param accountList
	 */
	private void pushAccountListMultiple(long pushId, List<String> accountList, long accessId, String secretKey) {
		if(accountList == null || accountList.size() <=0) {
			LogUtil.log.error("###pushAccountListMultiple-列表为空，不处理！");
		}
		LogUtil.log.error("###pushAccountListMultiple-size=" + accountList.size() + ",begin...");
		try {
			XingeApp push = new XingeApp(accessId, secretKey);
			JSONObject ret = push.pushAccountListMultiple (pushId, accountList);
			if(ret != null && ret.get(RET_CODE) != null) {
				int code = Integer.parseInt(ret.get(RET_CODE).toString());
				if(0 == code) {
					LogUtil.log.error("###pushAccountListMultiple-大批量推送成功!size=" + accountList.size());
				}
			}
		}catch(Exception e) {
			LogUtil.log.error("###pushAndroidAccountList-推送失败!");
			LogUtil.log.error(e.getMessage(),e);
		} 
		LogUtil.log.error("###pushAccountListMultiple-size=" + accountList.size() + ",end!");
	}
}
