package com.lm.live.common.utils;

import com.payeco.client.ConstantsClient;
import com.payeco.tools.Base64;
import com.payeco.tools.Tools;
import com.payeco.tools.Xml;
import com.payeco.tools.http.HttpClient;
import com.payeco.tools.rsa.Signatory;
/**
 * 商户对接通用接口封装
 * 易联服务器交易接口调用API封装，分别对以下接口调用进行了封装；
 * 接口封装了参数的转码（中文base64转码）、签名和验证签名、通讯和通讯报文处理
 * 1、商户订单查询接口 ： 		适用于所有对接方式
 * 2、商户订单冲正接口 ：		 除【互联网金融】行业外的所有接入方式
 * 3、商户订单退款申请接口 ：	除【互联网金融】行业外的所有接入方式
 * 4、商户订单退款结果查询接口 : 		除【互联网金融】行业外的所有接入方式
 * 5、验证订单结果通知签名 ：	适用于所有对接方式
 * 6、互联网金融行业银行卡解除绑定接口 ： 仅适合于【互联网金融】行业的商户
 */
public class TransactionClient {
	/**
	 * 商户订单查询接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderQuery(String merchantId, String merchOrderId, String tradeTime, 
			String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "QueryOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
		LogUtil.log.info("PrivateKey=" + priKey);
		LogUtil.log.info("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}
		LogUtil.log.info("sign=" + sign);

		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    LogUtil.log.info("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		LogUtil.log.info("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retExtData = Tools.getXMLValue(retStr, "ExtData");
		if (retExtData != null){
			retExtData = retExtData.replaceAll(" ", "+");
			retExtData = new String (Base64.decode(retExtData), ConstantsClient.PAYECO_DATA_ENCODE);
		}
		String retOrderId = Tools.getXMLValue(retStr, "OrderId");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retPayTime = Tools.getXMLValue(retStr, "PayTime");
		String retSettleDate = Tools.getXMLValue(retStr, "SettleDate");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setExtData(retExtData);
		retXml.setOrderId(retOrderId);
		retXml.setStatus(retStatus);
		retXml.setPayTime(retPayTime);
		retXml.setSettleDate(retSettleDate);
		retXml.setSign(retSign);
		  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
		  + "&Amount=" + retAmount + "&ExtData=" + retExtData + "&OrderId=" + retOrderId
		  + "&Status=" + retStatus + "&PayTime=" + retPayTime + "&SettleDate=" + retSettleDate;

		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + backSign);
		LogUtil.log.info("Sign=" + retSign);
		LogUtil.log.info("验证结果=" + b);
		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}
	
	
	/**
	 * 商户订单冲正接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param amount        :   订单金额
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderReverse(String merchantId, String merchOrderId, String amount, String tradeTime, 
			String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "QuashOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&Amount=" + amount + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
		LogUtil.log.info("PrivateKey=" + priKey);
		LogUtil.log.info("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}
		LogUtil.log.info("sign=" + sign);

		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    LogUtil.log.info("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		LogUtil.log.info("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setStatus(retStatus);
		retXml.setSign(retSign);
  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				+ "&Amount=" + retAmount + "&Status=" + retStatus + "&TradeTime=" + retTradeTime;
		
		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + backSign);
		LogUtil.log.info("Sign=" + retSign);
		LogUtil.log.info("验证结果=" + b);
		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}

	/**
	 * 商户订单退款申请接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param merchRefundId	:   商户退款申请号
	 * @param amount        :   商户退款金额
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderRefundReq(String merchantId, String merchOrderId, String merchRefundId, 
			String amount, String tradeTime, String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "RefundOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	    		+ "&MerchRefundId=" + merchRefundId + "&Amount=" + amount + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
		LogUtil.log.info("PrivateKey=" + priKey);
		LogUtil.log.info("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}
		LogUtil.log.info("sign=" + sign);

		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    LogUtil.log.info("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		LogUtil.log.info("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retMerchRefundId = Tools.getXMLValue(retStr, "MerchRefundId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retTsNo = Tools.getXMLValue(retStr, "TsNo");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setMerchRefundId(retMerchRefundId);
		retXml.setAmount(retAmount);
		retXml.setTsNo(retTsNo);
		retXml.setTradeTime(retTradeTime);
		retXml.setSign(retSign);
  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId 
				+ "&MerchOrderId=" + retMerchOrderId + "&MerchRefundId=" + retMerchRefundId  
				+ "&Amount=" + retAmount + "&TsNo=" + retTsNo + "&TradeTime=" + retTradeTime;
		
		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + backSign);
		LogUtil.log.info("Sign=" + retSign);
		LogUtil.log.info("验证结果=" + b);
		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}	
	
	
	/**
	 * 商户订单退款结果查询接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param merchRefundId	:   商户退款申请号
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderRefundQuery(String merchantId, String merchOrderId, String merchRefundId, 
			String tradeTime, String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "QueryRefund";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	    		+ "&MerchRefundId=" + merchRefundId + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
		LogUtil.log.info("PrivateKey=" + priKey);
		LogUtil.log.info("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}
		LogUtil.log.info("sign=" + sign);

		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    LogUtil.log.info("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		LogUtil.log.info("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retMerchRefundId = Tools.getXMLValue(retStr, "MerchRefundId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retTsNo = Tools.getXMLValue(retStr, "TsNo");	
		String retStatus = Tools.getXMLValue(retStr, "Status");	
		String retRefundTime = Tools.getXMLValue(retStr, "RefundTime");	
		String retSettleDate = Tools.getXMLValue(retStr, "SettleDate");	
		
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setMerchRefundId(retMerchRefundId);
		retXml.setAmount(retAmount);
		retXml.setTsNo(retTsNo);
		retXml.setStatus(retStatus);
		retXml.setRefundTime(retRefundTime);
		retXml.setSettleDate(retSettleDate);
		retXml.setSign(retSign);
  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				 + "&MerchRefundId=" + retMerchRefundId + "&Amount=" + retAmount+ "&TsNo=" + retTsNo
				 + "&Status=" + retStatus + "&RefundTime=" + retRefundTime + "&SettleDate=" + retSettleDate;
		
		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + backSign);
		LogUtil.log.info("Sign=" + retSign);
		LogUtil.log.info("验证结果=" + b);
		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}	
		
	
	/**
	 * 验证订单结果通知签名
	 * @param version       ： 通讯版本号
	 * @param merchantId    ： 商户代码
	 * @param merchOrderId  ：商户订单号
	 * @param amount		： 商户订单金额
	 * @param extData		：商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
	 * @param orderId		：易联订单号
	 * @param status		：订单状态
	 * @param payTime		：订单支付时间
	 * @param settleDate	：订单结算日期
	 * @param sign			：签名数据
	 * @param pubKey		：易联签名验证公钥
	 * @return				： true：验证通过； false：验证不通过
	 * @throws Exception
	 */
	public static boolean bCheckNotifySign(String version, String merchantId, 
			String merchOrderId, String amount, String extData, String orderId, 
			String status, String payTime, String settleDate, String sign,
			String pubKey) 
			throws Exception{
		// 对extData进行转码处理: base64转码
		if (extData != null) {
			extData = extData.replaceAll(" ", "+");
			extData = new String(Base64.decode(extData), ConstantsClient.PAYECO_DATA_ENCODE);
			LogUtil.log.info("extData=" + extData); // 日志输出，检查转码是否正确
		}
		 
		// 进行数据签名
		String data = "Version=" + version + "&MerchantId=" + merchantId
				+ "&MerchOrderId=" + merchOrderId + "&Amount=" + amount
				+ "&ExtData=" + extData + "&OrderId=" + orderId + "&Status="
				+ status + "&PayTime=" + payTime + "&SettleDate=" + settleDate;

		// 验证签名
		sign = sign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, data, sign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + data);
		LogUtil.log.info("Sign=" + sign);
		LogUtil.log.info("验证结果=" + b);
		return b;
	}
	
	/**
	 * 互联网金融行业银行卡解除绑定接口
	 * @param merchantId:		商户代码
	 * @param bankAccNo  	:	解除绑定的银行卡账号
	 * @param tradeTime		:	商户提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				:   处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String UnboundBankCard(String merchantId, String bankAccNo, String tradeTime, 
			String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "UnboundBankCard";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&BankAccNo=" + bankAccNo 
	             + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
		LogUtil.log.info("PrivateKey=" + priKey);
		LogUtil.log.info("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}
		LogUtil.log.info("sign=" + sign);

		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //解除绑定URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    LogUtil.log.info("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		LogUtil.log.info("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retBankAccNo = Tools.getXMLValue(retStr, "BankAccNo");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setBankAccNo(retBankAccNo);
		retXml.setStatus(retStatus);
		retXml.setTradeTime(retTradeTime);
		retXml.setSign(retSign);
		  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&BankAccNo=" + retBankAccNo 
		  + "&TradeTime=" + retTradeTime + "&Status=" + retStatus;

		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		LogUtil.log.info("PublicKey=" + pubKey);
		LogUtil.log.info("data=" + backSign);
		LogUtil.log.info("Sign=" + retSign);
		LogUtil.log.info("验证结果=" + b);
		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}	
	
}
