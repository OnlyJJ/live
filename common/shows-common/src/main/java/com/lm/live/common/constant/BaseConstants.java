package com.lm.live.common.constant;

import com.lm.live.common.utils.SpringContextListener;

public class BaseConstants {
	// 这些路径都需要修改，my-todo
	// 图片路径，begin
	/** 主播图片存储位置 */
	public static final String ANCHOR_IMG_FILE_URI = "images/anchor";  
	/** 个人用户头像 */
	public static final String ICON_IMG_FILE_URI = "images/icon"; 
	/** 用户默认头像图片 ，这个需要修改，my-todo*/
	public static final String USER_DEFAULT_ICON = "145432501711592.png";
	/** 礼物图片存储位置  */
	public static final String GIFT_IMG_FILE_URI = "images/gift";
	/** 工具（宝箱，钥匙，大喇叭…）图片存储位置 */
	public static final String TOOL_IMG_FILE_URI = "images/tool";
	/** 勋章图片位置 */
	public static final String DECORATE_IMG_FILE_URL = "images/decorate";
	/** 守护图片位置 */
	public static final String GUARD_IMG_FILE_URL = "images/guard";
	/** banner地址 */
	public static final String BANNER_IMG_FILE_URI = "images/banner";
	/** 收件箱图片位置 */
	public static final String INBOX_IMG_FILE_URL = "images/inbox";
	/** 用户直播间举报图片 */
	public static final String ACCUSATION_IMG_FILE_URL = "images/accusation";
	// 图片路径，end!
	
	// 直播
	/** 直播截图url */
	public static final String ANCHOR_SCREENSHOT_URL = "http://screenshot.9shows.com/live/";
	
	/** 测试服务器的http请求url */
	public static final String URL_TEST_SERVER = SpringContextListener.getContextProValue("testServiceHttpUrl", "http://testservice.9mitao.com/");
	
	/** 是否测试服务器:依据配置文件serviceIsTestSite属性判断 */
	public static final boolean flagServiceIsTestSite = "true".equals(SpringContextListener.getContextProValue("serviceIsTestSite", "false"));
	
	public static final String cdnPath = SpringContextListener.getContextProValue("cdnPath", "http://cdn.9shows.com/");
	
	public static final String SENDUSER_DEFAULT = "system"; //默认系统赠送
	
	/**  与IM之间约定的指定为系统身份的userId */
	public static final String 	SYSTEM_USERID_OF_IM = SpringContextListener.getContextProValue("system.user.userId", "100000");
	
	/** 与IM之间约定的全站通知时所用特殊房间号 */ 
	public static final String 	WHOLE_SITE_NOTICE_ROOMID = SpringContextListener.getContextProValue("whole_site_notice_roomId", "100000");
	
	/** service端接口项目地址 */
	public static final String SERVICEURL = SpringContextListener.getContextProValue("serviceUrl", "http://testservice.9mitao.com");
	
	/** app版本xxx后，采用aac视频格式的 */
	public static final int AppVersionOfAac = 999999999;
	
	public static final String DATA_BODY = "data";
	
	public static final String REQ_ATTR_DATA = "request_attribute_data";
	
	
	/** 默认字符编码，UTF-8 */
	public static final String DEFAULT_UNICODE = "UTF-8";
	/** 状态 （字符）, n*/
	public static final String FLAG_NO = "n";
	/** 状态 （字符） , y*/
	public static final String FLAG_YES = "y";
	/** 状态（int），0-否 */
	public static final int STATUS_0 = 0;
	/** 状态（int），1-是 */
	public static final int STATUS_1 = 1;
	/** 逗号分隔符 */
	public static final String SEPARATOR = ",";
	/** 日期格式，yyyy-MM-dd */
	public static String DATEFORMAT_YMD = "yyyy-MM-dd";
	/** 日期格式，yyyyMMdd */
	public static String DATEFORMAT_YMD_1 = "yyyyMMdd";
	/** 日期格式，yyyy-MM-dd HH:mm:ss */
	public static String DATEFORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式，yyyy-MM-dd HH:mm:ss.SS */
	public static String DATEFORMAT_YMDHMS_1 = "yyyy-MM-dd HH:mm:ss.SS";
	/** 图片后缀 */
	public static String IMG_PNG = ".png";
	public static String IMG_JPG = ".jpg";
	
	
}
