package com.jiun.shows.guard.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.constant.Constants;
import com.jiujun.shows.guard.domain.GuardPayHis;
import com.jiujun.shows.guard.enums.ErrorCode;
import com.jiujun.shows.guard.exception.GuardBizException;
import com.jiujun.shows.guard.service.IGuardPayHisService;
import com.jiun.shows.guard.biz.GuardPayHisBiz;
import com.jiun.shows.guard.dao.GuardPayHisMapper;



/**
 * Service -守护购买记录表，用户每购买或续期一次，都增加一条记录
 * @author shao.xiang
 * @date 2017-06-29
 */
public class GuardPayHisServiceImpl extends CommonServiceImpl<GuardPayHisMapper, GuardPayHis> implements IGuardPayHisService{

	private static final Logger log = Logger.getLogger(Constants.LOG_GUARD_SERVICE);
	
	@Resource
	private GuardPayHisBiz guardPayHisBiz;
	
	@Override
	public ServiceResult<Boolean> payForGuard(String userId, String anchorId, String roomId,
			int workId, int guardType, int priceType) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = guardPayHisBiz.payForGuard(userId, anchorId, roomId, workId, guardType, priceType);
			srt.setSucceed(true);
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
