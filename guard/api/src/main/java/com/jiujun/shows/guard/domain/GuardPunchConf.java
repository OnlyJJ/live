package com.jiujun.shows.guard.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * t_guard_punch_conf
 * @author shao.xiang
 * @date 2017-06-13
 *
 */
public class GuardPunchConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1814199019041897178L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 类型，对应购买守护的价格类型，1:月度、2:季度、3:年度
	 */
	private Integer type;
	/**
	 * 满足奖励的打卡条件，如每打够15次，就奖励7天
	 */
	private Integer punchDay;
	/**
	 * 奖励守护天数
	 */
	private Integer prizesDay;
	/**
	 * 每满足一次打卡条件，可奖励的次数，如月度只奖励1次等
	 */
	private Integer number;
	/**
	 * 是否启用，0-不启用，1-启用
	 */
	private int status;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getPunchday() {
		return this.punchDay;
	}
	
	public void setPunchday(Integer punchDay) {
		this.punchDay = punchDay;
	}
	public Integer getPrizesday() {
		return this.prizesDay;
	}
	
	public void setPrizesday(Integer prizesDay) {
		this.prizesDay = prizesDay;
	}
	public Integer getNumber() {
		return this.number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
