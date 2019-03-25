package com.jiujun.shows.car.service;

import java.util.Date;
import java.util.List;

import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.domain.UserCarPortDo;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 座驾服务
 * @author shao.xiang
 * @date 2017-07-10 
 */
public interface IUserCarPortService extends ICommonService<UserCarPortDo> {
	
	/**
	 * 获取当前正在使用的座驾详细信息
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-10
	 */
	ServiceResult<CarVo> getInUseUserCarPortDetailInfo(String userId);
	
	/**
	 * 购买座驾
	 * @author shao.xiang
	 * @date 2017-07-10
	 */
	ServiceResult<Boolean> buyCar(String userId,int carId);
	
	/**
	 * 用户之间赠送座驾
	 * @param sendUserId 赠送用户
	 * @param receiveUserId 收到用户
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<Boolean> giveCar(String sendUserId,String receiveUserId,int carId,String sendComment);
	
	
	/**
	 * 设置座驾为当前使用
	 * @param carPortId 座驾id
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<Boolean> inUse(String userId,int carPortId);
	
	/**
	 * 查询用户的座驾
	 * @param userId
	 * @param reqPageVo
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<Page> pageFindUserCarPort(String userId,Page reqPageVo);

	/**
	 * 取消使用
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<Boolean> cancelInUse(String userId);
	
	/**
	 * 把座驾加到用户车库
	 * @param userId
	 * @param carId
	 * @throws Excepiton
	 */
	ServiceResult<Boolean> addCar2User(String userId, int carId);
	
	/**
	 * 获取当前正在使用的座驾
	 * @param userId
	 * @return
	 */
	ServiceResult<UserCarPortDo> getInUseUserCarPort(String userId);

	/**
	 * 下发座驾
	 * @param userId
	 * @param carId
	 * @param type 类型,0:购买,1:用户赠送,2:系统赠送(连续登陆7天),3:系统赠送(蜜桃礼物),4:系统赠送(充值赠送),5:其他 6.:系统赠送(分享)
	 * @param roomId 房间号(1:不为空时用于群发消息;2:为空则不发房间消息)
	 * @param comment 说明
	 * @param flag2taskList 是否需要到任务页面领取(手动领取已取消，默认自动发)
	 * @return
	 */
	ServiceResult<Boolean> sysActiveGiveCar(String userId,int carId,int type,String roomId,String comment,boolean flag2taskList);
	
	/**
	 * 查询用户的座驾
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<List<SysCarDo>> findUserCars(String userId) throws Exception;
	
	/**
	 * 为用户加守护座驾
	 * @param isContinue 是否续期
	 * @param validate 座驾有效时间，与守护结束时间一致
	 * @param gkEndTime 守护最长结束时间
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月26日
	 */
	ServiceResult<Boolean> addCar2User(String userId, int carId, int guardType,
			int priceType, boolean isContinue, int validate, Date gkEndTime);
}
