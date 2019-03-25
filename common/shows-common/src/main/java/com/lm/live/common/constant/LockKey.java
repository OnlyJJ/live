package com.lm.live.common.constant;


/**
 * 锁对象集合（父类）<br>
 * 	说明：<br>
 * 		1、公共的锁对象，子模块如需细化，则自行维护一个对象，但需要继承此类;<br>
 * 		2、锁表达式为：lock + 模块名  + 业务简称  等等;<br>
 * 		3、所有的lock后，如需跟动态参数，则此参数必须要做非空校验，否则，不允许使用锁，举例:<br>
 * 			if userId is not null, then RdLock.lock(lock + userId);
 * @author shao.xiang
 * @date 2017年10月19日
 *
 */
public class LockKey {
	
	/**
	 * 账户锁，用户所有涉及到账户操作的业务，均使用此锁<br>
	 * 使用方法：<br>
	 * 	lock + userId;
	 */
	public static final String LOCK_USER_ACCOUNT = "lock:user:account:";
	
	/**
	 * 工具锁，除了金币外，其他都定义为道具，例如：礼物，喇叭，钥匙，宝箱，座驾等，均使用此锁；<br>
	 * 	使用方法：<br>
	 * 		可作为单独锁使用，也可以做复合锁（多个锁联合为同一把锁）使用<br>
	 * 		lock + userId
	 */
	public static final String LOCK_USER_TOOL = "lock:user:tool:";
	
	/**
	 * userId-用户code生成器
	 */
	public static final String LOCK_LOGIN_USERCODE = "lock:login:code";
	

}
