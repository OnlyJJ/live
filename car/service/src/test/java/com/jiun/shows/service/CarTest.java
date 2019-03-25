package com.jiun.shows.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.common.utils.LogUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class CarTest {
	@Resource
	private IUserCarPortService userCarPortService;
	
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
	public void test1() {
		try {
			String userId = "153706";
			userCarPortService.getInUseUserCarPortDetailInfo(userId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}
}
