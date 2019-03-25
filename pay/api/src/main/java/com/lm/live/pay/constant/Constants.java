package com.lm.live.pay.constant;

import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.SpringContextListener;

public class Constants extends BaseConstants {
	/** service日志输出文件名 */
	public static final String LOG_PAY_SERVICE = "pay_service";
	
	/** web日志输出文件名 */
	public static final String LOG_PAY_WEB = "pay_web";
	
	/** 充值显示说明 */
	public static String CHARGE_SUBJECT = "乐萌直播金币充值";
	
	/** 系统官方账号，用于测试 */
	public static final String OFFICIAL_USER = "100000";
	
	/** 最低充值金额，单位：分 */
	public static final int LEAST_MONEY = 500;
	
	/** 支付宝消息验证地址 */
	public static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/** 统一下单地址 */
	public static final String ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/** 公众号支付的签名方式 */
	public static final String PUBLIC_NUMBER_SIGNTYPE = "MD5";
	
	/** 支付宝充值说明 */
	public static final String ALI_PAY_REMARK = "支付宝充值";
	
}
