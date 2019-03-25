package com.jiujun.shows.dynamic.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.user.IDiaryUserManagerService;
import com.jiujun.shows.dynamic.vo.PublishVo;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.domain.UserAnchor;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;

@Component
@Aspect
public class DynamicAccessCheckAdvice {
	@Resource
	private IDiaryUserManagerService diaryUserManagerService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	/** 是否开启权限校验开关,true-开启，false-关闭*/
	private static final boolean USE_ACCESS_STATUS = true;
	
//	private static final String argNames = "execution(* com.jiujun.shows.dynamic.home.service.impl.DiaryInfoServiceImpl.publishDynamic(..))";
	
	@Pointcut("execution(* com.jiujun.shows.dynamic.service.impl.home.DiaryInfoServiceImpl.publishDynamic(..))")
	public void aspectPublish() {}
	
	@Pointcut("execution(* com.jiujun.shows.dynamic.service.impl.home.DiaryInfoServiceImpl.publishDiaryAccess(..))")
	public void aspectPublishBefor() {}
	
	@Pointcut("execution(* com.jiujun.shows.dynamic.service.impl.user.DiaryCommentServiceImpl.publishComments(..))")
	public void aspectPublishComment() {}
	
	@Before("aspectPublish() && args(userId,isAnchor,pubIp,appType,vo)")
	public void beforePublish(String userId, String isAnchor, String pubIp, int appType, PublishVo vo) throws Exception {
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，userId=" + userId + ",begin...");
		if(!USE_ACCESS_STATUS) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-是否开启校验发布动态权限-" + USE_ACCESS_STATUS);
			return;
		}
		// 校验是否被管理员拉黑了
		int powerStatus = 0;
		ServiceResult<Integer> srt = diaryUserManagerService.checkUserPowerStatus(userId);
		if(srt.isSucceed()) {
			powerStatus = srt.getData();
		}
		if(powerStatus == 1) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8002);
			throw e;
		}
		// 校验是否是主播
		boolean isAnchorFlag = false;
		UserAnchor anchor = userAnchorService.getAnchorFromCacheByUserId(userId);
		if(anchor != null) {
			isAnchorFlag = true;
		}
		// 校验是否满足发布条件
		UserInfoVo user = this.userCacheInfoService.getInfoFromCache(userId, null);
		if(user == null) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8017);
			throw e;
		}
		// 用户等级限制
		int userLevel = Integer.parseInt(user.getUserLevel().substring(1,user.getUserLevel().length()));
		int anchorLevel = Integer.parseInt(user.getAnchorLevel().substring(1,user.getAnchorLevel().length()));
		boolean ifOfficialUser = user.isIfOfficialUser();
		if(ifOfficialUser) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，官方人员，不设置拦截，userId=" + userId+",ifOfficialUser="+ifOfficialUser);
		} else {
			if(isAnchorFlag) { // 主播
				if(anchorLevel < 10) {
					Exception e = new DynamicBizException(ErrorCode.ERROR_8004);
					throw e;
				}
			} else { // 用户
//			if(userLevel < 10) {
				Exception e = new DynamicBizException(ErrorCode.ERROR_8002);
				throw e;
//			}
			}
		}
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，userId=" + userId + ",end");
	}
	
	@Before("aspectPublishBefor() && args(userId)")
	public void publishDiaryAccessBefor(String userId) throws Exception{
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，userId=" + userId + ",begin...");
		if(!USE_ACCESS_STATUS) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-是否开启校验发布动态权限-" + USE_ACCESS_STATUS);
			return;
		}
		// 校验是否被管理员拉黑了
		int powerStatus = 0;
		ServiceResult<Integer> srt = diaryUserManagerService.checkUserPowerStatus(userId);
		if(srt.isSucceed()) {
			powerStatus = srt.getData();
		}
		if(powerStatus == 1) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8002);
			throw e;
		}
		// 校验是否满足发布条件
		// 校验是否是主播
		boolean isAnchor = false;
		UserAnchor anchor = userAnchorService.getAnchorFromCacheByUserId(userId);
		if(anchor != null) {
			isAnchor = true;
		}
		UserInfoVo user = this.userCacheInfoService.getInfoFromCache(userId, null);
		if(user == null) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8017);
			throw e;
		}
		// 用户等级限制
		int userLevel = Integer.parseInt(user.getUserLevel().substring(1,user.getUserLevel().length()));
		int anchorLevel = Integer.parseInt(user.getAnchorLevel().substring(1,user.getAnchorLevel().length()));
		boolean ifOfficialUser = user.isIfOfficialUser();
		if(ifOfficialUser) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，官方人员，不设置拦截，userId=" + userId + ",ifOfficialUser=" + ifOfficialUser);
		} else {
			if(isAnchor) { // 主播
				if(anchorLevel < 10) {
					Exception e = new DynamicBizException(ErrorCode.ERROR_8004);
					throw e;
				}
			} else { // 用户
	//			if(userLevel < 10) {
					Exception e = new DynamicBizException(ErrorCode.ERROR_8002);
					throw e;
	//			}
			} 
		}
		
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布动态权限，userId=" + userId + ",ifOfficialUser="+ifOfficialUser+",end");
	}
	
	/**
	 * 发布评论权限校验
	 * @param userId
	 * @param isAnchor
	 * @param pubIp
	 * @param appType
	 * @param vo
	 * @throws Exception
	 */
	@Before("aspectPublishComment() && args(diaryInfoId, userId, content, appType, pubIp, toCommentId, commentType)")
	public void beforePublishComment(long diaryInfoId, String userId, String content, int appType, String pubIp,long toCommentId, int commentType) throws Exception {
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布评论权限，userId=" + userId + ",begin...");
		if(!USE_ACCESS_STATUS) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-是否开启校验发布动态权限-" + USE_ACCESS_STATUS);
			return;
		}
		// 校验是否被管理员拉黑了
		int powerStatus = 0;
		ServiceResult<Integer> srt = diaryUserManagerService.checkUserPowerStatus(userId);
		if(srt.isSucceed()) {
			powerStatus = srt.getData();
		}
		if(powerStatus == 1) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8002);
			throw e;
		}
		// 校验是否满足发布条件
		UserInfoVo user = this.userCacheInfoService.getInfoFromCache(userId, null);
		if(user == null) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8017);
			throw e;
		}
		// 用户等级限制
		int userLevel = Integer.parseInt(user.getUserLevel().substring(1,user.getUserLevel().length()));
		boolean ifOfficialUser = user.isIfOfficialUser();
		if(ifOfficialUser) {
			LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布评论权限，官方人员，不设置拦截，userId=" + userId+",ifOfficialUser="+ifOfficialUser);
		} else {
			if(userLevel < 1) {
				Exception e = new DynamicBizException(ErrorCode.ERROR_8014);
				throw e;
			}
		}
		LogUtil.log.error("### DynamicAccessCheckAdvice-校验发布评论权限，userId=" + userId + ",end");
	}
}
