package com.jiujun.shows.dynamic.service.impl.home;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.biz.home.DiaryInfoBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryInfoMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfo;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoService;
import com.jiujun.shows.dynamic.vo.PublishVo;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态信息表
 */
public class DiaryInfoServiceImpl extends CommonServiceImpl<DiaryInfoMapper, DiaryInfo>
	implements IDiaryInfoService{

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryInfoBiz diaryInfoBiz;
	
	@Override
	public ServiceResult<JSONObject> getDynamicInfos(Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = diaryInfoBiz.getDynamicInfos(page);
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
	public ServiceResult<Boolean> publishDynamic(String userId, String isAnchor, String pubIp,
			int appType, PublishVo vo) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryInfoBiz.publishDynamic(userId, isAnchor, pubIp, appType, vo);
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
	public ServiceResult<JSONObject> getUserAllDiaryInfos(String userId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = diaryInfoBiz.getUserAllDiaryInfos(userId, page);
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
	public ServiceResult<Boolean> deleteDiaryInfo(long diaryInfoId, String userId) {
		return diaryInfoBiz.deleteDiaryInfo(diaryInfoId, userId);
	}

	@Override
	public ServiceResult<Boolean> reportDiaryInfo(long diaryInfoId, String userId,
			String content, String ip) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryInfoBiz.reportDiaryInfo(diaryInfoId, userId, content, ip);
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
	public void publishDiaryAccess(String userId) throws Exception {
		// 由切面处理
	}

}
