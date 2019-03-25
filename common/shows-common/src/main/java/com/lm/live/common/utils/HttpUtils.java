package com.lm.live.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;
import com.lm.live.common.vo.BaseDataRequest;

public class HttpUtils {
	
	/**  设置向微信服务器发送http链接超时时间,单位:毫秒 */
	private static int connectTimeOutMillionSecondsOfWeichatLogin = 15000;
	
	/**  设置向微信服务器发送http读超时时间,单位:毫秒 */
	private static final int readTimeOutMillionSecondsOfWeichatLogin = 15000;
	
	/**  设置http链接超时时间,单位:毫秒 */
	private static int connectTimeOutMillionSeconds = 10000;
	
	/**  设置http读超时时间,单位:毫秒 */
	private static final int readTimeOutMillionSeconds = 10000;
	
	/**  设置http下载文件超时时间,单位:毫秒 */
	private static final int downloadFileReadTimeOutMillionSeconds = 20000;
	
	/** 设置http连接im服务器的超时时间,单位:毫秒 */
	private static int imConnectTimeoutMillionSeconds = 10000;
	
	/** 设置http等待IM读超时时间,单位:毫秒 */
	private static final int imReadTimeOutMillionSeconds = 5000;
	
	/** 赠送免费礼物茄子,设置http连接im服务器的超时时间,单位:毫秒 */
	private static int qieziImConnectTimeoutMillionSeconds = 2000;
	
	/** 赠送免费礼物茄子,设置http等待IM读超时时间,单位:毫秒 */
	private static final int qieziImReadTimeOutMillionSeconds = 2000;
	
	public static  final String webClient = "web"; 
	public static  final String androidClient = "android";
	public static  final String iosClient = "ios";
	
	public static  int webClienttypeInt = 1; 
	public static  int androidClienttypeInt = 2;
	public static  int iosClienttypeInt = 3;
	
	/** www主机地址 */
	private static final String wwwProjServerHostIp = SpringContextListener.getContextProValue("wwwProjServerHostIp", "");


	
	public static String get(String url) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		BufferedReader bf = null;
		InputStream in = null;
		String result = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.connect();
			in = conn.getInputStream();
			bf = new BufferedReader(new InputStreamReader(in));
			result = bf.readLine();
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
			} catch (IOException e) {

			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {

				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
				long endTimeMillis = System.currentTimeMillis();
				logResponseMsg(url, result,beginTimeMillis,endTimeMillis);
			}
		}
		LogUtil.log.info(String.format("http get: %s, response=%s", url, result));
		return result;
	}
	
	
	public static String get(String url,String chartSet) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		BufferedReader bf = null;
		InputStream in = null;
		String result = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.connect();
			in = conn.getInputStream();
			bf = new BufferedReader(new InputStreamReader(in,chartSet));
			result = bf.readLine();
		}catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		}finally {
			try {
				if (bf != null) {
					bf.close();
				}
			} catch (IOException e) {

			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {

				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result,beginTimeMillis,endTimeMillis);
		}
		return result;
	}


	public static String get(String url, String charset, String p) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		URL u = null;
		HttpURLConnection conn = null;
		String result = null;
		try {
			u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			conn.setRequestMethod("GET");
			conn.connect();
			java.io.OutputStream os = conn.getOutputStream();
			os.write(p.getBytes("UTF-8"));
			os.flush();
			os.close();
			result = getString(conn.getInputStream(), charset);
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		}finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			if (u != null) {
				u = null;
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result,beginTimeMillis,endTimeMillis);
		}
		return result;
	}
	
	/**
	 * 微信登录,向微信服务器发送get请求
	 * @param url
	 * @param charset
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public static String doHttpGetForWechatLogin(String url, String charset, String p) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		URL u = null;
		HttpURLConnection conn = null;
		String result = null;
		try {
			u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSecondsOfWeichatLogin);
			conn.setReadTimeout(readTimeOutMillionSecondsOfWeichatLogin);
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			conn.setRequestMethod("GET");
			conn.connect();
			java.io.OutputStream os = conn.getOutputStream();
			os.write(p.getBytes("UTF-8"));
			os.flush();
			os.close();
			result = getString(conn.getInputStream(), charset);
		}catch (IOException e) {
			LogUtil.log.error("###发送http请求发生异常,IOException,url:"+url);
			throw e;
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		}finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			if (u != null) {
				u = null;
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result,beginTimeMillis,endTimeMillis);
		}
		return result;
	}

	public static String getString(InputStream is, String charset) throws Exception {
		String content = null;
		try {
			if (charset == null || "".equals(charset)) {
				charset = "utf-8";
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) > 0) {
				baos.write(b, 0, len);
			}
			b = null;
			baos.flush();
			content = baos.toString(charset);
			baos.close();
			baos = null;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			is.close();
		}
		return content;
	}

	public static String post(String url) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
	
		return result.toString();
	}
	
	/**
	 * 发送post请求
	 * @param url
	 * @param confConnectTimeOutSeconds http链接超时时长(单位:秒)
	 * @param confRreadTimeOutSeconds http读超时时长(单位:秒)
	 * @return
	 * @throws Exception
	 */
	public static String post(String url,int confConnectTimeOutSeconds,int confRreadTimeOutSeconds) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(confConnectTimeOutSeconds*1000); //转成毫秒
			conn.setReadTimeout(confRreadTimeOutSeconds*1000);  //转成毫秒
			conn.setRequestMethod("POST");
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
	
		return result.toString();
	}
	
	/**
	 * 向im服务器发送http请求,链接配置参数不要轻易修改 
	 * @param url
	 * @param parms
	 * @return
	 */
	public static String post2IM(String url, Map<String, Object> parms) throws Exception{
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		String paramStr= maptostr(parms);
		//LogUtil.log.info("####sendMsg2IM-paramStr:"+paramStr);
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			//conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(imConnectTimeoutMillionSeconds);
			conn.setReadTimeout(imReadTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(paramStr.getBytes());
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			List<String> lineList = IOUtils.readLines(in, "UTF-8");  
			if(lineList != null && lineList.size() > 0){
				for(String ioLine:lineList){ 
					result.append(ioLine);
				}
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		return result.toString();
	}
	

	public static String post(String url, Map<String, Object> parms) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			//conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(maptostr(parms).getBytes());
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param url
	 * @param parms
	 * @param chartSet 为空则采用默认编码
	 * @return
	 * @throws Exception 
	 */
	public static String post(String url, Map<String, Object> parms,String chartSet) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			//conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			if(!StringUtils.isEmpty(chartSet)){
				conn.setRequestProperty("Charset", chartSet);
			}
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			String postStr = maptostr(parms);
			if(StringUtils.isEmpty(chartSet)){
				conn.getOutputStream().write(postStr.getBytes());
			}else{
				conn.getOutputStream().write(postStr.getBytes(chartSet));
			}
			
			conn.connect();
			InputStream in = conn.getInputStream();
			if(StringUtils.isEmpty(chartSet)){
				bufr = new BufferedReader(new InputStreamReader(in));
			}else{
				bufr = new BufferedReader(new InputStreamReader(in, chartSet));
			}
			
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		
		return result.toString();
	}
	
	public static String post(String url, String par) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			//conn.getOutputStream().write( GZipUtil.compressToByte(par,"utf-8"));
			conn.getOutputStream().write(par.getBytes("utf-8"));
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		}catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		
		return result.toString();
	}
	
	/**
	 * 苹果内购,向苹果服务器发送http请求,验证支付凭证
	 * @param url
	 * @param par
	 * @return
	 * @throws Exception
	 */
	public static String postToAppleServer(String url, String par) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(par.getBytes());
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		return result.toString();
	}
	
	public static String post(String url, String par,String parCharset) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeOutMillionSeconds);
			conn.setReadTimeout(readTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(par.getBytes(parCharset));
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while ((line = bufr.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		}finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		
		return result.toString();
	}

	/**
	 * 打印http响应日志
	 * @param url
	 * @param result
	 *//*
	private static void logResponseMsg(String url, StringBuffer result) {
		LogUtil.log.info(String.format("http-post: %s, response=%s", url, result));
		if(StringUtils.isEmpty(result)){
			LogUtil.log.warn(String.format("http-post-return-empty,url: %s", url));
		}
	}*/
	
	/**
	 * 打印http请求日志
	 * @param url
	 * @param result
	 * @param beginTimeMillis
	 * @param endTimeMillis
	 */
	private static void logResponseMsg(String url, String result,
			long beginTimeMillis, long endTimeMillis) {
		double second = ((double)(endTimeMillis-beginTimeMillis))/1000;
		LogUtil.log.info(String.format("###http请求日志, reqUrl:%s, response:%s, beginTimeMillis:%s, endTimeMillis:%s, useTimeSecond:%s,", url, result,beginTimeMillis,endTimeMillis,second));
	}

	private static String maptostr(Map<String, Object> parms) {
		if (parms == null || parms.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : parms.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 判断是不是app用户
	 * @param request
	 * @return
	 */
	public static boolean testIfAppClientType(HttpServletRequest request){
		String userAgent = request.getHeader("user-agent");
//		LogUtil.log.info("###userAgent:"+userAgent);
		if(StringUtils.isEmpty(userAgent)){//app调用时拿到的userAgent is null
			return true;
		}else{
			userAgent = userAgent.toLowerCase();
			if ( userAgent != null &&((userAgent.toLowerCase().indexOf("android") != -1)||(userAgent.toLowerCase().indexOf("ios") != -1))){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	/**
	 * 判断请求的客户端类型  <br />
	 * 返回数值依据t_pay_charge_order.createType: 1.www 2.安卓 3.ios<br />
	 * 与DeviceProperties.appType声明的不一样,app端历史版本已有坑,不敢改,0:android , 1:web,  3:ios <br />
	 * @param request
	 * @return
	 */
	public static int getClientTypeInt(HttpServletRequest request){
		// 默认web
		int clientTypeInt = webClienttypeInt;
		String userAgent = null;
		if(request != null){
			userAgent = request.getHeader("user-agent");
			if(!StringUtils.isEmpty(userAgent)){
				userAgent = userAgent.toLowerCase();
				if ( userAgent != null &&(userAgent.toLowerCase().indexOf("android") != -1)){
					clientTypeInt =  androidClienttypeInt;
				}else if ( userAgent != null &&(userAgent.toLowerCase().indexOf("ios") != -1)){
					clientTypeInt = iosClienttypeInt;
				}else{
					clientTypeInt = webClienttypeInt;
				}
			}	
		}
		
		LogUtil.log.info(String.format("###检测客户端平台(int),userAgent:%s,返回结果:%s",userAgent,clientTypeInt));
		return clientTypeInt;
	}
	
	/**
	 * 判断请求的客户端类型 web、android、ios
	 * @param request
	 * @return
	 */
	public static String getClientTypeStr(HttpServletRequest request){
		String clientTypeStr = webClient;
		String userAgent = null;
		if(request ==null){
			clientTypeStr = "";
		}else{
			userAgent = request.getHeader("user-agent");
			if(!StringUtils.isEmpty(userAgent)){
				userAgent = userAgent.toLowerCase();
				if ( userAgent != null &&(userAgent.toLowerCase().indexOf(androidClient) != -1)){
					clientTypeStr =  androidClient;
				}else if ( userAgent != null &&(userAgent.toLowerCase().indexOf(iosClient) != -1)){
					clientTypeStr = iosClient;
				}else{
					clientTypeStr = webClient;
				}
			}
		}
		//LogUtil.log.info(String.format("###检测客户端平台(string),userAgent:%s,返回结果:%s",userAgent,clientTypeStr));
		return clientTypeStr;
	}
	
	/**
	 * 将HTTP资源另存为文件
	 * @param sourceFileUrl String
	 * @param fileName String
	 * @throws Exception
	 */
	public static void downLoadFile(String sourceFileUrl, String fileName) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpConnection = null;
		try {
			int BUFFER_SIZE = 8096;//缓冲区大小
			fos = null;
			bis = null;
			httpConnection = null;
			URL url = null;
			byte[] buf = new byte[BUFFER_SIZE];
			int size = 0;

			//建立链接
			url = new URL(sourceFileUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setConnectTimeout(connectTimeOutMillionSeconds);
			httpConnection.setReadTimeout(downloadFileReadTimeOutMillionSeconds);
			//连接指定的资源
			httpConnection.connect();
			//获取网络输入流
			bis = new BufferedInputStream(httpConnection.getInputStream());
			//建立文件
			File file = new File(fileName);
			if(!file.exists()) {
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fos = new FileOutputStream(fileName);

			//保存文件
			while ((size = bis.read(buf)) != -1){
				fos.write(buf, 0, size);
			}
		} catch (Exception e) {
			LogUtil.log.error("###下载文件,发送http请求发生异常"+sourceFileUrl);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage() ,e);
				}
			}
			if(bis!=null){
				try {
					bis.close();
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage() ,e);
				}
			}
			if(httpConnection!=null){
				try {
					httpConnection.disconnect();
				} catch (Exception e) {
					LogUtil.log.error(e.getMessage() ,e);
				}
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(sourceFileUrl, "最终的文件名:"+fileName,beginTimeMillis,endTimeMillis);
		}
		
	}
	
	
	//获取请求的ip
	public final static String getIpAddress(HttpServletRequest request)  {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
		if(request==null){
			return null;
		}
        String ip = request.getHeader("X-Forwarded-For");  
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }  
	/**
	 * my-todo
	 * 获取用户ip
	 * @param data
	 * @return
	 */
	public static String getUserReallyIp(BaseDataRequest data) {
		if(data==null){
			return null;
		}
		String ip = "";
		String webIp = "";
		// my-todo
//		if(data.getUserBaseInfo() != null && data.getUserBaseInfo().getIp() != null) {
//			webIp = data.getUserBaseInfo().getIp();
//		}
//		String userIp = HttpUtils.getIpAddress(data.getRequest());
//		if(wwwProjServerHostIp.equals(userIp)) {
//			ip = webIp;
//		} else {
//			ip = userIp;
//		}
		return ip;
	}

	/**
	 *  赠送免费礼物茄子,所用的方法 <br />
	 *  http连接、读超时设置的更短
	 * @param url
	 * @param parms
	 * @return
	 * @throws Exception
	 */
	public static String post2IMQiezi(String url, Map parms) throws Exception{
		long beginTimeMillis = System.currentTimeMillis();
		HttpURLConnection conn = null;
		StringBuffer result = new StringBuffer();
		BufferedReader bufr = null;
		String paramStr= maptostr(parms);
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(qieziImConnectTimeoutMillionSeconds);
			conn.setReadTimeout(qieziImReadTimeOutMillionSeconds);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(paramStr.getBytes());
			conn.connect();
			InputStream in = conn.getInputStream();
			bufr = new BufferedReader(new InputStreamReader(in, "utf-8"));
			List<String> lineList = IOUtils.readLines(in, "UTF-8");  
			if(lineList != null && lineList.size() > 0){
				for(String ioLine:lineList){ 
					result.append(ioLine);
				}
			}
		} catch (Exception e) {
			LogUtil.log.error("###发送http请求发生异常"+url);
			LogUtil.log.error(e.getMessage(), e);
			Exception systemDefinitionException = new SystemDefinitionException(ErrorCode.ERROR_404) ;
			throw systemDefinitionException;
		} finally {
			try {
				if (bufr != null)
					bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
			long endTimeMillis = System.currentTimeMillis();
			logResponseMsg(url, result.toString(),beginTimeMillis,endTimeMillis);
		}
		return result.toString();
	}

}