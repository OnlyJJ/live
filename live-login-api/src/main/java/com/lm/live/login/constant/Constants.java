package com.lm.live.login.constant;

import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.SpringContextListener;

public class Constants extends BaseConstants {
	
	/** 提供web登录时使用的包名 */
	public static final String DEFAULT_PACKAGENAME = "com.lm.live";
	
	/** 微信拉取授权url */
	public static final String WECHATREFRESHURL = SpringContextListener.getContextProValue("", "https://api.weixin.qq.com/sns/oauth2/refresh_token");
	
	/** 微信url */
	public static final String WECHAT_URL = SpringContextListener.getContextProValue("url_weixin_access_token", "https://api.weixin.qq.com/sns/oauth2/access_token");
	
	/** 微信用户信息 */
	public static final String WECHATINFO_URL= SpringContextListener.getContextProValue("url_weixin_userinfo", "https://api.weixin.qq.com/sns/userinfo");
	
	/** 微博拉取用户信息url */
	public static final String WEIBO_URL = SpringContextListener.getContextProValue("url_weibo_sina_userinfo", "https://api.weibo.com/2/users/show.json");
	
	/** 拉取微博授权信息 */
	public static final String WEIBO_TOKEN_URL = "https://api.weibo.com/oauth2/get_token_info";
	
	// my-todo
	/** 存储在cdn上的图片url，这里要修改 */
	public static final String USER_ICON_URL = SpringContextListener.getContextProValue("cdnUpload", "/data/apps/xxwan_cdn/advert/");
	
	/** 伪登录的session key */
	public static final String PSEUDO_LOGIN_SESSION_KEY = "pesudo_";
	/** 伪登录设置校验的md5code */
	public static final String CHECK_MD5CODE = "lm&live";
	/** 自动注册默认设置的密码 */
	public static final String DEFAULT_AUTO_PWD = "autoRegist";
	/** 微信登录默认设置的密码 */
	public static final String DEFAULT_WECHAT_PWD = "appwechatlogin";
	/** APPQQ登录默认设置的密码 */
	public static final String DEFAULT_APPQQ_PWD = "appqqlogin";
	/** WEBQQ登录默认设置的密码 */
	public static final String DEFAULT_WEBQQ_PWD = "webqqlogin";
	/** APPWB登录默认设置的密码 */
	public static final String DEFAULT_APPWB_PWD = "appwblogin";
	/** WEBWB登录默认设置的密码 */
	public static final String DEFAULT_WEBWB_PWD = "webwblogin";
	
	/** 自动注册每个ip限制的个数，防止被刷*/
	public static int AUTO_REGIST_IP_TALL_LIMIT = 1000000;
	
	/** 每天自动注册量的限制 */
	public static int AUTO_REGIST_IP_DAY_LIMIT = 10000000;
	
	/** 自动注册给的名字 */
	public static String DEFAULT_AUTOREGIST_NAME = "王者农药,大乔,黄忠,诸葛亮,哪吒,太乙真人,杨戬,成吉思汗,橘右京,马可波罗,雅典娜,夏侯惇,蔡文姬,关羽,虞姬,安琪拉,项羽,后羿,刘禅,王昭君,赵云,花木兰,狄仁杰,韩信,墨子,鲁班,宫本武藏,牛魔,貂蝉,典韦,小乔,甄姬,妲己,孙膑,武则天,高渐离,吕布,孙悟空,孙尚香,达摩,程咬金,扁鹊,亚亚瑟,荆轲,姜子牙,周瑜,老夫子,嬴政,曹操,庄周,张良,钟无艳,芈月,露娜,廉颇,白起,张飞,刘备,兰陵王,娜可露露,李白,钟馗,李元芳,刘邦,不知火舞";
	
	/** 登录成功 */
	public static String USER_LOGIN_SUCCESS = "登录成功！";
	
}
