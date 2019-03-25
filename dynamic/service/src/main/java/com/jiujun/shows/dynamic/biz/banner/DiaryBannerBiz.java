package com.jiujun.shows.dynamic.biz.banner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.dynamic.dao.DiaryBannerMapper;
import com.jiujun.shows.dynamic.domain.banner.DiaryBanner;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.vo.DiaryBannerVo;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态-banner
 * @author shao.xiang
 * @date 2017-06-07
 */
@Service("diaryBannerBiz")
public class DiaryBannerBiz {
	
	@Resource
	private DiaryBannerMapper diaryBannerMapper;
	
	/**
	 * 获取所有banner
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年6月22日
	 */
	public ServiceResult<List<DiaryBannerVo>> getDiaryBannerList() {
		ServiceResult<List<DiaryBannerVo>> srt = new ServiceResult<List<DiaryBannerVo>>();
		srt.setSucceed(false);
		List<DiaryBannerVo> ret = new ArrayList<DiaryBannerVo>();
		try {
			List<DiaryBanner> list = diaryBannerMapper.getDiaryBannerList();
			if(list != null && list.size() >0) {
				Date now = new Date();
				for(DiaryBanner banner : list) {
					if(now.after(banner.getBegintime()) && now.before(banner.getEndtime())) {
						DiaryBannerVo vo = new DiaryBannerVo();
						vo.setTitle(banner.getTitle());
						vo.setTitleColor(banner.getTitlecolor());
						vo.setShowImgUrl(banner.getShowimgurl());
						vo.setLinkUrl(banner.getLinkurl());
						vo.setAppShowImgUrl(banner.getAppShowImgUrl());
						vo.setShowSort(banner.getShowSort());
						ret.add(vo);
					}
				}
			}
			srt.setSucceed(true);
			srt.setData(ret);
		} catch(Exception e) {
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
}
