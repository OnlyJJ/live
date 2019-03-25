package com.lm.live.common;

import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.KryoUtil;

public class KryoTest {
	
	public static void main(String[] args) throws ClassNotFoundException {
		UserInfo user = new UserInfo();
		user.setId(123847575);
		user.setUserId("12345677 12345677 12345677 12345677 12345677 12345677 12345677 12345677");
		user.setName("12345677 12345677 12345677 12345677 12345677 12345677 12345677 12345677");
		user.setCarid(123847575);
		user.setAge(123847575);
		
		String key = "user";
//		RedisUtil.set(key, user);
//		
//		RedisUtil.getObj(key);
		String seriz = KryoUtil.serializeToString(user);
		System.err.println("seriz=" + seriz);
		
		
	}
}
