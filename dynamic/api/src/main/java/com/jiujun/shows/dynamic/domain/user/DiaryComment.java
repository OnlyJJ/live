package com.jiujun.shows.dynamic.domain.user;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;



/**
 * 动态评论表
 * t_diary_comment
 */

public class DiaryComment extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	/**
	 * 动态id
	 */
	private long diaryInfoId;
	/**
	 * 评论回复类型,0:回复整个动态;1:回复某个评论
	 */
	private int commentType;
	/**
	 * 评论id,为空表示回复整个评论,不为空表示回复某条评论
	 */
	private long diaryCommentId;
	private Date commentTime;
	/**
	 * 评论文字内容
	 */
	private String commentTextInfo;
	private String ip;
	private int clientType;
	/**
	 * 是否被用户撤回,0:否,1:是
	 */
	private int isUserCancel;
	/**
	 * 管理状态,0:默认系统自审通过,1:被举报违规而删除,2:人工审核通过
	 */
	private int mgrState;
	/** 被评论的用户，commentType=0时，此值为动态发布者，commentType=1时，此值为被评论者 */
	private String toUserId;
	/**个人消息状态，0-未读，1-标记已读，2-标记清空*/
	private int readFlag;
//	
//	/**
//	 * 被赞总数
//	 */
//	private int prizeTotalNum;
//	/**
//	 * 被踩总数
//	 */
//	private int belittleTotalNum;
//	/**
//	 * 被评论总条数
//	 */
//	private int commentTotalNum;
	
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
	public long getDiaryinfoid() {
		return this.diaryInfoId;
	}
	
	public void setDiaryinfoid(long diaryInfoId) {
		this.diaryInfoId = diaryInfoId;
	}
	public int getCommenttype() {
		return this.commentType;
	}
	
	public void setCommenttype(int commentType) {
		this.commentType = commentType;
	}
	public long getDiarycommentid() {
		return this.diaryCommentId;
	}
	
	public void setDiarycommentid(long diaryCommentId) {
		this.diaryCommentId = diaryCommentId;
	}
	public Date getCommenttime() {
		return this.commentTime;
	}
	
	public void setCommenttime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public String getCommenttextinfo() {
		return this.commentTextInfo;
	}
	
	public void setCommenttextinfo(String commentTextInfo) {
		this.commentTextInfo = commentTextInfo;
	}
	public String getIp() {
		return this.ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getClienttype() {
		return this.clientType;
	}
	
	public void setClienttype(int clientType) {
		this.clientType = clientType;
	}
	public int getIsusercancel() {
		return this.isUserCancel;
	}
	
	public void setIsusercancel(int isUserCancel) {
		this.isUserCancel = isUserCancel;
	}
	public int getMgrstate() {
		return this.mgrState;
	}
	
	public void setMgrstate(int mgrState) {
		this.mgrState = mgrState;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public int getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

//	public int getPrizeTotalNum() {
//		return prizeTotalNum;
//	}
//
//	public void setPrizeTotalNum(int prizeTotalNum) {
//		this.prizeTotalNum = prizeTotalNum;
//	}
//
//	public int getBelittleTotalNum() {
//		return belittleTotalNum;
//	}
//
//	public void setBelittleTotalNum(int belittleTotalNum) {
//		this.belittleTotalNum = belittleTotalNum;
//	}

//	public int getCommentTotalNum() {
//		return commentTotalNum;
//	}
//
//	public void setCommentTotalNum(int commentTotalNum) {
//		this.commentTotalNum = commentTotalNum;
//	}
}
