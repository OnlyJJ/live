package com.lm.live.common.utils;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * app客户端相关工具类
 * @author shao.xiang
 * @date 2017年9月26日
 *
 */
public final class AppUtils {
	
	/**
	 * app版本比较
	 * @param oldVersion 老版本
	 * @param newVersion 新版本
	 * @return 
	 * @author shao.xiang
	 * @date 2017年6月6日
	 */
	public static boolean checkAppVersion(String oldVersion, String newVersion) {
		boolean flag = false;
		if(StringUtils.isEmpty(newVersion)) {
			return flag;
		}
        int intOldVersion = Integer.parseInt(oldVersion.replace(".", "0"));
        int intNewVersion = Integer.parseInt(newVersion.replace(".", "0"));
        if(intNewVersion >= intOldVersion) {
        	flag = true;
        }
		return flag;
	}
}