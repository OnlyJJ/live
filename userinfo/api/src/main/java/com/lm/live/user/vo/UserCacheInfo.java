package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 缓存用户的信息
 * @author Administrator
 *
 */
public class UserCacheInfo implements Serializable{
	
	private static final long serialVersionUID = -2007095266376017615L;

	/**
	 * 字符串，发送者ID
	 */
	private String uid;
	
	/**
	 * 发送者昵称
	 */
	private String nickname;
	
	/**
	 * 发送者头像
	 */
	private String avatar;
	
	/**
	 * 主播：子等级
	 */
	private String anchorLevel;
	
	/**
	 * 普通用子等级
	 */
	private String userLevel;
	
	/* 用户座驾对应的汽车Id */
	private int carId;
	/** car名称 */
	private String carName;
	/** car图片uri */
	private String carImg;
	/*  */
	private String channelId;
	
	/**是否显示主播等级图标*/
	private boolean isLevelIcon;
	
	
	/** 用户勋章   */
	private List userDecorateList;
	
	/** 主播勋章   */
	private List anchorDecorateList;
	
	/** 用户所有房间守护信息 */
	private List guardList;
	
	/** 充值次数 */
	private int payCount;
	
	/** 发消息的字体颜色 */
	private String fontColor;
	
	/**靓号等级*/
	private int goodCodeLevel;
	
	/**靓号等级图片地址*/
	private String goodCodeLevelUrl;
	
	/**当前靓号等级*/
	private int curGoodCodeLevel;
	
	/** 普通用户人气等级 */
	private String renqLevel;
	
	/** 用户身份，0-用户，1-主播 */
	private int identity;
	
	/** 用户徽章列表  */
	private List userBadgeList;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAnchorLevel() {
		return anchorLevel;
	}

	public void setAnchorLevel(String anchorLevel) {
		this.anchorLevel = anchorLevel;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List getUserDecorateList() {
		return userDecorateList;
	}

	public void setUserDecorateList(List userDecorateList) {
		this.userDecorateList = userDecorateList;
	}

	public List getAnchorDecorateList() {
		return anchorDecorateList;
	}

	public void setAnchorDecorateList(List anchorDecorateList) {
		this.anchorDecorateList = anchorDecorateList;
	}

	public List getGuardList() {
		return guardList;
	}

	public void setGuardList(List guardList) {
		this.guardList = guardList;
	}

	public int getPayCount() {
		return payCount;
	}

	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public boolean getIsLevelIcon() {
		return isLevelIcon;
	}

	public void setIsLevelIcon(boolean isLevelIcon) {
		this.isLevelIcon = isLevelIcon;
	}

	public int getGoodCodeLevel() {
		return goodCodeLevel;
		
	}

	public void setGoodCodeLevel(int goodCodeLevel) {
			this.goodCodeLevel = goodCodeLevel;
	}

	public String getGoodCodeLevelUrl() {
		return goodCodeLevelUrl;
	}

	public void setGoodCodeLevelUrl(String goodCodeLevelUrl) {
			this.goodCodeLevelUrl = goodCodeLevelUrl;
	}

	public int getCurGoodCodeLevel() {
			return curGoodCodeLevel;
	}

	public void setCurGoodCodeLevel(int curGoodCodeLevel) {
			this.curGoodCodeLevel = curGoodCodeLevel;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarImg() {
		return carImg;
	}

	public void setCarImg(String carImg) {
		this.carImg = carImg;
	}

	public String getRenqLevel() {
		return renqLevel;
	}

	public void setRenqLevel(String renqLevel) {
		this.renqLevel = renqLevel;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}
	
	public List getUserBadgeList() {
		return userBadgeList;
	}

	public void setUserBadgeList(List userBadgeList) {
		this.userBadgeList = userBadgeList;
	}
	
}
