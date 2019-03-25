package com.jiujun.shows.pay.biz;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.base.constant.SysConfTableEnum;
import com.jiujun.shows.base.domain.SysConf;
import com.jiujun.shows.base.service.ISysConfService;
import com.jiujun.shows.common.utils.HttpUtils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.utils.AlipayConfig;
import com.jiujun.shows.pay.utils.AlipayCore;
import com.jiujun.shows.pay.utils.RSA;

/**
 * 支付宝通知服务
 * @author shao.xiang
 * @date 2017年8月30日
 *
 */
@Service("alipayNotifyBiz")
public class AlipayNotifyBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
    
    @Resource
	private ISysConfService sysConfService;
    
    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public boolean verify(Map<String, String> params) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			//验证notify_id合法性校验
			responseTxt = verifyResponse(notify_id);
    		log.info(String.format("###验证notify_id合法性校验,结果notify_id:%s,responseTxt:%s",notify_id,responseTxt));
		}
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    //验证签名
	    boolean isSign = getSignVeryfy(params, sign);
		log.info(String.format("###验证签名,结果sign:%s,isSign:%s",sign,isSign));
        //写日志记录（若要调试，请取消下面两行注释）
        //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
	    //AlipayCore.logResult(sWord);
        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> Params, String sign) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        String sign_type="RSA";
        if(Params.get("sign_type") != null) {sign_type = Params.get("sign_type");}
        //获得签名验证结果
        boolean isSign = false;
        if("RSA".equalsIgnoreCase(sign_type)){
        	isSign = RSA.verify(preSignStr, sign, AlipayConfig.RSA_PUBLIC, AlipayConfig.INPUT_CHARSET);
        }
        else if("RSA2".equalsIgnoreCase(sign_type)){
        	isSign = RSA.verify2(preSignStr, sign, AlipayConfig.RSA2_PUBLIC, AlipayConfig.INPUT_CHARSET);
        }
        return isSign;
    }

    /**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private  String verifyResponse(String notify_id) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String partner = AlipayConfig.PARTNER_ID;
        String veryfy_url = Constants.HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
        return checkUrl(veryfy_url);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private  String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
    		// 链接超时,默认5秒
        	int confConnectTimeOutSeconds = 5;
        	// 读超时,默认5秒
        	int confRreadTimeOutSeconds = 5;
        	try {
				String qryCode = SysConfTableEnum.Code.PAYCHARGE_ALIPAY_HTTPTIMEOUT_CONF.getValue();
				SysConf sysConf = null;
				ServiceResult<SysConf> srt = sysConfService.getByCode(qryCode);
				if(srt.isSucceed()) {
					sysConf = srt.getData();
				}
				if(sysConf != null){
					String  confValStr = sysConf.getConfValue();
					JSONObject jsonConf = JsonUtil.strToJsonObject(confValStr);
					confConnectTimeOutSeconds = jsonConf.getIntValue("aliPayVerifyHttpConnTimeoutSecond");
					confRreadTimeOutSeconds = jsonConf.getIntValue("aliPayVerifyHttpReadTimeoutSecond");
				}else{
					log.info(String.format("###支付宝支付,在表t_sys_conf中没有code=%s且启用中的配置", qryCode));
				}
			} catch (Exception e) {
	    		log.error(String.format("###支付宝支付验证-链接支付宝服务器配置,查询db配置发生异常"));
			}
    		log.info(String.format("###支付宝支付验证-链接支付宝服务器配置,urlvalue:%s,httpConnTimeout(S):%s,httpReadTimeout(S):%s,",urlvalue,confConnectTimeOutSeconds,confRreadTimeOutSeconds));
        	inputLine = HttpUtils.post(urlvalue, confConnectTimeOutSeconds, confRreadTimeOutSeconds);
        } catch (Exception e) {
           log.error(e.getMessage(), e);
            inputLine = "";
        }
        return inputLine;
    }
   
}
