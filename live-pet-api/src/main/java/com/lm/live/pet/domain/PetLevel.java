package com.lm.live.pet.domain;

import com.lm.live.common.vo.BaseVo;

/**
* PetLevel
 * 宠物等级表
*/
public class PetLevel extends BaseVo {
	
	
	/**
	 * 对应t_pet_conf表的id
	 */
	private Integer petId;
	/**
	 * 宠物等级
	 */
	private Integer level;
	/**
	 * 等级对应的经验值
	 */
	private Integer point;
	/**
	 * 等级对应的图片
	 */
	private String image;
	
	
	public Integer getPetid() {
		return this.petId;
	}
	
	public void setPetid(Integer petId) {
		this.petId = petId;
	}
	public Integer getLevel() {
		return this.level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPoint() {
		return this.point;
	}
	
	public void setPoint(Integer point) {
		this.point = point;
	}
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
}
