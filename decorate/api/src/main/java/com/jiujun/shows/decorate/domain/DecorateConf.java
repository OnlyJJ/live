package com.jiujun.shows.decorate.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_decorate_conf
 * @author shao.xiang
 * @date 2017-06-08
 */
public class DecorateConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** decorateId */
	private Integer decorateId;
	/** decorateType */
	private String decorateType;
	/** level */
	private int level;
	/** addTime */
	private Date addTime;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setDecorateId(Integer decorateId){
		this.decorateId = decorateId;
	}
	
	public Integer getDecorateId() {
		return this.decorateId;
	}
	
	public void setDecorateType(String decorateType){
		this.decorateType = decorateType;
	}
	
	public String getDecorateType() {
		return this.decorateType;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}
	
	

}