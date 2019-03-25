package com.lm.live.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class EMailUtils {

	private static Logger log = Logger.getLogger(EMailUtils.class);
	
	private static Document config;
	
	static {
		SAXReader reader = new SAXReader();
		try {
			config = reader.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("productlib-email-config.xml"));
		} catch (DocumentException e) {
			log.error("read product-email-config.xml error , email not use ... ",e);
		}
		
	}
	
	public static String getValueByElement(String elemetPath){
		Node no = config.selectSingleNode(elemetPath);
		String value = no.getText();
//		log.info(value);
		return value;
	}
	
	public static List<String> getValuesByElement(String elemetPath){
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		List<Node> nodes = config.selectNodes(elemetPath);
		for (Node node : nodes) {
			list.add(node.getText());
		}
		return list;
	}
	
	public static boolean sendEmail(String title,String content) throws EmailException{
		log.info("start send email ....................到期提醒邮件 ");
		boolean success = true;
		HtmlEmail he = new HtmlEmail();
		he.setAuthentication(getValueByElement("/email-config/from-email/email"), getValueByElement("/email-config/from-email/password"));
		//he.setSslSmtpPort(getValueByElement("/email-config/ssl-port"));
		he.setHostName(getValueByElement("/email-config/server"));
		//he.setSSL(true);
		he.setCharset(getValueByElement("/email-config/char-set"));
		he.setSmtpPort(Integer.parseInt(getValueByElement("/email-config/port")));
		//he.addTo(getValuesByElement(""));
		List<String> sendTo = getValuesByElement("/email-config/send-email/mian-send/email");
		for (String to : sendTo) {
			he.addTo(to);
		}
		List<String> ccTo = getValuesByElement("/email-config/send-email/carbon/email");
		if(!ccTo.isEmpty()){
			for (String cc : ccTo) {
				he.addCc(cc);
			}
		}
		he.setFrom(getValueByElement("/email-config/from-email/email"),getValueByElement("/email-config/from-email/emailuser"),getValueByElement("/email-config/char-set"));
		he.setSubject(title);
		he.setHtmlMsg(content);
		log.info("end send email >>   " + he.send());
		return success;
	}
	
	public static boolean sendEmail(String title,String content,String email) throws EmailException{
		log.info("start send email ....................产品操作相关邮件 ");
		boolean success = true;
		HtmlEmail he = new HtmlEmail();
		he.setAuthentication(getValueByElement("/email-config/from-email/email"), getValueByElement("/email-config/from-email/password"));
		//he.setSslSmtpPort(getValueByElement("/email-config/ssl-port"));
		he.setHostName(getValueByElement("/email-config/server"));
		//he.setSSL(true);
		he.setCharset(getValueByElement("/email-config/char-set"));
		he.setSmtpPort(Integer.parseInt(getValueByElement("/email-config/port")));
		//he.addTo(getValuesByElement(""));
		if(email == null) {//空表示给管理员发
			List<String> sendTo = getValuesByElement("/email-config/send-email/mian-send/email");
			for (String to : sendTo) {
				he.addTo(to);
			}
		}else{
			he.addTo(email);//指定发送某人
		}
		he.setFrom(getValueByElement("/email-config/from-email/email"),getValueByElement("/email-config/from-email/emailuser"),getValueByElement("/email-config/char-set"));
		he.setSubject(title);
		he.setHtmlMsg(content);
		log.info("end send email >>   " + he.send());
		return success;
	}
	
	public static boolean sendEmail(String title,String content,String[] email) throws EmailException{
		log.info("start send email ....................产品操作相关邮件 ");
		boolean success = true;
		HtmlEmail he = new HtmlEmail();
		he.setAuthentication(getValueByElement("/email-config/from-email/email"), getValueByElement("/email-config/from-email/password"));
		//he.setSslSmtpPort(getValueByElement("/email-config/ssl-port"));
		he.setHostName(getValueByElement("/email-config/server"));
		//he.setSSL(true);
		he.setCharset(getValueByElement("/email-config/char-set"));
		he.setSmtpPort(Integer.parseInt(getValueByElement("/email-config/port")));
		//he.addTo(getValuesByElement(""));
		if(email == null) {//空表示给管理员发
			List<String> sendTo = getValuesByElement("/email-config/send-email/mian-send/email");
			for (String to : sendTo) {
				he.addTo(to);
			}
		}else{
			for(String e:email)
				he.addTo(e);//指定发送某人
			
		}
		he.setFrom(getValueByElement("/email-config/from-email/email"),getValueByElement("/email-config/from-email/emailuser"),getValueByElement("/email-config/char-set"));
		he.setSubject(title);
		he.setHtmlMsg(content);
		log.info("end send email >>   " + he.send());
		return success;
	}

	public static void main(String[] args) throws EmailException {
//		sendEmail("1x","xxxx2","long.yuntao@xxwan.com");
		sendEmail("xxxx", "xxx",new String[]{"chen.qi@xxwan.com","yang.zuyou@xxwan.com"});
	}
}
