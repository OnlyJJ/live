package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* UserPet
 * 用户宠物表
*/
public class UserPet extends BaseVo {
	
	
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 宠物id，对应t_pet_conf表的id
	 */
	private String petId;
	/**
	 * 用户自定义的宠物名称，没有修改则用默认名
	 */
	private String petName;
	/**
	 * 当前宠物等级
	 */
	private Integer level;
	/**
	 * 当前宠物经验
	 */
	private Integer petPoint;
	/**
	 * 宠物状态，0:未使用，1:正在使用
	 */
	private Boolean status;
	
	
	public String getUserid() {
		return this.userId;
	}
	
	public void setUserid(String userId) {
		this.userId = userId;
	}
	public String getPetid() {
		return this.petId;
	}
	
	public void setPetid(String petId) {
		this.petId = petId;
	}
	public String getPetname() {
		return this.petName;
	}
	
	public void setPetname(String petName) {
		this.petName = petName;
	}
	public Integer getLevel() {
		return this.level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPetpoint() {
		return this.petPoint;
	}
	
	public void setPetpoint(Integer petPoint) {
		this.petPoint = petPoint;
	}
	public Boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
