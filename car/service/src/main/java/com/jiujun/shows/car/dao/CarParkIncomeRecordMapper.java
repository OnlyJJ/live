package com.jiujun.shows.car.dao;


import java.util.List;

import com.jiujun.shows.car.domain.CarParkIncomeRecord;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-20
 *
 */
public interface CarParkIncomeRecordMapper extends ICommonMapper<CarParkIncomeRecord> {
	List<CarParkIncomeRecord> findCarParkIncomeRecord(String userId);
}
