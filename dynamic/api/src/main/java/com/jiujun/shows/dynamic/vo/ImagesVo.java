package com.jiujun.shows.dynamic.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.dynamic.constant.Constants;

public class ImagesVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 动态小图*/
	private static final String d_smallImage = "a";
	/** 动态原图 */
	private static final String d_bigImage = "b";
	
	private String smallImage;
	private String bigImage;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			if(smallImage != null) {
				setString(json,d_smallImage,BaseConstants.cdnPath+File.separator+Constants.DIARY_IMG_FILE_URI+File.separator + smallImage);
			}
			if(bigImage != null) {
				setString(json,d_bigImage,BaseConstants.cdnPath+File.separator+Constants.DIARY_IMG_FILE_URI+File.separator + bigImage);
			}
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
			smallImage = getString(json,d_smallImage);
			bigImage = getString(json,d_bigImage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}


	
}
