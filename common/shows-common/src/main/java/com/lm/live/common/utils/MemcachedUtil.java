package com.lm.live.common.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

//import net.spy.memcached.AddrUtil;
//import net.spy.memcached.MemcachedClient;

import org.springframework.stereotype.Component;

/**
 * Memcached服务
 * 
 * @author huangzp
 * @date 2015-4-7
 */
@Component
public class MemcachedUtil {

//	private static MemcachedClient mcClient = null;
	private static Properties systemProperties = System.getProperties();
	
    public static int DEFAULT_TIMEOUT = 2;  
    public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;  
    
	static {
		systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
		System.setProperties(systemProperties);
		try {
			if ("true".equals(SpringContextListener.getContextProValue("mc.enable", "false"))) {
				String serverlist = SpringContextListener.getContextProValue("mc.serverlist", "");
				serverlist = serverlist.replaceAll(",", " ");
//				mcClient = new MemcachedClient(AddrUtil.getAddresses(serverlist));
				LogUtil.log.info("###### memcached connect success ######");
			} else {
				LogUtil.log.info("###### memcached not enable ######");
			}
		} catch (Exception e) {
			LogUtil.log.error("###### memcached connect fail ######", e);
		}
	}


	@PreDestroy
	public void doDestroy() throws Exception {
//		if (null != mcClient) {
//			mcClient.shutdown();
//		}
	}

	/**
	 * 获取key对应的值
	 */
	public static Object get(String key) {
		Object obj = null;
		try {
//			if (null != mcClient) {
//				obj = mcClient.get(key);
//			} else {
//				LogUtil.log.info("mc is connect fail");
//			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage());
		}
		return obj;
	}

	/**
	 * 因为memcached不能对Integer对象做incr操作
	 */
	public static boolean set(String key, Object obj) {
		return set(key, obj, 0);
	}
	public static boolean set(String key, int obj) {
		return set(key, String.valueOf(obj), 0);
	}
	public static boolean set(String key, Integer obj) {
		return set(key, Integer.toString(obj), 0);
	}
	public static boolean set(String key, long obj) {
		return set(key, String.valueOf(obj), 0);
	}
	public static boolean set(String key, Long obj) {
		return set(key, Long.toString(obj), 0);
	}
	/**
	 * 设置值，以及过期时间(单位：秒)
	 */
	public static boolean set(String key, int obj, int timeoutSecond) {
		return set(key, String.valueOf(obj), timeoutSecond);
	}
	public static boolean set(String key, Integer obj, int timeoutSecond) {
		return set(key, Integer.toString(obj), timeoutSecond);
	}
	public static boolean set(String key, long obj, int timeoutSecond) {
		return set(key, String.valueOf(obj), timeoutSecond);
	}
	public static boolean set(String key, Long obj, int timeoutSecond) {
		return set(key, Long.toString(obj), timeoutSecond);
	}
	public static boolean set(String key, Object obj, int timeoutSecond) {
		boolean result = false;
		try{
			if (null == key || key.length() == 0) {
				LogUtil.log.error("key==null || key.length()==0");
			}
			if (key.length() >= 250) {
				LogUtil.log.error("key.length() >= 250");
			}
//			if (null != mcClient) {
//				mcClient.set(key, timeoutSecond, obj);
//				result = true;
//			} else {
//				LogUtil.log.info("mc is connect fail");
//			}
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 删除key对应的值
	 */
	public static boolean delete(String key) {
		boolean result = false;
//		if (null != mcClient) {
//			mcClient.delete(key);
//			result = true;
//		} else {
//			LogUtil.log.info("mc is connect fail");
//		}
		return result;
	}
	
	public Object asyncGet(String key) {
		Object obj = null;
//		Future<Object> f = mcClient.asyncGet(key);
//		try {
//			obj = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
//		} catch (Exception e) {
//			f.cancel(false);
//		}
		return obj;
	}

	public boolean add(String key, Object value, int expire) {
//		Future<Boolean> f = mcClient.add(key, expire, value);
//		return getBooleanValue(f);
		return false;
	}

	public boolean replace(String key, Object value, int expire) {
//		Future<Boolean> f = mcClient.replace(key, expire, value);
//		return getBooleanValue(f);
		return false;
	}

	public Map<String, Object> getMulti(Collection<String> keys) {
//		return mcClient.getBulk(keys);
		return null;
	}

	public Map<String, Object> getMulti(String[] keys) {
//		return mcClient.getBulk(keys);
		return null;
	}

	public Map<String, Object> asyncGetMulti(Collection<String> keys) {
		Map map = null;
//		Future<Map<String, Object>> f = mcClient.asyncGetBulk(keys);
//		try {
//			map = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
//		} catch (Exception e) {
//			f.cancel(false);
//		}
		return map;
	}

	public Map<String, Object> asyncGetMulti(String keys[]) {
		Map map = null;
//		Future<Map<String, Object>> f = mcClient.asyncGetBulk(keys);
//		try {
//			map = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
//		} catch (Exception e) {
//			f.cancel(false);
//		}
		return map;
	}

	public static long increment(String key, long by, long defaultValue, int expire) {
//		return mcClient.incr(key, by, defaultValue, expire);
		return 0;
	}
	public static long increment(String key, long by, long defaultValue) {
//		return mcClient.incr(key, by, defaultValue);
		return 0;
	}
	public static long increment(String key, long by) {
//		return mcClient.incr(key, by);
		return 0;
	}

	public static long decrement(String key, long by, long defaultValue, int expire) {
//		return mcClient.decr(key, by, defaultValue, expire);
		return 0;
	}
	public static long decrement(String key, long by,long defaultValue) {
//		return mcClient.decr(key, by,defaultValue);
		return 0;
	}
	public static long decrement(String key, long by) {
//		return mcClient.decr(key, by);
		return 0;
	}

	public static long asyncIncrement(String key, long by) {
//		Future<Long> f = mcClient.asyncIncr(key, by);
//		return getLongValue(f);
		return 0;
	}

	public static long asyncDecrement(String key, long by) {
//		Future<Long> f = mcClient.asyncDecr(key, by);
//		return getLongValue(f);
		return 0;
	}

	private static long getLongValue(Future<Long> f) {
		try {
			Long l = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
			return l.longValue();
		} catch (Exception e) {
			f.cancel(false);
		}
		return -1;
	}

	private static boolean getBooleanValue(Future<Boolean> f) {
		try {
			Boolean bool = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
			return bool.booleanValue();
		} catch (Exception e) {
			f.cancel(false);
			return false;
		}
	}
}
