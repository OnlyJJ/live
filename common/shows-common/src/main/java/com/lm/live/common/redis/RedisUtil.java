package com.lm.live.common.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;
import com.lm.live.common.utils.KryoUtil;


/**
 * redis工具类
 * @author shao.xiang
 * @version 1.0
 *		2017-09-04
 */
public class RedisUtil {
	
	private static Logger logger = Logger.getLogger(RedisUtil.class);
	
	private static JedisCluster jedisCluster;
	
	/** 如果不设置缓存时间，则使用默认时间，1h */
	private static final int DEFAULT_TIME = 60 * 60 * 1;
	/** set参数，nx */
	private static final String PARAMS_NX = "nx";
	/** set参数，xx */
	private static final String PARAMS_XX = "xx";
	/** set参数，ex */
	private static final String PARAMS_EX = "ex";
	/** lua脚本的key，集群中，一个lua脚本对应一个key */
	private static final String lua_ = "lua_";
	/** lua角本--setnx使用 */
	private static String script_;
	
	private RedisUtil() {}
	
	public static void setJedisCluster(JedisCluster jedisCluster) {
		RedisUtil.jedisCluster = jedisCluster;
	}
	
	public static String getScript_() {
		return script_;
	}

	public static void setScript_(String script_) {
		RedisUtil.script_ = script_;
	}

	/**
	 * 加载lua脚本<br>
	 * 	说明：<br>
	 * 		1、此角本在项目启动时，加载；<br>
	 * 		2、脚本缓存在redis内存中，可以长期缓存，目的是减少网络开销；<br>
	 * 		3、此角标用于setnx的原子操作，即当key不存在时，才能set成功，并且设置过期时间
	 */
	public static void init() {
		try {
			if(script_ == null) {
				final String script = "return redis.call('set', KEYS[1], ARGV[1], 'ex', ARGV[2], 'nx'); ";
				script_ = jedisCluster.scriptLoad(script, lua_);
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 缓存字符串（默认过期时间：1h）<br>
	 * @param key
	 * @param value 必须是字符串
	 * @return
	 */
	public static void set(String key, String value) {
		jedisCluster.set(key, value);
		jedisCluster.expire(key, DEFAULT_TIME);
	}
	
	/**
	 * 缓存字符串 （自定义过期时间）<br>
	 * @param key
	 * @param value 必须是字符串
	 * @param expire 过期时间
	 * @return
	 */
	public static void set(String key, String value, int expire) {
		jedisCluster.set(key, value);
		if (expire > 0) {
			jedisCluster.expire(key, expire);
		} else {
			jedisCluster.expire(key, DEFAULT_TIME);
		}
	}
	
	/**
	 * 当且仅当key不存在时，才可以设置成功（非原子操作）
	 * @param key
	 * @param value 必须是字符串
	 * @param time 单位：秒
	 * @return
	 */
	public static void setNx(String key, String value, long time) {
		jedisCluster.set(key, value, PARAMS_NX, PARAMS_EX, time);
	}
	
	/**
	 * 当且仅当key存在，才可以设置成功，可用于更新
	 * @param key
	 * @param value 必须是字符串
	 * @param time 单位：秒
	 * @return
	 */
	public static void setXx(String key, String value, long time) {
		jedisCluster.set(key, value, PARAMS_XX, PARAMS_EX, time);
	}
	
	/**
	 * 存储对象（默认过期时间1h）<br>
	 * 	说明：<br>
	 * 		1、使用默认过期时间，1h<br>
	 * 		2、序列化对象必须实现Serializable <br>
	 * @param key
	 * @param obj 序列化对象
	 * @return
	 */
	public static void set(String key, Object obj) {
		jedisCluster.set(key, KryoUtil.serializeToString(obj));
		jedisCluster.expire(key, DEFAULT_TIME);
	}
	
	/**
	 * 存储对象（自定义过期时间）<br>
	 * 	说明：<br>
	 * 		1、使用自定义过期时间<br>
	 * 		2、序列化对象必须实现Serializable <br>
	 * @param key
	 * @param obj 序列化对象
	 * @param expire 过期时间
	 * @return
	 */
	public static void set(String key, Object obj, int expire) {
		jedisCluster.set(key, KryoUtil.serializeToString(obj));
		if(expire > 0) {
			jedisCluster.expire(key, expire);
		} else {
			jedisCluster.expire(key, DEFAULT_TIME);
		}
	}
	
	/**
	 * 获取缓存（字符串使用）
	 * @param key
	 * @return 返回字符串
	 */
	public static String get(String key) {
		return jedisCluster.get(key);
	}
	
	/**
	 * 设置value为当前缓存值，并返回旧值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getSet(String key, String value) {
		return jedisCluster.getSet(key, value);
	}
	
	/**
	 * 获取缓存（对象使用，反序列化）
	 * @param key
	 * @return 返回对象
	 */
	public static Object getObj(String key) {
		return KryoUtil.deSerializeFromString(jedisCluster.get(key));
	}
	
	/**
	 * Map使用
	 * @param hkey 缓存map的key
	 * @param key map中键
	 * @return
	 */
	public static String hget(String hkey, String key) {
		String value = jedisCluster.hget(hkey, key);
		jedisCluster.expire(key, DEFAULT_TIME);
		return value;
	}

	/**
	 * Map使用
	 * @param hkey 需要缓存的map的key
	 * @param key map中的键
	 * @param value map中的键对应的val
	 * @return
	 */
	public static void hset(String hkey, String key, String value) {
		jedisCluster.hset(hkey, key, value);
	}

	/**
	 * 自增<br>
	 * 	说明:<br>
	 * 		1、如果key缓存的value非整数类型，如字符串，则会抛出异常;<br>
	 * 		2、如果key不存在，则设置当前key的value为1，并返回1;<br>
	 * 		3、如果key存在，并且为整数类型，则为当前值自增1，并返回自增后的值<br>
	 * @param key
	 * @return
	 */
	public static long incr(String key) {
		return jedisCluster.incr(key);
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param second 单位：秒
	 * @return
	 */
	public static long expire(String key, int second) {
		return jedisCluster.expire(key, second);
	}

	/**
	 * 获取key剩余的过期时间
	 * @param key
	 * @return 返回值说明：<br>
	 * 			>0：正常的剩余时间<br>
	 * 			=-2：已过期删除
	 */
	public static long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	/**
	 * 删除key
	 * @param key
	 * @return 成功返回1，失败返回0
	 */
	public static long del(String key) {
		return jedisCluster.del(key);
	}

	/**
	 * 删除hash中的键
	 * @param hkey hash对象的key
	 * @param key hash中的键
	 * @return
	 */
	public static long hdel(String hkey, String key) {
		return jedisCluster.hdel(hkey, key);
	}

	/**
	 * 此方法为原子操作，即存入缓存和设置时间不会被其他所干扰
	 * @param key 
	 * @param uuid
	 * @param acquireTimeout
	 * @param timeOutSeconds
	 * @return
	 */
	public static void setNxAtom(String key, String value, long timeOutSeconds) {
		jedisCluster.evalsha(script_, 1, key, value, String.valueOf(timeOutSeconds));
	}

}
