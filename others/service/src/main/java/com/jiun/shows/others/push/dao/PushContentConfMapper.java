package com.jiun.shows.others.push.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.others.push.domain.PushContentConf;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-15
 *
 */
public interface PushContentConfMapper extends ICommonMapper<PushContentConf> {
	PushContentConf getPushContentConf(int type);
}
