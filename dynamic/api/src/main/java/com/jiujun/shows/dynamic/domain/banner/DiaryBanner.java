package com.jiujun.shows.dynamic.domain.banner;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态-banner
 */

public class DiaryBanner extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private int id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 标题颜色，16进制或RGB都可
	 */
	private String titleColor;
	/**
	 * 链接url
	 */
	private String linkUrl;
	/** web显示图片*/
	private String showImgUrl;
	/**
	 * 0-停用,1-起用
	 */
	private int isUse;
	private Date addTime;
	private Date beginTime;
	private Date endTime;
	/** app显示图片 */
	private String appShowImgUrl;
	private int showSort;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitlecolor() {
		return this.titleColor;
	}
	
	public void setTitlecolor(String titleColor) {
		this.titleColor = titleColor;
	}
	public String getLinkurl() {
		return this.linkUrl;
	}
	
	public void setLinkurl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getShowimgurl() {
		return this.showImgUrl;
	}
	
	public void setShowimgurl(String showImgUrl) {
		this.showImgUrl = showImgUrl;
	}
	public int getIsuse() {
		return this.isUse;
	}
	
	public void setIsuse(int isUse) {
		this.isUse = isUse;
	}
	public Date getAddTime() {
		return this.addTime;
	}
	
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndtime() {
		return this.endTime;
	}
	
	public void setEndtime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAppShowImgUrl() {
		return appShowImgUrl;
	}

	public void setAppShowImgUrl(String appShowImgUrl) {
		this.appShowImgUrl = appShowImgUrl;
	}

	public int getShowSort() {
		return showSort;
	}

	public void setShowSort(int showSort) {
		this.showSort = showSort;
	}
}
