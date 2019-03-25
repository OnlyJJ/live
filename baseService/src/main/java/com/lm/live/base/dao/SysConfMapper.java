package com.lm.live.base.dao;

import com.lm.live.base.domain.SysConf;
import com.lm.live.common.dao.ICommonMapper;

/**
 * ip服务持久层接口
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface SysConfMapper extends ICommonMapper<SysConf> {
	
	SysConf getByCode(String code);
}
