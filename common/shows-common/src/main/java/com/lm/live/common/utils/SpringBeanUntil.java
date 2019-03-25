package com.lm.live.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUntil implements ApplicationContextAware{

	/**
	* 当前IOC
	*/
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		SpringBeanUntil.applicationContext = arg0;
		
	}
	
	/**
	 * 获取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * 根据bean id获取对应的实例化对象
	 * beanId,clazz 对应的class对象
	 */
	public static Object getBean(String beanId,Class<?> clazz){
		return applicationContext.getBean(beanId, clazz);
	}
	
}
