package com.jiujun.shows.dynamic.service.home;


import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.domain.home.DiaryInfo;
import com.jiujun.shows.dynamic.vo.PublishVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * Service - 动态信息表
 * @author shao.xiang
 * @date 2017-06-05
 */
public interface IDiaryInfoService extends ICommonService<DiaryInfo>{
	
	/**
	 * 获取动态首页信息
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月2日
	 */
	ServiceResult<JSONObject> getDynamicInfos(Page page);
	
	/**
	 * 发布动态
	 * @param isAnchor 是否主播，0-普通用户，1-主播
	 * @param pubIp 发布ip
	 * @param appType 客户端类型
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月2日
	 */
	ServiceResult<Boolean> publishDynamic(String userId, String isAnchor, String pubIp, int appType, PublishVo vo);
	
	/**
	 * 获取用户所有动态信息，D6调用
	 * @param userId 动态拥有者id
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月5日
	 */
	ServiceResult<JSONObject> getUserAllDiaryInfos(String userId, Page page);

	/**
	 * 用户删除动态（设置该动态为无效，并更新缓存）
	 * @param diaryInfoId动态id
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月6日
	 */
	ServiceResult<Boolean> deleteDiaryInfo(long diaryInfoId, String userId);
	
	/**
	 * 举报动态
	 * @param diaryInfoId 动态id
	 * @return 
	 * @author shao.xiang
	 * @date 2017-06-08
	 */
	ServiceResult<Boolean> reportDiaryInfo(long diaryInfoId, String userId, String content, String ip);
	
	/**
	 * 发布动态权限校验(特殊接口，供切面入口)
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年6月8日
	 */
	void publishDiaryAccess(String userId) throws Exception;
}
