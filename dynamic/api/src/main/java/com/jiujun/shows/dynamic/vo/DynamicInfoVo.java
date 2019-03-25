package com.jiujun.shows.dynamic.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.StrUtil;

public class DynamicInfoVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 用户信息实体 */
	private static final String d_dynamicId = "a";
	private static final String d_theme = "b";
	/** 动态发布者身份，0-普通用户，1-主播，2-系统 */
	private static final String d_publicFromType = "c";
	/** 动态文本信息 */
	private static final String d_diaryTextInfo = "d";
	/** 图片集 */
	private static final String d_imagesVo = "e";
	/** 被赞总数 */
	private static final String d_prizeTotalNum = "f";
	/** 被踩总数 */
	private static final String d_belittleTotalNum = "g";
	/** 被评论总数 */
	private static final String d_commentTotalNum = "h";
	/** 发布时间 */
	private static final String d_publicTime = "i";
	
	private long dynamicId;
	private String theme;
	private int publicFromType;
	private String diaryTextInfo;
	private int prizeTotalNum;
	private int belittleTotalNum;
	private int commentTotalNum;
	private List<ImagesVo> imagesVo;
	private long publicTime;
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setLong(json,d_dynamicId,dynamicId);
			setString(json,d_theme,theme);
			setInt(json,d_publicFromType,publicFromType);
			setString(json,d_diaryTextInfo,diaryTextInfo);
			setInt(json,d_prizeTotalNum,prizeTotalNum);
			setInt(json,d_belittleTotalNum,belittleTotalNum);
			setInt(json,d_commentTotalNum,commentTotalNum);
			List images = new ArrayList();
			if(imagesVo != null && imagesVo.size() > 0) {
				for(ImagesVo imagevo : imagesVo) {
					images.add(imagevo.buildJson());
				}
			}
			setList(json,d_imagesVo,images);
			setLong(json,d_publicTime,publicTime);
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
			imagesVo = getList(json,d_imagesVo);
			dynamicId = getLong(json,d_dynamicId);
			theme = getString(json,d_theme);
			diaryTextInfo = getString(json,d_diaryTextInfo);
			prizeTotalNum = getInt(json,d_prizeTotalNum);
			belittleTotalNum= getInt(json,d_belittleTotalNum);
			commentTotalNum = getInt(json,d_commentTotalNum);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private List<ImagesVo> getList(JSONObject json, String key) {
		List<ImagesVo> value = null;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
//			value = json.getJSONObject(key);
			value = (List<ImagesVo>) JsonUtil.jsonToBean(json.getJSONObject(key));
		}
		return value;
	}

	public int getPublicFromType() {
		return publicFromType;
	}

	public void setPublicFromType(int publicFromType) {
		this.publicFromType = publicFromType;
	}

	public String getDiaryTextInfo() {
		return diaryTextInfo;
	}

	public void setDiaryTextInfo(String diaryTextInfo) {
		this.diaryTextInfo = diaryTextInfo;
	}

	public int getPrizeTotalNum() {
		return prizeTotalNum;
	}

	public void setPrizeTotalNum(int prizeTotalNum) {
		this.prizeTotalNum = prizeTotalNum;
	}

	public int getBelittleTotalNum() {
		return belittleTotalNum;
	}

	public void setBelittleTotalNum(int belittleTotalNum) {
		this.belittleTotalNum = belittleTotalNum;
	}

	public int getCommentTotalNum() {
		return commentTotalNum;
	}

	public void setCommentTotalNum(int commentTotalNum) {
		this.commentTotalNum = commentTotalNum;
	}

	public long getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(long dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}


	public List<ImagesVo> getImagesVo() {
		return imagesVo;
	}

	public void setImagesVo(List<ImagesVo> imagesVo) {
		this.imagesVo = imagesVo;
	}

	public long getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(long publicTime) {
		this.publicTime = publicTime;
	}



}
