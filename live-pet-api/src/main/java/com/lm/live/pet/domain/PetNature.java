package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetNature
 * 宠物属性表
*/
public class PetNature extends BaseVo {
	
	
	/**
	 * 属性名
	 */
	private String name;
	/**
	 * 属性描述
	 */
	private String remark;
	/**
	 * 属性类型，0:额外宠物经验，1:额外用户经验
	 */
	private Byte natureType;
	/**
	 * 属性值
	 */
	private Integer natureVal;
	
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Byte getNaturetype() {
		return this.natureType;
	}
	
	public void setNaturetype(Byte natureType) {
		this.natureType = natureType;
	}
	public Integer getNatureval() {
		return this.natureVal;
	}
	
	public void setNatureval(Integer natureVal) {
		this.natureVal = natureVal;
	}
}
