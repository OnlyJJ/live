package com.lm.live.common.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Email163Util {
	
	public static void send(String emailAddr,String subject, String msg) throws Exception {
		String hostName = SpringContextListener.getContextProValue("mail.163.hostName", "smtp.163.com");
		String smtpPort = SpringContextListener.getContextProValue("mail.163.smtpPort", "25");
		String user = SpringContextListener.getContextProValue("mail.163.user", "vip9show@163.com");
		String passowrd = SpringContextListener.getContextProValue("mail.163.passowrd", "qjtfelktzsalvixf");
		
		SimpleEmail email = new SimpleEmail();
		
		email.setHostName(hostName);
		email.setSmtpPort(Integer.parseInt(smtpPort));
		email.setAuthentication(user, passowrd);
		email.setCharset("UTF-8");
				
		try {
			email.addTo(emailAddr);
			email.setFrom(user);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	

	/*@Test
	public void testSend() {
		try {
			//EmailUtil.send("vip9show@163.com", "lintobaidu","http://www.baidu.com");
			EmailUtil.send("vip9show@163.com", "lintobaidu","<a>http://www.baidu.com</a>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
