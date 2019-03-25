package com.jiujun.shows.dynamic.dao;


import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImgSub;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface DiaryInfoImgSubMapper extends ICommonMapper<DiaryInfoImgSub> {
	
	List<DiaryInfoImgSub> getImgByDiaryInfoImageId(long diaryInfoImageId);
}
