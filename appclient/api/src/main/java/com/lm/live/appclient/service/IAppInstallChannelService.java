package com.lm.live.appclient.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.framework.service.ServiceResult;
import com.lm.live.appclient.domain.AppInstallChannelDo;

/**
 * app聚到记录服务
 * @author shao.xiang
 * @date 2016-06-18
 *
 */
public interface IAppInstallChannelService extends ICommonService<AppInstallChannelDo> {
	
	/**
	 * 判断是否已有记录,并保存
	 * @param mac
	 * @param imei
	 * @return
	 */
	ServiceResult<Boolean> testIfExistsAndCache(String mac,String imei) ;

	/**
	 * 保存安装渠道(已保存过则忽略)
	 * @param device
	 */
	ServiceResult<Boolean> recordChannel(DeviceProperties device);
	
	
	ServiceResult<String> getChannelIdByImei(String imei);
	
	/**
	 * 获取渠道信息
	 * @param imei
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月18日
	 */
	ServiceResult<AppInstallChannelDo> getByImei(String imei);
	
}
