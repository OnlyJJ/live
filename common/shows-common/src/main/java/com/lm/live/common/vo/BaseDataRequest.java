package com.lm.live.common.vo;


/**
 * 接口参数初始化的基类，所有的vo都需要继承
 * @author shao.xiang
 * 		2017-08-21
 */
public abstract class BaseDataRequest {
	
	public abstract void reciveRequest(String sessionId,String uri) throws Exception;
}
