package com.lm.live.appclient.vo;

import java.io.Serializable;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class AppSubmitPb extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** 联系方式 */
	private String contact;
	/** 问题描述 
	 * 1、视频卡顿，2、聊天卡顿/3、手机卡顿，4、其他
	 */
	private String description;
	
	/** 问题详细描述 */
	private String content;
	
	/** 问题诊断结果 */
	private String diagnosisResult;
	
	private static final String t_contact = "a";
	private static final String t_description = "b";
	private static final String t_content = "c";
	private static final String t_diagnosisResult = "d";

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, t_contact, contact);
			setString(json,t_description, description);
			setString(json, t_contact, t_contact);
			setString(json, t_diagnosisResult, diagnosisResult);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			this.contact = getString(json, t_contact);
			this.description = getString(json, t_description);
			this.diagnosisResult = getString(json,t_diagnosisResult);
			this.content = getString(json, t_content);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}


	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDiagnosisResult() {
		return diagnosisResult;
	}

	public void setDiagnosisResult(String diagnosisResult) {
		this.diagnosisResult = diagnosisResult;
	}

}
