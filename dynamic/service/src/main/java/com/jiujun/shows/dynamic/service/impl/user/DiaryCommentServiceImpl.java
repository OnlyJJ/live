package com.jiujun.shows.dynamic.service.impl.user;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.biz.user.DiaryCommentBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryCommentMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.user.IDiaryCommentService;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态评论
 * @author shao.xiang
 * @date 2017年6月9日
 *
 */
public class DiaryCommentServiceImpl extends
		CommonServiceImpl<DiaryCommentMapper, DiaryComment> implements IDiaryCommentService {

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryCommentBiz diaryCommentServiceBiz;

	@Override
	public ServiceResult<Boolean> publishComments(long diaryInfoId,
			String userId, String content, int appType, String pubIp,
			long toCommentId, int commentType) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryCommentServiceBiz.publishComments(diaryInfoId, userId,
					content, appType, pubIp, toCommentId, commentType);
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
	public ServiceResult<JSONObject> getDiaryCommentByPage(long diaryInfoId,
			Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = diaryCommentServiceBiz.getDiaryCommentByPage(diaryInfoId, page);
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
	public ServiceResult<Boolean> deleteDiaryCommet(long diaryInfoId,
			long commentId, String userId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryCommentServiceBiz.deleteDiaryCommet(diaryInfoId, commentId, userId);
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
	public ServiceResult<JSONObject> getUserDiaryMSG(String userId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		try {
			srt = diaryCommentServiceBiz.getUserDiaryMSG(userId, page);
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
	public ServiceResult<Boolean> reportDiaryComment(long diaryInfoId,
			long commentId, String userId, String content) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryCommentServiceBiz.reportDiaryComment(diaryInfoId,
					commentId, userId, content);
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
	public ServiceResult<Boolean> deleteUserDiaryAllMSG(String toUserId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryCommentServiceBiz.deleteUserDiaryAllMSG(toUserId);
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
	public ServiceResult<Boolean> updateCommentMsgFlag(String userId, long diaryInfoId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = diaryCommentServiceBiz.updateCommentMsgFlag(userId, diaryInfoId);
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
