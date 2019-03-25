package com.jiujun.shows.car.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;




import org.apache.log4j.Logger;

import com.jiujun.shows.car.biz.UserCarPortBiz;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.UserCarPortMapper;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.domain.UserCarPortDo;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 用户座驾服务实现
 * @author shao.xiang
 * @date 2017-06-18
 */
public class UserCarPortServiceImpl extends CommonServiceImpl<UserCarPortMapper, UserCarPortDo> implements IUserCarPortService {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private UserCarPortBiz userCarPortBiz;
	
	@Override
	public ServiceResult<Boolean> buyCar(String userId, int carId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		System.err.println("debug.....");
		try {
			srt = userCarPortBiz.buyCar(userId, carId);
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

	@Override
	public ServiceResult<Boolean> giveCar(String sendUserId, String receiveUserId, int carId, String sendComment) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.giveCar(sendUserId, receiveUserId, carId, sendComment);
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

	@Override
	public ServiceResult<Boolean> inUse(String userId, int carPortId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.inUse(userId, carPortId);
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

	@Override
	public ServiceResult<Page> pageFindUserCarPort(String userId, Page reqPageVo) {
		ServiceResult<Page> srt = new ServiceResult<Page>();
		srt.setSucceed(false);
		try {
			srt = userCarPortBiz.pageFindUserCarPort(userId, reqPageVo);
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

	@Override
	public ServiceResult<CarVo> getInUseUserCarPortDetailInfo(String userId) {
		ServiceResult<CarVo> srt = new ServiceResult<CarVo>();
		srt.setSucceed(false);
		try {
			srt = userCarPortBiz.getInUseUserCarPortDetailInfo(userId);
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

	@Override
	public ServiceResult<UserCarPortDo> getInUseUserCarPort(String userId) {
		ServiceResult<UserCarPortDo> srt = new ServiceResult<UserCarPortDo>();
		srt.setSucceed(false);
		try {
			srt = userCarPortBiz.getInUseUserCarPort(userId);
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

	@Override
	public ServiceResult<Boolean> cancelInUse(String userId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.cancelInUse(userId);
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

	@Override
	public ServiceResult<Boolean> addCar2User(String userId, int carId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.addCar2User(userId, carId);
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

	@Override
	public ServiceResult<Boolean> sysActiveGiveCar(String userId, int carId, int type,
			String roomId, String comment, boolean flag2taskList) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.sysActiveGiveCar(userId, carId, type, roomId, comment, flag2taskList);
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

	@Override
	public ServiceResult<List<SysCarDo>> findUserCars(String userId) throws Exception {
		ServiceResult<List<SysCarDo>> srt = new ServiceResult<List<SysCarDo>>();
		srt.setSucceed(false);
		try {
			srt = userCarPortBiz.findUserCars(userId);
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

	@Override
	public ServiceResult<Boolean> addCar2User(String userId, int carId, int guardType,
			int priceType, boolean isContinue, int validate, Date gkEndTime) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = userCarPortBiz.addCar2User(userId, carId, guardType, priceType, isContinue, validate, gkEndTime);
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
