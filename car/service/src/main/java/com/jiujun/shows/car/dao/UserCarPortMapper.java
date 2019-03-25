package com.jiujun.shows.car.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiujun.shows.car.domain.UserCarPortDo;
import com.jiujun.shows.common.dao.ICommonMapper;

/**
 * 
 * @author shao.xiang
 * @Date 2017-09-20
 *
 */
public interface UserCarPortMapper extends ICommonMapper<UserCarPortDo> {
	UserCarPortDo getInUseUserCarPort(String userId);

	UserCarPortDo getUserCarPort(@Param("userId") String userId, @Param("carId") int carId);

	UserCarPortDo getInUseUserCarPort(@Param("userId") String userId, @Param("inUse") int inUseState);

	int countForGetListByPage(String userId);

	List<Map> getListByPage(String userId);

	Map getInUseDetailInfo(@Param("userId") String userId, @Param("inUse") int inUseState);

	UserCarPortDo getInUse(@Param("userId") String userId, @Param("inUse") int inUseState);
}
