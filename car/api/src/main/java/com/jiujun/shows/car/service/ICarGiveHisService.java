package com.jiujun.shows.car.service;

import com.jiujun.shows.car.domain.CarGiveHisDo;
import com.jiujun.shows.common.service.ICommonService;

/**
 * 赠送座驾历史服务
 * @author shao.xiang
 * @date 2017-06-10
 */
public interface ICarGiveHisService extends ICommonService<CarGiveHisDo> {

	CarGiveHisDo getGiveHisByDate(String userId,int carId,int type,String dateStr);

}
