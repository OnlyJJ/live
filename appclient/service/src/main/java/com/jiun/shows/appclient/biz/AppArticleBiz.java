package com.jiun.shows.appclient.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.redis.RedisUtil;
import com.jiun.shows.appclient.constant.CacheTime;
import com.jiun.shows.appclient.constant.RedisKey;
import com.jiun.shows.appclient.dao.AppArticleMapper;
import com.jiun.shows.appclient.domain.AppArticle;
import com.jiun.shows.appclient.vo.AppArticleVo;


/**
 * app公告业务
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appArticleServiceImpl")
public class AppArticleBiz {
	
	@Resource
	private AppArticleMapper appArticleMapper;
	
	public AppArticleVo getAppArticleVo() throws Exception {
		// mydebug 先设置60秒，等正式更新了，让web在新加的时候删key，服务这边保持12小时吧
		String key = RedisKey.APP_ARTICLE_INFO_DB_CACHE;
		
		AppArticle appArticle = null;
		List<AppArticle> list = null;
		list = this.getAppArticleCache();
		if(list != null && list.size() >0) {
			AppArticleVo vo = new AppArticleVo();
			appArticle = list.get(0); // 取最新的第一条
			if(appArticle != null) {
				if(appArticle.getTitle() != null) {
					vo.setTitle(appArticle.getTitle());
				}
				if(appArticle.getContent() != null) {
					vo.setContent(appArticle.getContent());
				}
				if(appArticle.getUrl() != null) {
					vo.setUrl(appArticle.getUrl());
				}
			}
			return vo;
		}
		return null;
	}

	
	public List<AppArticle> getAppArticleCache() throws Exception {
		List<AppArticle> list = null;
		String key = RedisKey.APP_ARTICLE_INFO_DB_CACHE;
		Object obj = RedisUtil.get(key);
		if(obj != null) {
			list = (List<AppArticle>) obj;
		} else {
			list = updateAndSetAppArticleData();
		}
		return list;
	}
	
	private List<AppArticle> updateAndSetAppArticleData() {
		List<AppArticle> list = null;
		String key = RedisKey.APP_ARTICLE_INFO_DB_CACHE;
		RedisUtil.del(key);
		list = appArticleMapper.getAppArticle();
		if(list != null) {
			RedisUtil.set(key, list, CacheTime.APP_ARTICLE_INFO_DB_CACHE);
		}
		return list;
	}


}
