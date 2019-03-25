package com.jiujun.shows.car.biz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.CarParkIncomeRecordMapper;
import com.jiujun.shows.car.domain.CarParkIncomeRecord;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.mysql.jdbc.StringUtils;


/**
 * 停车收益记录业务
 * @author shao.xiang
 * @date 2017-09-20
 */
@Service("carParkIncomeRecordBiz")
public class CarParkIncomeRecordBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private CarParkIncomeRecordMapper carParkIncomeRecordMapper;

	public List<CarParkIncomeRecord> findCarParkIncomeRecord(String userId)
			throws Exception {
		if(StringUtils.isNullOrEmpty(userId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			log.error(e.getMessage(), e);
			throw e;
		}
		List<CarParkIncomeRecord> list = carParkIncomeRecordMapper.findCarParkIncomeRecord(userId);
		return list;
	}
}
