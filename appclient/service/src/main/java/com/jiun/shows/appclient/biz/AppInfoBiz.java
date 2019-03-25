package com.jiun.shows.appclient.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.base.constant.SysConfTableEnum;
import com.jiujun.shows.base.domain.SysConf;
import com.jiujun.shows.base.service.ISysConfService;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.SystemUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiun.shows.appclient.constant.Constants;
import com.jiun.shows.appclient.dao.AppInfoMapper;
import com.jiun.shows.appclient.vo.AppConfigVo;
import com.jiun.shows.appclient.vo.AppInfo;


/**
 * app信息相关业务
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appInfoBiz")
public class AppInfoBiz {

	@Resource
	private AppInfoMapper appInfoMapper;

	@Resource
	private ISysConfService sysConfService;
	
	public AppInfo checkUpate(AppInfo appInfo) throws Exception{
		if(appInfo == null){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		AppInfo appInfoVo = new AppInfo();
		// 默认无需更新
		appInfoVo.setState(AppInfo.STATE_NO_NEED_UPDATE);
		
		Integer clientCurrentAppType = appInfo.getAppType();
		String clientCurrentVersionStr = appInfo.getVersion();
		String clientCurrentPackageName = appInfo.getPackageName();
		String clientCurrentSignatures = appInfo.getSignatures();
		 
		appInfoVo.setAppType(clientCurrentAppType);
		
		int clientCurrentVersionInt = SystemUtil.parseAppVersion(clientCurrentVersionStr);
		com.jiun.shows.appclient.domain.AppInfo newestAppInfo = appInfoMapper.getNewestVersion(clientCurrentAppType, null,null);
		if(StringUtils.isEmpty(clientCurrentSignatures)){ // 3.2.0之前的没有传参数:签名
			if(newestAppInfo != null){
				String dbNewestVersionStr = newestAppInfo.getVersion();
				int dbNewestVersionInt = SystemUtil.parseAppVersion(dbNewestVersionStr);
				if( clientCurrentVersionInt != dbNewestVersionInt ) { //    版本不等,提示升级
					appInfoVo.setState(AppInfo.STATE_NEED_UPDATE);
					appInfoVo.setUrl(newestAppInfo.getUrl());
					appInfoVo.setVersion(newestAppInfo.getVersion());
					appInfoVo.setMessage(newestAppInfo.getMessage());
					appInfoVo.setAddTime(newestAppInfo.getAddTime());
				}
			}else {
				LogUtil.log.error(String.format("###app检测更新(不传签名)时没发现有上传包,无需更新,appType:%s,packageName:%s,signatures:%s",clientCurrentAppType,clientCurrentPackageName,clientCurrentSignatures));
			}
		}else{ // 参数中传有签名
			if(newestAppInfo != null){
				boolean flag2update = false;
				String serverSignatures = null;
				// 服务端有的签名(多个是用逗号隔开)
				//String serverSignatures = newestAppInfo.getSignatures();
				String qryCode = SysConfTableEnum.Code.SYS_APP_ANDROID_SIGNATURES_CONF.getValue();
				SysConf sysConf = null;
				ServiceResult<SysConf> srt = sysConfService.getByCode(qryCode);
				if(srt.isSucceed()) {
					sysConf = srt.getData();
				}
				LogUtil.log.info(String.format("###表t_sys_conf中code:%s配置的签名数据:%s", qryCode,JsonUtil.beanToJsonString(sysConf))) ;
				if(sysConf==null){
					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
					throw e;
				}else{
					String confVal = sysConf.getConfValue();
					if(StringUtils.isEmpty(confVal)){
						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3094);
						throw e;
					}else{
						serverSignatures = confVal;
					}
				}
				String errorMsg = "";
				// 客户端的签名不在我们的签名集合中,提示更新
				if( !serverSignatures.contains(clientCurrentSignatures)){
					//这种情况还要另外设置errorMsg
					errorMsg = "基于安全考虑，建议不要使用非官方渠道来源应用，以免对手机安全和个人数据带来风险，请到官网下载";
					flag2update = true; 
				}else{
					String dbNewestVersionStr = newestAppInfo.getVersion();
					int dbNewestVersionInt = SystemUtil.parseAppVersion(dbNewestVersionStr);
					// 版本号不等,提示更新
					if( clientCurrentVersionInt != dbNewestVersionInt ) {  
						flag2update = true; 
					}else{
						LogUtil.log.info(String.format("###app检测更新时发现版本一致,无需更新"));
					}
				}
				
				
				if(flag2update){
					appInfoVo.setState(AppInfo.STATE_NEED_UPDATE);
					appInfoVo.setErrorMsg(errorMsg);
					appInfoVo.setUrl(newestAppInfo.getUrl());
					appInfoVo.setVersion(newestAppInfo.getVersion());
					appInfoVo.setMessage(newestAppInfo.getMessage());
					appInfoVo.setAddTime(newestAppInfo.getAddTime());
				}else{
					appInfoVo.setState(AppInfo.STATE_NO_NEED_UPDATE);
				}
			}else {
				LogUtil.log.error(String.format("###app检测更新(传签名)时没发现有上传包,无需更新,appType:%s,packageName:%s,signatures:%s",clientCurrentAppType,clientCurrentPackageName,clientCurrentSignatures));
			}
		}
					
		return appInfoVo;
	}

	public JSONArray appFunctionOpenCheck(String userId,
			DeviceProperties deviceProperties) throws Exception {
		// 获取配置
		SysConf sysConf = null;
		ServiceResult<SysConf> srt = sysConfService.getByCode(Constants.APP_FUNCTION_OPEN);
		if(srt.isSucceed()) {
			sysConf = srt.getData();
		}
		if(sysConf == null){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONArray jsonArray = new JSONArray();
		String confValue = sysConf.getConfValue();
		JSONArray jsonarray = JsonUtil.strToJSONArray(confValue);
		if(jsonarray != null && jsonarray.size() >0){
			for (Object object : jsonarray) {
				AppConfigVo vo = new AppConfigVo();
				JSONObject jsonObject = (JSONObject) object;
				if(jsonObject != null){
					int type = jsonObject.getIntValue("type");
					int level = jsonObject.getIntValue("level");
					if(!StringUtils.isEmpty(type)){
						vo.setType(type);
					}
					String isOpen = "n";//关闭
					if(!StringUtils.isEmpty(level)){
						vo.setLevel(level);
						if(level == 1){//控制用户
							if(jsonObject.containsKey("user")){
								JSONArray user = JsonUtil.strToJSONArray(jsonObject.getString("user"));
								for (Object obj : user) {
									String uid =  obj.toString();
									if(uid.equals(userId)){
										isOpen = "y";
										break;
									}
								}
							}
							
						}else if(level == 2){//控制设备
							String imei = deviceProperties.getImei();
							if(jsonObject.containsKey("device")){
								JSONArray device = JsonUtil.strToJSONArray(jsonObject.getString("device"));//IMEI
								for (Object obj : device) {
									String dev =  obj.toString();
									if(imei.equals(dev)){
										isOpen = "y";//开启
										break;
									}
								}
							}
						}else if(level == 3){//控制渠道、包名
							if(jsonObject.containsKey("channelAndPackage")){
								String chan = deviceProperties.getChannelId();
								String pack = deviceProperties.getPackageName();
								JSONArray channelAndPackage = JsonUtil.strToJSONArray(jsonObject.getString("channelAndPackage"));//IMEI
								for (Object obj : channelAndPackage) {
									JSONObject jobj = (JSONObject) obj;
									String channel = String.valueOf(jobj.getString("channel"));//渠道
									String packageName = String.valueOf(jobj.getString("package"));//包名
									//渠道、包名都匹配
									if(chan.equals(channel) && pack.equals(packageName)){
										isOpen = "y";
										break;
									}
								}
							}
						}else if(level == 4){//控制包名
							if(jsonObject.containsKey("package")){
								String pack = deviceProperties.getPackageName();
								JSONArray packageArr = JsonUtil.strToJSONArray(jsonObject.getString("package"));
								for (Object obj : packageArr) {
									String packageNaame =  obj.toString();
									if(pack.equals(packageNaame)){
										isOpen = "y";
										break;
									}
								}
							}
						}else if(level == 5){//全部
							isOpen = "y";
						}
						vo.setIsOpen(isOpen);
						jsonArray.add(vo.buildJson());
					}
				}
			}
		}
		return jsonArray;
	}

}
