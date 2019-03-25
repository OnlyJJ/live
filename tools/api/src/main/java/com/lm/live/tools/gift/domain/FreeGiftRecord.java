package com.lm.live.tools.gift.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 免费礼物记录
 * @author shao.xiang
 * @date 2017-06-29
 */
public class FreeGiftRecord extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	
	/** 普通用户角色总量*/
	private int userSum;
	/**主播角色总量*/
	private int anchorSum;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getUserSum() {
		return userSum;
	}
	public void setUserSum(int userSum) {
		this.userSum = userSum;
	}
	public int getAnchorSum() {
		return anchorSum;
	}
	public void setAnchorSum(int anchorSum) {
		this.anchorSum = anchorSum;
	}
	
}
