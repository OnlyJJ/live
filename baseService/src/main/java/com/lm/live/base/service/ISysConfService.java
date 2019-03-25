package com.lm.live.base.service;


import com.lm.live.base.domain.SysConf;
import com.lm.live.common.service.ICommonService;
import com.lm.live.framework.service.ServiceResult;
/**
 * @author shao.xiang
 * @date 2017-06-18 
 */
public interface ISysConfService extends ICommonService<SysConf>{
	
	/**
	 * 根据code查询inUse=1的记录
	 * @param code
	 * @return
	 */
	ServiceResult<SysConf> getByCode(String code);
	
}
