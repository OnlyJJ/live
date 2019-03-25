package com.jiujun.shows.dynamic.constant;

import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.utils.SpringContextListener;

public class Constants extends BaseConstants {
	/** service日志输出文件名 */
	public static final String LOG_CAR_SERVICE = "dynamic_service";
	/** web日志输出文件名 */
	public static final String LOG_CAR_WEB = "dynamic_web";
	
	/** 需要屏蔽的测试账号  */
	public final static String UN_SHOW_ROOM = SpringContextListener.getContextProValue("anchor.UserId.baiwan", "104573");
	
}
