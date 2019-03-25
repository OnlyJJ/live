package com.jiun.shows.others.push.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.others.push.domain.PushConfig;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-15
 *
 */
public interface PushConfigMapper extends ICommonMapper<PushConfig> {
	List<PushConfig> listPushConfig(int appType);
}
