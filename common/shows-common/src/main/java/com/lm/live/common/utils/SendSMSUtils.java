package com.lm.live.common.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SendSMSUtils {
	
	private static final String ISABLE_SENDMSM= SpringContextListener.getContextProValue("isAbleSendMsm", "0");//1:能发,0:不能发
	
	/** 服务器地址 */
	private static final String SERVER_URL = SpringContextListener.getContextProValue("serviceUrl", "http://service.9mitao.com/");
	
	private static final String IS_SENDVERIFY = "1"; // 是否开启发送短信功能，1-是，0-否
	/** 梦幻科技短信平台，参数设置，begin */
	private static final String USERID = SpringContextListener.getContextProValue("mhkj.userid", "J92578"); //用户账号 测试 J25394
	private static final String PWD = SpringContextListener.getContextProValue("mhkj.pwd", "220156"); //用户密码 测试 665215
	private static final String URI = SpringContextListener.getContextProValue("mjkj.uri", "http://61.145.229.26:8086/MWGate/wmgw.asmx/MongateSendSubmit"); //主IP信息  必填
	private static final String SUBPORT="*"; //扩展子号 （不带请填星号*，长度不大于6位）;
	private static final String USERMSGID="0"; //用户自定义流水号，不带请输入0（流水号范围-（2^63）……2^63-1）
	/** 梦幻科技短信平台，参数设置，end */

	/**
	 * 下发接口，晚上会延迟10分钟
	 * String param = "cmd=send&uid=" + loginName + "&psw=" + loginPwd + "&mobiles=" + mob + "&msgid=0&msg=" + content;
	 * 定时下发接口
	 * String param = "cmd=tsend&uid=" + loginName + "&psw=" + loginPwd + "&mobiles=" + mob + "&senddate=2011-12-18&sendtime=0:0&msg=" + content;
	 * @param number
	 * @param content
	 * @return
	 */
	public static boolean sendSMS(String number,String content){
		LogUtil.log.info(String.format("begin-发送短信,number:%s,content:%s", number,content));
		String userId =  "0233";
		String pwd=  "08ddb3cc78c65ccf9b45a0e50bdf41ac";
		
		userId="5722";
		pwd="75e6ee6eb84b1c167fe51915fc501fa0";

		String isSendVerify = SpringContextListener.getContextProValue("isAbleSendMsm", "0");//1:能发,0:不能发

		// 是否开启发送短信
		if("0".equals(isSendVerify)){
			LogUtil.log.info("未开通短信功能");
			return false;
		}
		if(null == number||"".equals(number)||null == content||"".equals(content)){
			LogUtil.log.info("短信内容或手机号码不能为空！");
			return false;
		}
		//content = content+"\n退订回T【蜜桃直播】";
		content = content+"\n如非本人操作,回T退订【美女直播】";
		long time1 = System.currentTimeMillis(); 
		String re = "";
		int byteMax = 50;
        try {
        	int msgId = new Long(System.currentTimeMillis()).intValue();
        	String u = "http://kltx.sms10000.com.cn/sdk/SMS?cmd=send&uid="+userId+"&psw="+pwd+"&mobiles="+number+"&msgid="+msgId+"&msg="+URLEncoder.encode(content,"GBK");
        	re = HttpUtils.post(u);
            if("100".equals(re)){
            	LogUtil.log.info("发送给 "+number+" 短信成功！result="+re + " " +(System.currentTimeMillis() - time1));
            	return true;
            }else{
            	LogUtil.log.error("发送给 "+number+" 发短信失败！result="+re + " " +(System.currentTimeMillis() - time1));
            	return false;
            }
        } catch (Exception e) {
        	LogUtil.log.error(e.getMessage(),e);
        }
		LogUtil.log.info(String.format("end-发送短信,number:%s,content:%s", number,content));
		return false;
	}
	
	public static boolean sendCodeSMS(String phoneNumber, String content) {
//		String isSendVerify = ISABLE_SENDMSM;
		LogUtil.log.error("### sendCodeSMS-山东短信平台，发送验证码，begin....phone=" + phoneNumber);
		String isSendVerify = "1";
		// 是否开启发送短信
		if("0".equals(isSendVerify)){
			LogUtil.log.info("未开通短信功能");
			return false;
		}
		if(null == phoneNumber||"".equals(phoneNumber)||null == content||"".equals(content)){
			LogUtil.log.info("短信内容或手机号码不能为空！");
			return false;
		}
		
		boolean status = false;
		String enterpriseKey = "LB21b9872607ff4bf7b8b35545b6310890"; // key
		String enterpriseSecret = "4bb0b9e3dbd040d7865ade1be76b3781"; // 密钥
		String msisdn = phoneNumber; // 手机
		Date now = new Date();
		String tradingTime = DateUntil.format2Str(now, "yyyyMMddHHmmss"); // 时间
		String transactionId = "jiujun" + tradingTime; // 订单号
		System.err.println("transactionId=" + transactionId);
		String notifyUrl = SERVER_URL + "sendMessageNotify/notifyResult"; // 回调地址
		String platformType = "agent_shandong_yidong"; // 门户类型
		String productId = "2003"; // 产品id
		String template = "1"; // 模板
//		String[] argv = {"2312"}; // 填充数
		List<String> list = new ArrayList<String>();
		list.add("2312");
		String argv = JSONArray.toJSONString(list);
		Map parms = new HashMap();
		parms.put("enterpriseKey", enterpriseKey);
		parms.put("enterpriseSecret", enterpriseSecret);
		parms.put("transactionId", transactionId);
		parms.put("msisdn", msisdn);
		parms.put("tradingTime", tradingTime);
		parms.put("notifyUrl", notifyUrl);
		parms.put("platformType", platformType);
		parms.put("productId", productId);
		parms.put("template", template);
		parms.put("argv", argv);
		
		content = content+"\n如非本人操作,回T退订【美女直播】";
		String url = "http://116.62.47.66/phoneMessage/sendMessage.do";
		try {
//			LogUtil.log.error("###sendCodeSMS-enterpriseKey="+ enterpriseKey 
//					+ ",enterpriseSecret=" + enterpriseSecret
//					+ ",transactionId=" + transactionId
//					+ ",msisdn=" + msisdn
//					+ ",tradingTime=" + tradingTime
//					+ ",notifyUrl=" + notifyUrl
//					+ ",platformType=" + platformType
//					+ ",productId=" + productId
//					+ ",template=" + template
//					+ ",argv=" + argv);
			String response = HttpUtils.post(url, parms);
			if(!StringUtils.isEmpty(response)) {
				JSONObject json = JsonUtil.strToJsonObject(response);
				if(json.containsKey("code")) {
					String flag = json.get("code").toString();
					if("0".equals(flag)) {
						status = true;
					}
				}
			}
			LogUtil.log.error("###sendCodeSMS-response=" + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.log.error("### sendCodeSMS-山东短信平台，发送验证码，phone=" +phoneNumber + ",end!");
		return status;
	}
	
	public static void main(String[] args) {
		//String phoneNumber = "18565579802";
		//String content = "同事您好，感谢您对此次测试的配合。123456";
		String phoneNumber = "13828761917";
		String content = "同事您好，感谢您对此次测试的配合。发送验证码";
		sendSMSTwo(phoneNumber,content);
//		String url = "http://testservice.9mitao.com/sendMessageNotify/notifyResult";
//		url = "http://testservice.9mitao.com/userCommonWeb/code/createImgCode";
//		JSONObject json = new JSONObject();
//		json.put("msisdn", "18565579802");
//		json.put("status", "f");
		try {
//			HttpUtils.post(url,json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 新接梦幻科技短信
	public static boolean sendSMSTwo(String phoneNumber, String content) {
		boolean ret = false;
		LogUtil.log.error("### sendSMSTwo-梦幻科技短信平台，发送验证码，begin....phone=" + phoneNumber);
		// 是否开启发送短信
		if("0".equals(IS_SENDVERIFY)){
			LogUtil.log.info("未开通短信功能");
			return false;
		}
		if(null == phoneNumber||"".equals(phoneNumber)||null == content||"".equals(content)){
			LogUtil.log.info("短信内容或手机号码不能为空！");
			return false;
		}
//		content = content+"\n如非本人操作,回T退订";
        Map parms = new HashMap();
        parms.put("userId", USERID);
		parms.put("password", PWD);
		System.out.println("USERID="+USERID);
		System.out.println("PWD="+PWD);
		parms.put("pszMobis", phoneNumber); // 多个手机号可使用逗号隔开
		parms.put("pszMsg", content);
		parms.put("iMobiCount", String.valueOf(phoneNumber.split(",").length));
		parms.put("pszSubPort", SUBPORT);
		parms.put("MsgId", USERMSGID);
		
		try {
			String response = HttpUtils.post(URI, parms);
			LogUtil.log.error("###sendCodeSMS-response=" + response);
			if(!StringUtils.isEmpty(response)) {
				Document document = DocumentHelper.parseText(response);  
				Node node = document.selectSingleNode("string");
				String retmsg = node.getStringValue();
				if(!StringUtils.isEmpty(retmsg)) {
					if(retmsg.length() >= 15 && retmsg.length() <= 25) {
						ret = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.log.error("### sendSMSTwo-梦幻科技短信平台，发送验证码，phone=" +phoneNumber + ",end!结果：ret="+ret);
		return ret;
	}
}
