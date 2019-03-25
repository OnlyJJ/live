package com.jiujun.shows.dynamic.service.impl.user;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.biz.user.DiaryFavourateBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryFavourateMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryFavourate;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.user.IDiaryFavourateService;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态喜好表(点赞、踩)
 * @author shao.xiang
 * @date 2017-06-07
 */
public class DiaryFavourateServiceImpl extends CommonServiceImpl<DiaryFavourateMapper, DiaryFavourate> 
	implements IDiaryFavourateService{

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryFavourateBiz diaryFavourateServiceBiz;
	
	@Override
	public ServiceResult<Integer> getPriseOrBelittleTotalFromCache(long diaryInfoId, int type) {
		ServiceResult<Integer> srt = new ServiceResult<Integer>();
		srt.setSucceed(false);
		try {
			srt = diaryFavourateServiceBiz.getPriseOrBelittleTotalFromCache(diaryInfoId, type);
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

	@Override
	public ServiceResult<Boolean> handleUserLoveOrHate(long diaryInfoId, String userId, int type) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryFavourateServiceBiz.handleUserLoveOrHate(diaryInfoId, userId, type);
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

	@Override
	public ServiceResult<JSONObject> getDiaryFavourateData(long diaryInfoId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = diaryFavourateServiceBiz.getDiaryFavourateData(diaryInfoId, page);
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
