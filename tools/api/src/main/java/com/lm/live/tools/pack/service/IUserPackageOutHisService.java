package com.lm.live.tools.pack.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.pack.domain.UserPackageOutHis;
/**
 * 扣背包记录
 * @author shao.xiang
 * @date 2017-07-02
 */
public interface IUserPackageOutHisService extends ICommonService<UserPackageOutHis> {
	

	/**
	 * 加支出流水
	 * @param userId 用户userId
	 * @param giftId 礼物id
	 * @param outNum 从包裹的扣除的礼物个数
	 * @param refId 关联的业务表id
	 * @param refDesc 关联业务表说明
	 * @param comment 备注
	 */
	public void addRecord(String userId,int giftId,int outNum,String refId,String refDesc,String comment);

}
