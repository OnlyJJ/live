package com.lm.live.appclient.domain;


import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * app用户网络诊断数据
 */

public class AppNetproblem extends BaseVo {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String userId;
	/**
	 * 联系方式，qq，微信，微博，手机登
	 */
	private String contact;
	/**
	 * 问题描述：1视频卡顿，2聊天卡顿，3手机卡顿，4其他
	 */
	private String description;
	/**
	 * 详细说明
	 */
	private String content;
	/**
	 * 网络诊断结果
	 */
	private String diagnosisResult;
	/**
	 * 提交时间
	 */
	private Date addTime;
	
	
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
	public String getContact() {
		return this.contact;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getDiagnosisresult() {
		return this.diagnosisResult;
	}
	
	public void setDiagnosisresult(String diagnosisResult) {
		this.diagnosisResult = diagnosisResult;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}
}
