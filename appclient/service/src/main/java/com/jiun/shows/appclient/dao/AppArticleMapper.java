package com.jiun.shows.appclient.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.appclient.domain.AppArticle;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-19
 *
 */
public interface AppArticleMapper extends ICommonMapper<AppArticle> {
	List<AppArticle> getAppArticle();
}
