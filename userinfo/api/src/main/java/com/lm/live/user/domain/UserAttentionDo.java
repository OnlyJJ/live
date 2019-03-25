package com.lm.live.user.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_user_attention
 * @author shao.xiang
 * @date 2017-08-17
 */
public class UserAttentionDo extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 关注人userId */
	private String userId;
	/** 被关注的人userId */
	private String toUserId;
	/** 添加时间 */
	private Date addTime;

	
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setToUserId(String toUserId){
		this.toUserId = toUserId;
	}
	
	public String getToUserId() {
		return this.toUserId;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}

}