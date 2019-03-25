package com.jiujun.shows.dynamic.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

public class CommentVo extends JsonParseInterface implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 用户信息实体 */
//	private static final String d_anchorInfo = "a";
	/** 发表评论用户 */
	private static final String d_userId = "a";
	/** 发表评论用户 */
	private static final String d_nickname = "b";
	/** 发表评论用户 */
	private static final String d_avatar = "c";
	/** 回复的评论用户*/
	private static final String d_toUserId = "d";
	/** 回复的评论用户*/
	private static final String d_toNickname = "e";
	/** 回复的评论用户*/
	private static final String d_toAvatar = "f";
	/** 评论内容*/
	private static final String d_content = "g";
	/** 评论/回复的时间 */
	private static final String d_commentTime = "h";
	/** 本条评论的id */
	private static final String d_commentId = "i";
	/** 回复的目标评论id*/
	private static final String d_toCommentId = "j";
	/** 评论类型，0-回复整条动态，1-回复某条评论 */
	private static final String d_commentType = "k";
	/** 状态，0-标记未读，1-标记已读，2-标记清空 */
	private static final String d_readFlag = "l";
	private String userId;
	private String nickname;
	private String avatar;
	private String toUserId;
	private String toNickname;
	private String toAvatar;
	private String content;
	private long commentTime;
	private long commentId;
	private long toCommentId;
	private int commentType;
	private int readFlag;
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			setString(json,d_userId,userId);
			setString(json,d_nickname,nickname);
			setString(json,d_avatar,avatar);
			setString(json,d_toUserId,toUserId);
			setString(json,d_toNickname,toNickname);
			setString(json,d_toAvatar,toAvatar);
			setString(json,d_content,content);
			setLong(json,d_commentTime,commentTime);
			setLong(json,d_commentId,commentId);
			setLong(json,d_toCommentId,toCommentId);
			setInt(json,d_commentType,commentType);
			setInt(json,d_readFlag,readFlag);
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
			userId = getString(json,d_userId);
			nickname = getString(json,d_nickname);
			avatar = getString(json,d_avatar);
			toUserId = getString(json,d_toUserId);
			toNickname = getString(json,d_toNickname);
			toAvatar = getString(json,d_toAvatar);
			content = getString(json,d_content);
			commentTime = getLong(json,d_commentTime);
			commentId = getLong(json,d_commentId);
			toCommentId = getLong(json,d_toCommentId);
			commentType = getInt(json,d_commentType);
			readFlag = getInt(json,d_readFlag);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToNickname() {
		return toNickname;
	}

	public void setToNickname(String toNickname) {
		this.toNickname = toNickname;
	}

	public String getToAvatar() {
		return toAvatar;
	}

	public void setToAvatar(String toAvatar) {
		this.toAvatar = toAvatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(long commentTime) {
		this.commentTime = commentTime;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getToCommentId() {
		return toCommentId;
	}

	public void setToCommentId(long toCommentId) {
		this.toCommentId = toCommentId;
	}

	public int getCommentType() {
		return commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}

	public int getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

	
}
