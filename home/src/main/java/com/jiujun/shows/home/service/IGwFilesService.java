package com.jiujun.shows.home.service;

import java.util.List;

import com.jiujun.shows.home.domain.GwFiles;
/**
 * 公告服务
 * @author shao.xiang 
 * @date 2017-06-11
 */
public interface IGwFilesService {

	/**
	 * 获取首页banner需要展示的内容
	 */
	public List<GwFiles> getIndexPageBanner(GwFiles vo);

	/**
	 * 获取首页开机需要展示的内容
	 */
	public List<GwFiles> getStartImgs();

	public GwFiles getById(int appBanner);
}
