package com.lm.live.appclient.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;


public class AppStartupPageVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = -3114662250001175259L;
	private static final String a_themeType = "a" ;
	private static final String a_imgArr = "b" ;
	private static final String a_jumpType = "c" ;
	private static final String a_jumpTarget = "d" ;
	private static final String a_anchorInfoJsonStr = "e" ;
	
	/** 主题类型，1：单张图片自动滚动2：多张图片自动滑动  3：多张图片手动滑动 4：渐变 5：单张图片没动画 */
	private int themeType;
	/** 图片地址(json数组组成的字符串) */
	private String imgArr;
	/** 0:不跳转;1:URL跳转;2:房间跳转 */
	private int jumpType;
	/** 跳转目标(根据jumpType而定,url地址或房间号) */
	private String jumpTarget;
	
	/** AnchorInfo实体的json字符串简码 */
	private String anchorInfoJsonStr;
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json,a_themeType,themeType);
			setString(json,a_imgArr,imgArr);
			setInt(json,a_jumpType,jumpType);
			setString(json,a_jumpTarget,jumpTarget);
			setString(json,a_anchorInfoJsonStr,anchorInfoJsonStr);
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
			
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		
	}


	public int getThemeType() {
		return themeType;
	}


	public void setThemeType(int themeType) {
		this.themeType = themeType;
	}


	public String getImgArr() {
		return imgArr;
	}


	public void setImgArr(String imgArr) {
		this.imgArr = imgArr;
	}


	public int getJumpType() {
		return jumpType;
	}


	public void setJumpType(int jumpType) {
		this.jumpType = jumpType;
	}


	public String getJumpTarget() {
		return jumpTarget;
	}


	public void setJumpTarget(String jumpTarget) {
		this.jumpTarget = jumpTarget;
	}


	public String getAnchorInfoJsonStr() {
		return anchorInfoJsonStr;
	}


	public void setAnchorInfoJsonStr(String anchorInfoJsonStr) {
		this.anchorInfoJsonStr = anchorInfoJsonStr;
	}

}
