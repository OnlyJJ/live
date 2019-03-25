package com.jiun.shows.others.push.service;

import com.jiun.shows.others.push.domain.PushContentConf;


/**
 * Service - 推送消息配置
 * @author shao.xiang
 * @date 2017-06-15
 */
public interface IPushContentConfService {
	/**
	 * 获取消息配置
	 * @param type 消息类型：0-自定义，用户点击打开到首页，1-开播提醒，打开到房间，2-活动消息，打开到活动页面
	 * @return
	 * @throws Exception
	 */
	PushContentConf getPushContentConf(int type) throws Exception;
}
