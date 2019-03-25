package com.lm.live.appclient.service;

import com.lm.live.common.vo.DeviceProperties;

/**
 * app包检测服务
 * @author shao.xiang
 * @date 2017-06-18
 *
 */
public interface IAppCheckConfService {

	/**
	 * 检测app包是否停用
	 * @param deviceProperties
	 */
	public boolean appPackageCheck(DeviceProperties deviceProperties);

}
