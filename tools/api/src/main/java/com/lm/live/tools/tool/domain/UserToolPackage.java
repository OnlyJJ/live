package com.lm.live.tools.tool.domain;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_tool_user_package
 * @author shao.xiang
 * @date 2017-06-29
 */
public class UserToolPackage extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 用户ID */
	private String userId;
	/** 工具ID */
	private Integer toolId;
	/** 数量 */
	private Integer number;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setToolId(Integer toolId){
		this.toolId = toolId;
	}
	
	public Integer getToolId() {
		return this.toolId;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	

}