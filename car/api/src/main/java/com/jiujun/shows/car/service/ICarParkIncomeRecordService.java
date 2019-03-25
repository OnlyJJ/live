package com.jiujun.shows.car.service;

import java.util.List;

import com.jiujun.shows.car.domain.CarParkIncomeRecord;
import com.jiujun.shows.common.service.ICommonService;

/**
 * Service - 停车收益记录
 * @author shao.xiang
 * @date 2017-07-10
 */
public interface ICarParkIncomeRecordService extends ICommonService<CarParkIncomeRecord>{
	
	/** 
	 * 根据用户id查找当天停靠收益记录
	 * @return
	 * @throws Exception
	 */
	public List<CarParkIncomeRecord> findCarParkIncomeRecord(String userId) throws Exception;
}
