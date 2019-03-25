package com.lm.live.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * md5加密工具
 * 
 * @author huangzp
 * @date 2015-4-7
 */
public class MD5Util {
	public static Logger log = Logger.getLogger(MD5Util.class);
	private static String myKey = "2015k23ntlq3k4jo*&lgq43j(#*nke5ty%(*6uyalkg45q0420";

	/**
	 * 自定义MD5  Base64
	 * 编码：utf-8
	 * @param string
	 * @return
	 */
	private static String clientEncode(String string) {
		if (string == null) {
			return "";
		}
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(string.getBytes("utf-8"));
			byte[] encode = Base64.encodeBase64(digest);
			return new String(encode, "utf-8");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return string;
	}
	
	/**
	 * 把客户端上传的Base64加密过的密文 改成 服务器端加密
	 * 自定义MD5  md5(Base64+myKey)
	 * 编码：utf-8
	 * @param string
	 * @return
	 */
	public static String changeCToSEncode(String string) {
		return md5(string+myKey);
	}
	/**
	 * 自定义MD5  md5(Base64+myKey)
	 * 编码：utf-8
	 * @param string
	 * @return
	 */
	public static String serverEncode(String string) {
		string = clientEncode(string);
		return md5(string+myKey);
	}
	
	
	
	public final static String md5(final String plainText ) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if(i<0) i+= 256;
				if(i<16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			md.reset();
			return buf.toString().toLowerCase();//32位的加密
		} catch (NoSuchAlgorithmException e) {
			log.error("系统加密异常" + e);
		} catch (UnsupportedEncodingException e) {
			log.error("系统加密异常编码" + e);
		}
		return null;
	}
	
	/**
	 * 标准MD5，小写
	 * @param plainText
	 * @param coding	指定编码
	 * @return
	 */
	public final static String md5(final String plainText ,String coding) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(coding));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if(i<0) i+= 256;
				if(i<16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			md.reset();
			return buf.toString().toLowerCase();//32位的加密
		} catch (NoSuchAlgorithmException e) {
			log.error("系统加密异常" + e);
		} catch (UnsupportedEncodingException e) {
			log.error("系统加密异常编码" + e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String md5mac=md5("0200 190011 0.3 20160114113301 113301 REFERENCE 302020000114 20160114113301 123456");
		System.out.println(md5mac);
	}
}
