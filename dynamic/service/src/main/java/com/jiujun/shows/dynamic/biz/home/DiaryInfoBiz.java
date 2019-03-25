package com.jiujun.shows.dynamic.biz.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.base.service.IIpStoreService;
import com.jiujun.shows.base.service.ImageHelper;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.biz.user.DiaryFavourateBiz;
import com.jiujun.shows.dynamic.constant.CacheKey;
import com.jiujun.shows.dynamic.constant.CacheTime;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryCommentMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoAccusationMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoImgMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoImgSubMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfo;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoAccusation;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImg;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImgSub;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.enums.FavourateTypeEnum;
import com.jiujun.shows.dynamic.enums.LockTarget;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoImgService;
import com.jiujun.shows.dynamic.vo.AnchorInfo;
import com.jiujun.shows.dynamic.vo.DynamicInfoVo;
import com.jiujun.shows.dynamic.vo.ImagesVo;
import com.jiujun.shows.dynamic.vo.PublishVo;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.domain.UserAnchor;
import com.jiujun.shows.user.service.IDoUserCommonService;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;


/**
 * 动态信息相关业务
 * @author shao.xiang
 * @date 2017年6月2日
 *
 */
@Service("diaryInfoBiz")
public class DiaryInfoBiz {
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryInfoMapper diaryInfoMapper;
	
	@Resource
	private DiaryInfoImgMapper diaryInfoImgMapper;
	
	@Resource
	private DiaryInfoImgSubMapper diaryInfoImgSubMapper;
	
	@Resource
	private DiaryCommentMapper diaryCommentMapper;
	
	@Resource
	private DiaryFavourateBiz diaryFavourateBiz;
	
	@Resource
	private IDiaryInfoImgService diaryInfoImgService;

	@Resource
	private ImageHelper imageHelper;
	
	@Resource
	private DiaryInfoAccusationMapper diaryInfoAccusationMapper;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	@Resource
	private IDoUserCommonService doUserCommonService;
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	/** 上传图片参数json中的图片key */
	private static final String PARAMS_IMG_KEY = "img";
	/** 图片上传处理后，生成的大图 */
	private static final String BIGIMG_KEY = "bigImg";
	/** 图片上传处理后，生成的小图 */
	private static final String SMALLIMG_KEY = "smallImg";
	
	/** 默认客户端单次请求最大数量 -36条*/
	private static final int DB_CACHE_MAIN_SIZE = 36;
	/** 动态首页每条动态附带的评论数量，目前2条 */
	private static final int DIARY_COMMENT_COUNT = 2;
	/** 获取个人所有动态分页，每页数量36 */
	private static final int DIARY_PAGE_SIZE = 36;

	/**
	 * 获取动态首页信息
	 * @param page
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月4日
	 */
	public ServiceResult<JSONObject> getDynamicInfos(Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(page.getPageNum()) ||
				StringUtils.isEmpty(page.getPagelimit())) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		// 1、校验是否能正常使用动态功能
		// 当前首页不需要做校验，因为游客也可以看本功能，如果后续需要加这个需求，则启用这段代码
//		int powerStatus = this.diaryUserManagerService.checkUserPowerStatus(userId);
//		if(powerStatus != 0) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_8001);
//			throw e;
//		}
		// 2、查询首页数据
		int pageNum = Integer.parseInt(page.getPageNum().toString()); // 页码
		int pageSize = Integer.parseInt(page.getPagelimit()); // 每页数量
		if(pageSize > DB_CACHE_MAIN_SIZE){ // 单次请求最大容量（与客户端约定值）
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
//		int index = pageNum > 1 ? (pageNum -1) * pageSize : 0; // 从第几条开始，第一页从0，第二页从12.。。。以此类推
		JSONArray array = new JSONArray();
		JSONObject retData = new JSONObject();
		List<String> userIdList = new ArrayList<String>();
		userIdList.add(Constants.UN_SHOW_ROOM);//屏蔽百万直播吃饭
		
		try {
			// 动态集合，根据动态id，再查询评论
			List<DiaryInfo> allDiaryInfos = getAllDiaryInfoFromCache();
			
			// 拿到动态信息后，组装数据
			if(allDiaryInfos != null) {
				int allSize = allDiaryInfos.size(); // 总条数
				page.setCount(allSize + "");
				retData.put("page", page.buildJson());
//			int index = allSize / pageSize > 1 ? (allSize / pageSize -1) * pageSize + (allSize % pageSize) : (allSize % pageSize); // 反向遍历，取分页数据结束的位置
//			int endIndex = index - pageSize >0 ? index : 0; // 所取长度大于每页容量，则从实际取，否则，取全部
				int start = pageNum > 1 ? (allSize - (pageNum - 1)*pageSize - 1) : allSize -1; 
				int end = 0;
				if(allSize > pageNum*pageSize) {
					end = allSize - pageNum * pageSize;
				}
				log.error("### getDynamicInfos-查询动态首页，获取分页数据，第" + pageNum +"页,从第" + start + "开始，第" + end +"结束，总共" + allSize + "条!!");
				for(int i=start; i>=end;i--) { // 从index开始，倒序拿，每次发布动态时，往list追加
					JSONObject dataJson = new JSONObject();
					DynamicInfoVo vo = new DynamicInfoVo();
					DiaryInfo diaryInfo = allDiaryInfos.get(i);
					String userId = diaryInfo.getUserId();
					// 被屏蔽掉的
					if(userIdList.contains(userId)) {
						log.error("### getDynamicInfos-获取动态首页，屏蔽的用户，userId=" + userId);
						continue;
					}
					long diaryInfoId = diaryInfo.getId();
					vo.setDynamicId(diaryInfoId);
					// 1、从缓存中获取动态信息发布者的基本信息
					UserInfoVo user = this.userCacheInfoService.getInfoFromCache(userId, null);
					AnchorInfo anchor = new AnchorInfo();
					if(user == null) {
						log.error("### getDynamicInfos-获取动态首页，查询用户信息失败，userId=" + userId);
						continue;
					}
					String isAnchor = "0";
					String roomId = "";
					UserAnchor userAnchor = this.userAnchorService.getAnchorFromCacheByUserId(userId);
					if(userAnchor != null) {
						isAnchor = "1";
						roomId = userAnchor.getRoomId();
					}
					anchor.setRoomId(roomId);
					anchor.setIsAnchor(isAnchor);
					anchor.setUserId(userId);
					anchor.setUserLevel(user.getUserLevel());
					anchor.setAnchorLevel(user.getAnchorLevel());
					anchor.setNickName(user.getNickname());
					anchor.setIcon(user.getAvatar());
					dataJson.put("anchorinfo", anchor.buildJson());
					// 2、动态信息
					int publicFromType = 0;
					if(user.isIfOfficialUser()) {
						publicFromType = 2;
					} else {
						diaryInfo.getPublicFromType();
					}
					vo.setPublicFromType(publicFromType);
					vo.setDiaryTextInfo(diaryInfo.getDiaryTextInfo());
					vo.setPublicTime(diaryInfo.getPublicTime().getTime());
					vo.setTheme(diaryInfo.getTheme());
					// 获取图片
					List<ImagesVo> imageList = null;
					ServiceResult<List<ImagesVo>> imgsrt = diaryInfoImgService.getImagesVoFromCache(diaryInfoId);
					if(imgsrt.isSucceed()) {
						imageList = imgsrt.getData();
					}
					vo.setImagesVo(imageList);
					// 3、从缓存中获取该条动态的评论
					// 获取点赞总数
					int priseTotal = 0;
					ServiceResult<Integer> psrt = diaryFavourateBiz.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.prise.getValue());
					if(psrt.isSucceed()) {
						priseTotal = psrt.getData();
					}
					vo.setPrizeTotalNum(priseTotal);
					// 获取踩总数
					int betillteTotal = 0;
					ServiceResult<Integer> bsrt = diaryFavourateBiz.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.belittle.getValue());
					if(bsrt.isSucceed()) {
						betillteTotal = bsrt.getData();
					}
					vo.setBelittleTotalNum(betillteTotal);
					// 获取评论总数
					int commentTotal = getDiaryCommentTotalNumFromCache(diaryInfoId, userId);
					vo.setCommentTotalNum(commentTotal);
					// 返回客户端数据
					dataJson.put("dynamicInfovo", vo.buildJson());
					array.add(dataJson);
				}
				retData.put("list",array.toString());
				srt.setSucceed(true);
				srt.setData(retData);
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}

	/**
	 * 获取动态信息列表集，只包含动态信息，不包含其他信息，如评论等
	 * @return
	 * @throws Exception
	 */
	private List<DiaryInfo> getAllDiaryInfoFromCache() throws Exception {
		List<DiaryInfo> allDiaryInfos = null; 
		String allInfosKey = CacheKey.DYNAMIC_ALL_INFOS_CACHE;
		Object dataCache = MemcachedUtil.get(allInfosKey);
		if(dataCache != null) {
			allDiaryInfos = (List<DiaryInfo>) dataCache;
		} else {
			// 没有缓存，则从db中查询数据
			allDiaryInfos = diaryInfoMapper.getAllDiaryInfos();
			if(allDiaryInfos != null) { // 放入缓存
				MemcachedUtil.set(allInfosKey, allDiaryInfos);
			}
		}
		return allDiaryInfos;
	}

	/**
	 * 从缓存中获取用户个人所有动态信息
	 *  @param userId
	 * @return
	 * @throws Exception
	 */
	private List<DiaryInfo> getUserDiaryInfosFromCache(String userId) throws Exception {
		List<DiaryInfo> allDiaryInfos = null; 
		String allInfosKey = CacheKey.DYNAMIC_USER_ALL_INFOS_CACHE + userId;
		Object dataCache = MemcachedUtil.get(allInfosKey);
		if(dataCache != null) {
			allDiaryInfos = (List<DiaryInfo>) dataCache;
		} else {
			// 没有缓存，则从db中查询数据
			allDiaryInfos = diaryInfoMapper.getUserAllDiaryInfos(userId);
			if(allDiaryInfos != null) { // 放入缓存
				MemcachedUtil.set(allInfosKey, allDiaryInfos);
			}
		}
		return allDiaryInfos;
	}
	
	/**
	 * 发布动态
	 * @param userId
	 * @param isAnchor 是否主播，0-普通用户，1-主播
	 * @param pubIp 发布ip
	 * @param appType 客户端类型
	 * @param vo
	 * @param request
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月4日
	 */
	public ServiceResult<Boolean> publishDynamic(String userId, String isAnchor, String pubIp, int appType, PublishVo vo) {
		log.error("### publishDynamic-开始处理发布动态业务begin。。。userId=" + userId + ",isAnchor=" + isAnchor + ",appType=" + appType);
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		
		// 内容或者图片至少有一样不为空
		String diaryText = vo.getContent();
		JSONArray imgList = vo.getImageList();
		if(StringUtils.isEmpty(diaryText) && (imgList == null || imgList.size() ==0)) {
			srt.setResultCode(ErrorCode.ERROR_8005.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8005.getResultDescr());
			return srt;
		}
		// 字数限定
//		if(diaryText.length() >=120) {
//			diaryText = diaryText.substring(0,120);
//		}
		// 1、插入动态表
		Date now = new Date();
		String theme = vo.getTheme();
		try {
			String pubAdress = this.ipStoreService.getAddressByIp(pubIp);
			int publicFromType = Integer.parseInt(isAnchor);
			
			UserInfoVo user = this.userCacheInfoService.getInfoFromCache(userId, null);
			if(user != null && user.isIfOfficialUser()) {
				publicFromType = 2;
			}
			
			DiaryInfo info = new DiaryInfo();
			info.setUserId(userId);
			info.setTheme(theme);
			info.setDiaryTextInfo(diaryText);
			info.setPublicFromType(publicFromType);
			info.setPublicTime(now);
			info.setClientType(appType);
			info.setPublicIp(pubIp);
			info.setPublicAddress(pubAdress);
			info.setMgrState(0);
			info.setIsUserCancel(0);
			diaryInfoMapper.insert(info);
			
			// 2、处理图片
			long diaryInfoId = info.getId();
			// 开启一条线程处理图片(mydebug，这里可能需要改动)
			int ratioIndex = 1;
			if(imgList != null && imgList.size() >0) {
				for(int i=0;i<imgList.size();i++) {
					JSONObject json = imgList.getJSONObject(i);
					if(!json.containsKey(PARAMS_IMG_KEY)) {
						// my-todo-logger
						continue;
					}
					String tempImg = json.getString(PARAMS_IMG_KEY);
					JSONObject ret = imageHelper.creatFile(userId, diaryInfoId,ratioIndex,tempImg);
					if(ret != null) {
						DiaryInfoImg df = null;
						if(ret.containsKey(BIGIMG_KEY) && ret.get(BIGIMG_KEY) != null) {
							// 插入大图
							df = new DiaryInfoImg();
							df.setUploadtime(now);
							df.setDiaryinfoid(diaryInfoId);
							df.setUrl(ret.get(BIGIMG_KEY).toString());
							df.setRatioindex(ratioIndex);
							diaryInfoImgMapper.insert(df);
						}
						if(ret.containsKey(SMALLIMG_KEY) && ret.get(SMALLIMG_KEY) != null && df != null) {
							// 插入小图
							long diaryInfoImagId = df.getId();
							DiaryInfoImgSub sub = new DiaryInfoImgSub();
							sub.setDiaryinfoimgid(diaryInfoImagId);
							sub.setUrl(ret.get(SMALLIMG_KEY).toString());
							sub.setUploadtime(now);
							diaryInfoImgSubMapper.insert(sub);
						}
					}
					ratioIndex++;
				}
			}
			
			// 3、更新缓存,同步处理，防止并发错误
			String lockname = LockTarget.LOCK_DIARYINFO.getLockName();
			try {
				RdLock.lock(lockname);
				// 1、更新所有缓存
				String allInfosKey = CacheKey.DYNAMIC_ALL_INFOS_CACHE;
				List<DiaryInfo> allDiaryInfos = getAllDiaryInfoFromCache();
				if(allDiaryInfos == null || allDiaryInfos.size() == 0) {
					allDiaryInfos = new ArrayList<DiaryInfo>();
				}
				allDiaryInfos.add(info);
				MemcachedUtil.set(allInfosKey, allDiaryInfos);
				// 更新个人缓存
				String userInfosKey = CacheKey.DYNAMIC_USER_ALL_INFOS_CACHE + userId;
				List<DiaryInfo> userInfos = getUserDiaryInfosFromCache(userId);
				if(userInfos == null || userInfos.size() ==0) {
					userInfos = new ArrayList<DiaryInfo>();
				}
				userInfos.add(info);
				MemcachedUtil.set(userInfosKey, userInfos);
			} catch(Exception e) {
				log.error("获取锁异常，锁：lock_diaryinfo");
			} finally {
				RdLock.unlock(lockname);
			}
			try {
				// 发布动态后，处理用户的关联业务-勋章
				doUserCommonService.handleUserDecorateCommon(userId);
			} catch(Exception e) {
				log.error(e.getMessage(), e);
			}
			srt.setSucceed(true);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());	
		}
		log.error("### publishDynamic-处理发布动态业务结束！end");
		return srt;
	}

	/**
	 * 获取用户所有动态信息，D6调用
	 * @param userId 动态拥有者id
	 * @param page
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月5日
	 */
	public ServiceResult<JSONObject> getUserAllDiaryInfos(String userId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		JSONObject retData = new JSONObject();
		JSONArray array = new JSONArray();
		int pageSize = Integer.parseInt(page.getPagelimit());
		int pageNum = Integer.parseInt(page.getPageNum());
		
		try {
			List<DiaryInfo> list = getUserDiaryInfosFromCache(userId);
			if(list != null && list.size() >0) {
				UserInfoVo user = userCacheInfoService.getInfoFromCache(userId, null);
				if(user == null) {
					log.error("### getUserAllDiaryInfos-获取用户动态信息，查询用户信息失败，userId=" + userId);
				}
				
				int allSize = list.size();
				page.setCount(allSize + "");
				retData.put("page", page.buildJson());
				AnchorInfo anchor = new AnchorInfo();
				String isAnchor = "0";
				String roomId = "";
				UserAnchor userAnchor = userAnchorService.getAnchorFromCacheByUserId(userId);
				if(userAnchor != null) {
					isAnchor = "1";
					roomId = userAnchor.getRoomId();
				}
				anchor.setRoomId(roomId);
				anchor.setIsAnchor(isAnchor);
				anchor.setUserId(userId);
				anchor.setUserLevel(user.getUserLevel());
				anchor.setAnchorLevel(user.getAnchorLevel());
				anchor.setNickName(user.getNickname());
				anchor.setIcon(user.getAvatar());
				retData.put("anchorinfo", anchor.buildJson());
				
				int start = pageNum > 1 ? (allSize - (pageNum -1)*pageSize - 1) : allSize -1; 
				int end = 0;
				if(allSize > pageNum*pageSize) {
					end = allSize - pageNum * pageSize;
				}
				log.error("### getUserAllDiaryInfos-查询个人动态，获取分页数据，第" + pageNum +"页,从第" + start + "开始，第" + end +"结束，总共" + allSize + "条");
				// 倒序拿
				for(int i=start;i>=end;i--) {
					DiaryInfo diaryInfo = list.get(i);
					DynamicInfoVo vo = new DynamicInfoVo();
					long diaryInfoId = diaryInfo.getId();
					vo.setDynamicId(diaryInfoId);
					vo.setPublicTime(diaryInfo.getPublicTime().getTime());
					vo.setPublicFromType(diaryInfo.getPublicFromType());
					vo.setDiaryTextInfo(diaryInfo.getDiaryTextInfo());
					// 获取图片
					List<ImagesVo> imageList = null;
					ServiceResult<List<ImagesVo>> imgsrt = diaryInfoImgService.getImagesVoFromCache(diaryInfoId);
					if(imgsrt.isSucceed()) {
						imageList = imgsrt.getData();
					}
					vo.setImagesVo(imageList);
					// 获取点赞总数
					int priseTotal = 0;
					ServiceResult<Integer> psrt = diaryFavourateBiz.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.prise.getValue());
					if(psrt.isSucceed()) {
						priseTotal = psrt.getData();
					}
					vo.setPrizeTotalNum(priseTotal);
					// 获取踩总数
					int betillteTotal = 0;
					ServiceResult<Integer> bsrt = diaryFavourateBiz.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.belittle.getValue());
					if(bsrt.isSucceed()) {
						betillteTotal = bsrt.getData();
					}
					vo.setBelittleTotalNum(betillteTotal);
					// 获取评论总数
					int commentTotal = getDiaryCommentTotalNumFromCache(diaryInfoId, userId);
					vo.setCommentTotalNum(commentTotal);
					// 返回客户端数据
					array.add(vo.buildJson());
				}
				retData.put("dynamicInfovo", array.toString());
				srt.setSucceed(true);
				srt.setData(retData);
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}

	/**
	 * 用户删除动态（设置该动态为无效，并更新缓存）
	 * @param diaryInfoId动态id
	 * @param userId 
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月6日
	 */
	public ServiceResult<Boolean> deleteDiaryInfo(long diaryInfoId, String userId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		DiaryInfo diaryInfo = diaryInfoMapper.getObjectById(diaryInfoId);
		if(diaryInfo == null) {
			srt.setResultCode(ErrorCode.ERROR_8007.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8007.getResultDescr());
			return srt;
		}
		String diaryInfoUser = diaryInfo.getUserId();
		if(!userId.equals(diaryInfoUser)) {
			srt.setResultCode(ErrorCode.ERROR_8013.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8013.getResultDescr());
			return srt;
		}
		long id = diaryInfo.getId();
		int useStatus = 1;
		int flag = diaryInfoMapper.updateUseStatus(id, useStatus);
		
		// 更新缓存
		if(flag == 1) {
			// 个人缓存
			String userInfosKey = CacheKey.DYNAMIC_USER_ALL_INFOS_CACHE + userId;
			// 首页缓存
			String allInfosKey = CacheKey.DYNAMIC_ALL_INFOS_CACHE;
			RedisUtil.del(userInfosKey);
			RedisUtil.del(allInfosKey);
		}
		srt.setSucceed(true);
		return srt;
	}

	/**
	 * 举报动态
	 * @param diaryInfoId 动态id
	 * @param userId 用户id
	 * @author shao.xiang
	 * @date 2017-07-07
	 * @return 
	 */
	public ServiceResult<Boolean> reportDiaryInfo(long diaryInfoId, String userId, String content, String ip) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		DiaryInfo diaryInfo = diaryInfoMapper.getObjectById(diaryInfoId);
		if(diaryInfo == null) {
			srt.setResultCode(ErrorCode.ERROR_8007.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8007.getResultDescr());
			return srt;
		}
		Date now = new Date();
		String ownerId = diaryInfo.getUserId(); // 被举报
		DiaryInfoAccusation dca = new DiaryInfoAccusation();
		dca.setUserid(userId);
		dca.setDiaryinfoid(diaryInfoId);
		dca.setTouserid(ownerId);
		dca.setAccusationtime(now);
		dca.setAccusationinfo(content);
		dca.setAccusationip(ip);
		int mgrState = 0;
		dca.setMgrstate(mgrState);
		this.diaryInfoAccusationMapper.insert(dca);
		srt.setSucceed(true);
		return srt;
	}
	
	/**
	 * 获取用户某一动态的评论总数，直接从缓存中拿，因为每次评论时，都会更新该缓存
	 * @param diaryInfoId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private int getDiaryCommentTotalNumFromCache(long diaryInfoId, String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8015);
			throw e;
		}
		int retNum = 0;
		String commentTotalNumKey = CacheKey.DIARYINFO_COMMENT_TOTAL_CACHE + diaryInfoId;
		Object totalCache = RedisUtil.get(commentTotalNumKey);
		if(totalCache != null) {
			retNum = Integer.parseInt(totalCache.toString());
		} else {
			List<DiaryComment> commentList = diaryCommentMapper.getDiaryCommentByDiaryInfoId(diaryInfoId);
			if(commentList != null && commentList.size() > 0) {
				retNum = commentList.size();
				RedisUtil.set(commentTotalNumKey, String.valueOf(retNum), CacheTime.DIARYINFO_COMMENT_TIME);
			}
		}
		return retNum;
	}

}
