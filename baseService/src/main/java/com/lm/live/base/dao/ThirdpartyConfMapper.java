package com.lm.live.base.dao;

import com.lm.live.base.domain.ThirdpartyConf;
import com.lm.live.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-24
 *
 */
public interface ThirdpartyConfMapper extends ICommonMapper<ThirdpartyConf> {
	/**
	 * 查询启用状态中的第三方登录配置信息
	 * @param thirdpartyType 第三方类型,0:qq;1:微信;2:微博
	 * @param packageName 包名,为空则查官方包名所用的配置
	 * @param clientType  客户端类型,0:三端通用; 1:web; 2:android; 3:ios;
	 * @return
	 */
	ThirdpartyConf getThirdpartyConf(int thirdpartyType,
			String packageName, int clientType);
	
	/**
	 * 查询启用状态中的第三方登录配置信息
	 * @param thirdpartyType 第三方类型,0:qq;1:微信;2:微博
	 * @param packageName 包名,为空则查官方包名所用的配置
	 * @param clientType  客户端类型,0:三端通用; 1:web; 2:android; 3:ios;
	 * @return
	 */
	ThirdpartyConf getThirdpartyConf1(int thirdpartyType,
			String mchid, int clientType);
}
