package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetConf
 * 宠物配置表
*/
public class PetConf extends BaseVo {
	
	
	/**
	 * 默认的宠物名，用户可修改
	 */
	private String petName;
	/**
	 * 孵化时间，单位:分钟
	 */
	private Integer hatchTime;
	/**
	 * 属性id，关联宠物属性配置表id
	 */
	private Integer natureId;
	/**
	 * 是否有效，0:无效，1:有效
	 */
	private Boolean vaild;
	
	
	public String getPetname() {
		return this.petName;
	}
	
	public void setPetname(String petName) {
		this.petName = petName;
	}
	public Integer getHatchtime() {
		return this.hatchTime;
	}
	
	public void setHatchtime(Integer hatchTime) {
		this.hatchTime = hatchTime;
	}
	public Integer getNatureid() {
		return this.natureId;
	}
	
	public void setNatureid(Integer natureId) {
		this.natureId = natureId;
	}
	public Boolean getVaild() {
		return this.vaild;
	}
	
	public void setVaild(Boolean vaild) {
		this.vaild = vaild;
	}
}
