package com.jiujun.shows.car.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.car.biz.CarParkIncomeRecordBiz;
import com.jiujun.shows.car.dao.CarParkIncomeRecordMapper;
import com.jiujun.shows.car.domain.CarParkIncomeRecord;
import com.jiujun.shows.car.service.ICarParkIncomeRecordService;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;


/**
 * 停车收益记录
 * @author shao.xiang
 * @date 2017-06-16
 */
public class CarParkIncomeRecordServiceImpl extends CommonServiceImpl<CarParkIncomeRecordMapper, CarParkIncomeRecord> implements ICarParkIncomeRecordService{

	@Resource
	private CarParkIncomeRecordBiz carParkIncomeRecordBiz;

	@Override
	public List<CarParkIncomeRecord> findCarParkIncomeRecord(String userId) throws Exception {
		return carParkIncomeRecordBiz.findCarParkIncomeRecord(userId);
	}
}
