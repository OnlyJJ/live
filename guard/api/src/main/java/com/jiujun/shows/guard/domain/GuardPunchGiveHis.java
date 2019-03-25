package com.jiujun.shows.guard.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * t_guard_punch_give_his
 * @author shao.xiang
 * @date 2017-06-13
 */
public class GuardPunchGiveHis extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5336674858285537941L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 关联t_guard_punch_give的id
	 */
	private Integer punchGiveId;
	/**
	 * 奖励的次数
	 */
	private int number;
	/**
	 * 赠送时间
	 */
	private Date addTime;
	
	
	public Integer getPunchgiveid() {
		return this.punchGiveId;
	}
	
	public void setPunchgiveid(Integer punchGiveId) {
		this.punchGiveId = punchGiveId;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
