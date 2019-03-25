package com.jiujun.shows.car.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-20
 *
 */
public interface SysCarMapper extends ICommonMapper<SysCarDo> {
	
	int countForGetListByPage(SysCarDo sysCarDo);
	
	List<SysCarDo> findUserCars(String userId);
	
//	List<SysCarDo> getListByPage(@Param("userId") String userId, @Param("type") int type, @Param("carName") String carName);
	
	List<SysCarDo> getListByPage(SysCarDo sysCarDo);
}
