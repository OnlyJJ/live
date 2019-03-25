package com.jiujun.shows.car.service;

import java.util.List;

import com.jiujun.shows.car.domain.CarParkRecord;
import com.jiujun.shows.car.vo.CarportVo;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * Service - 停车记录
 * @author shao.xiang
 * @date 2017-07-10
 */
public interface ICarParkRecordService extends ICommonService<CarParkRecord> {
	
	/**
	 * 根据房间号查询该房间内是否有停靠的车位
	 * @return 已经停靠的车位，没有则为空
	 * @author shao.xiang
	 * @date 2017年7月15日
	 */
	ServiceResult<List<CarportVo>> findCarportByRoomId(String roomId);
	
	/**
	 * 抢车位
	 * @param roomId 主播房间
	 * @param anchorId 主播id
	 * @param userId 抢车位用户id
	 * @param anchorLev 主播等级
	 * @param carport 所抢车位号
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月15日
	 */
	ServiceResult<Boolean> grapCarport(String roomId,String anchorId,String userId,String anchorLev,int carport);
	
	/**
	 * 定时任务使用，处理车位停靠后，停用时间及金币收益
	 * @throws Exception
	 */
	void handlCarportUsed() throws Exception;
	
	
}
