package com.lm.live.user.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.user.constant.Constants;

/**
 * 首页实体
 * @author shao.xiang
 * @date 2017-09-04
 */
public abstract class HomepageVo extends UserBaseInfo implements Serializable {
	
	private static final long serialVersionUID = -6874129572214793335L;
	/** 视频图片地址 */
	private String showImg;
	/** 在线人数 **/
	private int audienceCount;
	/** 直播状态 , 1:开播  0:停播**/
	private int status;
	/** 是否有录播视频,0：没有，1：有 */
	private int videoFlag;
	/** 上角标url */
	private String upLogoUrl;
	/** 下角标url */
	private String downLogoUrl;
	/** 主播分类 ，0：未分类*/
	private int anchorStyle;
	
	// 字段key
	private static final String h_showImg = "b";
	private static final String h_audienceCount = "c";
	private static final String h_status = "d";
	private static final String h_videoFlag ="e";
	private static final String h_upLogoUrl = "f";
	private static final String h_downLogoUrl = "g";
	private static final String h_anchorStyle = "h"; // guard
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			if( null != showImg ){ setString(json, h_showImg, Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator+showImg); }
			setInt(json, h_audienceCount, audienceCount );
			setInt(json, h_status, status); 
			setInt(json, h_videoFlag, videoFlag);
			setString(json,h_upLogoUrl, Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator + upLogoUrl);
			setString(json,h_downLogoUrl, Constants.cdnPath+ Constants.ANCHOR_IMG_FILE_URI + File.separator + downLogoUrl);
			setInt(json,h_anchorStyle, anchorStyle);
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
			anchorStyle = getInt(json,h_anchorStyle);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	public String getShowImg() {
		return showImg;
	}
	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}
	public int getAudienceCount() {
		return audienceCount;
	}
	public void setAudienceCount(int audienceCount) {
		this.audienceCount = audienceCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getVideoFlag() {
		return videoFlag;
	}
	public void setVideoFlag(int videoFlag) {
		this.videoFlag = videoFlag;
	}
	public String getUpLogoUrl() {
		return upLogoUrl;
	}
	public void setUpLogoUrl(String upLogoUrl) {
		this.upLogoUrl = upLogoUrl;
	}
	public String getDownLogoUrl() {
		return downLogoUrl;
	}
	public void setDownLogoUrl(String downLogoUrl) {
		this.downLogoUrl = downLogoUrl;
	}
	public int getAnchorStyle() {
		return anchorStyle;
	}
	public void setAnchorStyle(int anchorStyle) {
		this.anchorStyle = anchorStyle;
	}
	
}
