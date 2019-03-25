package com.jiujun.shows.dynamic.service.impl.banner;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.dynamic.biz.banner.DiaryBannerBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryBannerMapper;
import com.jiujun.shows.dynamic.domain.banner.DiaryBanner;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.banner.IDiaryBannerService;
import com.jiujun.shows.dynamic.vo.DiaryBannerVo;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态-banner
 * @author shao.xiang
 * @date 2017-06-04
 * 
 */
public class DiaryBannerServiceImpl extends CommonServiceImpl<DiaryBannerMapper, DiaryBanner>
	implements IDiaryBannerService{
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private DiaryBannerBiz diaryBannerBiz;
	
	@Override
	public ServiceResult<List<DiaryBannerVo>> getDiaryBannerList() {
		ServiceResult<List<DiaryBannerVo>> srt = new ServiceResult<List<DiaryBannerVo>>();
		srt.setSucceed(false);
		try {
			srt = diaryBannerBiz.getDiaryBannerList();
		} catch(DynamicBizException e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
}
