package com.jiujun.shows.dynamic.domain.home;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态图片表-用户上传的原图
 * t_diary_info_img
 */

public class DiaryInfoImg extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	/**
	 * 动态id
	 */
	private long diaryinfoId;
	/**
	 * 图片顺序
	 */
	private int ratioIndex;
	/**
	 * 分辨率信息
	 */
	private String ratioInfo;
	private String url;
	private Date uploadTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDiaryinfoid() {
		return this.diaryinfoId;
	}
	
	public void setDiaryinfoid(long diaryinfoId) {
		this.diaryinfoId = diaryinfoId;
	}
	public int getRatioindex() {
		return this.ratioIndex;
	}
	
	public void setRatioindex(int ratioIndex) {
		this.ratioIndex = ratioIndex;
	}
	public String getRatioinfo() {
		return this.ratioInfo;
	}
	
	public void setRatioinfo(String ratioInfo) {
		this.ratioInfo = ratioInfo;
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
