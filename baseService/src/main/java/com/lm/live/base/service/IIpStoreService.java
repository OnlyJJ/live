package com.lm.live.base.service;

import java.util.Map;

/**
 * ip服务类
 * @author shao.xiang
 * @date 2017-06-02
 */
public interface IIpStoreService {
	/**
	 * 获取真实客户端ip
	 * @param ip
	 * @return
	 */
	public String getAddressByIp(String ip);
	
	public Map<String,String> getIpInfo(String ip);
	
}
