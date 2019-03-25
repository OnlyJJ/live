package com.jiujun.shows.rank.domain;


import com.jiujun.shows.common.vo.BaseVo;

/**
 * 用户送礼汇总
 * @author shao.xiang
 * @date 2017-07-18
 *
 */
public class StatFuhaoDo extends BaseVo {
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String timeframe;
	
	private String userId;
	
	private String toUserId;
	
	private long gold;
	
	private long totalGold;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTimeFrame() {
		return timeframe;
	}

	public void setTimeFrame(String timeframe) {
		this.timeframe = timeframe;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public long getTotalGold() {
		return totalGold;
	}

	public void setTotalGold(long totalGold) {
		this.totalGold = totalGold;
	}
	
	
}
