package com.jiujun.shows.dynamic.service.home;

import java.util.List;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImg;
import com.jiujun.shows.dynamic.vo.ImagesVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态图片表
 * @author shao.xiang
 * @date 2017-06-05
 */
public interface IDiaryInfoImgService extends ICommonService<DiaryInfoImg> {
	
	/**
	 * 根据动态id获取动态图片
	 * @param dirayInfoId
	 * @return
	 * @author shao.xiang
	 * @date 2017-06-08
	 */
	ServiceResult<List<ImagesVo>> getImagesVoFromCache(long diaryInfoId);
}
