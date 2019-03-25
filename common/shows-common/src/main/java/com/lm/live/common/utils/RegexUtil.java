package com.lm.live.common.utils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {  
  
    private static Pattern         pattern  = null;  
  
    /** 
     * 根据正则过滤条件判断是否匹配
     *  
     * @param input 
     * @param patternString 
     * @return 
     */  
    public static boolean contains(String input, String patternString) {  
    	pattern =  Pattern.compile(patternString);
    	Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;  
        }else{ 
        	return false;  
        }  
    }  
  
    /** 
     * 根据批量正则过滤条件判断是否匹配
     *  
     * @param input 
     * @param patternStrings 
     * @return 
     */  
    public static boolean contains(String input, List<String> patternStrings) {  
        for (Iterator<String> lt = patternStrings.listIterator(); lt.hasNext();) {  
            if (contains(input, (String) lt.next())) {  
                return true;  
            }  
            continue;  
        }  
        return false;  
    }  
}