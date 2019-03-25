package test;

import java.io.IOException;









import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class CarTest {
	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
//	@Test
//	public void test1() {
//		try {
//			String userId = "153706";
//			userCarPortService.getInUseUserCarPortDetailInfo(userId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
