package com.lm.live.appclient.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class AppArticleVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 3512726407043441149L;
	private String title;
	private String content;
	private String url;
	
	private static final String a_title = "a";
	private static final String a_content = "b";
	private static final String a_url = "c";
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json,a_title,title);
			setString(json,a_content,content);
			setString(json,a_url,url);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null) {
			return;
		}
		try {
			this.title=getString(json,a_title);
			this.content=getString(json,a_content);
			this.url=getString(json,a_url);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
