package com.jiujun.shows.dynamic.biz.user;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.constant.CacheKey;
import com.jiujun.shows.dynamic.constant.CacheTime;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryFavourateMapper;
import com.jiujun.shows.dynamic.domain.user.DiaryFavourate;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.enums.LockTarget;
import com.jiujun.shows.dynamic.vo.AnchorInfo;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;


/**
 * Service -动态喜好表(点赞、踩)
 */
@Service("diaryFavourateBiz")
public class DiaryFavourateBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private DiaryFavourateMapper mapper;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	/** 每页数量，36条 */
	private static final int COMMENT_PAGE_SIZE = 36;
	
	public ServiceResult<Integer> getPriseOrBelittleTotalFromCache(long diaryInfoId, int type) {
		ServiceResult<Integer> srt = new ServiceResult<Integer>();
		srt.setSucceed(false);
		int ret = 0;
		String cacheKey = CacheKey.DIARY_COMMENT_FAVOURATE_TOTAL_CACHE + diaryInfoId + "_" + type;
		try {
			Object cache = RedisUtil.get(cacheKey);
			if(cache != null) {
				ret = Integer.parseInt(cache.toString());
			} else {
				ret = mapper.getPriseOrBelittleTotalByType(diaryInfoId, type);
			}
			// 缓存不设置失效时间，每次用户操作时，需要对该缓存更新
			RedisUtil.set(cacheKey, String.valueOf(ret), CacheTime.DIARYINFO_FAVOURATE_TIME);
			srt.setSucceed(true);
			srt.setData(ret);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}

	/**
	 * 处理用户点赞或者踩业务，每次处理完后，需要更新点赞或踩总数的缓存
	 * @param diaryInfoId 动态id
	 * @param userId 用户id
	 * @param type 0-点赞，1-踩
	 * @author shao.xiang
	 * @date 2017-07-08
	 * @return
	 */
	public ServiceResult<Boolean> handleUserLoveOrHate(long diaryInfoId, String userId, int type) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
		}
		// 1、处理用户操作记录
		Date now = new Date();
		// 用户是否对本条动态操作过
		String userFavourateKey = CacheKey.DIARY_FAVOURATE_CACHE + userId + "_" + diaryInfoId + "_" + type;
		String lockname = LockTarget.LOCK_FAVOURATE_INFO.getLockName();
		try {
			Object userFavourateCache = RedisUtil.get(userFavourateKey);
			if(userFavourateCache != null) {
				log.error("### handleUserLoveOrHate-用户：userId="+ userId + ",已经操作过了，不重复记录，type(0:点赞,1:踩)=" + type);
				srt.setResultCode(ErrorCode.ERROR_8009.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_8009.getResultDescr());
				return srt;
			}
			RedisUtil.set(userFavourateKey, String.valueOf(type), CacheTime.DIARYINFO_FAVOURATE_TIME);
			// 2、插入记录
			DiaryFavourate po = new DiaryFavourate();
			po.setUserid(userId);
			po.setFavouratetype(type);
			po.setDiaryinfoid(diaryInfoId);
			po.setResulttime(now);
			// 3、更新缓存
			RdLock.lock(lockname);
			// 插入记录
			mapper.insert(po);
			// 处理点赞用户信息缓存
			this.handleFavourateListForCache(diaryInfoId, po);
			// 处理点赞总数缓存
			this.handleFavourateForCache(diaryInfoId, type);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
			return srt;
		} finally {
			RdLock.unlock(lockname);
		}
		srt.setSucceed(true);
		return srt;
	}

	/**
	 *  获取动态点赞用户信息
	 * @param diaryInfoId 动态id
	 * @param page 分页实体
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public ServiceResult<JSONObject> getDiaryFavourateData(long diaryInfoId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		JSONObject retData = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<DiaryFavourate> list = this.getDiaryFavourateFromCache(diaryInfoId);
			if(list != null && list.size() > 0) {
				int pageSize = Integer.parseInt(page.getPagelimit());
				int pageNum = Integer.parseInt(page.getPageNum());
				int allSize = list.size();
				page.setCount(allSize + "");
				retData.put("page", page.buildJson());
				int index = pageNum > 1 ? (pageNum -1) * pageSize : 0; // 从第几条开始
				for(int i=0; i<pageSize;i++) {
					if(allSize <= index) {
						log.error("### getDiaryFavourateData:获取动态的所有点赞用户信息，pageSize=" + pageSize + ",all=" + allSize + ",index=" + index);
						break;
					}
					String userId = "";
					String icon = "";
					String nickName = "";
					AnchorInfo info = new AnchorInfo();
					DiaryFavourate df = list.get(index);
					userId = df.getUserid();
					UserInfoVo vo = this.userCacheInfoService.getInfoFromCache(userId, null);
					if(vo == null) {
						log.error("### getDiaryFavourateData:获取用户信息失败，userId=" + userId);
						continue;
					}
					icon = vo.getAvatar();
					nickName = vo.getNickname();
					info.setIcon(icon);
					info.setUserId(userId);
					info.setNickName(nickName);
					array.add(info.buildJson());
					index++;
				}
				if(array != null) {
					retData.put("list", array.toString());
				}
			}
			srt.setSucceed(true);
			srt.setData(retData);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
	
	/**
	 * 从缓存中获取动态点赞信息
	 * @param diaryInfoId
	 * @return
	 * @throws Exception
	 */
	private List<DiaryFavourate> getDiaryFavourateFromCache(long diaryInfoId) throws Exception {
		List<DiaryFavourate> list = null;
		String prizeUsersKey = CacheKey.DIARY_ALL_FAVOURATE_USER_CACHE + diaryInfoId;
		Object prizeCache = RedisUtil.get(prizeUsersKey);
		if(prizeCache != null) {
			list = (List<DiaryFavourate>) prizeCache;
		} else {
			int type = 0; // 点赞
			list = mapper.getDiaryFavourate(diaryInfoId, type);
			if(list != null && list.size() >0) {
				RedisUtil.set(prizeUsersKey, list, CacheTime.DIARY_DEFAULT_CACHE_TIME);
			}
		}
		return list;
	}
	
	/**
	 * 处理点赞总数缓存，因为此方法是在insert之后，因此，总数++操作只需在有缓存时执行
	 * @param diaryInfoId
	 * @param type
	 * @throws Exception
	 */
	private void handleFavourateForCache(long diaryInfoId, int type) throws Exception {
		int ret = 0;
		String cacheKey = CacheKey.DIARY_COMMENT_FAVOURATE_TOTAL_CACHE + diaryInfoId + "_" + type;
		Object cache = RedisUtil.get(cacheKey);
		if(cache != null) {
			ret = Integer.parseInt(cache.toString());
			ret++;
		} else {
			ret = mapper.getPriseOrBelittleTotalByType(diaryInfoId, type);
		}
		// 缓存不设置失效时间，每次用户操作时，需要对该缓存更新
		RedisUtil.set(cacheKey, String.valueOf(ret), CacheTime.DIARYINFO_FAVOURATE_TIME);
	}
	
	/**
	 * 处理点赞用户信息缓存
	 * @param diaryInfoId
	 * @throws Exception
	 */
	private void handleFavourateListForCache(long diaryInfoId, DiaryFavourate po) throws Exception {
		List<DiaryFavourate> list = null;
		String prizeUsersKey = CacheKey.DIARY_ALL_FAVOURATE_USER_CACHE + diaryInfoId;
		Object prizeCache = RedisUtil.get(prizeUsersKey);
		if(prizeCache != null) {
			list = (List<DiaryFavourate>) prizeCache;
			list.add(po);
			RedisUtil.set(prizeUsersKey, list, CacheTime.DIARY_DEFAULT_CACHE_TIME);
		} else {
			int type = 0; // 点赞
			list = mapper.getDiaryFavourate(diaryInfoId, type);
			if(list != null && list.size() >0) {
				RedisUtil.set(prizeUsersKey, list, CacheTime.DIARY_DEFAULT_CACHE_TIME);
			}
		}
	}
}
