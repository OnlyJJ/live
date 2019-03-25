package com.jiun.shows.appclient.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiun.shows.appclient.constant.CacheTime;
import com.jiun.shows.appclient.constant.RedisKey;
import com.jiun.shows.appclient.dao.AppInstallChannelMapper;
import com.jiun.shows.appclient.domain.AppInstallChannelDo;

/**
 * app渠道业务
 * @author shao.xiang
 * @date 2017-06-19
 *
 */
@Service("appInstallChannelBiz")
public class AppInstallChannelBiz {
	
	@Resource
	private AppInstallChannelMapper appInstallChannelMapper;

	public ServiceResult<Boolean> testIfExistsAndCache(String mac, String imei) { 
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		boolean  flag = false;
	//	if(StringUtils.isEmpty(mac)&&StringUtils.isEmpty(imei)){
		if(StringUtils.isEmpty(imei)){
			srt.setSucceed(true);
			return srt;
		}
		//String key = MCPrefix.APP_HAVE_INSTALL_DEVICE_IN_CACHE+mac+imei;
		String key = MCPrefix.APP_HAVE_INSTALL_DEVICE_IN_CACHE+imei;
		Object obj = RedisUtil.get(key);
		if(obj != null){
			LogUtil.log.info(String.format("###检测app安装渠道,在缓存中发现已有记录,imei:%s,mac:%s,cacheKey:%s",imei,mac,key));
			flag = true;
		}else{
			//AppInstallChannelDo d = appInfoMapper.getByMacImei(mac, imei);
			AppInstallChannelDo d = appInstallChannelMapper.getByImei(imei);
			if(d !=null){
				flag = true;
				RedisUtil.set(key, d,CacheTime.APP_HAVE_INSTALL_DEVICE_IN_CACHE_TIMEOUTSECOND);
			}
		}
		srt.setSucceed(flag);
		return srt;
	}

	public ServiceResult<Boolean> recordChannel(DeviceProperties device) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		boolean  flag = false;
		if(device==null){
			return srt;
		}
		String mac  = device.getMac();
		String imei = device.getImei();
		
		//
		//if(StringUtils.isEmpty(mac)||StringUtils.isEmpty(imei)){
		/*
		if(StringUtils.isEmpty(imei) 
				|| (imei.length()!=15 && imei.length()!=32 && imei.length()!=36) 
				|| imei.equals("000000000000000")
				|| imei.equals("111111111111111")
				|| imei.equals("222222222222222")
				|| imei.equals("333333333333333")
				|| imei.equals("444444444444444")
				|| imei.equals("555555555555555")
				|| imei.equals("666666666666666")
				|| imei.equals("777777777777777")
				|| imei.equals("888888888888888")
				|| imei.equals("999999999999999")
				|| imei.equals("123456789012345")
				|| imei.equals("012345678912345")
				|| imei.equals("123456789123456")
				||  StringUtils.isEmpty(device.getChannelId())){
			return;
		}
		*/
		//只存32或36位长的uuid
		if(StringUtils.isEmpty(imei) 
				|| (imei.length()!=32 && imei.length()!=36) 
				||  StringUtils.isEmpty(device.getChannelId())){
			return srt;
		}
		ServiceResult<Boolean> tsrt = testIfExistsAndCache(mac,imei);
		if(tsrt.isSucceed()){//若已有记录，则不重复保存
			return srt;
		}
		
		AppInstallChannelDo d = new AppInstallChannelDo();
		d.setChannelId(device.getChannelId());
		d.setMac(mac);
		d.setRecordTime(new Date());
		d.setImei(imei);
		d.setAppPackage(device.getPackageName());
		d.setAppType(device.getAppType());
		appInstallChannelMapper.insert(d);
		LogUtil.log.info(String.format("###成功记录app安装的渠道信息,imei:%s", imei));
		srt.setSucceed(true);
		return srt;
	}

	public ServiceResult<String> getChannelIdByImei(String imei) {
		ServiceResult<String> srt = new ServiceResult<String>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(imei)){
			return srt;
		}else{
			String channelId = appInstallChannelMapper.getChannelIdByImei(imei);
			if(!StringUtils.isEmpty(channelId)) {
				srt.setData(channelId);
				srt.setSucceed(true);
			}
		}
		return srt;
	}

	public ServiceResult<AppInstallChannelDo> getByImei(String imei) {
		ServiceResult<AppInstallChannelDo> srt = new ServiceResult<AppInstallChannelDo>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(imei)){
			LogUtil.log.info(String.format("###通过imei获取渠道记录信息，imei为空，不处理"));
			return srt;
		}
		AppInstallChannelDo vo = null;
		String key = RedisKey.APPINSTALL_CHANNEL_INFO_CACHE + imei;
		Object obj = RedisUtil.get(key);
		if(obj != null){
			vo = (AppInstallChannelDo) obj;
		}else{
			AppInstallChannelDo d = appInstallChannelMapper.getByImei(imei);
			if(d !=null){
				srt.setData(d);
				srt.setSucceed(true);
				RedisUtil.set(key, d,CacheTime.APP_HAVE_INSTALL_DEVICE_IN_CACHE_TIMEOUTSECOND);
			}
		}
		return srt;
	}

	
}
