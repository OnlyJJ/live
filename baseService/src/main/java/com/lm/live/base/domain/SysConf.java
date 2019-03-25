package com.lm.live.base.domain;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_sys_conf
 * @author shao.xiang
 * @date 2017-06-21
 */
public class SysConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 配置编号(保持唯一),代码中要以此来取 */
	private String code;
	/** 内容配置 */
	private String confValue;
	/** isUse */
	private int isUse;
	/** 活动标题 */
	private String desc;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setConfValue(String confValue){
		this.confValue = confValue;
	}
	
	public String getConfValue() {
		return this.confValue;
	}
	
	public void setIsUse(int isUse){
		this.isUse = isUse;
	}
	
	public int getIsUse() {
		return this.isUse;
	}
	
	public void setDesc(String desc){
		this.desc = desc;
	}
	
	public String getDesc() {
		return this.desc;
	}
	

}