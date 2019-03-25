package com.jiujun.shows.car.dao;


import com.jiujun.shows.car.domain.CarParkIncomeConf;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-20
 *
 */
public interface CarParkIncomeConfMapper extends ICommonMapper<CarParkIncomeConf> {
	CarParkIncomeConf getCarParkIncomeConfByCarId(int carId);
	
	CarParkIncomeConf getParkTimeByCarId(int carId);
	
	CarParkIncomeConf getParkTime(int carId);
	
	CarParkIncomeConf getReciveGold(int carId);
}
