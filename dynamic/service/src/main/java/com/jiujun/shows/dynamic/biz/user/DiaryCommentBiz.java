package com.jiujun.shows.dynamic.biz.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.redis.RdLock;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.SensitiveWordUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.constant.CacheKey;
import com.jiujun.shows.dynamic.constant.CacheTime;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryCommentAccusationMapper;
import com.jiujun.shows.dynamic.dao.DiaryCommentMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfo;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;
import com.jiujun.shows.dynamic.domain.user.DiaryCommentAccusation;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.enums.FavourateTypeEnum;
import com.jiujun.shows.dynamic.enums.LockTarget;
import com.jiujun.shows.dynamic.exception.DynamicBizException;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoImgService;
import com.jiujun.shows.dynamic.service.other.ImSendComponent;
import com.jiujun.shows.dynamic.service.user.IDiaryFavourateService;
import com.jiujun.shows.dynamic.vo.AnchorInfo;
import com.jiujun.shows.dynamic.vo.CommentVo;
import com.jiujun.shows.dynamic.vo.DynamicInfoVo;
import com.jiujun.shows.dynamic.vo.ImagesVo;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;

@Service("DiaryCommentBiz")
public class DiaryCommentBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);

	@Resource
	private DiaryCommentMapper diaryCommentServiceMapper;
	
	@Resource
	private DiaryCommentAccusationMapper diaryCommentAccusationMapper;
	
	@Resource
	private DiaryInfoMapper diaryInfoMapper;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	// my-todo，与im交互应该使用im服务，这里暂时先放到common中，等最后服务改造完毕，再改造im
	@Resource
	private ImSendComponent imSendComponent;
	
	@Resource
	private IDiaryInfoImgService diaryInfoImgService;
	
	@Resource
	private IDiaryFavourateService diaryFavourateService;
	
	
	/** 每页评论条数，36条 */
	private static final int COMMENT_PAGE_SIZE = 36;
	/** 动态评论字数限定，20个 */
	private static final int COMMENT_LENGTH = 20;
	/** 分页请求，单页最大条数，36条 */
	private static final int COMMENT_PAGENUM_MAX = 36;
	
	/**
	 * 从缓存中获取评论信息
	 * @param diaryInfoId
	 * @param size 说明：大于0时，返回size评论条数，默认按时间排序（越早越靠前）；当传入size参数为-1时，表示取所有的评论
	 * @return
	 * @throws Exception
	 */
	// mydebug-当用户撤销评论时，这里的缓存如何处理？已经排好序，如何找到删除的那条？如果用linkedHashMap，对评论进行map存储并排序。。。
	private List<DiaryComment> getDiaryCommentCache(long diaryInfoId, int size) throws Exception {
		List<DiaryComment> allCommentList = null;
		List<DiaryComment> commentList = null;
		String commentKey = CacheKey.DIARYINFO_COMMENT_CACHE + diaryInfoId;
		Object commentCache = RedisUtil.get(commentKey);
		if(commentCache != null) {
			allCommentList = (List<DiaryComment>) commentCache;
		} else {
			allCommentList = diaryCommentServiceMapper.getDiaryCommentByDiaryInfoId(diaryInfoId);
			if(allCommentList != null) {
				RedisUtil.set(commentKey, allCommentList, CacheTime.DIARYINFO_COMMENT_TIME);
			}
		}
		if(allCommentList != null && allCommentList.size() >0) {
			commentList = new ArrayList<DiaryComment>();
			for(int i=0;i<allCommentList.size();i++) {
				if(size == -1) {
					return allCommentList;
				}
				if(size > 0 && i >= size) {
					break;
				}
				DiaryComment diaryComment = allCommentList.get(i);
				commentList.add(diaryComment);
			}
		} 
		return commentList;
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
			List<DiaryComment> commentList = diaryCommentServiceMapper.getDiaryCommentByDiaryInfoId(diaryInfoId);
			if(commentList != null && commentList.size() > 0) {
				retNum = commentList.size();
				RedisUtil.set(commentTotalNumKey, String.valueOf(retNum), CacheTime.DIARYINFO_COMMENT_TIME);
			}
		}
		return retNum;
	}

	/**
	 * 发表评论
	 * @param diaryInfoId 动态id
	 * @param userId 用户
	 * @param content 评论内容
	 * @param appType 客户端类型
	 * @param pubIp 发表ip
	 * @param commentId 评论id
	 * @param toCommentId 当回复某条评论时使用，参数为回复的目标评论id
	 * @author shao.xiang
	 * @date 2017-07-08
	 * @return
	 * 
	 */
	public ServiceResult<Boolean> publishComments(long diaryInfoId, String userId,
			String content, int appType, String pubIp,
			long toCommentId, int commentType) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		// 1、根据id查询是否存在此动态
		DiaryInfo info = diaryInfoMapper.getObjectById(diaryInfoId);
		if(info == null) {
			srt.setResultCode(ErrorCode.ERROR_8007.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8007.getResultDescr());
			return srt;
		}
		String toUserId = "";
		
		// 2、插入评论
		DiaryComment po = new DiaryComment();
		if(commentType == 1) { // 客户端没有获取到值时，默认传0
			DiaryComment old = diaryCommentServiceMapper.getObjectById(toCommentId);
			if(old != null) {
				po.setDiarycommentid(toCommentId);
				toUserId = old.getUserid();
			}
		} else if(commentType == 0){
			toUserId = info.getUserId();
		} else {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		
		if(userId.equals(toUserId)) { // 自己不能评论自己
			srt.setResultCode(ErrorCode.ERROR_8010.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8010.getResultDescr());
			return srt;
		}
		Date now = new Date();
		String commentText = SensitiveWordUtil.replaceSensitiveWord(content); // 过滤内容
		if(commentText.length() > COMMENT_LENGTH) { // 字数限定
			commentText = commentText.substring(0, COMMENT_LENGTH);
		}
		po.setCommenttextinfo(commentText); 
		po.setDiaryinfoid(diaryInfoId);
		po.setUserid(userId);
		po.setIp(pubIp);
		po.setClienttype(appType);
//		po.setIsusercancel(0);
//		po.setMgrstate(0);
		po.setCommenttime(now);
		
		po.setToUserId(toUserId);
		po.setCommenttype(commentType);
		int readFlag = 0;
		po.setReadFlag(readFlag);
		
		// 3、根据动态，获取所有评论，然后往里追加
		// 这里需要同步处理
		String lockname = LockTarget.LOCK_COMMENTINFO.getLockName();
		try {
			RdLock.lock(lockname);
			diaryCommentServiceMapper.insert(po);
			// 更新动态评论缓存
			this.handleCommentListCache(diaryInfoId, po);
			
			// 更新消息缓存，缓存失效后，从db中重新查询
			this.handleCommentMsgListCache(toUserId, po);
			
			// 更新评论总数量缓存
			this.handleCommentTotalCache(diaryInfoId, info.getUserId());
			
			// 清楚个人消息缓存，封装返回客户端数据，缓存失效后，从消息缓存中重新封装
			for(int i=0;i<20;i++) {
				String retKey = CacheKey.DIARY_ALL_COMMENT_CACHE + toUserId + "_" + i;
				RedisUtil.del(retKey);
			}
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
			return srt;
		} finally {
			RdLock.unlock(lockname);
		}
		
		// 4、发布成功后，发送一条全站消息，通知客户端接收评论消息
		JSONObject jsonContent = new JSONObject();
		jsonContent.put("userId", toUserId);
		try {
			imSendComponent.sendMsgForDiary(jsonContent);
		} catch(Exception e) {
			log.error("###publishComments-发表评论后，发送通知失败,userId=" + userId + ",需要通知的用户:toUserId=" + toUserId);
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		srt.setSucceed(true);
		return srt;
	}

	/**
	 * 获取分页评论信息
	 * @param diaryInfoId 动态id 
	 * @param page 分页实体
	 * @author shao.xiang
	 * @date 2017-07-09
	 * @return
	 */
	public ServiceResult<JSONObject> getDiaryCommentByPage(long diaryInfoId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		int pageNum = Integer.parseInt(page.getPageNum());
		int pageSize = Integer.parseInt(page.getPagelimit());
		if(pageSize > COMMENT_PAGE_SIZE) {
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
		JSONObject retData = new JSONObject();
		
		// 获取所有评论缓存
		try {
			List<DiaryComment> allList = getDiaryCommentCache(diaryInfoId, -1);
			if(allList != null && allList.size() >0) {
				page.setCount(allList.size() + "");
				retData.put("page", page.buildJson());
				JSONArray comentArray = new JSONArray();
				int index = pageNum > 1 ? (pageNum -1) * pageSize : 0; // 从第几条开始
				for(int i=0; i<pageSize;i++) {
					if(allList.size() <= index) {
						log.error("### 获取动态的所有评论，pageSize=" + pageSize + ",all=" + allList.size() + ",index=" + index);
						break;
					}
					DiaryComment comment = allList.get(index);
					CommentVo vo = new CommentVo();
					String userId = comment.getUserid();
					UserInfoVo user = userCacheInfoService.getInfoFromCache(userId, null);
					vo.setUserId(userId);
					vo.setAvatar(user.getAvatar());
					vo.setNickname(user.getNickname());
					vo.setCommentId(comment.getId());
					vo.setCommentTime(comment.getCommenttime().getTime());
					if(comment.getCommenttype() == 1) {
						long toCommentId = comment.getDiarycommentid();
						DiaryComment toDiaryComment = diaryCommentServiceMapper.getObjectById(toCommentId);
						if(toDiaryComment != null) {
							UserInfoVo toCommentUser = userCacheInfoService.getInfoFromCache(toDiaryComment.getUserid(), null);
							vo.setToUserId(toDiaryComment.getUserid());
							vo.setToAvatar(toCommentUser.getAvatar());
							vo.setToNickname(toCommentUser.getNickname());
							vo.setToCommentId(toCommentId);
						}
					}
					vo.setContent(comment.getCommenttextinfo());
					comentArray.add(vo.buildJson());
					index++;
				}
				retData.put("commentvos", comentArray.toString());
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
	 * 用户撤回某一动态的某条评论
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
 	 * @param userId 用户id
 	 * @author shao.xiang
 	 * @date 2017-07-09
 	 * @return
	 */
	public ServiceResult<Boolean> deleteDiaryCommet(long diaryInfoId, long commentId, String userId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		DiaryInfo diaryInfo = this.diaryInfoMapper.getObjectById(diaryInfoId);
		if(diaryInfo == null) {
			srt.setResultCode(ErrorCode.ERROR_8007.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8007.getResultDescr());
			return srt;
		}
		DiaryComment comment = this.diaryCommentServiceMapper.getObjectById(commentId);
		if(comment == null) {
			srt.setResultCode(ErrorCode.ERROR_8008.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8008.getResultDescr());
			return srt;
		}
		String commentUser = comment.getUserid();
		if(!userId.equals(commentUser)) {
			srt.setResultCode(ErrorCode.ERROR_8012.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8012.getResultDescr());
			return srt;
		}
		log.error("###deleteDiaryCommet:" + comment.getUserid() + "," + comment.getReadFlag());
		int useStatus = 1;
		comment.setIsusercancel(useStatus);
		this.diaryCommentServiceMapper.update(comment);
		
		// 更新缓存
		// 动态评论总数缓存
		String diaryUser = diaryInfo.getUserId();
		int num = 0;
		String lockname = LockTarget.LOCK_COMMENTALL.getLockName();
		try {
			RdLock.lock(lockname);
			String commentTotalNumKey = CacheKey.DIARYINFO_COMMENT_TOTAL_CACHE + diaryInfoId;
			Object totalCache = RedisUtil.get(commentTotalNumKey);
			if(totalCache != null) {
				num = Integer.parseInt(totalCache.toString());
				if(num >0) {
					num--;
				}
				RedisUtil.set(commentTotalNumKey, String.valueOf(num), CacheTime.DIARYINFO_COMMENT_TIME);
			} 
			// 评论
			String commentKey = CacheKey.DIARYINFO_COMMENT_CACHE + diaryInfoId;
			RedisUtil.del(commentKey);
			srt.setSucceed(true);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			RdLock.unlock(lockname);
		}
		return srt;
	}

	/**
	 * 获取用户收到的评论消息
	 * @param userId
	 * @author shao.xiang
	 * @date 2017-07-10
	 * @return
	 */
	public ServiceResult<JSONObject> getUserDiaryMSG(String toUserId, Page page) {
		ServiceResult<JSONObject> srt = new ServiceResult<JSONObject>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(toUserId)) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		// 从缓存中获取当前用户收到的所有评论
		int pageNum = Integer.parseInt(page.getPageNum());
		int pageSize = Integer.parseInt(page.getPagelimit());
		if(pageSize > COMMENT_PAGENUM_MAX) {
			srt.setResultCode(ErrorCode.ERROR_8015.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8015.getResultDescr());
			return srt;
		}
		JSONObject retData = new JSONObject();
		String retKey = CacheKey.DIARY_ALL_COMMENT_CACHE + toUserId + "_" + pageNum;
		try {
			Object retCache = RedisUtil.get(retKey);
			if(retCache != null) {
				retData = (JSONObject) retCache;
			} else {
				JSONArray array = new JSONArray();
				List<DiaryComment> msgComment = getUserCommentMsgFromCache(toUserId);
				if(msgComment != null && msgComment.size() >0) {
					page.setCount(msgComment.size() + "");
					retData.put("page", page.buildJson());
					int allSize = msgComment.size();
					int start = pageNum > 1 ? (allSize - (pageNum - 1)*pageSize - 1) : allSize -1; 
					int end = 0;
					if(allSize > pageNum*pageSize) {
						end = allSize - pageNum * pageSize;
					}
					for(int i=start; i>=end;i--) { // 倒序获取，最新评论排最前
						JSONObject json = new JSONObject();
						DiaryComment comment = msgComment.get(i);
						CommentVo vo = new CommentVo();
						String userId = comment.getUserid();
						vo.setContent(comment.getCommenttextinfo());
						vo.setReadFlag(comment.getReadFlag());
						vo.setCommentId(comment.getId());
						vo.setCommentTime(comment.getCommenttime().getTime());
						vo.setReadFlag(comment.getReadFlag());
						
						UserInfoVo user = userCacheInfoService.getInfoFromCache(userId, null);
						vo.setNickname(user.getNickname());
						vo.setAvatar(user.getAvatar());
						vo.setUserId(userId);
						
						long diaryInfoId = comment.getDiaryinfoid();
						DiaryInfo diaryInfo = diaryInfoMapper.getObjectById(diaryInfoId);
						if(diaryInfo == null) {
							log.error("###getUserDiaryMSG-获取动态信息失败，info为null");
							continue;
						}
						DynamicInfoVo info = new DynamicInfoVo();
						info.setDynamicId(diaryInfoId);
						info.setPublicFromType(diaryInfo.getPublicFromType());
						info.setDiaryTextInfo(diaryInfo.getDiaryTextInfo());
						info.setPublicTime(diaryInfo.getPublicTime().getTime());
						info.setTheme(diaryInfo.getTheme());
						info.setDiaryTextInfo(diaryInfo.getDiaryTextInfo());
						List<ImagesVo> imageList = null;
						ServiceResult<List<ImagesVo>> imgsrt = diaryInfoImgService.getImagesVoFromCache(diaryInfoId);
						if(imgsrt.isSucceed()) {
							imageList = imgsrt.getData();
						}
						info.setImagesVo(imageList);
						int priseTotal = 0;
						ServiceResult<Integer> psrt = diaryFavourateService.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.prise.getValue());
						if(psrt.isSucceed()) {
							priseTotal = psrt.getData();
						}
						info.setPrizeTotalNum(priseTotal);
						// 获取踩总数
						int betillteTotal = 0;
						ServiceResult<Integer> bsrt = diaryFavourateService.getPriseOrBelittleTotalFromCache(diaryInfoId, FavourateTypeEnum.Type.belittle.getValue());
						if(bsrt.isSucceed()) {
							betillteTotal = bsrt.getData();
						}
						info.setBelittleTotalNum(betillteTotal);
						// 获取评论总数
						int commentTotal = this.getDiaryCommentTotalNumFromCache(diaryInfoId, userId);
						info.setCommentTotalNum(commentTotal);
						
						// 动态发布者
						AnchorInfo anchor = new AnchorInfo();
						String diaryInfoUserId = diaryInfo.getUserId();
						UserInfoVo diaryInfoUser = userCacheInfoService.getInfoFromCache(diaryInfoUserId, null);
						anchor.setUserId(diaryInfoUserId);
						anchor.setUserLevel(diaryInfoUser.getUserLevel());
						anchor.setAnchorLevel(diaryInfoUser.getAnchorLevel());
						anchor.setNickName(diaryInfoUser.getNickname());
						anchor.setIcon(diaryInfoUser.getAvatar());
						
						json.put(anchor.getShortName(), anchor.buildJson());
						json.put(vo.getShortName(), vo.buildJson());
						json.put(info.getShortName(), info.buildJson());
						array.add(json);
					}
					retData.put("list", array.toString());
					
					// 放入缓存
					RedisUtil.set(retKey, retData);
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
	 * 举报评论
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
	 * @param userId 举报用户
	 * @author shao.xiang
	 * @date 2017-07-07
	 */
	public ServiceResult<Boolean> reportDiaryComment(long diaryInfoId, long commentId, String userId, String content) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		try {
			DiaryInfo diaryInfo = this.diaryInfoMapper.getObjectById(diaryInfoId);
			if(diaryInfo == null) {
				srt.setResultCode(ErrorCode.ERROR_8007.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_8007.getResultDescr());
				return srt;
			}
			DiaryComment comment = this.diaryCommentServiceMapper.getObjectById(commentId);
			if(comment == null) {
				srt.setResultCode(ErrorCode.ERROR_8008.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_8008.getResultDescr());
				return srt;
			}
			String commentUser = comment.getUserid();
			if(userId.equals(commentUser)) {
				srt.setResultCode(ErrorCode.ERROR_8011.getResultCode());
				srt.setResultMsg(ErrorCode.ERROR_8011.getResultDescr());
				return srt;
			}
			Date now = new Date();
			String ownerId = comment.getUserid(); // 被举报
			DiaryCommentAccusation dca = new DiaryCommentAccusation();
			dca.setUserid(userId);
			dca.setTouserid(ownerId);
			dca.setAccusationtime(now);
			dca.setCommentid(commentId);
			dca.setAccusationinfo(content);
			int mgrState = 0;
			dca.setMgrstate(mgrState);
			this.diaryCommentAccusationMapper.insert(dca);
			srt.setSucceed(true);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}

	/**
	 * 获取用户动态或评论被评论的所有信息
	 * @param toUserId 被评论的用户id
	 * @param readFlag 状态，0-未读，1-已读，2-清空
	 * @return
	 * @throws Exception
	 */
	private List<DiaryComment> getUserCommentMsgFromCache(String toUserId) throws Exception {
		if(StringUtils.isEmpty(toUserId)) {
			Exception e = new DynamicBizException(ErrorCode.ERROR_8006);
			throw e;
		}
		List<DiaryComment> msgComment = null;
		String msgKey = CacheKey.DIARY_COMMENT_MSG_CACHE + toUserId;
		Object msgCache = RedisUtil.get(msgKey);
		if(msgCache != null) {
			msgComment = (List<DiaryComment>) msgCache;
		} else {
			msgComment = new ArrayList<DiaryComment>();
			// 缓存失效后，从db中获取数据
			List<DiaryComment>  coments = this.diaryCommentServiceMapper.getUserCommentedByToUserId(toUserId);
			if(coments != null && coments.size() >0) {
				msgComment = coments;
				RedisUtil.set(msgKey, msgComment, CacheTime.DIARYINFO_COMMENT_MSG_TIME);
			}
		}
		return msgComment;
	}

	/**
	 * 清空个人所有动态消息
	 * @param toUserId 动态消息接受者
	 * @author shao.xiang
	 * @date 2017-07-11
	 * @return
	 */
	public ServiceResult<Boolean> deleteUserDiaryAllMSG(String toUserId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(toUserId)) {
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		// 修改读状态
		try {
			List<DiaryComment> msgComment = getUserCommentMsgFromCache(toUserId);
			if(msgComment != null && msgComment.size() >0) {
				for(int i=msgComment.size() -1;i>=0;i--) { 
					DiaryComment comment = msgComment.get(i);
					comment.setReadFlag(2);
					this.diaryCommentServiceMapper.update(comment);
				}
			}
			// 清空缓存
			// 这个接口暂时不用，但是这里存在一个bug，消息缓存需要分页清除
			String msgKey = CacheKey.DIARY_COMMENT_MSG_CACHE + toUserId;
			String retKey = CacheKey.DIARY_ALL_COMMENT_CACHE + toUserId;
			RedisUtil.del(retKey);
			RedisUtil.del(msgKey);
			
			srt.setSucceed(true);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}

	/**
	 * 更新用户动态消息列表中，某条消息的读状态，并且关联这条评论的动态所有该用户相关的评论，都设置为已读
	 * @param userId
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public ServiceResult<Boolean> updateCommentMsgFlag(String userId, long diaryInfoId) {
		ServiceResult<Boolean> srt = new ServiceResult<Boolean>(false);
		if(StringUtils.isEmpty(userId)) {
			srt.setResultCode(ErrorCode.ERROR_8006.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8006.getResultDescr());
			return srt;
		}
		// 获取这条动态与当前用户相关的所有评论
		try {
			List<DiaryComment> list = diaryCommentServiceMapper.findAllReCommentByTouser(diaryInfoId, userId);
			if(list != null && list.size() >0) {
				for(DiaryComment comment : list) {
					if(comment == null) {
						srt.setResultCode(ErrorCode.ERROR_8008.getResultCode());
						srt.setResultMsg(ErrorCode.ERROR_8008.getResultDescr());
						return srt;
					}
					int readFlag = 1; // 标记已读
					comment.setReadFlag(readFlag);
					this.diaryCommentServiceMapper.update(comment);
				}
			}
			
			// 清楚缓存
			for(int i=0;i<20;i++) {
				String retKey = CacheKey.DIARY_ALL_COMMENT_CACHE + userId + "_" + i;
				RedisUtil.del(retKey);
			}
			String msgKey = CacheKey.DIARY_COMMENT_MSG_CACHE + userId;
			RedisUtil.del(msgKey);
			srt.setSucceed(true);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
	
	/**
	 * 处理评论总数缓存，因为是在每次评论完后，再处理的，所以，只需要在有缓存时执行++操作
	 * @param diaryInfoId
	 * @param userId
	 * @throws Exception
	 */
	private void handleCommentTotalCache(long diaryInfoId, String userId) throws Exception {
		int retNum = 0;
//		String commentTotalNumKey = CacheKey.DIARYINFO_COMMENT_TOTAL_CACHE + userId + "_" + diaryInfoId;
		String commentTotalNumKey = CacheKey.DIARYINFO_COMMENT_TOTAL_CACHE + diaryInfoId;
		Object totalCache = RedisUtil.get(commentTotalNumKey);
		if(totalCache != null) {
			retNum = Integer.parseInt(totalCache.toString());
			retNum++;
			RedisUtil.set(commentTotalNumKey, String.valueOf(retNum), CacheTime.DIARYINFO_COMMENT_TIME);
		} else {
			List<DiaryComment> commentList = this.diaryCommentServiceMapper.getDiaryCommentByDiaryInfoId(diaryInfoId);
			if(commentList != null && commentList.size() > 0) {
				retNum = commentList.size();
				RedisUtil.set(commentTotalNumKey, String.valueOf(retNum), CacheTime.DIARYINFO_COMMENT_TIME);
			}
		}
	}
	
	/**
	 * 处理评论后评论列表缓存
	 * @param diaryInfoId
	 * @param po
	 * @throws Exception
	 */
	private void handleCommentListCache(long diaryInfoId, DiaryComment po) throws Exception {
		List<DiaryComment> allCommentList = null;
		String commentKey = CacheKey.DIARYINFO_COMMENT_CACHE + diaryInfoId;
		Object commentCache = RedisUtil.get(commentKey);
		if(commentCache != null) {
			allCommentList = (List<DiaryComment>) commentCache;
			allCommentList.add(po);
			RedisUtil.set(commentKey, allCommentList, CacheTime.DIARYINFO_COMMENT_TIME);
		}else {
			allCommentList = this.diaryCommentServiceMapper.getDiaryCommentByDiaryInfoId(diaryInfoId);
			if(allCommentList != null) {
				RedisUtil.set(commentKey, allCommentList, CacheTime.DIARYINFO_COMMENT_TIME);
			}
		}
	}
	
	/**
	 * 处理评论后，评论消息缓存
	 * @param toUserId
	 * @param po
	 * @throws Exception
	 */
	private void handleCommentMsgListCache(String toUserId, DiaryComment po) throws Exception {
		List<DiaryComment> msgComment = null;
		String msgKey = CacheKey.DIARY_COMMENT_MSG_CACHE + toUserId;
		Object msgCache = RedisUtil.get(msgKey);
		if(msgCache != null) {
			msgComment = (List<DiaryComment>) msgCache;
			msgComment.add(po);
			RedisUtil.set(msgKey, msgComment, CacheTime.DIARYINFO_COMMENT_MSG_TIME);
		} else {
			msgComment = new ArrayList<DiaryComment>();
			// 缓存失效后，从db中获取数据
			List<DiaryComment>  coments = this.diaryCommentServiceMapper.getUserCommentedByToUserId(toUserId);
			if(coments != null && coments.size() >0) {
				msgComment = coments;
				RedisUtil.set(msgKey, msgComment, CacheTime.DIARYINFO_COMMENT_MSG_TIME);
			}
		}
	}
	
	public DiaryComment getDiaryComment(long id) {
		return diaryCommentServiceMapper.getObjectById(id);
	}
}
