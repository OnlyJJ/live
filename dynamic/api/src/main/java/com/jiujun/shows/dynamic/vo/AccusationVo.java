package com.jiujun.shows.dynamic.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class AccusationVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 举报的动态id */
	private static final String d_diaryInfoId = "a";
	/** 举报的评论id */
	private static final String d_commentId = "b";
	/** 举报类型 ,1-举报动态，2-举报评论 */
	private static final String d_type = "c";
	/** 举报信息说明 */
	private static final String d_content = "d";
	
	private long diaryInfoId;
	private long commentId;
	private int type;
	private String content;
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setLong(json,d_diaryInfoId,diaryInfoId);
			setLong(json,d_commentId,commentId);
			setInt(json,d_type,type);
			setString(json,d_content,content);
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
			diaryInfoId = getLong(json,d_diaryInfoId);
			commentId = getLong(json,d_commentId);
			type = getInt(json,d_type);
			content = getString(json,d_content);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public long getDiaryInfoId() {
		return diaryInfoId;
	}

	public void setDiaryInfoId(long diaryInfoId) {
		this.diaryInfoId = diaryInfoId;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
}
