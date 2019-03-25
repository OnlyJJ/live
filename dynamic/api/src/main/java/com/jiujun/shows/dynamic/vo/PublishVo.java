package com.jiujun.shows.dynamic.vo;

import java.io.Serializable;




import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class PublishVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 动态主题*/
	private static final String d_theme = "a";
	/** 动态内容 */
	private static final String d_content = "b";
	/** 动态图片数组 */
	private static final String d_imageList = "c";
	
	private String theme;
	private String content;
	private JSONArray imageList;
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setString(json,d_theme,theme);
			setString(json,d_content,content);
			setJSONArray(json,d_imageList,imageList);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		// TODO Auto-generated method stub
		if(json == null) {
			return;
		}
		try {
			theme = getString(json,d_theme);
			content = getString(json,d_content);
			imageList = getJSONArray(json,d_imageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JSONArray getImageList() {
		return imageList;
	}

	public void setImageList(JSONArray imageList) {
		this.imageList = imageList;
	}




	
}
