package com.jiujun.shows.car.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.car.domain.CarParkRecord;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-04
 *
 */
public interface CarParkRecordMapper extends ICommonMapper<CarParkRecord> {
	List<Map> findCarportByRoomId(String roomId);
	
	List<CarParkRecord> findCarportAll();
	
	void updateCarportEndStatus(String userId);
	
	CarParkRecord findCarportByUserId(String userId);
	
	CarParkRecord getCarportRecord(@Param("roomId") String roomId, @Param("carport") int carport);
}
