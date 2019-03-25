package com.jiujun.shows.dynamic.service.banner;

import java.util.List;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.dynamic.domain.banner.DiaryBanner;
import com.jiujun.shows.dynamic.vo.DiaryBannerVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 *  动态-banner
 * @author shao.xiang
 * @date 2017年9月25日
 *
 */
public interface IDiaryBannerService extends ICommonService<DiaryBanner> {

	/**
	 * 获取所有banner
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月22日
	 */
	ServiceResult<List<DiaryBannerVo>> getDiaryBannerList();
}
