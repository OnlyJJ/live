package com.lm.live.base.dao;

import java.util.List;

import com.lm.live.base.domain.Province;
import com.lm.live.common.dao.ICommonMapper;

/**
 * 省份
 * @author shao.xiang
 * @Date 2017-06-04
 *
 */
public interface ProvinceMapper extends ICommonMapper<Province> {
	
	/***
	 * 获得所有记录
	 * @return
	 */
	List<Province> getListByAll();
	
}
