package com.jiujun.shows.rank.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 主播收礼汇总
 * @author shao.xiang
 * @Date 2017-07-18
 *
 */
public class StatMeiliDo extends BaseVo {
	private static final long serialVersionUID = -3925765548957527690L;
	private int id;
	private String timeframe;
	private int familyId;
	private String userId;
	private long diamond;
	private long totalDiamond;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	public int getFamilyId() {
		return familyId;
	}
	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getDiamond() {
		return diamond;
	}
	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}
	public long getTotalDiamond() {
		return totalDiamond;
	}
	public void setTotalDiamond(long totalDiamond) {
		this.totalDiamond = totalDiamond;
	}
	
	
	

}
