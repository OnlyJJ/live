package com.lm.live.base.service;

import com.lm.live.common.service.ICommonService;
import com.lm.live.base.domain.Province;

/**
 * 省份服务
 * @author shao.xiang
 * @date 2018年3月10日
 *
 */
public interface IProvinceService extends ICommonService<Province> {
	/**
	 * 设置省市缓存
	 */
	public void getProvinceSetCache();
	
	/**
	 * 根据ip获取省份
	 * @param ip
	 * @return
	 */
	public String getProviceBy(String ip);

}
