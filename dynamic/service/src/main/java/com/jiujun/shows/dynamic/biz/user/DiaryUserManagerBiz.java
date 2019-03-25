package com.jiujun.shows.dynamic.biz.user;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.dynamic.constant.CacheKey;
import com.jiujun.shows.dynamic.constant.CacheTime;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryUserManagerMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryUserManager;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态用户管理表
 * @author shao.xiang
 * @date 2017-06-07
 */
@Service("diaryUserManagerBiz")
public class DiaryUserManagerBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryUserManagerMapper diaryUserManagerMapper;

	/**
	 * 校验用户使用动态功能的状态
	 * @return int-返回状态值，0-正常，1-禁止发布，2-只允许查看
	 * @param userId
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月24日
	 */
	public ServiceResult<Integer> checkUserPowerStatus(String userId) {
		ServiceResult<Integer> srt = new ServiceResult<Integer>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		int powerStatus = 0;
		// 先查缓存，缓存没有再查db
		String userKey = CacheKey.USER_POWER_STATUS_CACHE + userId;
		try {
			Object userCache = RedisUtil.get(userKey);
			if(userCache != null) {
				powerStatus = Integer.parseInt(userCache.toString());
			} else {
				DiaryUserManager userManager = diaryUserManagerMapper.getUserManagerByUserId(userId);
				if(userManager !=null) {
					powerStatus = userManager.getPowerstatus();
				}
				// 没有查询到记录，说明该用户是正常使用的
				RedisUtil.set(userKey, powerStatus, CacheTime.USER_POWER_STATUS_TIME);
			}
			srt.setSucceed(true);
			srt.setData(powerStatus);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
}
