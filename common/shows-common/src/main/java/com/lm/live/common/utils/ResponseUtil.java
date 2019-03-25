package com.lm.live.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public class ResponseUtil {
	
	/**
	 * 往客户端留输出数据
	 * @param responseStr 响应给客户端的字符
	 * @param request
	 * @param response
	 * @param isCompress 是否压缩
	 */
	 public static void out(String responseStr, HttpServletRequest request, HttpServletResponse response,boolean isCompress) {
		OutputStream out = null;
		String responseCharSet = "utf-8";
		try {
			if(StringUtils.isEmpty(responseStr)){
				LogUtil.log.warn("#####responseStr is empty");
			}else{
				// 字符串中的特殊字符trim掉
				responseStr = StrUtil.trimControlCharacter(responseStr);
				out = response.getOutputStream();
				if( isCompress ){
					response.setHeader("Content-Encoding", "gzip");
					GZIPOutputStream gzip = new GZIPOutputStream(response.getOutputStream());
					gzip.write(responseStr.getBytes(responseCharSet));
					gzip.finish();
					gzip.flush();
					gzip.close();
				}else{
					out.write(responseStr.getBytes(responseCharSet));
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}finally{
			if( null != out){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					LogUtil.log.error(e.getMessage(), e);
				}finally{
					if( null != out){
						out = null;
					}
				}
			}
		}
	}
	 
	 
	 /**
	  *  往客户端留输出数据
	  * @param data
	  * @param req
	  * @param response
	  */
	 public static void  out(String data, HttpServletRequest request, HttpServletResponse response) {
		OutputStream out = null;
		String responseCharSet = "utf-8";
		try {
			if(StringUtils.isEmpty(data)){
				LogUtil.log.warn("#######responseStr is empty");
			}else{
				// 字符串中的特殊字符trim掉
				data = StrUtil.trimControlCharacter(data);
				out = response.getOutputStream();
				if (null != out) {
					String responseStr = data;
					out.write(responseStr.getBytes(responseCharSet));
					out.flush();
					out.close();
				}
			}
			
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					LogUtil.log.error(e.getMessage(), e);
				} finally {
					if (null != out) {
						out = null;
					}
				}
			}
		}
	}

}
