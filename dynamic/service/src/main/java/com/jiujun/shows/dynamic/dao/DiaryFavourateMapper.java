package com.jiujun.shows.dynamic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryFavourate;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-04
 *
 */
public interface DiaryFavourateMapper extends ICommonMapper<DiaryFavourate> {
	
	int getPriseOrBelittleTotalByType(@Param("diaryInfoId")long diaryInfoId, @Param("type")int type);
	
	List<DiaryFavourate> getDiaryFavourate(@Param("diaryInfoId")long diaryInfoId, @Param("type")int type);
}
