package com.jiujun.shows.decorate.service;

import java.util.List;

import com.jiujun.shows.decorate.domain.DecorateConf;

/**
 * 勋章配置服务
 * @author shao.xiang
 * @date 2017-06-03 
 */
public interface IDecorateConfService {
	/**
	 * 根据勋章id，获取该勋章同类型的所有勋章
	 * @param decorateId
	 * @return
	 * @throws Exception
	 */
	List<DecorateConf> getDecorateConfByDecorateIdList(int decorateId) throws Exception;
	
	/**
	 * 获取勋章类型信息
	 * @param decorateId
	 * @return
	 * @throws Exception
	 */
	DecorateConf getDecorateConfByDecorateId(int decorateId) throws Exception;
}
