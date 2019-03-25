package com.jiujun.shows.car.constant;

import java.io.File;

import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.utils.SpringContextListener;


public class Constants extends BaseConstants {
	
	/** service日志输出文件名 */
	public static final String LOG_CAR_SERVICE = "car_service";
	/** web日志输出文件名 */
	public static final String LOG_CAR_WEB = "car_web";
	
	/** 座驾图片地址 */
	public static final String CAR_CDN_URL = SpringContextListener.getContextProValue("cdnPath", "") + Constants.CAR_IMG_FILE_URI + File.separator;
	
}
