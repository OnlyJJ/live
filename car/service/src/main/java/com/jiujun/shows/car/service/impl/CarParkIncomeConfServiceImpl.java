package com.jiujun.shows.car.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.car.biz.CarParkIncomeConfBiz;
import com.jiujun.shows.car.dao.CarParkIncomeConfMapper;
import com.jiujun.shows.car.domain.CarParkIncomeConf;
import com.jiujun.shows.car.service.ICarParkIncomeConfService;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;


/**
 * 停车收益配置
 * @author shao.xiang
 * @date 2017-06-16
 */
public class CarParkIncomeConfServiceImpl extends CommonServiceImpl<CarParkIncomeConfMapper, CarParkIncomeConf> implements ICarParkIncomeConfService{

	@Resource
	private CarParkIncomeConfBiz carParkIncomeConfBiz;
	
	@Override
	public CarParkIncomeConf getReciveGold(int carId) throws Exception {
		return carParkIncomeConfBiz.getReciveGold(carId);
	}

	@Override
	public CarParkIncomeConf getParkTime(int carId) throws Exception {
		return carParkIncomeConfBiz.getParkTime(carId);
	}
}
