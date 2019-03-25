package com.lm.live.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.service.IDecorateService;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;
import com.lm.live.user.constant.MCPrefix;
import com.lm.live.user.dao.UserInfoMapper;
import com.lm.live.user.domain.UserInfoDo;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.user.service.IUserAttentionService;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;

@Service
public class UserInfoServiceImpl extends CommonServiceImpl<UserInfoMapper, UserInfoDo> implements IUserInfoService {

	@Resource
	private IUserAttentionService userAttentionService;
	
	@Resource
	private IDecorateService decorateService;
	
	@Resource
	private IUserPetService userPetService;
	
	@Override
	public UserInfo getUserDetailInfo(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfo vo = dao.getUserDetailInfo(userId);
		if(vo == null) {
			throw new UserBizException(ErrorCode.ERROR_1000);
		}
		// 用户关注数
		int attentionCount = userAttentionService.getAttentionCounts(userId);
		vo.setAttentionCount(attentionCount);
		// 用户粉丝数
		int fans = userAttentionService.getFansounts(userId);
		vo.setFans(fans);
		return vo;
	}

	@Override
	public UserInfo getUserInfo(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfo vo = dao.getUserDetailInfo(userId);
		if(vo == null) {
			throw new UserBizException(ErrorCode.ERROR_1000);
		}
		// 获取用户正在使用的座驾
		PetVo pet = userPetService.getUsePet(userId);
		vo.setPetVo(pet);
		// 获取用户勋章列表
		List<Decorate> userDecorateList = decorateService.findListOfCommonUser(userId);
		vo.setDecorate(userDecorateList);
		return vo;
	}
	
	@Override
	public UserInfoDo getUserByUserId(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		return dao.getUserByUserId(userId);
	}
	
	@Override
	public UserInfoDo getByWechatUnionid(String unionid) {
		if(StringUtils.isEmpty(unionid)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		return dao.getByWechatUnionid(unionid);
	}

	@Override
	public void updateByUserId(UserInfoDo user) {
		if(user == null) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		dao.updateByUserId(user);
	}

	@Override
	public UserInfoDo getByQQConnectUnionid(String unionid) {
		if(StringUtils.isEmpty(unionid)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		return dao.getByQQConnectUnionid(unionid);
	}

	@Override
	public UserInfoDo getByWeiboUid(String uid) {
		if(StringUtils.isEmpty(uid)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		return dao.getByWeiboUid(uid);
	}

	@Override
	public UserInfoDo getUserInfoFromCache(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new UserBizException(ErrorCode.ERROR_101);
		}
		UserInfoDo vo = null;
		String cacheKey = MCPrefix.USER_INFODO_CACHE + userId;
		Object cacheObj = MemcachedUtil.get(cacheKey);
		if(cacheObj != null){
			vo = (UserInfoDo) cacheObj;
		}else{
			vo = dao.getUserByUserId(userId);
			if(vo != null){
				MemcachedUtil.set(cacheKey, vo, MCTimeoutConstants.DEFAULT_TIMEOUT_5M);
			}
		}
		return vo;
	}

	

}
