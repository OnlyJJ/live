package com.lm.live.common.utils;

import org.apache.commons.lang.StringUtils;


public class SystemUtil {
	
	
	
	
	/**
	 * 解析app端的版本号 (约定由三段组成,即有两个点间隔的数字)，超过则返回0<br />
	 * .去掉,如:3.0.1 将转换成301
	 * @param versionStr
	 * @return
	 */
	public static int parseAppVersion(String paramVersionStr){
		int versionInt = 0;
		if(StringUtils.isNotEmpty(paramVersionStr)){
			String versionStr = StrUtil.trimStr(paramVersionStr);
			versionStr = versionStr.replaceAll("\\.","");
			versionInt = Integer.parseInt(versionStr);
		}else{
			LogUtil.log.info(String.format("###解析app版本号 时发现version为空"));
		}
		LogUtil.log.info(String.format(String.format("###解析app版本号,字符串参数:%s,解析int结果:%s",paramVersionStr,versionInt)));
		return versionInt;
	}
	
}
