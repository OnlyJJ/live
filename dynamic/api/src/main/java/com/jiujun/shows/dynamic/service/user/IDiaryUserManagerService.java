package com.jiujun.shows.dynamic.service.user;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.dynamic.domain.user.DiaryUserManager;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 动态用户管理表
 * @author shao.xiang
 * @date 2017-06-03
 */
public interface IDiaryUserManagerService extends ICommonService<DiaryUserManager>{

	/**
	 * 校验用户使用动态功能的状态
	 * @return int-返回状态值，0-正常，1-禁止发布，2-只允许查看
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月24日
	 */
	ServiceResult<Integer> checkUserPowerStatus(String userId);
}
