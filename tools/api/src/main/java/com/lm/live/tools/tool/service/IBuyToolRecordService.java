package com.lm.live.tools.tool.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.tool.domain.BuyToolRecord;
/**
 * @author shao.xiang
 * @date 2017-06-29
 */
public interface IBuyToolRecordService extends ICommonService<BuyToolRecord> {

	public void insertVo(String userId , int type);

}
