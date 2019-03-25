package com.jiujun.shows.dynamic.domain.home;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态信息表
 * t_diary_info
 */

public class DiaryInfo extends BaseVo {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	/**
	 * 动态发布来源,0:普通用户,1:主播,2:系统发布
	 */
	private int publicFromType;
	/**
	 * 主题
	 */
	private String theme;
	/**
	 * 动态文本信息
	 */
	private String diaryTextInfo;
	/**
	 * 动态链接url
	 */
	private String diaryLinkUrl;
	/**
	 * 发布时间
	 */
	private Date publicTime;
	
	/**
	 * 发布ip
	 */
	private String publicIp;
	/**
	 * 发布地址
	 */
	private String publicAddress;
	/**
	 * 客户端类型
	 */
	private int clientType;
	private String remark;
	/**
	 * 排序权重,值越大越靠前
	 */
	private int sortWeight;
	/**
	 * 是否被用户撤回,0:否,1:是
	 */
	private int isUserCancel;
	/**
	 * 管理控制状态,0:新建,1:审核违规-删除,2-审核未违规
	 */
	private int mgrState;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPublicFromType() {
		return publicFromType;
	}
	public void setPublicFromType(int publicFromType) {
		this.publicFromType = publicFromType;
	}
	public String getDiaryTextInfo() {
		return diaryTextInfo;
	}
	public void setDiaryTextInfo(String diaryTextInfo) {
		this.diaryTextInfo = diaryTextInfo;
	}
	public String getDiaryLinkUrl() {
		return diaryLinkUrl;
	}
	public void setDiaryLinkUrl(String diaryLinkUrl) {
		this.diaryLinkUrl = diaryLinkUrl;
	}
	public Date getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(Date publicTime) {
		this.publicTime = publicTime;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getPublicAddress() {
		return publicAddress;
	}
	public void setPublicAddress(String publicAddress) {
		this.publicAddress = publicAddress;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(int sortWeight) {
		this.sortWeight = sortWeight;
	}
	public int getIsUserCancel() {
		return isUserCancel;
	}
	public void setIsUserCancel(int isUserCancel) {
		this.isUserCancel = isUserCancel;
	}
	public int getMgrState() {
		return mgrState;
	}
	public void setMgrState(int mgrState) {
		this.mgrState = mgrState;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
}
