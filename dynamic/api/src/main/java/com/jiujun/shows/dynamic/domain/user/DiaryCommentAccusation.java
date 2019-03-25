package com.jiujun.shows.dynamic.domain.user;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;


/**
 * 动态评论举报信息
 * t_diary_comment_accusation
 */

public class DiaryCommentAccusation extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	/**
	 * 举报人userId
	 */
	private String userId;
	/**
	 * 被举报人userId
	 */
	private String toUserId;
	/**
	 * 举报时间
	 */
	private Date accusationTime;
	/**
	 * 被举报的动态评论id
	 */
	private long commentId;
	/**
	 * 举报信息说明
	 */
	private String accusationInfo;
	/**
	 * 后台管理员处理状态,0:新建,1:已处理-删除(违规),2:已处理-未删除(未违规)
	 */
	private int mgrState;
	/**
	 * 管理员操作的用户userId
	 */
	private String operateUserId;
	/**
	 * 管理员操作时间
	 */
	private Date operateTime;
	/**
	 * 管理员操作说明
	 */
	private String operateComment;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getTouserid() {
		return this.toUserId;
	}
	
	public void setTouserid(String toUserId) {
		this.toUserId = toUserId;
	}
	public Date getAccusationtime() {
		return this.accusationTime;
	}
	
	public void setAccusationtime(Date accusationTime) {
		this.accusationTime = accusationTime;
	}
	public long getCommentid() {
		return this.commentId;
	}
	
	public void setCommentid(long commentId) {
		this.commentId = commentId;
	}
	public String getAccusationinfo() {
		return this.accusationInfo;
	}
	
	public void setAccusationinfo(String accusationInfo) {
		this.accusationInfo = accusationInfo;
	}
	public int getMgrstate() {
		return this.mgrState;
	}
	
	public void setMgrstate(int mgrState) {
		this.mgrState = mgrState;
	}
	public String getOperateuserid() {
		return this.operateUserId;
	}
	
	public void setOperateuserid(String operateUserId) {
		this.operateUserId = operateUserId;
	}
	public Date getOperatetime() {
		return this.operateTime;
	}
	
	public void setOperatetime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperatecomment() {
		return this.operateComment;
	}
	
	public void setOperatecomment(String operateComment) {
		this.operateComment = operateComment;
	}
}
