package com.jiujun.shows.pay.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jiujun.shows.account.domain.UserAccountBook;
import com.jiujun.shows.account.service.IUserAccountService;
import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.Md5CommonUtils;
import com.jiujun.shows.common.utils.StrUtil;
import com.jiujun.shows.decorate.service.IDecoratePackageService;
import com.jiujun.shows.decorate.service.IDecorateService;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.pay.constant.Constants;
import com.jiujun.shows.pay.dao.PayXiaolajiaoOrderMapper;
import com.jiujun.shows.pay.dao.PayXiaolajiaoProductMapper;
import com.jiujun.shows.pay.domain.PayXiaolajiaoOrder;
import com.jiujun.shows.pay.domain.PayXiaolajiaoProduct;
import com.jiujun.shows.pay.enums.ErrorCode;
import com.jiujun.shows.pay.enums.PayChargeOrderTableEnum;
import com.jiujun.shows.pay.exception.PayBizException;
import com.jiujun.shows.tools.tool.service.IUserToolPackageService;


/**
 * 小辣椒支付
 * @author shao.xiang
 * @date 2017年8月22日
 *
 */
@Service("payXiaolajiaoOrderBiz")
public class PayXiaolajiaoOrderBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_PAY_SERVICE);
	
	@Resource
	private PayXiaolajiaoOrderMapper payXiaolajiaoOrderMapper;
	
	@Resource
	private PayXiaolajiaoProductMapper payXiaolajiaoProductMapper;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private IUserCarPortService userCarPortService;
	
	@Resource
	private IDecorateService decorateService;
	
	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IUserToolPackageService userToolPackageService;
	
	/**
	 * 小辣椒支付生成订单
	 * @param paramPayXiaolajiaoOrder
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月22日
	 */
	public ServiceResult<String> createPayXiaolajiaoOrder(PayXiaolajiaoOrder paramPayXiaolajiaoOrder) throws Exception {
		ServiceResult<String> srt = new ServiceResult<String>();
		log.info(String.format("###begin-createPayXiaolajiaoOrder,paramPayXiaolajiaoOrder:%s", JsonUtil.beanToJsonString(paramPayXiaolajiaoOrder)));
		if(paramPayXiaolajiaoOrder == null) {
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		String paramProductPacKId = paramPayXiaolajiaoOrder.getProductPackId();
		String userId = paramPayXiaolajiaoOrder.getUserId();
		String generateOrderIp = paramPayXiaolajiaoOrder.getGenerateOrderIp();
		String generateOrderAddr = paramPayXiaolajiaoOrder.getGenerateOrderAddr();
		if(StringUtils.isEmpty(paramProductPacKId) 
				|| StringUtils.isEmpty(generateOrderIp)
				|| StringUtils.isEmpty(userId)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		PayXiaolajiaoProduct payXiaolajiaoProduct = payXiaolajiaoProductMapper.getByProductPackId(paramProductPacKId);
		if(payXiaolajiaoProduct == null || payXiaolajiaoProduct.getInUse()==0){
			log.error(String.format("###小辣椒支付下单,商品不存在,productPackId:%s", paramProductPacKId));
			PayBizException e = new PayBizException(ErrorCode.ERROR_5028);
			log.error(e.getMessage(),e);
			throw e;
		}
		int defaultNewOrderStatus = PayChargeOrderTableEnum.OrderStatus.CREATE.getValue();
		String remark = "小辣椒支付-下订单";
		Date nowDate = new Date(); 
		String orderId =  StrUtil.getOrderId();
		PayXiaolajiaoOrder generatepayXiaolajiaoOrder = new PayXiaolajiaoOrder();
		generatepayXiaolajiaoOrder.setOrderId(orderId);
		generatepayXiaolajiaoOrder.setProductPackId(paramProductPacKId);
		generatepayXiaolajiaoOrder.setUserId(userId);
		generatepayXiaolajiaoOrder.setOrderStatus(defaultNewOrderStatus);
		generatepayXiaolajiaoOrder.setChargeTime(nowDate);
		generatepayXiaolajiaoOrder.setRemark(remark);
		generatepayXiaolajiaoOrder.setGenerateOrderIp(generateOrderIp);
		generatepayXiaolajiaoOrder.setGenerateOrderAddr(generateOrderAddr);
		payXiaolajiaoOrderMapper.insert(generatepayXiaolajiaoOrder);
		log.info(String.format("###end-createPayXiaolajiaoOrder,orderId:%s", orderId));
		srt.setSucceed(true);
		srt.setData(orderId);
		return srt;
	}

	/**
	 * 支付成功后回调业务处理
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2017年8月22日
	 */
	@Transactional(rollbackFor=Exception.class)
	public ServiceResult<Boolean> paySuccessNotifyXiaolajiaoOrder(String loginUserId,PayXiaolajiaoOrder paramPayXiaolajiaoOrder,
			String paramMd5Checkcode,String sessionId) throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		log.info(String.format("###begin-小辣椒支付-paySuccessNotifyXiaolajiaoOrder,loginUserId:%s,paramPayXiaolajiaoOrder:%s", loginUserId,JsonUtil.beanToJsonString(paramPayXiaolajiaoOrder)));
		if(StringUtils.isEmpty(loginUserId) || paramPayXiaolajiaoOrder==null){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		String paramOrderId = paramPayXiaolajiaoOrder.getOrderId();
		String paramProductPackId = paramPayXiaolajiaoOrder.getProductPackId();
		String notifyIp = paramPayXiaolajiaoOrder.getNotifyIp();
		String notifyAddr = paramPayXiaolajiaoOrder.getNotifyAddr();
		String userId = loginUserId;
		String roomId = null;
		Date nowDate = new Date();
		if(StringUtils.isEmpty(paramOrderId) 
				|| StringUtils.isEmpty(notifyIp)
				|| StringUtils.isEmpty(paramProductPackId) 
				|| StringUtils.isEmpty(paramMd5Checkcode)
				|| StringUtils.isEmpty(sessionId)){
			PayBizException e = new PayBizException(ErrorCode.ERROR_5025);
			log.error(e.getMessage(),e);
			throw e;
		}
		//与app端约定好校验码的生成方式:md5两次(userId+sessionId+orderId)
		String serverCheckcode = Md5CommonUtils.getMD5String(loginUserId+sessionId+paramOrderId);
		serverCheckcode = Md5CommonUtils.getMD5String(serverCheckcode);
		// 比较校验码
		if(!paramMd5Checkcode.toUpperCase().equals(serverCheckcode.toUpperCase())){
			String failRemark = String.format("###小辣椒支付通知-服务端比较md5校验码发现不相等,服务端校验码:%s,参数校验码:%s", serverCheckcode,paramMd5Checkcode);
			log.error(failRemark);
			update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
			srt.setSucceed(true);
			return srt;
		}
		
		PayXiaolajiaoOrder dbPayXiaolajiaoOrder= payXiaolajiaoOrderMapper.getByOrderId(paramOrderId);
		if(dbPayXiaolajiaoOrder == null){
			log.error(String.format("###小辣椒支付通知-订单不存在,paramOrderId:%s", paramOrderId));
			PayBizException e = new PayBizException(ErrorCode.ERROR_5012);
			log.error(e.getMessage(),e);
			throw e;
		}else{
			String dbPayXiaolajiaoOrderUserId = dbPayXiaolajiaoOrder.getUserId();
			if(!dbPayXiaolajiaoOrderUserId.equals(loginUserId)){
				String failRemark = String.format("###小辣椒支付通知-下单用户与通知用户不一致,下单用户:%s,通知用户:%s", dbPayXiaolajiaoOrderUserId,loginUserId);
				log.error(failRemark);
				update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
				srt.setSucceed(true);
				return srt;
			}
			
			int orderStatus = dbPayXiaolajiaoOrder.getOrderStatus();
			
			// 判断是否已经成功处理过
			if(orderStatus == PayChargeOrderTableEnum.OrderStatus.SUCCESS.getValue()){
				String failRemark = String.format("###小辣椒支付-支付通知,此订单(本地订单号orderId):"+paramOrderId+"已处理成功(无需重复处理)");
				log.error(failRemark);
				srt.setSucceed(true);
				return srt;
			}else{
				
				String dbProductPackId = dbPayXiaolajiaoOrder.getProductPackId();
				
				// 检验参数的paramProductPackId 与 订单的 dbProductPackId是否相等
				if(!paramProductPackId.equals(dbProductPackId)){
					String failRemark = String.format("###小辣椒支付-通知参数,paramProductPackId:%s与订单中的dbProductPackId:%s不一致", paramProductPackId,dbProductPackId);
					log.error(failRemark);
					update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
					srt.setSucceed(true);
					return srt;
				}
				
				PayXiaolajiaoProduct payXiaolajiaoProduct = payXiaolajiaoProductMapper.getByProductPackId(dbProductPackId);
				if(payXiaolajiaoProduct == null){
					String failRemark = String.format("###小辣椒支付-商品不存在,productPackId:%s", dbProductPackId);
					log.error(failRemark);
					update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
					srt.setSucceed(true);
					return srt;
				}
				
				if(payXiaolajiaoProduct.getInUse()==0){
					String failRemark = String.format("###小辣椒支付通知,商品已经停用,productPackId:%s", paramOrderId);
					log.error(failRemark);
					update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
					srt.setSucceed(true);
					return srt;
				}
				
				String generateOrderIp = dbPayXiaolajiaoOrder.getGenerateOrderIp();
				
				// 校验通知来源ip与生成订单时的ip是否一致
				String generateOrderAddr = dbPayXiaolajiaoOrder.getGenerateOrderAddr();
				if(!notifyIp.equals(generateOrderIp)){
					String failRemark = String.format("###小辣椒支付-生成订单ip与通知来源ip不一致,userId:%s,orderId:%s,generateOrderIp:%s,generateOrderAddr:%s,notifyIp:%s,notifyAddr:%s", userId,paramOrderId,generateOrderIp,generateOrderAddr,notifyIp,notifyAddr) ;
					log.error(failRemark);
					update2FailStatus(paramPayXiaolajiaoOrder,failRemark);
					srt.setSucceed(true);
					return srt;
				}
				
				int getGold = payXiaolajiaoProduct.getGetGold();
				if(getGold > 0){
					// 账户明细
					UserAccountBook userAccountBook = new UserAccountBook();
					userAccountBook.setUserId(userId);
					userAccountBook.setChangeGolds(getGold);
					userAccountBook.setSourceId(paramOrderId);
					userAccountBook.setSourceDesc("sourceId为t_pay_xiaolajiao_order的订单orderId");
					userAccountBook.setRecordTime(nowDate);
					userAccountBook.setContent("小辣椒充值");
					userAccountService.addGolds(userId, getGold, userAccountBook);
				}
				int carId = payXiaolajiaoProduct.getGetCarId();
				if(carId > 0 ){
					int type = 5;
					String comment = "小辣椒充值orderId:"+paramOrderId;
					boolean flag2taskList = false;
					// my-todo-servicer
					// 这里回滚事务不可取，因为已经支付成功，应该使用策略(消息通知)，在赠送道具或者礼物的时候，保证成功，而不是回滚
					ServiceResult<Boolean> csrt = userCarPortService.sysActiveGiveCar(userId, carId, type, roomId, comment, flag2taskList);
				}
				int getDecorateId = payXiaolajiaoProduct.getGetDecorateId();
				if(getDecorateId > 0){
					boolean isPeriod = true;
					int number = 1;
					int days = decorateService.getObjectById(getDecorateId).getLightenDay();
					// my-todo-servicer
					// 这里回滚事务不可取，因为已经支付成功，应该使用策略(消息通知)，在赠送道具或者礼物的时候，保证成功，而不是回滚
					ServiceResult<Boolean> dsrt = decoratePackageService.addPackage(userId, roomId, getDecorateId, isPeriod, number, days);
				}
				int getToolId = payXiaolajiaoProduct.getGetToolId();
				if(getToolId > 0 ){
					int num = 1;
					// my-todo-servicer
					// 这里回滚事务不可取，因为已经支付成功，应该使用策略(消息通知)，在赠送道具或者礼物的时候，保证成功，而不是回滚
					ServiceResult<Boolean> tsrt = userToolPackageService.addToolPackage(userId, getToolId, num);
				}
				dbPayXiaolajiaoOrder.setOrderStatus(PayChargeOrderTableEnum.OrderStatus.SUCCESS.getValue());
				dbPayXiaolajiaoOrder.setRemark("小辣椒支付-充值成功");
				dbPayXiaolajiaoOrder.setNotifyIp(notifyIp);
				dbPayXiaolajiaoOrder.setNotifyAddr(notifyAddr) ;
				dbPayXiaolajiaoOrder.setResultTime(nowDate);
				payXiaolajiaoOrderMapper.update(dbPayXiaolajiaoOrder);
				log.info(String.format("###小辣椒支付成功-已发完礼物,userId:%s,orderId:%s,generateOrderIp:%s,generateOrderAddr:%s,notifyIp:%s,notifyAddr:%s,加金币:%s", userId,paramOrderId,generateOrderIp,generateOrderAddr,notifyIp,notifyAddr,getGold));
			}
		}
		log.info(String.format("###end-小辣椒支付-paySuccessNotifyXiaolajiaoOrder,loginUserId:%s,paramPayXiaolajiaoOrder:%s", loginUserId,JsonUtil.beanToJsonString(paramPayXiaolajiaoOrder)));
		srt.setSucceed(true);
		return srt;
	}
	
	/**
	 * 更新小辣椒支付订单状态为失败
	 * @param paramOrderId
	 * @param failRemark
	 */
	private void update2FailStatus(PayXiaolajiaoOrder paramPayXiaolajiaoOrder,String failRemark) {
		try {
			if(paramPayXiaolajiaoOrder == null) {
				Exception e = new PayBizException(ErrorCode.ERROR_5025);
				throw e;
			}
			String paramOrderId = paramPayXiaolajiaoOrder.getOrderId();
			String notifyIp = paramPayXiaolajiaoOrder.getNotifyIp();
			String notifyAddr = paramPayXiaolajiaoOrder.getNotifyAddr();
			log.info(String.format("###begin-更新小辣椒支付订单为失败状态,orderId:%s,remark:%s", paramOrderId,failRemark));
			if(StringUtils.isEmpty(paramOrderId) || StringUtils.isEmpty(failRemark)){
				Exception e = new PayBizException(ErrorCode.ERROR_5025);
				throw e;
			}
			PayXiaolajiaoOrder dbPayXiaolajiaoOrder= payXiaolajiaoOrderMapper.getByOrderId(paramOrderId);
			if(dbPayXiaolajiaoOrder == null){
				log.info(String.format("###begin-更新小辣椒支付订单为失败状态,orderId:%s对应订单不存在", paramOrderId));
				Exception e = new PayBizException(ErrorCode.ERROR_5012);
				throw e;
			}
			int orderStatus = dbPayXiaolajiaoOrder.getOrderStatus();
			// 判断是否已经成功处理过,成功处理过则不给再更新成失败状态
			if(orderStatus == PayChargeOrderTableEnum.OrderStatus.SUCCESS.getValue()){
				log.error("###小辣椒支付-支付通知,此订单(本地订单号orderId):"+paramOrderId+"已处理成功过,不能再修改成失败状态");
				return;
			}
			Date nowDate = new Date();
			dbPayXiaolajiaoOrder.setRemark("小辣椒充值失败-"+failRemark);
			dbPayXiaolajiaoOrder.setOrderStatus(PayChargeOrderTableEnum.OrderStatus.FAIL.getValue());
			dbPayXiaolajiaoOrder.setNotifyIp(notifyIp);
			dbPayXiaolajiaoOrder.setNotifyAddr(notifyAddr);
			dbPayXiaolajiaoOrder.setResultTime(nowDate);
			payXiaolajiaoOrderMapper.update(dbPayXiaolajiaoOrder);
			log.info(String.format("###end-更新小辣椒支付订单为失败状态,orderId:%s,remark:%s", paramOrderId,failRemark));
		} catch(PayBizException e) {
			log.error(e.getMessage(),e);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
