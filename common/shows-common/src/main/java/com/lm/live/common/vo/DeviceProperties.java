package com.lm.live.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

/**
 * 设备参数<br>
 * 	说明：<br>
 * 		此参数供所有接口使用，因此，放在公共vo包下
 * @author shao.xiang
 * @date 2017-09-04
 *
 */
public class DeviceProperties extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 404121353502021548L;
	private static final String u_phoneModel = "a";
	private static final String u_uuid = "b";// 
	private static final String u_imei = "c"; // 
	private static final String u_densityDpi = "d";
	private static final String u_displayScreenWidth = "e";
	private static final String u_displayScreenHeight = "f";
	private static final String u_channelId = "g"; // 渠道ID
	private static final String u_networkInfo = "h";
	private static final String u_appType = "i";
	private static final String u_sysVersion = "j";
	private static final String u_appVersion = "k";
	private static final String u_mac = "l";
	private static final String u_phoneNickname = "m";
	private static final String u_appStartTime = "n";
	private static final String u_wifiName = "o";
	
	private static final String u_packageName = "p";
	
	private static final String u_signatures = "q";
	private static final String u_ip = "r";
	
	public String phoneModel;
	public String uuid;
	public String imei;
	public int densityDpi;
	public int displayScreenWidth;
	public int displayScreenHeight;
	private String channelId;
	private String networkInfo;
	/** 0:android , 1:web,  3:ios **/
	private int appType;
	private String sysVersion;
	private String appVersion;
	/** ios use **/
	private String mac;
	/** 手机别名 */
	private String phoneNickname;
	/** 开机时间 */
	private String appStartTime;
	/** wifi名 */
	private String wifiName;
	
	/** web、android */
	private String clientType;
	
	/** 包名 */
	private String packageName;
	
	/** 签名 */
	private String signatures;
	
	/** ip地址 */
	private String ip;
	
	public JSONObject buildJson() {
		try {
			JSONObject json = new JSONObject();
			setString(json,u_phoneModel,phoneModel);
			setString(json,u_uuid,uuid);
			setString(json,u_imei,imei);
			setInt(json,u_densityDpi,densityDpi);
			setInt(json,u_displayScreenWidth,displayScreenWidth);
			setInt(json,u_displayScreenHeight,displayScreenHeight);
			setString(json,u_networkInfo,networkInfo);
			setString(json,u_channelId,channelId);
			setInt(json,u_appType,appType);
			setString(json,u_sysVersion,sysVersion);
			setString(json,u_appVersion,appVersion);
			setString(json, u_mac, mac);
			setString(json, u_ip, ip);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return null;
	}

	public void parseJson(JSONObject json) {
		if (json == null)
			return;
		try {
			phoneModel = getString(json, u_phoneModel);
			uuid = getString(json, u_uuid);
			imei = getString(json, u_imei);
			densityDpi = getInt(json, u_densityDpi);
			displayScreenWidth = getInt(json, u_displayScreenWidth);
			displayScreenHeight = getInt(json, u_displayScreenHeight);
			networkInfo = getString(json, u_networkInfo);
			channelId = getString(json, u_channelId);
			appType = getInt(json, u_appType);
			sysVersion = getString(json, u_sysVersion);
			appVersion = getString(json, u_appVersion);
			mac = getString(json, u_mac);
			phoneNickname = getString(json, u_phoneNickname);
			appStartTime = getString(json, u_appStartTime);
			wifiName = getString(json, u_wifiName);
			packageName = getString(json, u_packageName);
			signatures = getString(json, u_signatures);
			ip = getString(json, u_ip);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String toString() {
		return "";
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}

	public int getDisplayScreenWidth() {
		return displayScreenWidth;
	}

	public void setDisplayScreenWidth(int displayScreenWidth) {
		this.displayScreenWidth = displayScreenWidth;
	}

	public int getDisplayScreenHeight() {
		return displayScreenHeight;
	}

	public void setDisplayScreenHeight(int displayScreenHeight) {
		this.displayScreenHeight = displayScreenHeight;
	}

	public String getNetworkInfo() {
		return networkInfo;
	}

	public void setNetworkInfo(String networkInfo) {
		this.networkInfo = networkInfo;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getSysVersion() {
		return sysVersion;
	}

	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getPhoneNickname() {
		return phoneNickname;
	}

	public void setPhoneNickname(String phoneNickname) {
		this.phoneNickname = phoneNickname;
	}

	public String getAppStartTime() {
		return appStartTime;
	}

	public void setAppStartTime(String appStartTime) {
		this.appStartTime = appStartTime;
	}

	public String getWifiName() {
		return wifiName;
	}

	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSignatures() {
		return signatures;
	}

	public void setSignatures(String signatures) {
		this.signatures = signatures;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
