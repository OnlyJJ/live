package com.jiun.shows.others.push.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 客户端推送消息设备信息记录表
 * @entity
 * @table t_push_dev
 * @author shao.xiang
 * @date 2017-06-15
*/
public class PushDev extends BaseVo {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * 账户id
	 */
	private String userId;
	/**
	 * 接收消息的设备 Token
	 */
	private String token;
	/**
	 * 客户端类型 1-IOS；0-Android；默认0
	 */
	private int appType;
	/**
	 * 包名
	 */
	private String pckName;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	public int getApptype() {
		return this.appType;
	}
	
	public void setApptype(int appType) {
		this.appType = appType;
	}
	public Date getCreattime() {
		return this.creatTime;
	}
	
	public void setCreattime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getPckName() {
		return pckName;
	}

	public void setPckName(String pckName) {
		this.pckName = pckName;
	}
}
