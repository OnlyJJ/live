package com.jiun.shows.others.push.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
* @entity
* @table t_push_config
* @author shao.xiang
* @date 2017-06-15
*/
public class PushConfig extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * app应用注册信鸽生成id
	 */
	private long accessId;
	/**
	 * app应用注册信鸽生成的密钥
	 */
	private String secretKey;
	/**
	 * 客户端类型 3-IOS；0-Android；默认0
	 */
	private int appType;
	/**
	 * app包名
	 */
	private String pckName;
	/**
	 * 启用状态，1-启用，0-停用
	 */
	private int useStatus;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getAccessid() {
		return this.accessId;
	}
	
	public void setAccessid(long accessId) {
		this.accessId = accessId;
	}
	public String getSecretkey() {
		return this.secretKey;
	}
	
	public void setSecretkey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getApptype() {
		return this.appType;
	}
	
	public void setApptype(int appType) {
		this.appType = appType;
	}
	public String getPckname() {
		return this.pckName;
	}
	
	public void setPckname(String pckName) {
		this.pckName = pckName;
	}
	public int getUsestatus() {
		return this.useStatus;
	}
	
	public void setUsestatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}
}
