package com.lm.live.base.service;

import com.lm.live.base.domain.ThirdpartyConf;
import com.lm.live.common.service.ICommonService;
import com.lm.live.framework.service.ServiceResult;


/**
 * 第三方服务配置
 * @author shao.xiang
 * @date 2017年6月29日
 *
 */
public interface IThirdpartyConfService extends ICommonService<ThirdpartyConf> {
	
	/**
	 * 查询启用状态中的第三方登录配置信息
	 * @param thirdpartyType 第三方类型,0:qq;1:微信;2:微博
	 * @param packageName 包名,为空则查官方包名(com.jj.shows)所用的配置
	 * @param clientType  客户端类型,0:andorid;1:web;2:三端通用;3:ios;
	 * @return
	 */
	ServiceResult<ThirdpartyConf> getThirdpartyConf(int thirdpartyType,String packageName,int clientType);

	/**
	 * 查询启用状态中的第三方登录配置信息
	 * @param thirdpartyType 第三方类型,0:qq;1:微信;2:微博
	 * @param mchid 商户号
	 * @param clientType  客户端类型,0:andorid;1:web;2:三端通用;3:ios;
	 * @return
	 */
	ServiceResult<ThirdpartyConf> getThirdpartyConf1(int thirdpartyType,String mchid,int clientType);
	
	/**
	 * 获取第三方配置信息，（需要用包名区别的）
	 * @param thirdpartyType 
	 * @param packageName 包名
	 * @param clientType
	 * @return
	 */
	ServiceResult<ThirdpartyConf> getThirdpartyConfNeedPackage(int thirdpartyType,String packageName,int clientType);
}
