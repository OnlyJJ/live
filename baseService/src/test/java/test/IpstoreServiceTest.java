package test;


import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lm.live.base.service.IIpStoreService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class IpstoreServiceTest {
	
	@Resource(name="ipStoreService")
	private IIpStoreService ipStoreService;
	
	public static void main(String[] args) {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
	                new String[] {"classpath:spring/spring-context.xml"});
	        context.start();
	        try {
				System.in.read(); // exit
			} catch (IOException e) {
				e.printStackTrace();
			} 
	}
	
	@Test
	public void test() {
		try {
			String ip = "218.66.37.244";
			String res = ipStoreService.getAddressByIp(ip);
			System.err.println("res="+res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
