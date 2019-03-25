package com.lm.live.user.service;

import java.util.List;

import com.lm.live.framework.service.ServiceResult;
import com.lm.live.user.domain.InboxFiles;

/**
 * 用户收件箱服务
 * @author shao.xiang
 * @date 2017-06-11
 *
 */
public interface IInboxFilesService {
	
	/**
	 * 获取用户收件箱内容
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月11日
	 */
	public ServiceResult<List<InboxFiles>> getInboxFilesByUserId(String userId);
}
