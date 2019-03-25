package com.jiujun.shows.dynamic.service.impl.user;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.dynamic.biz.user.DiaryUserManagerBiz;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryUserManagerMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryUserManager;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.user.IDiaryUserManagerService;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 动态用户管理
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public class DiaryUserManagerServiceImpl extends 
	CommonServiceImpl<DiaryUserManagerMapper, DiaryUserManager> implements IDiaryUserManagerService{

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource()
	private DiaryUserManagerBiz diaryUserManagerBiz;

	@Override
	public ServiceResult<Integer> checkUserPowerStatus(String userId) {
		ServiceResult<Integer> srt = new ServiceResult<Integer>();
		srt.setSucceed(false);
		try {
			srt = diaryUserManagerBiz.checkUserPowerStatus(userId);
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
