package test;

import com.lm.live.user.vo.UserInfo;

public class Test {
	
	public static void main(String[] args) {
		UserInfo info = new UserInfo();
		info.setUserId("1100011");
		info.setIcon("3294985723.jpg");
		info.setNickName("厉害的飞车党");
		info.setIsFirttimeLogin(0);
		info.buildJson();
		
		System.err.println(info.buildJson().toString());
	}
	
	public void test() {
		
	}
}
