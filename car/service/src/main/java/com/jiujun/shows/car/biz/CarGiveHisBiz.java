package com.jiujun.shows.car.biz;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarGiveHisMapper;
import com.jiujun.shows.car.domain.CarGiveHisDo;


/**
 * 座驾赠送历史记录业务
 * @author shao.xiang
 * @date 2017-09-20
 */
@Service("carGiveHisBiz")
public class CarGiveHisBiz {
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	@Resource
	private CarGiveHisMapper carGiveHisMapper;

	public CarGiveHisDo getGiveHisByDate(String userId,int carId,int type, String dateStr) {
		
		return carGiveHisMapper.getGiveHisByDate(userId,carId,type,dateStr);
	}

}
