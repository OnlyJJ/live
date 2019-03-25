package com.lm.live.common.redis;

import org.apache.log4j.Logger;

import com.esotericsoftware.minlog.Log;
import com.lm.live.common.utils.KryoUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;
/**
 * Redis 单机工具类
 * @author shao.xiang
 *	2017-09-04
 */
public class RedisSingleUtils {
	private static Logger logger = Logger.getLogger(RedisSingleUtils.class);
	/** redis 服务端ip */
	private static final String IP = "192.168.1.149";
	/** redis 服务端口 */
	private static final int PORT = 6379;
	/** 连接池重连等待时间 */
	private static final int CONNECTION_TIMEOUT = 500;
	/** 最大连接数  */
	private static final int MAX_TOTAL = 100;
	/** 最大空闲连接数  */
	private static final int MAX_IDLE = 30;
	/** 最大空闲连接数  */
	private static final int MIN_IDLE = 10;
	/** 获取连接时的最大等待毫秒数,超出时间则抛JedisConnectionException异常*/
	private static final int MAX_WAITMILLIS = 2 * 1000;
	
	private static JedisPool jedisPool = null;
	
	static {
		try {
			System.err.println("init jedis pool.....");
			final JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(MAX_TOTAL);
			poolConfig.setMaxIdle(MAX_IDLE);
			poolConfig.setMinIdle(MIN_IDLE);
			poolConfig.setMaxWaitMillis(MAX_WAITMILLIS);
			jedisPool = new JedisPool(poolConfig, IP, PORT, CONNECTION_TIMEOUT);
		} catch(Exception e) {
			Log.error("### pool init faild!");
		}
	}
	
	private RedisSingleUtils() {}
	
	/**
	 * 通过key获取缓存，缓存的value必须是String类型，否则返回错误
	 * @param key
	 * @return 字符串
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = jedisPool.getResource();
		try{
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 通过key获取缓存，
	 * @param key byte数组
	 * @return byte数组
	 */
	public static byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try{
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 加入缓存，不使用序列化
	 * 不设置过期时间
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		boolean ret = true;
		try {
			value = jedis.set(key, value);
		}catch (Exception e) {
			ret = false;
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return ret;
	}
	
	/**
	 * 字符串缓存，不使用序列化 
	 * 自定义过期时间
	 * @param key 
	 * @param value
	 * @param expire 过期时间
	 * @return
	 */
	public static String set(String key, String value, int expire) {
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		}catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return value;
	}

	
	public static String set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String v=null;
		try {
			v =jedis.set(key, value);
		}catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return v;
	}

	
	public static String set(byte[] key, byte[] value, int expire) {
		Jedis jedis = jedisPool.getResource();
		String v=null;
		try {
			v =jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return v;
	}

	
	public static String hget(String hkey, String key) {
		String value =null;
		Jedis jedis = jedisPool.getResource();
		try{
			value = jedis.hget(hkey, key);
			if("nil".equals(value)) {
				value = null;
			}
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return value;
	}

	
	public static long hset(String hkey, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result =null;
		try{
			result= jedis.hset(hkey, key, value);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * key自增
	 * @param key
	 * @return
	 */
	public static long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.incr(key);
		}catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param second
	 * @return
	 */
	public static long expire(String key, int second) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.expire(key, second);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 查询缓存剩余过期时间，单位：秒
	 * @param key
	 * @return
	 */
	public static long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.ttl(key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key
	 * @return
	 */
	public static long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.del(key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key
	 * @return
	 */
	public static long del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.del(key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	
	public static long hdel(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try{
			result = jedis.hdel(hkey, key);
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return result;
	}

	
	public static Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		}catch (Exception e){
			return null;
		}
		try {
			keys = jedis.keys(pattern.getBytes());
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return keys;
	}

	/**
	 * 清空redis所有的缓存
	 */
	public static void flushDB() {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.flushDB();
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
	}

	/**
	 * 缓存使用量
	 * @return
	 */
	public static Long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try {
			dbSize = jedis.dbSize();
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return dbSize;
	}

	/**
	 * 获取缓存对象
	 * 说明：此方法用于获取对象缓存，基本类型请用get(key)
	 * @param key
	 * @return
	 */
	public static Object getObject(String key) {
		Object ret = null;
		Jedis jedis = jedisPool.getResource();
		try{
			String value = jedis.get(key);
			if(null != value) {
				ret = KryoUtil.deSerializeFromString(value);
			}
		} catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return ret;
	}

	/**
	 * 加入缓存，未定义过期时间
	 * 说明：此方法是将对象序列化后，存储在缓存中
	 * @param key
	 * @param obj 缓存对象，如：list，map等
	 * @return
	 */
	public static boolean set(String key, Object obj) {
		Jedis jedis = jedisPool.getResource();
		boolean ret = true;
		try {
			 jedis.set(key, KryoUtil.serializeToString(obj));
		}catch (Exception e) {
			ret = false;
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return ret;
	}

	/**
	 * 放入缓存，自定义过期时间
	 * 说明：此方法是将对象序列化后，存储在缓存中
	 * @param key
	 * @param obj 缓存对象，如：list，map等
	 * @param expire 过期时间
	 * @return
	 */
	public static boolean set(String key, Object obj, int expire) {
		Jedis jedis = jedisPool.getResource();
		boolean ret = true;
		try {
			jedis.set(key, KryoUtil.serializeToString(obj));
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		}catch (Exception e) {
			ret = false;
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return ret;
	}

	/**
	 * 只有key不存在，才会设置成功，并返回1，否则返回0
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public static int setNx(String key, String value, int expire) {
		Jedis jedis = jedisPool.getResource();
		int ret = 0;
		try {
			Long val = jedis.setnx(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
			if(1 == val) {
				ret = 1;
			}
		}catch (Exception e) {
			logger.error("### 获取缓存失败,e=" + e.getMessage());
		} finally {
			jedis.close();
		}
		return ret;
	}
}
