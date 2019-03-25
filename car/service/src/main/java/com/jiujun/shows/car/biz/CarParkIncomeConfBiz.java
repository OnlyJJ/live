package com.jiujun.shows.car.biz;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarParkIncomeConfMapper;
import com.jiujun.shows.car.domain.CarParkIncomeConf;


/**
 * Service -停车收益配置业务
 * @author shao.xiang
 * @date 2017-09-20
 */
@Service("carParkIncomeConfBiz")
public class CarParkIncomeConfBiz {
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private CarParkIncomeConfMapper carParkIncomeConfMapper;
	
	public CarParkIncomeConf getReciveGold(int carId) throws Exception {
		CarParkIncomeConf conf = carParkIncomeConfMapper.getCarParkIncomeConfByCarId(carId);
		return conf;
	}

	public CarParkIncomeConf getParkTime(int carId) throws Exception {
		CarParkIncomeConf conf = carParkIncomeConfMapper.getParkTimeByCarId(carId);
		return conf;
	}
}
