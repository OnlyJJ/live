package com.lm.live.tools.pack.service;

import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.tools.pack.domain.UserPackageInHis;
/**
 * 加背包记录
 * @date 2017-07-02
 * @author shao.xiang
 */
public interface IUserPackageInHisService extends ICommonService<UserPackageInHis> {
	
	/**
	 * 加入账流水
	 * @param userId 用户userId
	 * @param giftId 礼物id
	 * @param inNum 加入包裹的礼物个数
	 * @param refId 关联的业务表id
	 * @param refDesc 关联业务表说明
	 * @param comment 备注
	 */
	public void addRecord(String userId,int giftId,int inNum,String refId,String refDesc,String comment);

}
