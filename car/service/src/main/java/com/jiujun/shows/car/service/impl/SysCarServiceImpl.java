package com.jiujun.shows.car.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.car.biz.SysCarBiz;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.SysCarMapper;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.car.service.ISysCarService;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 
 * @author shao.xiang
 * @date 2017-06-16
 */
public class SysCarServiceImpl extends CommonServiceImpl<SysCarMapper, SysCarDo> implements ISysCarService {

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private SysCarBiz sysCarBiz;
	
	@Override
	public ServiceResult<Page> pageFind(CarVo carVo, Page reqPageVo, String userId) {
		ServiceResult<Page> srt = new ServiceResult<Page>();
		System.err.println("debug.....1");
		srt.setSucceed(false);
		try {
			srt = sysCarBiz.pageFind(carVo, reqPageVo, userId);
		} catch(CarBizException e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(e.getErrorCode().getResultCode());
			srt.setResultMsg(e.getErrorCode().getResultDescr());
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		}
		return srt;
	}
	
}
