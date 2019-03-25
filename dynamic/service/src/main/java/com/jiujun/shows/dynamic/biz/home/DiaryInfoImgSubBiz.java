package com.jiujun.shows.dynamic.biz.home;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.dynamic.dao.DiaryInfoImgSubMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImgSub;


/**
 * Service -动态图片表(各种分辨率)
 */
@Service("diaryInfoImgSubBiz")
public class DiaryInfoImgSubBiz {
	
	@Resource
	private DiaryInfoImgSubMapper diaryInfoImgSubMapper;

	public List<DiaryInfoImgSub> getDiaryInfoImgSubList(long diaryInfoImageId) {
		return diaryInfoImgSubMapper.getImgByDiaryInfoImageId(diaryInfoImageId);
	}
}
