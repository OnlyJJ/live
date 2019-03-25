package com.lm.live.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class UserInfoUtil {

	/**
	 * 检测userId是否有效(必须都是数字)
	 * @param userId
	 * @return
	 */
	public static boolean checkUserIdIfValid(String userId){
		boolean flag = false;
		if(!StringUtils.isEmpty(userId)){
			Pattern pattern = Pattern.compile("^\\d+$");
			Matcher match = pattern.matcher(userId);
			if(match.matches()){
				flag = true;
			}
		}
		return flag;
		
	}
	
	/**
	 * 检测搜索时是否包含非法字符(如:sql注入字符or,and)
	 * @param searchStr
	 * @return
	 */
	public static boolean checkIfContainInvalidStrInSearch(String searchStr){
		boolean flag = false;
		if(!StringUtils.isEmpty(searchStr)){
			Pattern pattern = Pattern.compile("(or|and){1,}");
			Matcher match = pattern.matcher(searchStr);
			if(match.matches()){
				flag = true;
			}
		}
		return flag;
		
	}
	
	/**
	 * 转换用户等级成int(db中查出的用户等级如V1,V2,所以去掉头部V,将String转成int)
	 * @param userLevelStr
	 * @return
	 */
	public static int converUserLevelStr2Int(String userLevelStr){
		int senderNewUserLevel = 0 ;
		if(!StringUtils.isEmpty(userLevelStr)&&userLevelStr.length() > 1){
			senderNewUserLevel = Integer.parseInt(userLevelStr.substring(1));
		}
		return senderNewUserLevel;
	}
}
