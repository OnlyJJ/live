package com.jiujun.shows.dynamic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfo;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface DiaryInfoMapper extends ICommonMapper<DiaryInfo> {
	
	int updateUseStatus(@Param("id")long id, @Param("useStatus")int useStatus);
	
	List<DiaryInfo> getAllDiaryInfos();
	
	List<DiaryInfo> getUserAllDiaryInfos(String userId);
}
