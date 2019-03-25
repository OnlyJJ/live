package com.lm.live.appclient.service;

import com.lm.live.appclient.domain.AppNetproblem;
import com.lm.live.appclient.vo.AppSubmitPb;


/**
 * Service - app用户网络诊断数据
 * @author shao.xiang
 * @date 2017-06-18
 */
public interface IAppNetproblemService {
	
	/**
	 * 获取用户已提交的诊断数据，按时间排序，取最新一条
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	AppNetproblem findAppNetproblem(String userId) throws Exception;
	
	/**
	 * 处理app提交网络诊断结果
	 * @param vo
	 * @throws Exception
	 */
	void handleAppSubmitProblem(String userId, AppSubmitPb vo) throws Exception;
}
