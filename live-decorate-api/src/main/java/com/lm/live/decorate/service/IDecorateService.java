package com.lm.live.decorate.service;

import java.util.List;

import com.lm.live.common.service.ICommonService;
import com.lm.live.decorate.domain.Decorate;


/**
 * Service - 勋章表
 * @author shao.xiang
 * @date 2017-06-08
 */
public interface IDecorateService extends ICommonService<Decorate>{
	/**
	 * 根据类型获取配置信息 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Decorate getDecorateByType(int type) throws Exception;
	
	/**
	 * 获取所有的勋章列表
	 * @return
	 * @throws Exception
	 */
	public List<Decorate> getDecorateList(int resource) throws Exception;
	
	/**
	 * 查询用户所有获得的且有效的勋章(普通用户勋章)
	 * @param userId
	 * @return
	 */
	public List<Decorate> findListOfCommonUser(String userId) throws Exception;

	
	public List<Decorate> getDecorateListByDailyGift(List listDecorateId);
	
	/**
	 * 查询勋章(普通用户勋章)
	 * @param userId
	 * @return
	 */
	public List<Decorate> findListOfAnchor(String anchorId) throws Exception;
	
	/**
	 * 查询勋章
	 * @param userId
	 * @param category
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Decorate> getDecorateData(String userId, int category, int type) throws Exception;
	
	/**
	 * 查询用户有效的且佩戴的勋章(普通用户勋章)
	 * @param userId
	 * @return
	 */
	public List<Decorate> findHasAdornListOfCommonUser(String userId) throws Exception;

	/**
	 * 查询有效的新人勋章数量
	 * @param userId
	 */
	public int getNewManDecorateCount(String userId);
}
