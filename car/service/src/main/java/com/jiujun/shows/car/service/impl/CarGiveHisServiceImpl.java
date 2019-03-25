package com.jiujun.shows.car.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.car.biz.CarGiveHisBiz;
import com.jiujun.shows.car.dao.CarGiveHisMapper;
import com.jiujun.shows.car.domain.CarGiveHisDo;
import com.jiujun.shows.car.service.ICarGiveHisService;
import com.jiujun.shows.common.service.impl.CommonServiceImpl;


/**
 * @date 2017-06-16
 * @author shao.xiang
 */
public class CarGiveHisServiceImpl extends CommonServiceImpl<CarGiveHisMapper, CarGiveHisDo> implements ICarGiveHisService {

	@Resource
	private CarGiveHisBiz carGiveHisBiz;

	@Override
	public CarGiveHisDo getGiveHisByDate(String userId,int carId,int type, String dateStr) {
		return carGiveHisBiz.getGiveHisByDate(userId,carId,type,dateStr);
	}

}
