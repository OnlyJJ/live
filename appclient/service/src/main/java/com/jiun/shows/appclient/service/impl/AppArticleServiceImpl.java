package com.jiun.shows.appclient.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.appclient.biz.AppArticleBiz;
import com.jiun.shows.appclient.domain.AppArticle;
import com.jiun.shows.appclient.service.IAppArticleService;
import com.jiun.shows.appclient.vo.AppArticleVo;


/**
 * app公告服务实现
 * @author shao.xiang
 * @date 2017-09-15
 * 
 */
@Service("appArticleServiceImpl")
public class AppArticleServiceImpl implements IAppArticleService{

	@Resource
	private AppArticleBiz appArticleBiz;
	
	@Override
	public AppArticleVo getAppArticleVo() throws Exception {
		return appArticleBiz.getAppArticleVo();
	}

	@Override
	public List<AppArticle> getAppArticleCache() throws Exception {
		return appArticleBiz.getAppArticleCache();
	}

}
