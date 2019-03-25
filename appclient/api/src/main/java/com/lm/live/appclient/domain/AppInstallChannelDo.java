package com.lm.live.appclient.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * app安装渠道记录
 *
 */
public class AppInstallChannelDo extends BaseVo{
	private static final long serialVersionUID = 1L;
	private int  id;
	private String  channelId;
	private Date  recordTime;
	private String  mac;
	private String  uuid;
	private String  appPackage;
	/** 0:android、3:ios*/
	private int  appType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}
	
	
}
