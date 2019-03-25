package com.jiun.shows.guard.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.constant.Constants;
import com.jiujun.shows.guard.domain.GuardConf;
import com.jiujun.shows.guard.domain.GuardWorkConf;
import com.jiujun.shows.guard.enums.ErrorCode;
import com.jiujun.shows.guard.exception.GuardBizException;
import com.jiujun.shows.guard.service.IGuardWorkConfService;
import com.jiun.shows.guard.biz.GuardWorkConfBiz;
import com.jiun.shows.guard.dao.GuardWorkConfMapper;


/**
 * 守护配置服务
 * @author shao.xiang
 * @date 2017-06-30
 */
public class GuardWorkConfServiceImpl extends CommonServiceImpl<GuardWorkConfMapper, GuardWorkConf> implements IGuardWorkConfService{

	private static final Logger log = Logger.getLogger(Constants.LOG_GUARD_SERVICE);
	
	@Resource
	private GuardWorkConfBiz guardWorkConfBiz;

	@Override
	public ServiceResult<GuardWorkConf> getGuardWorkConfDataCache(String roomId) throws Exception {
		ServiceResult<GuardWorkConf> srt = new ServiceResult<GuardWorkConf>();
		srt.setSucceed(false);
		try {
			srt = guardWorkConfBiz.getGuardWorkConfDataCache(roomId);
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
