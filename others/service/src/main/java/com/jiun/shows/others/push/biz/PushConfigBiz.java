package com.jiun.shows.others.push.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.push.dao.PushConfigMapper;
import com.jiun.shows.others.push.domain.PushConfig;



/**
 * 信鸽推送配置服务
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("pushConfigBiz")
public class PushConfigBiz {

	@Resource
	private PushConfigMapper pushConfigMapper;
	
	public List<PushConfig> listPushConfig(int appType) {
		return pushConfigMapper.listPushConfig(appType);
	}
}
