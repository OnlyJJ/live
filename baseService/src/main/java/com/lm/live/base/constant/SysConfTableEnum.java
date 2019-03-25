package com.lm.live.base.constant;


/** t_sys_conf  */
public class SysConfTableEnum {

	public static enum Code{
		
		/**
		 * code=open_box_conf(关于开启宝箱的配置)
		 */
		OPENBOX("open_box_conf"),
		
		/**
		 * code=qixi_2016_xunzhang(2016七夕勋章)
		 */
	    QIXI2016XUNZHANG("qixi_2016_xunzhang"),
	    
	    /**
	     * code=aoyun_2016_chongzhi_xunzhang,奥运期间充值累计送奥运勋章
	     */
	    AOYUN2016XUNZHANG("aoyun_2016_chongzhi_xunzhang"),
	    
	    /**
	     * 2016，9月恋爱勋章活动
	     */
	    LIANAI92016XUNZHANG("lianaiji9_2016_songlixunzhang"),
		
		 /**
	     * android签名配置
	     */
	    SYS_APP_ANDROID_SIGNATURES_CONF("sys_app_android_signatures_conf"),
		
		 /**
	     * 苹果支付,存在异常行为的ip,利用代充漏洞的ip(不加金币),若存在多个异常ip,则用逗号隔开
	     */
	    IOS_PAY_INVALID_IP_CONF("ios_pay_invalid_ip_conf"),
		
		 /**
	     * 苹果支付,bundle_id
	     */
	    IOS_PAY_BUNDLE_ID_CONF("ios_pay_bundle_id_conf"),
		
		 /**
	     * 送礼开关配置
	     */
	    SYSTEM_SENDGIFT_INUSE_STATUS("system_sendgift_inuse_status"),
		
		 /**
	     * 支付宝支付,http请求超时设置
	     */
	    PAYCHARGE_ALIPAY_HTTPTIMEOUT_CONF("payCharge_aliPay_httpTimeout_conf"),
		
		 /**
	     * 系统帮未注册用户自动生成一个预登录帐号(和登录帐号拥有一致功能)的开关控制
	     */
	    SYSTEM_AUTO_REGIST_LIMIT_CONF("system_auto_regist_limit_conf");
		
		
		private final String value;
		
		Code(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static enum IsUse{
		/** 1 : 启用*/
		Yes(1),
		
		/** 1 : 停用*/
		No(0);
		
		private final int value;
		
		IsUse(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
