package com.lm.live.account.biz;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.account.enums.ErrorCode;
import com.lm.live.account.constant.Constants;
import com.lm.live.account.dao.UserAccountMapper;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.framework.service.ServiceResult;

/**
 * 账户业务处理类
 * @author shao.xiang
 * @date 2018年2月28日
 *
 */
@Service
public class UserAccountBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_ACCOUNT_SERVICE);
	
	@Resource
	private UserAccountMapper accountMapper;
	
	/**
	 * 查询用户账户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年2月28日
	 */
	public ServiceResult<UserAccount> getUserAccount(String userId) throws Exception {
		ServiceResult<UserAccount> srt = new ServiceResult<UserAccount>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_1001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_1001.getResultDescr());
			return srt;
		}
		UserAccount account = accountMapper.getUserAccount(userId);
		srt.setData(account);
		srt.setSucceed(true);
		return srt;
	}
	
	/**
	 * 送礼业务
	 * @param userId
	 * @param anchorId
	 * @param giftId
	 * @param sendNum
	 * @param roomId
	 * @param isFromPackage 是否从背包扣除
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年2月28日
	 */
	public ServiceResult<Boolean> doSendGiftBusiness(String userId, String anchorId, String roomId,
			int giftId, int sendNum, boolean isFromPackage) throws Exception {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(anchorId)
				|| StringUtils.isEmpty(roomId)) {
			srt.setResultCode(ErrorCode.ERROR_1001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_1001.getResultDescr());
			return srt;
		}
		
		// 校验礼物是否可以使用
		
		// 处理送礼
		synchronized(UserAccount.class) {
			// 区分用金币购买，还是扣背包
			if(isFromPackage) { // 从背包中扣除
				
			} 
			// 判断背包/金币是否足额支付
			
			// 扣背包/金币
			
			// 加用户经验
			
			// 加主播水晶
			
			// 加主播经验
			
			// 加金币流水记录
			
			// 发消息
		}
		return srt;
	}
}
