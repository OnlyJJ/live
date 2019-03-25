package com.lm.live.base.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


public class ServiceLog extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户userId */
	private String userId;
	/** 操作内容 */
	private String info;
	/** 操作IP */
	private String ip;
	/** 操作时间 */
	private Date actTime;
	/**判断操作时间，大于或等于 */
	private Date	gtActTime;
	/**判断操作时间，小于或等于 */
	private Date	ltActTime;
	
	/** 用户名  */
	private String userName ;
	
	/** 客户端类型,ios,android,web */
	private String clientType ;
	
	/** 客户端设备信息  */
	private String deviceproperties ;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	
	public void setInfo(String info){
		this.info = info;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public void setIp(String ip){
		this.ip = ip;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setActTime(Date actTime){
		this.actTime = actTime;
	}
	
	public Date getActTime() {
		return this.actTime;
	}
	
	public void setGtActTime(Date gtActTime){
		this.gtActTime = gtActTime;
	}
	
	public Date getGtActTime() {
		return this.gtActTime;
	}
	
	public void setLtActTime(Date ltActTime){
		this.ltActTime = ltActTime;
	}
	
	public Date getLtActTime() {
		return this.ltActTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getDeviceproperties() {
		return deviceproperties;
	}

	public void setDeviceproperties(String deviceproperties) {
		this.deviceproperties = deviceproperties;
	}
	
	

}