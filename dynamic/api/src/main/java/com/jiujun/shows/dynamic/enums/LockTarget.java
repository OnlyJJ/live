package com.jiujun.shows.dynamic.enums;


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

	/** 动态信息发布  */
	LOCK_DIARYINFO("lock:dynamic:diaryinfo"),
	/** 点赞行为 */
	LOCK_FAVOURATE_INFO("lock:dynamic:fot"),
	/** 动态评论总数锁 */
	LOCK_COMMENTALL("lock:dynamic:cmt:all"),
	/** 动态评论锁 */
	LOCK_COMMENTINFO("lock:dynamic:cmt:info");
	
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
