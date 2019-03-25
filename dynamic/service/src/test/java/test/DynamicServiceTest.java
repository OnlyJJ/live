package test;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;
import com.jiujun.shows.dynamic.service.user.IDiaryCommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class DynamicServiceTest {
	
//	@Resource(name="diaryCommentServiceImpl")
//	private IDiaryCommentService diaryCommentService;
	
	@Resource
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
	
//	@Test
//	public void test() {
//		try {
//			List<DiaryComment> list = diaryCommentService.getDiaryCommentCache(54,10);
//			if(list != null && list.size() >0) {
//				System.err.println("list not null, list.get(0)=" + list.get(0).getCommenttextinfo());
//			} else {
//				System.err.println("list is null...");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void test1() {
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
