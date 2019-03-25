package com.jiujun.shows.dynamic.service.user;


import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.domain.user.DiaryFavourate;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态喜好表(点赞、踩)
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public interface IDiaryFavourateService extends ICommonService<DiaryFavourate>{
	/**
	 * 获取动态点赞或被踩总数
	 * @param diaryInfoId 动态id
	 * @param type 0-点赞，1-踩
	 * @return
	 * @author shao.xiang
	 * @date 2017-06-28
	 */
	ServiceResult<Integer> getPriseOrBelittleTotalFromCache(long diaryInfoId, int type);
	
	/**
	 * 处理用户点赞或者踩业务，每次处理完后，需要更新点赞或踩总数的缓存
	 * @param diaryInfoId 动态id
	 * @param type 0-点赞，1-踩
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-08
	 */
	ServiceResult<Boolean> handleUserLoveOrHate(long diaryInfoId, String userId, int type);
	
	/**
	 *  获取动态点赞用户信息
	 * @param diaryInfoId 动态id
	 * @param page 分页实体
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<JSONObject> getDiaryFavourateData(long diaryInfoId, Page page);
}
