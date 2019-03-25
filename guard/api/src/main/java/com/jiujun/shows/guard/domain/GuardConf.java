package com.jiujun.shows.guard.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 守护配置表
 * @table t_guard_conf
 * @author shao.xiang
 * @date 2017-06-13
 */

public class GuardConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3768479633163238957L;
	/**
	 * id
	 */
	private int id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 图片
	 */
	private String image;
	/**
	 * 等级
	 */
	private Integer level;
	/**
	 * 价格，金币
	 */
	private Integer price;
	/**
	 * 对应的人民币价格，分
	 */
	private Double priceRMB;
	/**
	 * 有效天数
	 */
	private Integer validate;
	/**
	 * 分成比例
	 */
	private Double rate;
	/**
	 * 主播分配的钻石
	 */
	private Integer diamond;
	/**
	 * 用户增加的经验值
	 */
	private Integer userPoint;
	/**
	 * 主播增加的经验值
	 */
	private Integer anchorPoint;
	/**
	 * 守护类型，1-白银，2-黄金，依次类推
	 */
	private Integer guardType;
	/**
	 * 价格类型，1-月、2-季、3-年
	 */
	private Integer priceType;
	/**
	 * 是否有时间限制，0-没有，1-有（默认）
	 */
	private Integer isPeriod;
	
	/**
	 * 是否启用，默认1-启用，0-停用
	 */
	private Integer isUse;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getLevel() {
		return this.level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPrice() {
		return this.price;
	}
	
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Double getPricermb() {
		return this.priceRMB;
	}
	
	public void setPricermb(Double priceRMB) {
		this.priceRMB = priceRMB;
	}
	public Integer getValidate() {
		return this.validate;
	}
	
	public void setValidate(Integer validate) {
		this.validate = validate;
	}
	public Double getRate() {
		return this.rate;
	}
	
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getDiamond() {
		return this.diamond;
	}
	
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}
	public Integer getUserpoint() {
		return this.userPoint;
	}
	
	public void setUserpoint(Integer userPoint) {
		this.userPoint = userPoint;
	}
	public Integer getAnchorpoint() {
		return this.anchorPoint;
	}
	
	public void setAnchorpoint(Integer anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public Integer getGuardType() {
		return guardType;
	}

	public void setGuardType(Integer guardType) {
		this.guardType = guardType;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Integer getIsPeriod() {
		return isPeriod;
	}

	public void setIsPeriod(Integer isPeriod) {
		this.isPeriod = isPeriod;
	}

	public Integer getIsUse() {
		return isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	
}
