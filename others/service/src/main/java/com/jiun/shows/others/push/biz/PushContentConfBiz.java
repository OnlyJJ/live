package com.jiun.shows.others.push.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.push.dao.PushContentConfMapper;
import com.jiun.shows.others.push.domain.PushContentConf;


/**
 * 推送消息配置
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("pushContentConfBiz")
public class PushContentConfBiz {

	@Resource
	private PushContentConfMapper pushContentConfMapper;
	
	public PushContentConf getPushContentConf(int type) throws Exception {
		return pushContentConfMapper.getPushContentConf(type);
	}
}
