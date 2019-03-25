package com.jiujun.shows.dynamic.domain.user;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 动态喜好表(点赞、踩)
 * t_diary_favourate
 */

public class DiaryFavourate extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String userId;
	/**
	 * 动态id
	 */
	private long diaryinfoId;
	/**
	 * 用户操作类型，0-点赞，1-踩
	 */
	private int favourateType;
	private Date resultTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public long getDiaryinfoid() {
		return this.diaryinfoId;
	}
	
	public void setDiaryinfoid(long diaryinfoId) {
		this.diaryinfoId = diaryinfoId;
	}
	public int getFavouratetype() {
		return this.favourateType;
	}
	
	public void setFavouratetype(int favourateType) {
		this.favourateType = favourateType;
	}
	public Date getResulttime() {
		return this.resultTime;
	}
	
	public void setResulttime(Date resultTime) {
		this.resultTime = resultTime;
	}
}
