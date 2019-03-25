package com.lm.live.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;


/**
 * 通用的md5加密，没有加混淆的数据
 * @author Administrator
 *
 */
public class Md5CommonUtils {
	
	private static Logger log = Logger.getLogger(Md5CommonUtils.class);
	
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
  
	
	public static String getMD5String(String str) {  
        return getMD5String(str.getBytes()); 
    } 
	
	public static String getMD5String(String str,String charSet) throws UnsupportedEncodingException {  
        return getMD5String(str.getBytes(charSet)); 
    } 
	
	 private static String getMD5String(byte[] bytes) {  
		MessageDigest messagedigest = null;  
		    
        try {  
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException nsaex) {  
            log.error(Md5CommonUtils.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");  
            return "";
        }  

	    messagedigest.update(bytes);  
	    return bufferToHex(messagedigest.digest());  
    }  
	 
	 private static String bufferToHex(byte bytes[]) {  
	        return bufferToHex(bytes, 0, bytes.length);  
	    }  
	  
	    private static String bufferToHex(byte bytes[], int m, int n) {  
	        StringBuffer stringbuffer = new StringBuffer(2 * n);  
	        int k = m + n;  
	        for (int l = m; l < k; l++) {  
	            appendHexPair(bytes[l], stringbuffer);  
	        }  
	        return stringbuffer.toString();  
	    }  
	    
	    
	    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
	        char c0 = hexDigits[(bt & 0xf0) >> 4];  
	        char c1 = hexDigits[bt & 0xf];  
	        stringbuffer.append(c0);  
	        stringbuffer.append(c1);  
	    }  

}
