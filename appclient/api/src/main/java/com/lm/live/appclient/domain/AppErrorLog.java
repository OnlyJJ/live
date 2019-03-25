package com.lm.live.appclient.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_app_errorLog
 * @date 2016-04-11 11:46:35
 * @author long.bin
 */
public class AppErrorLog extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 错误信息 */
	private String errorMsg;
	/** 设备信息 */
	private String deviceMsg;
	/** 错误记录时间 */
	private Date recordTime;

	/** app版本   */
	private String appVersion;
	
	/** app类型  1.www 2.安卓 3.ios  */
	private int appType;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public String getErrorMsg() {
		return this.errorMsg;
	}
	
	public void setDeviceMsg(String deviceMsg){
		this.deviceMsg = deviceMsg;
	}
	
	public String getDeviceMsg() {
		return this.deviceMsg;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	
}