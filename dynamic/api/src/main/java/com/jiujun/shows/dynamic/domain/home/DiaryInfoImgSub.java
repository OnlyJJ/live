package com.jiujun.shows.dynamic.domain.home;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态图片表(各种分辨率)
 * t_diary_info_img_sub
 */

public class DiaryInfoImgSub extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long diaryinfoImgId;
	/**
	 * 分辨率信息
	 */
	private String ratio;
	/**
	 * 图片顺序,设置多分辨率时用
	 */
	private String ratioIndex;
	private String url;
	private Date uploadTime;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDiaryinfoimgid() {
		return this.diaryinfoImgId;
	}
	
	public void setDiaryinfoimgid(long diaryinfoImgId) {
		this.diaryinfoImgId = diaryinfoImgId;
	}
	public String getRatio() {
		return this.ratio;
	}
	
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getRatioindex() {
		return this.ratioIndex;
	}
	
	public void setRatioindex(String ratioIndex) {
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
