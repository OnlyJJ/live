package com.jiujun.shows.dynamic.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class DiaryBannerVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** banner名称*/
	private static final String d_title = "a";
	/** 名称颜色*/
	private static final String d_titleColor = "b";
	/** banner链接 */
	private static final String d_linkUrl = "c";
	/** web显示图片 */
	private static final String d_showImgUrl = "d";
	/** app显示图片 */
	private static final String d_appShowImgUrl= "e";
	/** 显示排序 */
	private static final String d_showSort = "f";
	
	private String title;
	private String titleColor;
	private String linkUrl;
	private String showImgUrl;
	private String appShowImgUrl;
	private int showSort;
	
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setString(json,d_title,title);
			setString(json,d_titleColor,titleColor);
			setString(json,d_linkUrl,linkUrl);
			if(showImgUrl != null) {
				setString(json,d_showImgUrl,BaseConstants.cdnPath + BaseConstants.BANNER_IMG_FILE_URI  + File.separator + showImgUrl);
			}
			if(appShowImgUrl != null) {
				setString(json,d_appShowImgUrl,BaseConstants.cdnPath+File.separator+BaseConstants.BANNER_IMG_FILE_URI+File.separator + appShowImgUrl);
			}
			setInt(json,d_showSort,showSort);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			title = getString(json,d_title);
			titleColor = getString(json,d_titleColor);
			linkUrl = getString(json,d_linkUrl);
			showImgUrl = getString(json,d_showImgUrl);
			appShowImgUrl = getString(json,d_appShowImgUrl);
			showSort = getInt(json,d_showSort);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getShowImgUrl() {
		return showImgUrl;
	}

	public void setShowImgUrl(String showImgUrl) {
		this.showImgUrl = showImgUrl;
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
