package com.jiujun.shows.car.service;

import com.jiujun.shows.car.domain.CarParkIncomeConf;
import com.jiujun.shows.common.service.ICommonService;

/**
 * Service - 停车收益配置
 * @author shao.xiang
 * @date 2017-06-10
 */
public interface ICarParkIncomeConfService extends ICommonService<CarParkIncomeConf> {
	/**
	 * 根据carId和停靠时长，获取对应的金币收益
	 * @param carId
	 * @param parkTime
	 * @return
	 * @throws Exception
	 */
	public CarParkIncomeConf getReciveGold(int carId) throws Exception;
	
	/**
	 * 根据carId获取对应的停靠时间
	 * @param carId
	 * @return
	 * @throws Exception
	 */
	public CarParkIncomeConf getParkTime(int carId) throws Exception;
}
