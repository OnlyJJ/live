package com.jiun.shows.others.push.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.push.biz.PushConfigBiz;
import com.jiun.shows.others.push.domain.PushConfig;
import com.jiun.shows.others.push.service.IPushConfigService;


/**
 * 信鸽推送配置服务
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("pushConfigServiceImpl")
public class PushConfigServiceImpl implements IPushConfigService{

	@Resource
	private PushConfigBiz pushConfigBiz;
	
	@Override
	public List<PushConfig> listPushConfig(int appType) {
		return pushConfigBiz.listPushConfig(appType);
	}
}
