package com.lm.live.appclient.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_app_info
 * @date 2016-01-13 14:25:37
 * @author gw
 */
public class AppInfo extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** app类型，1-安卓，2-IOS */
	private Integer appType;
	/** app版本号 */
	private String version;
	/** 下载链接 */
	private String url;
	/** 版本更新说明 */
	private String message;
	/** 添加时间 */
	private String addTime;
	/**判断添加时间，大于或等于 */
	private Date	gtAddTime;
	/**判断添加时间，小于或等于 */
	private Date	ltAddTime;
	
	/** 包名 */
	private String packageName;
	
	/** 签名 */
	private String signatures;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setAppType(Integer appType){
		this.appType = appType;
	}
	
	public Integer getAppType() {
		return this.appType;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public void setGtAddTime(Date gtAddTime){
		this.gtAddTime = gtAddTime;
	}
	
	public Date getGtAddTime() {
		return this.gtAddTime;
	}
	
	public void setLtAddTime(Date ltAddTime){
		this.ltAddTime = ltAddTime;
	}
	
	public Date getLtAddTime() {
		return this.ltAddTime;
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
}