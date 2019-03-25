package com.jiujun.shows.car.dao;


import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.car.domain.CarGiveHisDo;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-20
 *
 */
public interface CarGiveHisMapper extends ICommonMapper<CarGiveHisDo> {
	
	CarGiveHisDo getGiveHisByDate(@Param("userId") String userId, @Param("carId") int carId, @Param("type") int type, @Param("dateStr") String dateStr);
}
