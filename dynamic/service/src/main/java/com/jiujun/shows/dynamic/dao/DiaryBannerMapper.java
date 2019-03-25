package com.jiujun.shows.dynamic.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.banner.DiaryBanner;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-04
 *
 */
public interface DiaryBannerMapper extends ICommonMapper<DiaryBanner> {
	/**
	 * 获取所有的banner
	 * @return
	 */
	List<DiaryBanner> getDiaryBannerList();
}
