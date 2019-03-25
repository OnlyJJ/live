package com.jiujun.shows.dynamic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.dubbo.config.support.Parameter;
import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-04
 *
 */
public interface DiaryCommentMapper extends ICommonMapper<DiaryComment> {
	List<DiaryComment> getDiaryCommentByDiaryInfoId(long diaryInfoId);
	
	List<DiaryComment> findAllReCommentByTouser(@Param("diaryInfoId")long diaryInfoId, @Param("userId")String userId);
	
	List<DiaryComment> getUserCommentedByToUserId(String toUserId);
	
	DiaryComment getDiaryComment(long id);
}
