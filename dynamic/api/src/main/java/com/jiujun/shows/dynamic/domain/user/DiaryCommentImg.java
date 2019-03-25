package com.jiujun.shows.dynamic.domain.user;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 评论图片表
 */

public class DiaryCommentImg extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	/**
	 * 评论id
	 */
	private long diaryCommentId;
	/**
	 * 分辨率信息
	 */
	private String ratioInfo;
	/**
	 * 排序
	 */
	private int ratioIndex;
	private String url;
	private Date uploadTime;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDiarycommentid() {
		return this.diaryCommentId;
	}
	
	public void setDiarycommentid(long diaryCommentId) {
		this.diaryCommentId = diaryCommentId;
	}
	public String getRatioinfo() {
		return this.ratioInfo;
	}
	
	public void setRatioinfo(String ratioInfo) {
		this.ratioInfo = ratioInfo;
	}
	public int getRatioindex() {
		return this.ratioIndex;
	}
	
	public void setRatioindex(int ratioIndex) {
		this.ratioIndex = ratioIndex;
	}
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getUploadtime() {
		return this.uploadTime;
	}
	
	public void setUploadtime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
}
