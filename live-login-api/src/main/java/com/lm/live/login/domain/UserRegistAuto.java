package com.lm.live.login.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_user_regist_auto
 * @date 2017-03-09 15:29:51
 * @author 
 */
public class UserRegistAuto extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** userId */
	private String userId;
	/** deviceMac */
	private String deviceMac;
	/** deviceImsi */
	private String uuId;
	/** deviceProperties */
	private String deviceProperties;
	/** recordTime */
	private Date recordTime;
	/** verifyId */
	private String verifyId;
	/** isChangeNickname */
	private int isChangeNickname;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setDeviceMac(String deviceMac){
		this.deviceMac = deviceMac;
	}
	
	public String getDeviceMac() {
		return this.deviceMac;
	}
	
	public void setUuId(String uuId){
		this.uuId = uuId;
	}
	
	public String getUuId() {
		return this.uuId;
	}
	
	public void setDeviceProperties(String deviceProperties){
		this.deviceProperties = deviceProperties;
	}
	
	public String getDeviceProperties() {
		return this.deviceProperties;
	}
	
	public void setRecordTime(Date recordTime){
		this.recordTime = recordTime;
	}
	
	public Date getRecordTime() {
		return this.recordTime;
	}
	
	public void setVerifyId(String verifyId){
		this.verifyId = verifyId;
	}
	
	public String getVerifyId() {
		return this.verifyId;
	}
	
	public void setIsChangeNickname(int isChangeNickname){
		this.isChangeNickname = isChangeNickname;
	}
	
	public int getIsChangeNickname() {
		return this.isChangeNickname;
	}
	

}