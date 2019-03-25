package com.lm.live.common.redis;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 分布式锁
 * @author shao.xiang
 *		2017-09-08
 */
public class RdLock {
	private static Logger logger = Logger.getLogger(RdLock.class);
	
	/** 默认线程最大等待时间  */
	private static final int DEFAULT_WAITTIME = 10;
	/** 默认锁过期自动释放时间，15秒 */
	private static final int DEFAULT_LEASETIME =15;
	
	private static RedissonClient redissonClient;

	public static void setRedissonClient(RedissonClient redissonClient) {
		RdLock.redissonClient = redissonClient;
	}
	
	private RdLock() {}
	
	/**
	 * 获取锁<br>
	 * 	使用说明：<br>
	 * 		1、本锁为公平锁，锁释放后，先请求的线程将优先得到锁，避免饥饿争抢;<br>
	 * 		2、获取锁成功后，默认使用30秒自动过期释放锁，防止死锁<br>
	 * @param lockname 锁名称
	 */
	public static void lock(final String lockname) {
		RLock fairLock = null;
		try {
			fairLock = redissonClient.getFairLock(lockname);
			String thread_ = Thread.currentThread().getName();
			long begin = System.currentTimeMillis();
			System.err.println("开始竞争锁，Thread=" + thread_);
			// 超时等待，这种用法并发有问题，如果获得锁的线程自动释放了锁，并且其他线程等待时间都过了，那么其他线程有可能都会同时获得这个锁
//			fairLock.tryLock(DEFAULT_WAITTIME, DEFAULT_LEASETIME, TimeUnit.SECONDS);
			fairLock.lock(DEFAULT_LEASETIME, TimeUnit.SECONDS);
			long end = System.currentTimeMillis();
			System.err.println("加锁成功！Thread=" + thread_ + ",time=" + (end - begin));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	} 
	
	/**
	 * 释放锁
	 * @param lockname 锁名称
	 */
	public static void unlock(String lockname) {
		try {
			System.err.println("处理解锁，Thread=" + Thread.currentThread().getName());
			RLock lock = redissonClient.getFairLock(lockname);
			lock.unlock();
		} catch(IllegalMonitorStateException e) {
			logger.error("###锁已过期删除，不再处理");
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	} 
}
