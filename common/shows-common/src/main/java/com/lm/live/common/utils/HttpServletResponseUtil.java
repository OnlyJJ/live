package com.lm.live.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class HttpServletResponseUtil {
	
	/**
	 * 返回数据流
	 * @param response
	 * @param data
	 * @param isCompress 是否压缩
	 */
	public static void out( HttpServletResponse response,JSONObject data,boolean isCompress) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String responseStr = data.toString();
			if( isCompress){
				response.setHeader("Content-Encoding", "gzip");
				GZIPOutputStream gzip = new GZIPOutputStream(response.getOutputStream());
				gzip.write(responseStr.getBytes("utf-8"));
				gzip.finish();
				gzip.flush();
				gzip.close();
				data = null;
			}else{
				out.write(responseStr.getBytes("utf-8"));
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}finally{
			if( null != out){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if( null != out){
						out = null;
					}
				}
			}
		}
	}
	
	/**
	 * 返回数据流
	 * @param response
	 * @param responseStr
	 * @param isCompress 是否压缩
	 */
	public static void out(HttpServletResponse response,String responseStr, boolean isCompress) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			if(isCompress){
				response.setHeader("Content-Encoding", "gzip");
				GZIPOutputStream gzip = new GZIPOutputStream(response.getOutputStream());
				gzip.write(responseStr.getBytes("utf-8"));
				gzip.finish();
				gzip.flush();
				gzip.close();
			}else{
				out.write(responseStr.getBytes("utf-8"));
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}finally{
			if( null != out){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if( null != out){
						out = null;
					}
				}
			}
		}
	}

}
