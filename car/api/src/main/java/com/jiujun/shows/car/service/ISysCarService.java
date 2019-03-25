package com.jiujun.shows.car.service;

import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 商城座驾
 * @date 2017-06-10
 * @author shao.xiang
 */
public interface ISysCarService extends ICommonService<SysCarDo> {
	
	/**
	 * 查询商城座驾信息
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月29日
	 */
	ServiceResult<Page> pageFind(CarVo carVo,Page reqPageVo,String userId);

}
