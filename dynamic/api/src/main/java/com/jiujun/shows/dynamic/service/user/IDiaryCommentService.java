package com.jiujun.shows.dynamic.service.user;


import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.service.ICommonService;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.dynamic.domain.user.DiaryComment;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态评论表
 * @author shao.xiang
 * @date 2017年7月5日
 *
 */
public interface IDiaryCommentService extends ICommonService<DiaryComment> {
	
	/**
	 * 发表评论
	 * @param diaryInfoId 动态id
	 * @param content 评论内容
	 * @param appType 客户端类型
	 * @param pubIp 发表ip
	 * @param commentId 评论id
	 * @param toCommentId 当回复某条评论时使用，参数为回复的目标评论id
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-08
	 * 
	 */
	ServiceResult<Boolean> publishComments(long diaryInfoId, String userId, String content, int appType, String pubIp, 
			 long toCommentId, int commentType);
	
	/**
	 * 获取分页评论信息
	 * @param diaryInfoId 动态id
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-08
	 */
	ServiceResult<JSONObject> getDiaryCommentByPage(long diaryInfoId, Page page);
	
	/**
	 * 用户撤回某一动态的某条评论
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
	 * @return
 	 * @author shao.xiang
 	 * @date 2017-07-08
	 */
	ServiceResult<Boolean> deleteDiaryCommet(long diaryInfoId, long commentId, String userId);
	
	/**
	 * 获取用户收到的评论消息
	 * @return
	 * @author shao.xiang
	 * @date 2017-07-10
	 */
	ServiceResult<JSONObject> getUserDiaryMSG(String userId, Page page);
	
	/**
	 * 举报评论
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
	 * @param userId 举报用户
	 * @author shao.xiang
	 * @date 2017-07-07
	 */
	ServiceResult<Boolean> reportDiaryComment(long diaryInfoId, long commentId, String userId, String content);
	
	/**
	 * 清空个人所有动态消息
	 * @param toUserId 动态消息接受者
	 * @author shao.xiang
	 * @date 2017-07-11
	 * @return
	 */
	ServiceResult<Boolean> deleteUserDiaryAllMSG(String toUserId);
	
	/**
	 * 更新用户动态消息列表中，某条消息的读状态，并且关联这条评论的动态所有该用户相关的评论，都设置为已读
	 * @param diaryInfoId 动态id
	 * @param commentId 评论id
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	ServiceResult<Boolean> updateCommentMsgFlag(String userId, long diaryInfoId);
}
