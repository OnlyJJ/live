package com.lm.live.common.utils;

import java.util.UUID;

/**
 * 
 *通用唯一识别码工具
 */
public class UUIDUtil {
	
	/**
	 * 生成32位的uuid字符串,如:c879fa31c1ef4061ac27b0554eb55635
	 * @return
	 */
	public static String getUUID() {  
        UUID uuid = UUID.randomUUID();  
        String str = uuid.toString();  
        // 去掉"-"符号  
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);  
        return temp;  
    }  

}
