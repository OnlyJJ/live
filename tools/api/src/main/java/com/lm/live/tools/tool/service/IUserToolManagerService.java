package com.lm.live.tools.tool.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.tool.domain.UserToolManager;

/**
 * 用户工具管理服务
 * @author shao.xiang
 * @date 2017-06-29
 */
public interface IUserToolManagerService extends ICommonService<UserToolManager>{
	/**
	 * 查询用户工具是否可以正常使用
	 * @param userId
	 * @return false-非正常状态，true-正常使用
	 * @throws Exception
	 */
	public boolean findUserToolStatus(String userId, int toolType) throws Exception;
}
