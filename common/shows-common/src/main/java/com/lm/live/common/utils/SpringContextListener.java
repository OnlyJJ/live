package com.lm.live.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
 * 全局配置文件工具类
 * 获取配置文件（properties）属性
 * @author shao.xiang
 * @date 2017-6-7
 */
public final class SpringContextListener extends PropertyPlaceholderConfigurer implements BeanFactoryAware {

	private static Properties context = new Properties();
	
	private static BeanFactory beanFactory;

	/**
	 * 此方法加载多个properties 文件
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		
		InputStream in = null;
		try {
			// 读取一般的配置
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
			if(in != null ) {
				Properties p = new Properties();
				p.load(in);
				for (Object key : p.keySet()) {
					String keyStr = key.toString().trim();
					String value = p.getProperty(keyStr).trim();
					props.put(keyStr, value);
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try { 
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				LogUtil.log.error(e.getMessage(), e);
			}
		}
		
		try {
			// 读取特殊的配置(正式服和测试服分别用不同的配置:如域名、memcache的地址)
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf-special.properties");
			if(in != null ) {
				Properties p = new Properties();
				p.load(in);
				for (Object key : p.keySet()) {
					String keyStr = key.toString().trim();
					String value = p.getProperty(keyStr).trim();
					props.put(keyStr, value);
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try { 
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				LogUtil.log.error(e.getMessage(), e);
			}
		}
		
		super.processProperties(beanFactoryToProcess, props);
		context = props;
	}

	/**
	 * 获取配置文件的属性值
	 * @param key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getContextProValue(String key, String defaultValue) {
		String value = defaultValue;
		if(null != context && context.size() > 0 ){
			value = context.getProperty(key, defaultValue);
		}
		return value;
	}
	
	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		if(SpringContextListener.beanFactory != null) {
			throw new RuntimeException("beanFactory inited .............");
		}
		LogUtil.log.info("SpringContextListener init ...........");
 		SpringContextListener.beanFactory = beanFactory;
	}

	public static <T> T getBean(String beanName, Class<T> clazs) {  
        return clazs.cast(beanFactory.getBean(beanName));  
	}  
	
	// my-todo
//	@SuppressWarnings("static-access")
//	public DynamicDataSource getDBS(){
//		return this.getBean("dataSources", DynamicDataSource.class);
//	}

}
