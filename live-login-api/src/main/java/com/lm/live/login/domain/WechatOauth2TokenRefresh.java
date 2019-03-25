package com.lm.live.login.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lm.live.common.vo.BaseVo;


public class WechatOauth2TokenRefresh extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** appid */
	private String appid;
	/** openid */
	private String openid;
	/** code */
	private String code;
	/** accessToken */
	private String accessToken;
	/** expiresinSecond */
	private int expiresinSecond;
	/** refreshToken */
	private String refreshToken;
	/** scope */
	private String scope;
	/** resultTime */
	private Date resultTime;
	/**判断resultTime，大于或等于 */
	private Date	gtResultTime;
	/**判断resultTime，小于或等于 */
	private Date	ltResultTime;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setAppid(String appid){
		this.appid = appid;
	}
	
	public String getAppid() {
		return this.appid;
	}
	
	public void setOpenid(String openid){
		this.openid = openid;
	}
	
	public String getOpenid() {
		return this.openid;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public void setExpiresinSecond(int expiresinSecond){
		this.expiresinSecond = expiresinSecond;
	}
	
	public int getExpiresinSecond() {
		return this.expiresinSecond;
	}
	
	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}
	
	public String getRefreshToken() {
		return this.refreshToken;
	}
	
	public void setScope(String scope){
		this.scope = scope;
	}
	
	public String getScope() {
		return this.scope;
	}
	
	public void setResultTime(Date resultTime){
		this.resultTime = resultTime;
	}
	
	public Date getResultTime() {
		return this.resultTime;
	}
	
	public void setGtResultTime(Date gtResultTime){
		this.gtResultTime = gtResultTime;
	}
	
	@JsonIgnore
	public Date getGtResultTime() {
		return this.gtResultTime;
	}
	
	public void setLtResultTime(Date ltResultTime){
		this.ltResultTime = ltResultTime;
	}
	
	@JsonIgnore
	public Date getLtResultTime() {
		return this.ltResultTime;
	}
	

}