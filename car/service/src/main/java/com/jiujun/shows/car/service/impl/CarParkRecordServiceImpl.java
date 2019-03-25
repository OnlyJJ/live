package com.jiujun.shows.car.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jiujun.shows.car.biz.CarParkRecordBiz;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarParkRecordMapper;
import com.jiujun.shows.car.domain.CarParkRecord;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.exceptions.CarBizException;
import com.jiujun.shows.car.service.ICarParkRecordService;
import com.jiujun.shows.car.vo.CarportVo;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 停车记录
 * @author shao.xiang
 * @date 2017-06-16
 */
public class CarParkRecordServiceImpl extends CommonServiceImpl<CarParkRecordMapper, CarParkRecord> implements ICarParkRecordService {
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	@Resource
	private CarParkRecordBiz carParkRecordBiz;
	
	@Override
	public ServiceResult<List<CarportVo>> findCarportByRoomId(String roomId) {
		ServiceResult<List<CarportVo>> srt = new ServiceResult<List<CarportVo>>();
		srt.setSucceed(false);
		try {
			srt = carParkRecordBiz.findCarportByRoomId(roomId);
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
	public ServiceResult<Boolean> grapCarport(String roomId, String anchorId, String userId,
			String anchorLev, int carport) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		try {
			srt = carParkRecordBiz.grapCarport(roomId, anchorId, userId, anchorLev, carport);
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
	public void handlCarportUsed() throws Exception {
		carParkRecordBiz.handlCarportUsed();
	}

}
