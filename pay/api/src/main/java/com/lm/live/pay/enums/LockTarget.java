package com.lm.live.pay.enums;


/**
 * 锁对象集合<br>
 * 	说明：<br>
 * 		1、本服务下所有锁对象必须放在此处，以避免锁重复造成的问题;<br>
 * 		2、锁表达式为：lock + 模块名  + 业务简称  等等
 * @author shao.xiang
 * @date 2017年10月19日
 *
 */
public enum LockTarget {
	
	/** 支付成功后，处理的业务锁，lock + orderId */
	LOCK_PAYAFTER_BIZ("lock:pay:afterbiz:"),

	/** 微信支付锁，lock + userId */
	LOCK_PAY_WCH("lock:pay:wch:");
	
	private String lockName; 
	
	private LockTarget(String lockName) {
		this.lockName = lockName;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	
}
