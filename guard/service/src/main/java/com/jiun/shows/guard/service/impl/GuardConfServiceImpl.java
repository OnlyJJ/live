package com.jiun.shows.guard.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.constant.Constants;
import com.jiujun.shows.guard.domain.GuardConf;
import com.jiujun.shows.guard.enums.ErrorCode;
import com.jiujun.shows.guard.exception.GuardBizException;
import com.jiujun.shows.guard.service.IGuardConfService;
import com.jiun.shows.guard.biz.GuardConfBiz;
import com.jiun.shows.guard.dao.GuardConfMapper;


/**
 * Service -守护配置表
 * @author shao.xiang
 * @date 2017-06-29
 */
public class GuardConfServiceImpl extends CommonServiceImpl<GuardConfMapper, GuardConf> implements IGuardConfService{

	private static final Logger log = Logger.getLogger(Constants.LOG_GUARD_SERVICE);
	
	@Resource
	private GuardConfBiz guardConfBiz;
	
	@Override
	public ServiceResult<GuardConf> getGuardConfData(int guardType, int priceType) {
		ServiceResult<GuardConf> srt = new ServiceResult<GuardConf>();
		srt.setSucceed(false);
		try {
			srt = guardConfBiz.getGuardConfData(guardType, priceType);
		} catch(GuardBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_12000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_12000.getResultDescr());
		}
		return srt;
	}

	@Override
	public ServiceResult<List<GuardConf>> getGuardConfAllDataCache() {
		ServiceResult<List<GuardConf>> srt = new ServiceResult<List<GuardConf>>();
		srt.setSucceed(false);
		try {
			srt = guardConfBiz.getGuardConfAllDataCache();
		} catch(GuardBizException e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_12000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_12000.getResultDescr());
		}
		return srt;
	}

}
