package com.jiujun.shows.dynamic.service.impl.home;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.dynamic.biz.home.DiaryInfoImgBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryInfoImgMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImg;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoImgService;
import com.jiujun.shows.dynamic.vo.ImagesVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * Service -动态图片表
 * 
 * @author shao.xiang
 * @date 2017-06-05
 */
public class DiaryInfoImgServiceImpl extends
		CommonServiceImpl<DiaryInfoImgMapper, DiaryInfoImg> implements
		IDiaryInfoImgService {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private DiaryInfoImgBiz diaryInfoImgBiz;

	@Override
	public ServiceResult<List<ImagesVo>> getImagesVoFromCache(long diaryInfoId) {
		ServiceResult<List<ImagesVo>> srt = new ServiceResult<List<ImagesVo>>();
		srt.setSucceed(false);
		try {
			srt = diaryInfoImgBiz.getImagesVoFromCache(diaryInfoId);
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
