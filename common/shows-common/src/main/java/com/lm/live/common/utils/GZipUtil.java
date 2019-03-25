package com.lm.live.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import com.lm.live.common.enums.ErrorCode;
import com.lm.live.common.exception.SystemDefinitionException;

/**
 * GZIP压缩解压类
 * 
 * @author huangzp
 * @date 2015-4-14
 */
public class GZipUtil {

	private static String encode = "utf-8";
	private static final int length = 1024;

	public String getEncode() {
		return encode;
	}

	/**
	 * 设置 编码，默认编码：UTF-8
	 */
	public void setEncode(String encode) {
		GZipUtil.encode = encode;
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encode));
			gzip.close();
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return out.toByteArray();
	}

	/**
	 * 字符串压缩为字节数组
	 */
	public static byte[] compressToByte(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return out.toByteArray();
	}

	/**
	 * 字节数组解压缩后返回字符串
	 */
	public static String uncompressToString(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return out.toString();
	}

	/**
	 * 字节数组解压缩后返回字符串
	 * @throws IOException 
	 */
	public static String uncompressToString(byte[] b, String encoding) throws IOException {
		if (b == null || b.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(b);

		try {
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[length];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		}catch(ZipException zipException){ 
			LogUtil.log.error("###传输数据解析异常,非压缩数据被告知为压缩数据");
			SystemDefinitionException e = new SystemDefinitionException(ErrorCode.ERROR_205);
			LogUtil.log.error(e.getMessage(), e);
		}catch (IOException e) {
			LogUtil.log.error(e.getMessage(), e);
			throw e;
		}
		return out.toString(encoding);
	}
}
