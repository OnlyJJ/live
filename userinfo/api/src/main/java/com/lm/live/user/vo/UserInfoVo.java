package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 聊天用户的信息
 * @author Administrator
 *
 */
public class UserInfoVo implements Serializable{
	
	private static final long serialVersionUID = -2324530588638614515L;

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
	 * 发送者等级(指用户等级)
	 */
	private String level; 
	
	/**
	 * 发送者类型  1:主播，2:普通用户，3:房管  4:游客
	 */
	private String type;
	
	/* 是否禁言 */
	private boolean isForbidSpeak;
	
	/* 是否被踢 */
	private boolean isForceOut;
	
	/* 用户座驾对应的汽车Id */
	private int carId;
	
	/** car名称 */
	private String carName;
	/** car图片uri */
	private String carImg;
	
	/* 渠道号  */
	private String channelId;
	
	private String anchorLevel;
	
	private String userLevel;
	
	/** 是否官方人员,默认false  */
	private boolean ifOfficialUser = false;
	
	/** 用户身份的勋章  */
	private List userDecorateList;
	
	/** 是否是房间守护 */
	private boolean isRoomGuard = false;
	
	/** 用户所有房间守护信息 */
	private List guardList;
	
	/** 充值次数 */
	private int payCount;
	
	/** 发消息的字体颜色 */
	private String fontColor;
	
	/**是否显示主播等级图标*/
	private boolean anchorLevelIcon;
	
	/**靓号等级* 0非靓号 1至尊 2顶级 3超级 4主题 5自定义*/
	private int goodCodeLevel;
	
	/**靓号等级图片URL*/
	private String goodCodeLevelUrl;
	
	/**当前靓号等级*/
	private int curGoodCodeLevel;
	
	/**真爱粉等级图标*/
	private String fansLevelIcon;
	
	/** 普通用户人气等级 */
	private String renqLevel;
	
	/** 用户身份，0-用户，1-主播 */
	private int identity;
	
	/** 用户徽章列表  */
	private List userBadgeList;

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public String getRenqLevel() {
		return renqLevel;
	}

	public void setRenqLevel(String renqLevel) {
		this.renqLevel = renqLevel;
	}

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isForbidSpeak() {
		return isForbidSpeak;
	}

	public void setForbidSpeak(boolean isForbidSpeak) {
		this.isForbidSpeak = isForbidSpeak;
	}

	public boolean isForceOut() {
		return isForceOut;
	}

	public void setForceOut(boolean isForceOut) {
		this.isForceOut = isForceOut;
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

	public boolean isIfOfficialUser() {
		return ifOfficialUser;
	}

	public void setIfOfficialUser(boolean ifOfficialUser) {
		this.ifOfficialUser = ifOfficialUser;
	}

	public List getUserDecorateList() {
		return userDecorateList;
	}

	public void setUserDecorateList(List userDecorateList) {
		this.userDecorateList = userDecorateList;
	}

	public boolean isRoomGuard() {
		return isRoomGuard;
	}

	public void setRoomGuard(boolean isRoomGuard) {
		this.isRoomGuard = isRoomGuard;
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

	public boolean getAnchorLevelIcon() {
		return anchorLevelIcon;
	}

	public void setAnchorLevelIcon(boolean isAnchorLevelIcon) {
		this.anchorLevelIcon = isAnchorLevelIcon;
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

	public String getFansLevelIcon() {
		return fansLevelIcon;
	}

	public void setFansLevelIcon(String fansLevelIcon) {
		this.fansLevelIcon = fansLevelIcon;
	}
	
	public List getUserBadgeList() {
		return userBadgeList;
	}

	public void setUserBadgeList(List userBadgeList) {
		this.userBadgeList = userBadgeList;
	}
	
}
