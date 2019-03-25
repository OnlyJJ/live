package com.lm.live.pay.utils;

import com.lm.live.common.utils.SpringContextListener;

/**
 * 支付宝基础配置常量类
 * 提示：如何获取安全校验码Key和合作身份者ID(partner_id)
 * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 * 2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 * 3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”
 */
public final class AlipayConfig {
	
	/*原来的*/
	/** 合作身份者ID，以2088开头由16位纯数字组成的字符串 */
	//public static final String PARTNER_ID= "2088611519270071";
	/** 签约支付宝账号或卖家收款支付宝帐户 */
	//public static final String SELLER_EMAIL = "lily@ninebox.cn";
	//商户私钥，pkcs8格式
	/*public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMf3Jmew1EzRvwXN"
									+"RCY4hyOFuOd0nWVJLgYOGi38h3O+UM+1X9sg7VKYyqOJfkmDjp/ON6GmbOR8rp8j"
									+"fitAt2IpvyvqMyHEldhqO+BdhZSdYL+dW2KiP1Ma21DvYdANCTBJxmZYizjD0ypZ"
									+"oIq1qyEskobJUtoHNZqS6CDcBU0jAgMBAAECgYBgQXUoNrOLIu/73/otF8rda5I9"
									+"QCI/zkEsQv+RIV0mdvc5dMpmlewHdWsd26KByqUOAds/ofgK6IW6LwCd23IqILlU"
									+"9zazMMA0CU9k5abBx7QVTIzJpeSRw7EOK6ENBs16FQu8CAFsltXZwfNJBVcxYbTA"
									+"u7ywKGEZv2k3SOwumQJBAPigm5CrdgXEUO3Cdn3kQ/r6JkTW2mgkr5eHjMqTYf/D"
									+"q2HIS1i3jKKgO5p87AhhSNyQlqzy6NjAWJ+btVP0jt0CQQDN5SMtBcfokPolL+pb"
									+"JDAt43RYTEMRBETPbhNjWqImiYXTuzKbjDEU6uZwSVqvdiV2I2ZuDC5kNXlB2c7Z"
									+"LYv/AkEAtC2AT3lSkJ9Snttcj6NK+Kkr7ptdsDdV4RRknbHtRTMCElza7bInAvvn"
									+"9e8uBmyqJt3JtBBdWicakqB+6hbDKQJBAL4BKc//mLb/K0isCiqnn6qJT7G4Vt/a"
									+"P6Ho5A1481B68rws8Yvn+Ndj2tpeZ0iqz/FQw9LkfN9PFaJ2XNPAm4MCQBVjjlAz"
									+"QIRDCfjgOuZe7wa3oNpW7n7+IMClvnHl1M+0AM9JF1b4qgiwyDihg+gkLj9xm3LT"
									+"A/ZuyKCvJ3OWBTY=";
	
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6Bk" +
			"RAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9" +
			"q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";*/
		
	//米玩支付宝
	//public static final String PARTNER_ID= "2088421320041843";
	//public static final String SELLER_EMAIL = "miwan@miwan123.com";
	//public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMO1/um4vpnldoHUKTUoQ70XRAiu84Mgj7kn1Ym7mQ0B1NIkhZoXCs48xTDy7dkLRRzGdBsGVn+w4Hd07H/NDjkIP7XwqzHQss3Hl3yeFeCqW/CMa1vO6wN6XUNmJe8OE4HX79sjOD472c/6tWkR00nM98XKiwoOtyPQV+5VTkPPAgMBAAECgYBtVIcyA72edVxtjJr0kii9QnkELM/mSdNgSQt2ZPFMHeDJABQOj56+tZ4nCR6r6CrUt1x3GGRL8wCCWzQe9OtjIHYRe+qHwkZQdYIkl72c8+dnZ3vtVuYrnNfwhp24kDgfZMiFlQEXoOTW0P4Kav6SCpQkUtPKo4ZXfFj0Ufk6kQJBAOvY/TIeJLKAjIJUwhegAU71VO2kpUelye99JfgxRp8yXExknQcUH0UFNTZJ7nBsg4P1BvPUigWp9JPXLQVJ1FcCQQDUbwsEdhONZ0mV9o8v25KVX2Hu9MX9aTffBokqQNNE6cAD/wVSGNSrb06kTfmerKuxIj8/fJl6hl0Cz/JfOqFJAkEAx8CxukgDhtgPqbdF6KKRWSG6u8gOYJ+PZ/KUPSDnEqChweYZhUxKq7eceQ2nELsnrJTUPaIPAHsQ9kJyFB+S4wJAP0aoMHb/UfI/H/UmJ2ou2mFj6WptgxMROlRXDQfsJi2+LTKMIi0VQ89OzHARd2sVZRi/YCi8Cpv1Hn5UQntTSQJAVk5Y+4q/wSJpq/KR7iDzpUbGVxUgqHMU32blQ/pQs5geJiaalaYsMoDeu3GNwAMbPYaAXWSudMJRIqYgKvsjXA==";
	//public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//卓告支付宝
	//public static final String PARTNER_ID = "2088221177536525";
	//public static final String SELLER_EMAIL = "yumi@yumiyouxi.com";
	//public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANwag0+Z+Ud6iDGe8AZVBWBZmm1I14xhTD6ici0rtBmsLScEJS17QepE57FyktBeav+c0KnQuhnweMnWPpkNNs+ds/1+EgODjTsWbz2euyl+bEBYXcfL/r9WRVOfcyFg9038KUS2Zvj+6Gjf4I1v58tt8Vj3nSf7T2XAGgQLXZwRAgMBAAECgYA1/1MjnjSnsonjEoj0JLuS7aiTEwG+cMhRvWyhZE1k1vak0cGFLO342Tq2L9OQxvkdCI2we4KMhpA7hBILXlCJnizelCMgDppLr9zwJ2KoR33xzgb2tOp9fh2bw8WpUT0nOnuC+36busuGfrCAyCNsBO6G1CwlARTb98dRmc40+QJBAPYiVS+j3IqCXiWTQLz83japdiSDHsjvs+03vyQ/ON0kcDwHHbEqatJ6Tk/4Hu4AxsbpysaONMoHYQ9cvrd7nX8CQQDk7RJdf2CNbvJOYJ73aqzRhItnt447gB5pezkxNGaCo1CGWvCtYiT/9G+uBGZYT1bOaaxGF5/2bzPXohXUSK5vAkEAwz+w+72s8WNBc+U/8atWS15ajdRF03fy7zbNtd9BKIwDTFsIvsW7P4QuI1ncj0imZpyHcsLUNX9tC1azI7I32QJAFrq/Jw9dm81XO5b2UaSC0i6x1LNx6NB4tIpqjEsinnfup9hhqyVifRNc/08qhjwiZ04wr6jq1gIGiLNsLW6l+wJAdzEPtlJwhZ4aVvn66qb+odVfZksZDTZIqSqjTRlBmBYYa+BXQwmHd8GyopICppPigEhFfdre18byUdUGeJowcQ==";
	//public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCL6d0V7wwpSRtnIkOIy8r2/2bk1L0KZ6OdM32YUJSu4TG+QJFqSXMNBwpJTsfzFqTEJUammyPh0ytXye4DN5yFZpWpS/i7sAPOfZJ5fSwxj2luwDL4Z/MqfvTQhHcPASTa+v+5oR2vt55GfGLR6MARMT1488JPl8fXLErZiANxwIDAQAB";
	
	//叉叉，支付宝
	//public static final String PARTNER_ID = "2088011586505900";
	//public static final String SELLER_EMAIL = "2871760428@qq.com";
	//public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMZb3bFEZeFuPlGakzujK1xSRkCPdtyiEe7CUv6R9g3u39zioAZ7A4J6e1I05wZCOU9iA/XaBVvbLzrvvWlmyDChqkhE3f+OZ7WueYHdbT/HWVsVtWS3pnlz7Fhc5/ONgV6TGPJ+qJZ0ysLjSaDsFuikrUBxOeOpjNxdBb+LyBOJAgMBAAECgYBtEnTcFfVDuYAxFSNixr2AHYmd1IWSmtfrhxVmAFLhpHmal09RYPE0HS+Uq5xhl6a13Pq6hLEvql3VNaDcxXTMOIql8GjG+JM6GtKbpHadYH0oMzaPHa15+pq5RalDesXxLD2glnJCOtpJdRIQes2jQPhs4QsyF16Ebo/fDcpjgQJBAPKJRqQJMHvI9cSgYaa05Hbe1SbSQwu4W1PPiy4iOmKE5hQwNFQMZew9ZwB1sCeTeYSAvwXJfhY+lBLUgJY0QtECQQDRXscV2OXhbMFcv0k1lrxJqYnUnKjOQjcLhxxjMARBQsGUFHX9wCpjSCw3iYC8AJE6DFcvJ9SVJ9LidAnnQMM5AkAGgYQxlkWErIlbMuorrKKMRcH1/qHpyJI6l1CmdiiWx/XcCMb+ki6RvbvrmTyWczBJS6LUJumI8zjJoIWqgdBhAkEApZi8CCiccbauHWhiRBNr6ytAC7y4uyKeHO0JY94nj2J4b2HbjQ7/t7Tu3CJSguBD/VCjaHWtayerYVb8Oj776QJBAICHt+LFYLM7uQjRLzX1iYHOk7M7HNaK5wpUuMjLpglIMGJuXxGdlc5suUNSrxscouy2xvxO8W7abTLaCBdj+Tc=";
    //public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1GduM5BGdV/ZUjsdzwC/Dz0MEgoy7/CiTiZ2DaTUDBQ6LZZl9OL+eCkbKqunRiD2QXd2A6nO419iZdktAql7gpc3Jz+S5ByOYKck0EIMvdiT/oLtj1xzWlQdy4PZc9h4jvkmZXSrIe8eDhvJbZ7n1UIuG5O/7m8lMlQH5yHXQIQIDAQAB";
	
	//支付宝公钥
	//public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	
	public static String PARTNER_ID = SpringContextListener.getContextProValue("ZFB_partner", "");
	public static String SELLER_EMAIL = SpringContextListener.getContextProValue("ZFB_seller", "");
	public static String RSA_PRIVATE = SpringContextListener.getContextProValue("ZFB_privateKey", "");
	public static String RSA_PUBLIC = SpringContextListener.getContextProValue("ZFB_ali_publicKey", "");
	public static String RSA2_PUBLIC = SpringContextListener.getContextProValue("ZFB_ali_publicKey_rsa2", "");
	
//  public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMf3Jmew1EzRvwXNRCY4hyOFuOd0nWVJLgYOGi38h3O+UM+1X9sg7VKYyqOJfkmDjp/ON6GmbOR8rp8jfitAt2IpvyvqMyHEldhqO+BdhZSdYL+dW2KiP1Ma21DvYdANCTBJxmZYizjD0ypZoIq1qyEskobJUtoHNZqS6CDcBU0jAgMBAAECgYBgQXUoNrOLIu/73/otF8rda5I9QCI/zkEsQv+RIV0mdvc5dMpmlewHdWsd26KByqUOAds/ofgK6IW6LwCd23IqILlU9zazMMA0CU9k5abBx7QVTIzJpeSRw7EOK6ENBs16FQu8CAFsltXZwfNJBVcxYbTAu7ywKGEZv2k3SOwumQJBAPigm5CrdgXEUO3Cdn3kQ/r6JkTW2mgkr5eHjMqTYf/Dq2HIS1i3jKKgO5p87AhhSNyQlqzy6NjAWJ+btVP0jt0CQQDN5SMtBcfokPolL+pbJDAt43RYTEMRBETPbhNjWqImiYXTuzKbjDEU6uZwSVqvdiV2I2ZuDC5kNXlB2c7ZLYv/AkEAtC2AT3lSkJ9Snttcj6NK+Kkr7ptdsDdV4RRknbHtRTMCElza7bInAvvn9e8uBmyqJt3JtBBdWicakqB+6hbDKQJBAL4BKc//mLb/K0isCiqnn6qJT7G4Vt/aP6Ho5A1481B68rws8Yvn+Ndj2tpeZ0iqz/FQw9LkfN9PFaJ2XNPAm4MCQBVjjlAzQIRDCfjgOuZe7wa3oNpW7n7+IMClvnHl1M+0AM9JF1b4qgiwyDihg+gkLj9xm3LTA/ZuyKCvJ3OWBTY=";
//	公钥
//	MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMrOB1tiWdZKmrwQr3XVOSRogqP8RDGF2Mt5qpLieOtFbqs+OJVL3zPjWlKdfch1RD20+f7+umtRXg/zGljmD1TV7E/j/UGf17i803AefMvJSwhpT1IabdWCFN5i/o5TdYxbz4h0jptzA51VurzU7+VN6GRkOfZ2WdITZrHxdwSQIDAQAB
//	私钥
//	MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMys4HW2JZ1kqavBCvddU5JGiCo/xEMYXYy3mqkuJ460Vuqz44lUvfM+NaUp19yHVEPbT5/v66a1FeD/MaWOYPVNXsT+P9QZ/XuLzTcB58y8lLCGlPUhpt1YIU3mL+jlN1jFvPiHSOm3MDnVW6vNTv5U3oZGQ59nZZ0hNmsfF3BJAgMBAAECgYEAvzShXFYVPbxUZYep3vzonbYWbqGzj5qrilHbwkDD46TFrezvm9P2v85V/z3f0rz6E4pGEHvqAYsaLV852hfwqo0fub///kshiQfAlAHH/IWHQ3Irfag62J+BClQ66j+551yuk+db4nZ0gi4pSkerron8iCUXUasyKpoS8WAT+KECQQDphN0A2xQY6WJa2NKKfExVmixovXd8DXMQWPiobLnvu3nDXLy9+AmtGduUgfKeN7sIQc6NWTLbrl67C2Zyf9ujAkEA4GEnLgYf3I0BkcnTv3Tf+2Jxrdo8CLZYvgZPVPSYyWDQKbkLFyg5gshbogtc2vhlKU8AkllaPNKMhdKo8GiDIwJBAKikE/rHPDdVbHtRNJNgerwjUEj/82c6S0uWBVWU157i1Gea9I94rnw9PU99nRpYAxFOfP2lWk4+GhmYfQ7ZeWMCQQC5paiVG9wgz0oNJcBdGIZ5p+oGvhoN21FxaCcbU4VWmROiccgLUOzEmKG4YU5inkKU+xit/d/7kvlRgArnXpGhAkEAoieeUXGuc7yXABQqi9CqbgA6M0YS2AJyLZT9W2KRglA+cejZ0i4Pz+qbQKt/ykH6pjq9EErnrsSBO7g9oX9P+Q==
	
	
	/** 商户的私钥(安全校验码Key) MD5加密的32位字符串 */
//	public static final String SECURITY_KEY= "9h7xxnixphxo5eienbqv5b2ooc4vh6l6";

	/** 字符编码格式 目前支持 GBK 或 UTF-8 */
	public static final String INPUT_CHARSET = "utf-8";
	/** 签名方式: MD5加密  */
	public static final String SIGN_TYPE = "RSA";
	
	
	/** 支付宝提供给商户的服务接入网关URL(新) */
    public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?_input_charset=" + INPUT_CHARSET;
    /** 支付宝用于防钓鱼，调用接口query_timestamp来获取时间戳的地址 */
    public static final String ALIPAY_QUERY_TIMESTAMP_URL = "https://mapi.alipay.com/gateway.do?service=query_timestamp&partner=" + PARTNER_ID + "&_input_charset" + INPUT_CHARSET;
    /** 支付宝消息验证地址 */
    public static final String ALIPAY_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&partner=" + PARTNER_ID;
	
	/** 调试用，创建TXT日志文件夹路径 */
	public static final String LOG_PATH= "D:\\";
	
	//public static final String NOTIFY_URL = SpringContextListener.getContextProValue("serviceUrl", "")+"/alipayNotify";
	/**  hostOfAliPayForward: 支付宝-支付,中间转发的主机域名(用于模拟代理支付) */
	public static final String NOTIFY_URL = SpringContextListener.getContextProValue("hostOfAliPayForward", "")+"/alipayNotify";
	
	public static final String SERVICE = "create_direct_pay_by_user" ; 
	
	


	
}
