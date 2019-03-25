package com.lm.live.base.dao;

import java.util.List;

import com.lm.live.base.domain.IpRepositor;
import com.lm.live.common.dao.ICommonMapper;

/**
 * ip服务持久层接口
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface IpRepositorMapper extends ICommonMapper<IpRepositor> {
	/**
	 * 获取所有的ip
	 * @return
	 */
	List<IpRepositor> getIpRepositor();
}
