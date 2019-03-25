package com.jiun.shows.others.push.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.push.biz.PushContentConfBiz;
import com.jiun.shows.others.push.domain.PushContentConf;
import com.jiun.shows.others.push.service.IPushContentConfService;


/**
 * Service -推送消息配置
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("pushContentConfServiceImpl")
public class PushContentConfServiceImpl implements IPushContentConfService {

	@Resource
	private PushContentConfBiz pushContentConfBiz;
	
	@Override
	public PushContentConf getPushContentConf(int type) throws Exception {
		return pushContentConfBiz.getPushContentConf(type);
	}
}
