package com.lm.live.tools.tool.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_user_tool_manager
 * @author shao.xiang
 * @date 2017-06-29
 */
public class UserToolManager extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2700578044234326488L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 工具id，关联t_tool表id
	 */
	private Integer toolType;
	/**
	 * 解/封状态，0-封，1-解
	 */
	private int status;
	/**
	 * 封停开始时间
	 */
	private Date beginTime;
	/**
	 * 封停结束时间
	 */
	private Date endTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public Integer getToolType() {
		return this.toolType;
	}
	
	public void setToolType(Integer toolType) {
		this.toolType = toolType;
	}
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getBegintime() {
		return this.beginTime;
	}
	
	public void setBegintime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndtime() {
		return this.endTime;
	}
	
	public void setEndtime(Date endTime) {
		this.endTime = endTime;
	}
}
