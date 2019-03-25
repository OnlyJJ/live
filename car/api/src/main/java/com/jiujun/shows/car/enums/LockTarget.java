package com.jiujun.shows.car.enums;


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

	/** 抢车位锁，key + roomId */
	LOCK_CAR_GRABPORT("lock:car:garb:"),
	/** 停车收益，锁头+ userId*/
	LOCK_CAR_ADDGOLD("lock:car:addgold:"),
	/** 赠送座驾锁头，使用时：锁头+userId  */
	LOCK_CAR_GIVE("lock:car:give:"),
	/** 购买座驾锁头，使用时：锁头+userId  */
	LOCK_CAR_BUY("lock:car:buy:");
	
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
