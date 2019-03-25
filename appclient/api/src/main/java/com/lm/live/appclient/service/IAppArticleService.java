package com.lm.live.appclient.service;

import java.util.List;

import com.lm.live.appclient.domain.AppArticle;
import com.lm.live.appclient.vo.AppArticleVo;

/**
 * app公告服务
 * @author shao.xiang
 * @date 2017-06-18
 */
public interface IAppArticleService {
	
	/**
	 * 获取公告信息
	 * @return
	 * @throws Exception
	 */
	public AppArticleVo getAppArticleVo() throws Exception;
	
	/**
	 * 查询公告信息，按开始时间倒序,缓存5分钟
	 * @return
	 * @throws Exception
	 */
	public List<AppArticle> getAppArticleCache() throws Exception;
}
