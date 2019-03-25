package com.jiun.shows.others.push.service;

import java.util.List;

import com.jiun.shows.others.push.domain.PushConfig;


/**
 * 推送配置服务
 * @author shao.xiang
 * @date 2017-06-15 
 */
public interface IPushConfigService {
	/**
	 * 根据app类型获取注册的配置信息
	 * @param appType
	 * @return
	 */
	List<PushConfig> listPushConfig(int appType) throws Exception;
}
